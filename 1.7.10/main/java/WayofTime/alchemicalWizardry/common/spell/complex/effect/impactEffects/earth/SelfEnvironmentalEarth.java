package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfEnvironmentalEarth extends SelfSpellEffect
{
	public SelfEnvironmentalEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		float radius = this.powerUpgrades*2 + 1.5f;
		int dur = this.powerUpgrades*5*20 + 60;
		
		List<Entity> entities = SpellHelper.getEntitiesInRange(world, player.posX, player.posY, player.posZ, radius, radius);
		
		for(Entity entity : entities)
		{
			if(entity instanceof EntityLiving)
			{
				((EntityLiving) entity).addPotionEffect(new PotionEffect(Potion.weakness.id,dur,this.potencyUpgrades));
			}
		}
	}
}
