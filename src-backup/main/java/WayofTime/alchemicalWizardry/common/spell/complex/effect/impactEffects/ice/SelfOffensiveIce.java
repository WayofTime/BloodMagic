package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfOffensiveIce extends SelfSpellEffect
{
	public SelfOffensiveIce(int power, int potency, int cost)
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		double horizRadius = this.powerUpgrades+1;
		double vertRadius = 0.5*this.powerUpgrades+1;
		
		List<Entity> entities = SpellHelper.getEntitiesInRange(world, player.posX, player.posY, player.posZ,horizRadius, vertRadius);
		
		if(entities==null)
		{
			return;
		}
		
		int i=0;
		int number = this.powerUpgrades+1;
		
		for(Entity entity : entities)
		{
			if(i>=number)
			{
				continue;
			}
			
			if(entity instanceof EntityLivingBase && !entity.equals(player))
			{
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,60*(1+this.powerUpgrades),this.potencyUpgrades));
				
				i++;
			}
		}
	}
}
