package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.ritual.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualFullStomach extends Ritual
{
    public static final String FILL_RANGE = "fillRange";
    public static final String CHEST_RANGE = "chest";

    public RitualFullStomach()
    {
        super("ritualFullStomach", 0, 100000, "ritual." + BloodMagic.MODID + ".fullStomachRitual");
        addBlockRange(FILL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-25, -25, -25), 51));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(FILL_RANGE, 0, 25, 25);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
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
                if (!stack.isEmpty() && stack.getItem() instanceof ItemFood)
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
                masterRitualStone.getOwnerNetwork().causeNausea();
                break;
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(getRefreshCost() * totalEffects);
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
