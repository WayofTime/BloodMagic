package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import WayofTime.alchemicalWizardry.common.Int3;

public interface IHoardDemon 
{
	public void setPortalLocation(Int3 position);
    public Int3 getPortalLocation();
    public boolean thrallDemon(Int3 location);
    public boolean isSamePortal(IHoardDemon demon);
}
