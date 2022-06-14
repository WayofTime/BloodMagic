package wayoftime.bloodmagic.anointment;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.ItemAnointmentProvider;

public class AnointmentColor implements ItemColor
{
	@Override
	public int getColor(ItemStack stack, int layer)
	{
		if (layer == 0 && stack.getItem() instanceof ItemAnointmentProvider)
		{
			return ((ItemAnointmentProvider) stack.getItem()).getColor();
		}

		return 0xFFFFFF;
	}
}
