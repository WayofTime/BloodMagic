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

public class SelfDefaultWind extends SelfSpellEffect
{
	public SelfDefaultWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		player.extinguish();
		player.fallDistance = 0;
	}
}
