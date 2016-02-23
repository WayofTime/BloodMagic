package WayofTime.bloodmagic.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;

public class TileDemonPylon extends TileEntity implements ITickable, IDemonWillConduit
{
    public DemonWillHolder holder = new DemonWillHolder();
    public final int maxWill = 100;
    public final double drainRate = 1;

    public TileDemonPylon()
    {

    }

    @Override
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        for (EnumDemonWillType type : EnumDemonWillType.values())
        {
            double currentAmount = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);

            for (EnumFacing side : EnumFacing.HORIZONTALS)
            {
                BlockPos offsetPos = pos.offset(side, 16);
                double sideAmount = WorldDemonWillHandler.getCurrentWill(worldObj, offsetPos, type);
                if (sideAmount > currentAmount)
                {
                    double drainAmount = Math.min((sideAmount - currentAmount) / 2, drainRate);
                    double drain = WorldDemonWillHandler.drainWill(worldObj, offsetPos, type, drainAmount, true);
                    WorldDemonWillHandler.fillWill(worldObj, pos, type, drain, true);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        holder.readFromNBT(tag, "Will");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        holder.writeToNBT(tag, "Will");
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
}