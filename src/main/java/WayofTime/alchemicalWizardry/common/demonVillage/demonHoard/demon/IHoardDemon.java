package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public interface IHoardDemon 
{
	public void setPortalLocation(Int3 position);
    public Int3 getPortalLocation();
    public boolean thrallDemon(TEDemonPortal teDemonPortal);
    public boolean isSamePortal(IHoardDemon demon);
}
