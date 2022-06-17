package wayoftime.bloodmagic.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDaggerSyringe;

public class ItemThrowingDaggerSyringe extends ItemThrowingDagger
{
	@Override
	public AbstractEntityThrowingDagger getDagger(ItemStack stack, Level world, Player player)
	{
		AbstractEntityThrowingDagger dagger = new EntityThrowingDaggerSyringe(stack, world, player);
		dagger.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.5F);
		dagger.setDamage(8);
		return dagger;
	}
}
