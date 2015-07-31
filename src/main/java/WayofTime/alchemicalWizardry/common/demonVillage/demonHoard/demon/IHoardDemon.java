package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import WayofTime.alchemicalWizardry.api.Int3;

public interface IHoardDemon 
{
	void setPortalLocation(Int3 position);
    Int3 getPortalLocation();
    boolean thrallDemon(Int3 location);
    boolean isSamePortal(IHoardDemon demon);
}
