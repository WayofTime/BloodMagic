package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualFullStomach extends Ritual
{
    public static final String FILL_RANGE = "fillRange";

    public RitualFullStomach()
    {
        super("ritualFullStomach", 0, 100000, "ritual." + Constants.Mod.MODID + ".fullStomachRitual");
        addBlockRange(FILL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-25, -25, -25), 51));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        TileEntity tile = world.getTileEntity(pos.up());
        if (!(tile instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tile;

        int lastSlot = 0;

        AreaDescriptor fillingRange = getBlockRange(FILL_RANGE);

        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, fillingRange.getAABB(pos));

        for (EntityPlayer player : playerList)
        {
            FoodStats foodStats = player.getFoodStats();
            float satLevel = foodStats.getSaturationLevel();

            for (int i = lastSlot; i < inventory.getSizeInventory(); i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood)
                {
                    ItemFood foodItem = (ItemFood) stack.getItem();

                    int healAmount = foodItem.getHealAmount(stack);
                    float saturationAmount = foodItem.getSaturationModifier(stack) * healAmount * 2.0f;

                    if (saturationAmount + satLevel <= 20)
                    {
                        NBTTagCompound nbt = new NBTTagCompound();
                        foodStats.writeNBT(nbt);
                        nbt.setFloat("foodSaturationLevel", saturationAmount + satLevel);
                        foodStats.readNBT(nbt);

                        inventory.decrStackSize(i, 1);
                        totalEffects++;
                        lastSlot = i;
                        break;
                    }
                }
            }

            if (totalEffects >= maxEffects)
            {
                network.causeNauseaToPlayer();

                break;
            }
        }

        network.syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 20;
    }

    @Override
    public int getRefreshCost()
    {
        return 100;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 3, 0, EnumRuneType.FIRE);
        this.addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        this.addOffsetRunes(components, 1, 2, 0, EnumRuneType.AIR);
        this.addCornerRunes(components, 4, 0, EnumRuneType.WATER);
        this.addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualFullStomach();
    }
}
