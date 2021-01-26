package wayoftime.bloodmagic.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDaggerSyringe;

public class ItemThrowingDaggerSyringe extends ItemThrowingDagger
{
	@Override
	public AbstractEntityThrowingDagger getDagger(ItemStack stack, World world, PlayerEntity player)
	{
		AbstractEntityThrowingDagger dagger = new EntityThrowingDaggerSyringe(stack, world, player);
		dagger.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 3F, 0.5F);
		dagger.setDamage(8);
		return dagger;
	}
}
