package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfDefensiveWind extends SelfSpellEffect
{
	public SelfDefensiveWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		double radius = 1.5d*this.powerUpgrades+1.5d;
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;
		
		List<Entity> entities = SpellHelper.getEntitiesInRange(world, posX, posY, posZ, radius, radius);
		
		for(Entity entity: entities)
		{
			if((!entity.equals(player))&&entity instanceof EntityLiving)
			{
				((EntityLiving)entity).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionHeavyHeart.id,200,this.potencyUpgrades));
			}
		}
	}
}
