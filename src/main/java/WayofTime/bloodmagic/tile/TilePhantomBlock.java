package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TilePhantomBlock extends TileEntity implements ITickable
{
    private int ticksRemaining;

    public TilePhantomBlock()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        ticksRemaining = tagCompound.getInteger(Constants.NBT.TICKS_REMAINING);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
    }

    @Override
    public void update()
    {
        ticksRemaining--;

        if (ticksRemaining <= 0)
        {
            worldObj.setBlockToAir(pos);
        }
    }

    public void setDuration(int duration)
    {
        ticksRemaining = duration;
    }
}
