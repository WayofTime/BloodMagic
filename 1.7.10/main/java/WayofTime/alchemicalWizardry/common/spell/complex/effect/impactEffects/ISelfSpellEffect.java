package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISelfSpellEffect 
{
	public void onSelfUse(World world, EntityPlayer player);
}
