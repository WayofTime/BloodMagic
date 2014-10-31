package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfOffensiveFire extends SelfSpellEffect 
{
	public SelfOffensiveFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFlameCloak.id,300*(2*this.powerUpgrades+1),this.potencyUpgrades));
	}
}
