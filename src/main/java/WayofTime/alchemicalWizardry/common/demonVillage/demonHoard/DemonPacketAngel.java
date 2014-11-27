package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonPacketAngel extends DemonHoardPacket 
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
		EntityLivingBase entity = new EntityMinorDemonGrunt(world);
		entity.setPosition(x, y, z);
		
		world.spawnEntityInWorld(entity);
		
		teDemonPortal.enthrallDemon(entity);
		
		return 1;
	}
}
