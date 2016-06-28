package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;

public class AlchemyArrayEffectMovement extends AlchemyArrayEffect
{
    public AlchemyArrayEffectMovement(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectMovement(key);
    }
}
