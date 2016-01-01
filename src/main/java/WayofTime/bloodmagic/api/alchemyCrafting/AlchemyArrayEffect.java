package WayofTime.bloodmagic.api.alchemyCrafting;

import net.minecraft.tileentity.TileEntity;

public abstract class AlchemyArrayEffect
{
    public abstract boolean update(TileEntity tile, int ticksActive);
}
