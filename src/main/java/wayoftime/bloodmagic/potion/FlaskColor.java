package wayoftime.bloodmagic.potion;

import java.util.Collection;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;

public class FlaskColor implements IItemColor
{
	@Override
	public int getColor(ItemStack stack, int layer)
	{
		if (layer == 0 || layer == 2 && stack.getItem() instanceof ItemAlchemyFlask)
		{
			Collection<EffectInstance> instanceList = PotionUtils.getEffectsFromStack(stack);
			int color = instanceList.isEmpty() ? PotionUtils.getPotionColor(Potions.WATER)
					: PotionUtils.getPotionColorFromEffectList(instanceList);
//			return color == 16253176 ? PotionUtils.getPotionColor(Potions.WATER) : color;
			return color;
		}

		return 0xFFFFFF;
	}
}
