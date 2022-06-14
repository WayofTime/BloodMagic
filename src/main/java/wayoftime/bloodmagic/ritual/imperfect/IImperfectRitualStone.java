package wayoftime.bloodmagic.ritual.imperfect;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * This interface is for internal implementation only.
 * <p>
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IImperfectRitualStone
{

	boolean performRitual(Level world, BlockPos pos, ImperfectRitual imperfectRitual, Player player);

	Level getRitualWorld();

	BlockPos getRitualPos();
}
