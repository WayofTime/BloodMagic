package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.Utils;

public class RitualZephyr extends Ritual
{
    public static final String ZEPHYR_RANGE = "zephyrRange";

    public RitualZephyr()
    {
        super("ritualZephyr", 0, 1000, "ritual." + Constants.Mod.MODID + ".zephyrRitual");
        addBlockRange(ZEPHYR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();
        TileEntity tileInventory = world.getTileEntity(masterRitualStone.getBlockPos().up());

        if (!masterRitualStone.getWorldObj().isRemote && tileInventory != null && tileInventory instanceof IInventory)
        {
            if (currentEssence < getRefreshCost())
            {
                network.causeNauseaToPlayer();
                return;
            }

            AreaDescriptor zephyrRange = getBlockRange(ZEPHYR_RANGE);

            List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, zephyrRange.getAABB(masterRitualStone.getBlockPos()));
            int count = 0;

            if (itemList != null)
            {
                for (EntityItem entityItem : itemList)
                {
                    ItemStack copyStack = entityItem.getEntityItem().copy();
                    int originalAmount = copyStack.stackSize;
                    ItemStack newStack = Utils.insertStackIntoInventory(copyStack, (IInventory) tileInventory, EnumFacing.DOWN);

                    if (newStack != null && newStack.stackSize < originalAmount)
                    {
                        count++;
                        if (newStack.stackSize <= 0)
                            entityItem.setDead();

                        entityItem.getEntityItem().stackSize = newStack.stackSize;
                    }
                }
            }

            network.syphon(this.getRefreshCost() * Math.min(count, 100));
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 1;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 2, 0, EnumRuneType.AIR);
        this.addCornerRunes(components, 1, 1, EnumRuneType.AIR);
        this.addParallelRunes(components, 1, -1, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualZephyr();
    }
}
