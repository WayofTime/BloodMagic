package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;

public class DemonPacketAngel extends DemonHoardPacket 
{
	@Override
	public boolean canFitType(DemonType type) 
	{
		return true;
	}

	@Override
	public float getRelativeChance(DemonType type, int tier) 
	{
		return 1.0f;
	}

	@Override
	public int summonDemons(World world, int x, int y, int z, DemonType type, int tier) 
	{
		Entity entity = new EntityFallenAngel(world);
		entity.setPosition(x, y, z);
		
		world.spawnEntityInWorld(entity);
		return 1;
	}
}
