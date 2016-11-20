package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.Utils;

public class RitualZephyr extends Ritual
{
    public static final String ZEPHYR_RANGE = "zephyrRange";
    public static final String CHEST_RANGE = "chest";

    public RitualZephyr()
    {
        super("ritualZephyr", 0, 1000, "ritual." + Constants.Mod.MODID + ".zephyrRitual");
        addBlockRange(ZEPHYR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(ZEPHYR_RANGE, 0, 10, 10);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();
        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        if (!masterRitualStone.getWorldObj().isRemote && tileInventory != null)
        {
            if (currentEssence < getRefreshCost())
            {
                network.causeNausea();
                return;
            }

            AreaDescriptor zephyrRange = getBlockRange(ZEPHYR_RANGE);

            List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, zephyrRange.getAABB(masterRitualStone.getBlockPos()));
            int count = 0;

            if (itemList != null)
            {
                for (EntityItem entityItem : itemList)
                {
                    if (entityItem.isDead)
                    {
                        continue;
                    }

                    ItemStack copyStack = entityItem.getEntityItem().copy();
                    int originalAmount = copyStack.stackSize;
                    ItemStack newStack = Utils.insertStackIntoTile(copyStack, tileInventory, EnumFacing.DOWN);

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
