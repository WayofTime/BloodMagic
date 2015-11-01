package WayofTime.alchemicalWizardry.api.ritual.imperfect;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IImperfectRitualStone {

    String getOwner();

    World getWorld();

    BlockPos getPos();
}
