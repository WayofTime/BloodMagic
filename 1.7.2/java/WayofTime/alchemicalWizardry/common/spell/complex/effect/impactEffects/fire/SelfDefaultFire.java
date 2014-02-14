package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ISelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SelfDefaultFire extends SelfSpellEffect
{
	public SelfDefaultFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		player.setFire((int)(10*Math.pow(1.5, powerUpgrades+1.5*potencyUpgrades)));
	}
}
