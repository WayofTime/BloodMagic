package wayoftime.bloodmagic.common.item.potion;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.entity.projectile.EntityPotionFlask;

public class ItemAlchemyFlaskLingering extends ItemAlchemyFlaskThrowable
{
	@Override
	public void prepPotionFlask(ItemStack stack, PlayerEntity player, EntityPotionFlask potionEntity)
	{
		potionEntity.setIsLingering(true);
	}

	@Override
	public double getDurationModifier(ItemStack stack)
	{
		return 0.25;
	}
}
