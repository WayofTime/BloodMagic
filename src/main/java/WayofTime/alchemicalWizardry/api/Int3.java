package WayofTime.alchemicalWizardry.api;

import net.minecraft.nbt.NBTTagCompound;


public class Int3
{
    public int xCoord;
    public int yCoord;
    public int zCoord;

    public Int3(int xCoord, int yCoord, int zCoord)
    {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    public static Int3 readFromNBT(NBTTagCompound tag)
    {
        return new Int3(tag.getInteger("xCoord"), tag.getInteger("yCoord"), tag.getInteger("zCoord"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("xCoord", xCoord);
        tag.setInteger("yCoord", yCoord);
        tag.setInteger("zCoord", zCoord);

        return tag;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Int3 ? ((Int3) o).xCoord == this.xCoord && ((Int3) o).yCoord == this.yCoord && ((Int3) o).zCoord == this.zCoord : false;
    }
    
    @Override
    public int hashCode()
    {
        return this.xCoord + this.yCoord << 8 + this.zCoord << 16;
    }
}
