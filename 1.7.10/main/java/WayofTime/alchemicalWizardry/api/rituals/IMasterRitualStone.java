package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.world.World;

public interface IMasterRitualStone 
{
	public void performRitual(World world, int x, int y, int z, String ritualID);
	
    public String getOwner();

    public void setCooldown(int newCooldown);

    public int getCooldown();

    public void setVar1(int newVar1);

    public int getVar1();

    public void setActive(boolean active);

    public int getDirection();
    
    public World getWorld();
    
    public int getXCoord();
    
    public int getYCoord();
    
    public int getZCoord();
}
