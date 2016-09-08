package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.base.TileTicking;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;

@NoArgsConstructor
public class TilePhantomBlock extends TileTicking
{
    private int ticksRemaining = 10;

    public TilePhantomBlock(int ticksRemaining)
    {
        this.ticksRemaining = ticksRemaining;
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound)
    {
        this.ticksRemaining = tagCompound.getInteger(Constants.NBT.TICKS_REMAINING);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        return tagCompound;
    }

    @Override
    public void onUpdate()
    {
        ticksRemaining--;

        if (ticksRemaining <= 0)
        {
            worldObj.setBlockToAir(getPos());
            worldObj.removeTileEntity(getPos());
        }
    }
}
