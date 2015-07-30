package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.util.BlockPos;

public interface IHoardDemon 
{
	void setPortalLocation(BlockPos position);

    BlockPos getPortalLocation();

    boolean thrallDemon(BlockPos location);

    boolean isSamePortal(IHoardDemon demon);
}
