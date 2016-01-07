package WayofTime.bloodmagic.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileSoulForge extends TileInventory implements ITickable
{
    public TileSoulForge()
    {
        super(6, "soulForge");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
    }

    @Override
    public void update()
    {

    }
}
