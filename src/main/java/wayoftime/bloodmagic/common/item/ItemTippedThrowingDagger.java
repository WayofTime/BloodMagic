package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDagger;

public class ItemTippedThrowingDagger extends ItemThrowingDagger
{
	@Override
	public AbstractEntityThrowingDagger getDagger(ItemStack stack, Level world, Player player)
	{
		AbstractEntityThrowingDagger dagger = new EntityThrowingDagger(stack, world, player);
		dagger.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.5F);
		dagger.setDamage(10);
		dagger.setEffectsFromItem(stack);
		return dagger;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);

		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);

	}
}
