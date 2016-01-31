package WayofTime.bloodmagic.api.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is for internal implementation only.
 * 
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IImperfectRitualStone
{

    boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player);

    World getRitualWorld();

    BlockPos getRitualPos();
}
