package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardian;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianWind;
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

		{
			switch(type)
			{
			case FIRE:
				if(spawnGuardian)
				{
					entity = new EntityMinorDemonGruntGuardianFire(world);
				}else
				{
					entity = new EntityMinorDemonGruntFire(world);
				}
				break;
			case ICE:
				if(spawnGuardian)
				{
					entity = new EntityMinorDemonGruntGuardianIce(world);
				}else
				{
					entity = new EntityMinorDemonGruntIce(world);
				}
				break;
			case EARTH:
				if(spawnGuardian)
				{
					entity = new EntityMinorDemonGruntGuardianEarth(world);
				}else
				{
					entity = new EntityMinorDemonGruntEarth(world);
				}
				break;
			case WIND:
				if(spawnGuardian)
				{
					entity = new EntityMinorDemonGruntGuardianWind(world);
				}else
				{
					entity = new EntityMinorDemonGruntWind(world);
				}
				break;
			case NORMAL:
			default:
				if(spawnGuardian)
				{
					entity = new EntityMinorDemonGruntGuardian(world);
				}else
				{
					entity = new EntityMinorDemonGrunt(world);
				}
				break;
			
			}
		}
		
		entity.setPosition(x, y, z);
		
		world.spawnEntityInWorld(entity);
		
		teDemonPortal.enthrallDemon(entity);
		entity.setAggro(true);
		entity.setDropCrystal(false);
		
		return spawnGuardian ? 3 : 1;
	}
}
