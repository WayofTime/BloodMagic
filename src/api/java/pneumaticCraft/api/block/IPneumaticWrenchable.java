package pneumaticCraft.api.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Should be implemented by any block that allows to be rotated by a Pneumatic Wrench. It uses almost the same
 * rotate method as the Vanilla (Forge) method. However it uses energy to rotate (when rotateBlock() return true).
 */
public interface IPneumaticWrenchable{

    public boolean rotateBlock(World world, EntityPlayer player, int x, int y, int z, ForgeDirection side);
}
