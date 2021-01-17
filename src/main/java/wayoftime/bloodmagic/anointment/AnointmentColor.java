package wayoftime.bloodmagic.anointment;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.common.item.ItemAnointmentProvider;

public class AnointmentColor implements IItemColor
{
	@Override
	public int getColor(ItemStack stack, int layer)
	{
		if (layer == 1 && stack.getItem() instanceof ItemAnointmentProvider)
		{
			return ((ItemAnointmentProvider) stack.getItem()).getColor();
		}

		return 0xFFFFFF;
	}
}
