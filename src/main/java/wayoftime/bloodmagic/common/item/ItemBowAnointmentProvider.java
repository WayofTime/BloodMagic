package wayoftime.bloodmagic.common.item;

import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBowAnointmentProvider extends ItemAnointmentProvider
{
	boolean crossbowsValid;

	public ItemBowAnointmentProvider(ResourceLocation anointRL, int colour, int level, int maxDamage, boolean crossbowsValid)
	{
		super(anointRL, colour, level, maxDamage);
		this.crossbowsValid = crossbowsValid;
	}

	public boolean isItemValidForApplication(ItemStack stack)
	{
		return isItemBow(stack) || (crossbowsValid && isItemCrossbow(stack));
	}

	public static boolean isItemBow(ItemStack stack)
	{
		return (stack.getItem() instanceof BowItem);
	}

	public static boolean isItemCrossbow(ItemStack stack)
	{
		return (stack.getItem() instanceof CrossbowItem);
	}
}
