package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileDemonCrystallizer extends TileEntity implements ITickable, IDemonWillConduit
{
    //The whole purpose of this block is to grow a crystal initially. The acceleration and crystal growing is up to the crystal itself afterwards.
    public DemonWillHolder holder = new DemonWillHolder();
    public static final int maxWill = 100;
    public static final double drainRate = 1;

    public static final double willToFormCrystal = 99;
    public static final double totalFormationTime = 1000;
    public double internalCounter = 0;

    public TileDemonCrystallizer()
    {

    }

    @Override
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        BlockPos offsetPos = pos.offset(EnumFacing.UP);
        if (worldObj.isAirBlock(offsetPos)) //Room for a crystal to grow
        {
            EnumDemonWillType highestType = WorldDemonWillHandler.getHighestDemonWillType(worldObj, pos);
            double amount = WorldDemonWillHandler.getCurrentWill(worldObj, pos, highestType);
            if (amount >= willToFormCrystal)
            {
                internalCounter += getCrystalFormationRate(amount);
                if (internalCounter >= totalFormationTime)
                {
                    if (WorldDemonWillHandler.drainWill(worldObj, getPos(), highestType, willToFormCrystal, false) >= willToFormCrystal)
                    {
                        if (highestType == EnumDemonWillType.DEFAULT && formRandomSpecialCrystal(offsetPos) || formCrystal(highestType, offsetPos))
                        {
                            WorldDemonWillHandler.drainWill(worldObj, getPos(), highestType, willToFormCrystal, true);
                            internalCounter = 0;
                        }
                    }
                }
            }
        }
    }

    public boolean formCrystal(EnumDemonWillType type, BlockPos position)
    {
        worldObj.setBlockState(position, ModBlocks.demonCrystal.getStateFromMeta(type.ordinal()));
        TileEntity tile = worldObj.getTileEntity(position);
        if (tile instanceof TileDemonCrystal)
        {
            ((TileDemonCrystal) tile).setPlacement(EnumFacing.UP);
            return true;
        }

        return false;
    }

    public boolean formRandomSpecialCrystal(BlockPos position)
    {
        if (worldObj.rand.nextDouble() > 0.1)
        {
            return formCrystal(EnumDemonWillType.DEFAULT, position);
        }
        EnumDemonWillType crystalType = EnumDemonWillType.values()[worldObj.rand.nextInt(EnumDemonWillType.values().length - 1) + 1];
        return formCrystal(crystalType, position);
    }

    public double getCrystalFormationRate(double currentWill)
    {
        return 1;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        holder.readFromNBT(tag, "Will");
        internalCounter = tag.getDouble("internalCounter");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        holder.writeToNBT(tag, "Will");
        tag.setDouble("internalCounter", internalCounter);
        return tag;
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
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), -999, nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
}