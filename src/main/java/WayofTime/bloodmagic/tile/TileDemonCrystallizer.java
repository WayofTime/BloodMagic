package WayofTime.bloodmagic.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;

public class TileDemonCrystallizer extends TileEntity implements ITickable, IDemonWillConduit
{
    public DemonWillHolder holder = new DemonWillHolder();
    public final int maxWill = 100;
    public final double drainRate = 1;

    public TileDemonCrystallizer()
    {

    }

    @Override
    public void update()
    {

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