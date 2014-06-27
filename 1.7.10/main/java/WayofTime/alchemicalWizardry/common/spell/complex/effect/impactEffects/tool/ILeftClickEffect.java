package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILeftClickEffect 
{
	public abstract int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder);
}
