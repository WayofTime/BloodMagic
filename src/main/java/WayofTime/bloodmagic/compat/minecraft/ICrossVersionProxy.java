package WayofTime.bloodmagic.compat.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Allows for Blood Magic to support multiple MC versions that have only slight changes.
 *
 * Implementation copied from <a href="https://github.com/williewillus/Botania">Botania</a>.
 */
public interface ICrossVersionProxy
{
    TileEntity createTileFromData(World world, NBTTagCompound tagCompound);
}
