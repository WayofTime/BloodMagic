package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntWind;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonPacketMinorGrunt extends DemonHoardPacket 
{
	@Override
	public boolean canFitType(DemonType type) 
	{
		return true;
	}

	@Override
	public float getRelativeChance(DemonType type, int tier, boolean spawnGuardian) 
	{
		return 1.0f;
	}

	@Override
	public int summonDemons(TEDemonPortal teDemonPortal, World world, int x, int y, int z, DemonType type, int tier, boolean spawnGuardian) 
	{
		EntityMinorDemonGrunt entity;
		
		switch(type)
		{
		case FIRE:
			entity = new EntityMinorDemonGruntFire(world);
			break;
		case ICE:
			entity = new EntityMinorDemonGruntIce(world);
			break;
		case WATER:
			entity = new EntityMinorDemonGruntEarth(world);
			break;
		case WIND:
			entity = new EntityMinorDemonGruntWind(world);
			break;
		case NORMAL:
		default:
			entity = new EntityMinorDemonGrunt(world);
			break;
		
		}
		
		entity.setPosition(x, y, z);
		
		world.spawnEntityInWorld(entity);
		
		teDemonPortal.enthrallDemon(entity);
		entity.setAggro(true);
		entity.setDropCrystal(false);
		
		return spawnGuardian ? 3 : 1;
	}
}
