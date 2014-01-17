package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;

public class EntityFireElemental extends EntityElemental implements IMob
{
	public EntityFireElemental(World world)
	{
		super(world, AlchemicalWizardry.entityFireElementalID);
		isImmuneToFire = true;
	}

	@Override
	public void inflictEffectOnEntity(Entity target)
	{
		if (target instanceof EntityLivingBase)
		{
			((EntityLivingBase)target).setFire(10);
			((EntityLivingBase)target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
		}
	}
}