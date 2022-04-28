package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;

public class ItemSyntheticPoint extends Item implements IDowngradePointProvider
{
	public ItemSyntheticPoint()
	{
		super(new Item.Properties().group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.syntheticpoint.desc").mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public int getTotalPoints(ItemStack stack)
	{
		return stack.getCount();
	}

	@Override
	public int getAvailablePoints(ItemStack stack, int syphonPoints)
	{
		return Math.min(syphonPoints, getTotalPoints(stack));
	}

	@Override
	public boolean canSyphonPoints(ItemStack stack, int syphonPoints)
	{
		return true;
	}

	@Override
	public ItemStack getResultingStack(ItemStack stack, int syphonedPoints)
	{
		if (canSyphonPoints(stack, syphonedPoints))
		{
			ItemStack newStack = stack.copy();
			newStack.setCount(Math.max(0, stack.getCount() - syphonedPoints));

			return newStack;
		}

		return stack;
	}
}
