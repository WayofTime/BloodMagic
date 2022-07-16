package wayoftime.bloodmagic.potion;

import java.util.Collection;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import wayoftime.bloodmagic.common.item.ItemThrowingDagger;

public class TippedDaggerColor implements ItemColor
{
	@Override
	public int getColor(ItemStack stack, int layer)
	{
		if (layer == 1 && stack.getItem() instanceof ItemThrowingDagger)
		{
			Collection<MobEffectInstance> instanceList = PotionUtils.getMobEffects(stack);
			int color = instanceList.isEmpty() ? PotionUtils.getColor(Potions.WATER)
					: PotionUtils.getColor(instanceList);
//			return color == 16253176 ? PotionUtils.getPotionColor(Potions.WATER) : color;
			return color;
		}

		return 0xFFFFFF;
	}
}