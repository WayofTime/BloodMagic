package WayofTime.bloodmagic.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import WayofTime.bloodmagic.block.BlockDemonCrystal;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.base.TileTicking;

public class TileDemonCrystal extends TileTicking
{
    public static final double sameWillConversionRate = 50;
    public static final double defaultWillConversionRate = 100;
    public static final double timeDelayForWrongWill = 0.6;
    public final int maxWill = 100;
    public final double drainRate = 1;
    public DemonWillHolder holder = new DemonWillHolder();
    public double progressToNextCrystal = 0;
    public int internalCounter = 0;
    public int crystalCount = 1;
    public EnumFacing placement = EnumFacing.UP; //Side that this crystal is placed on.

    public TileDemonCrystal()
    {
        this.crystalCount = 1;
    }

    @Override
    public void onUpdate()
    {
        if (getWorld().isRemote)
        {
            return;
        }

        internalCounter++;

        if (internalCounter % 20 == 0 && crystalCount < 7)
        {
            EnumDemonWillType type = getType();

            double value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
            if (type != EnumDemonWillType.DEFAULT)
            {
                if (value >= 0.5)
                {
                    double nextProgress = getCrystalGrowthPerSecond(value);
                    progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), type, nextProgress * sameWillConversionRate, true) / sameWillConversionRate;
                } else
                {
                    value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, EnumDemonWillType.DEFAULT);
                    if (value > 0.5)
                    {
                        double nextProgress = getCrystalGrowthPerSecond(value) * timeDelayForWrongWill;
                        progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), EnumDemonWillType.DEFAULT, nextProgress * defaultWillConversionRate, true) / defaultWillConversionRate;
                    }
                }
            } else
            {
                if (value > 0.5)
                {

                    double nextProgress = getCrystalGrowthPerSecond(value);
                    progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), type, nextProgress * sameWillConversionRate, true) / sameWillConversionRate;
                }
            }

            checkAndGrowCrystal();
        }

//        if (getWorld().getWorldTime() % 200 == 0)
//        {
//            crystalCount = Math.min(crystalCount + 1, 7);
//            getWorld().markBlockForUpdate(pos);
//        }
    }

    /**
     * Encourages the crystal to grow by a large percentage by telling it to
     * drain will from the aura.
     *
     * @param willDrain
     *        The amount of drain that is needed for the crystal to grow
     *        successfully for the desired amount. Can be more than the base
     *        amount.
     * @param progressPercentage
     * @return percentage actually grown.
     */
    public double growCrystalWithWillAmount(double willDrain, double progressPercentage)
    {
        if (crystalCount >= 7)
        {
            return 0;
        }

        IBlockState state = getWorld().getBlockState(pos);
        int meta = this.getBlockType().getMetaFromState(state);
        EnumDemonWillType type = EnumDemonWillType.values()[meta];

        double value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
        double percentDrain = willDrain <= 0 ? 1 : Math.min(1, value / willDrain);
        if (percentDrain <= 0)
        {
            return 0;
        }

        // Verification that you can actually drain the will from this chunk, for future proofing.
        WorldDemonWillHandler.drainWill(getWorld(), pos, type, percentDrain * willDrain, true);
        progressToNextCrystal += percentDrain * progressPercentage;

        checkAndGrowCrystal();

        return percentDrain * progressPercentage;
    }

    public EnumDemonWillType getType()
    {
        return EnumDemonWillType.values()[this.getBlockMetadata()];
    }

    public void checkAndGrowCrystal()
    {
        if (progressToNextCrystal >= 1 && internalCounter % 100 == 0)
        {
            progressToNextCrystal--;
            crystalCount++;
            markDirty();
            notifyUpdate();
        }
    }

    public double getMaxWillForCrystal()
    {
        return 50;
    }

    public boolean dropSingleCrystal()
    {
        if (!getWorld().isRemote && crystalCount > 1)
        {
            IBlockState state = getWorld().getBlockState(pos);
            EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
            ItemStack stack = BlockDemonCrystal.getItemStackDropped(type, 1);
            if (!stack.isEmpty())
            {
                crystalCount--;
                InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
                notifyUpdate();
                return true;
            }
        }

        return false;
    }

    public double getCrystalGrowthPerSecond(double will)
    {
        return 1.0 / 200 * Math.sqrt(will / 200);
    }

    public int getCrystalCountForRender()
    {
        return MathHelper.clamp(crystalCount - 1, 0, 6);
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        holder.readFromNBT(tag, "Will");
        crystalCount = tag.getInteger("crystalCount");
        placement = EnumFacing.getFront(tag.getInteger("placement"));
        progressToNextCrystal = tag.getDouble("progress");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        holder.writeToNBT(tag, "Will");
        tag.setInteger("crystalCount", crystalCount);
        tag.setInteger("placement", placement.getIndex());
        tag.setDouble("progress", progressToNextCrystal);
        return tag;
    }

    public int getCrystalCount()
    {
        return crystalCount;
    }

    public void setCrystalCount(int crystalCount)
    {
        this.crystalCount = crystalCount;
    }

    public EnumFacing getPlacement()
    {
        return placement;
    }

    public void setPlacement(EnumFacing placement)
    {
        this.placement = placement;
    }
}