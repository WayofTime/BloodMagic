package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SelfDefaultFire implements ISelfSpellEffect
{
	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		player.setFire(500);	
	}
}
