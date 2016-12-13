package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.tile.base.TileTicking;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.block.BlockDemonCrystal;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;

public class TileDemonCrystal extends TileTicking
{
    public DemonWillHolder holder = new DemonWillHolder();
    public final int maxWill = 100;
    public final double drainRate = 1;
    public static final double sameWillConversionRate = 50;
    public static final double defaultWillConversionRate = 100;
    public static final double timeDelayForWrongWill = 0.6;

    public double progressToNextCrystal = 0;
    public int internalCounter = 0;

    @Getter
    @Setter
    public int crystalCount = 1;
    @Getter
    @Setter
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
            EnumDemonWillType type = EnumDemonWillType.values()[this.getBlockMetadata()];

            double value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
            if (type != EnumDemonWillType.DEFAULT)
            {
                if (value >= 100)
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

    public void checkAndGrowCrystal()
    {
        if (progressToNextCrystal >= 1)
        {
            progressToNextCrystal--;
            crystalCount++;
            IBlockState thisState = getWorld().getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, thisState, thisState, 3);
            markDirty();
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
            if (stack != null)
            {
                crystalCount--;
                getWorld().spawnEntity(new EntityItem(getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
                return true;
            }
        }

        return false;
    }

    public double getCrystalGrowthPerSecond(double will)
    {
        return 1.0 / 800 * Math.sqrt(will / 200);
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
}