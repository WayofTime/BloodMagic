package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.world.World;

public abstract class DemonHoardPacket 
{
	public DemonHoardPacket()
	{
		
	}
	
	public abstract int summonDemons(World world, int x, int y, int z, DemonType type, int tier);
	
	public abstract boolean canFitType(DemonType type);
	
	public abstract float getRelativeChance(DemonType type, int tier);
}
