package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IMeleeSpellEntityEffect 
{
	public void onEntityImpact(World world, EntityPlayer entityPlayer);
}
