package WayofTime.bloodmagic.api.alchemyCrafting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

@RequiredArgsConstructor
public abstract class AlchemyArrayEffect
{
    @Getter
    public final String key;

    public abstract boolean update(TileEntity tile, int ticksActive);

    public abstract void writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);

    public abstract AlchemyArrayEffect getNewCopy();
}
