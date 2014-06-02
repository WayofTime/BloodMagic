package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IMeleeSpellWorldEffect 
{
	public void onWorldEffect(World world, EntityPlayer entityPlayer);
}
