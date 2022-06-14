package wayoftime.bloodmagic.common.item.potion;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.entity.projectile.EntityPotionFlask;

public class ItemAlchemyFlaskLingering extends ItemAlchemyFlaskThrowable
{
	@Override
	public void prepPotionFlask(ItemStack stack, Player player, EntityPotionFlask potionEntity)
	{
		potionEntity.setIsLingering(true);
	}

	@Override
	public double getDurationModifier(ItemStack stack)
	{
		return 0.25;
	}
}
