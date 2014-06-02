package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfDefensiveEarth extends SelfSpellEffect
{

	public SelfDefensiveEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		int pot = 2*this.potencyUpgrades + 1;
		int duration = 20*60*(this.powerUpgrades+1);
		
		player.addPotionEffect(new PotionEffect(Potion.field_76434_w.id,duration, pot));
		player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionHeavyHeart.id, duration, pot));
	}
}
