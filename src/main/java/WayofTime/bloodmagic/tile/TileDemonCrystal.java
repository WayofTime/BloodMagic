package WayofTime.bloodmagic.tile;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.block.BlockDemonCrystal;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;

public class TileDemonCrystal extends TileEntity implements ITickable, IDemonWillConduit
{
    public DemonWillHolder holder = new DemonWillHolder();
    public final int maxWill = 100;
    public final double drainRate = 1;
    public static final double sameWillConversionRate = 5;
    public static final double defaultWillConversionRate = 50;
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
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        internalCounter++;

        if (internalCounter % 20 == 0 && crystalCount < 7)
        {
            EnumDemonWillType type = EnumDemonWillType.values()[this.getBlockMetadata()];

            double value = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);
            if (type != EnumDemonWillType.DEFAULT)
            {
                if (value >= 100)
                {
                    double nextProgress = getCrystalGrowthPerSecond(value);
                    progressToNextCrystal += WorldDemonWillHandler.drainWill(worldObj, getPos(), type, nextProgress * sameWillConversionRate, true) / sameWillConversionRate;
                } else
                {
                    value = WorldDemonWillHandler.getCurrentWill(worldObj, pos, EnumDemonWillType.DEFAULT);
                    if (value > 0.5)
                    {
                        double nextProgress = getCrystalGrowthPerSecond(value) * timeDelayForWrongWill;
                        progressToNextCrystal += WorldDemonWillHandler.drainWill(worldObj, getPos(), EnumDemonWillType.DEFAULT, nextProgress * defaultWillConversionRate, true) / defaultWillConversionRate;
                    }
                }
            } else
            {
                if (value > 0.5)
                {
                    double nextProgress = getCrystalGrowthPerSecond(value);
                    progressToNextCrystal += WorldDemonWillHandler.drainWill(worldObj, getPos(), type, nextProgress * sameWillConversionRate, true) / sameWillConversionRate;
                }
            }

            if (progressToNextCrystal >= 1)
            {
                progressToNextCrystal--;
                crystalCount++;
                worldObj.markBlockForUpdate(pos);
            }
        }

//        if (worldObj.getWorldTime() % 200 == 0)
//        {
//            crystalCount = Math.min(crystalCount + 1, 7);
//            worldObj.markBlockForUpdate(pos);
//        }
    }

    public boolean dropSingleCrystal()
    {
        if (!worldObj.isRemote && crystalCount > 1)
        {
            IBlockState state = worldObj.getBlockState(pos);
            EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
            ItemStack stack = BlockDemonCrystal.getItemStackDropped(type, 1);
            if (stack != null)
            {
                crystalCount--;
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
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
        return MathHelper.clamp_int(crystalCount - 1, 0, 6);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        holder.readFromNBT(tag, "Will");
        crystalCount = tag.getInteger("crystalCount");
        placement = EnumFacing.getFront(tag.getInteger("placement"));
        progressToNextCrystal = tag.getDouble("progress");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        holder.writeToNBT(tag, "Will");
        tag.setInteger("crystalCount", crystalCount);
        tag.setInteger("placement", placement.getIndex());
        tag.setDouble("progress", progressToNextCrystal);
    }

    // IDemonWillConduit

    @Override
    public int getWeight()
    {
        return 10;
    }

    @Override
    public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill)
    {
        if (amount <= 0)
        {
            return 0;
        }

        if (!canFill(type))
        {
            return 0;
        }

        if (!doFill)
        {
            return Math.min(maxWill - holder.getWill(type), amount);
        }

        return holder.addWill(type, amount, maxWill);
    }

    @Override
    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
    {
        double drained = amount;
        double current = holder.getWill(type);
        if (current < drained)
        {
            drained = current;
        }

        if (doDrain)
        {
            return holder.drainWill(type, amount);
        }

        return drained;
    }

    @Override
    public boolean canFill(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public boolean canDrain(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public double getCurrentWill(EnumDemonWillType type)
    {
        return holder.getWill(type);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(getPos(), -999, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
        worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
    }
}