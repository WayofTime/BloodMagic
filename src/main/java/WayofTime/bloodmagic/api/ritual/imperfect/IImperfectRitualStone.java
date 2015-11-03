package WayofTime.bloodmagic.api.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IImperfectRitualStone {

    boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player);

    World getWorld();

    BlockPos getPos();
}
