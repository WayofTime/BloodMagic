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

public class ItemSyntheticPoint extends Item implements ILivingUpgradePointsProvider
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
	public int getAvailableUpgradePoints(ItemStack stack, int drain)
	{
		return Math.min(getTotalUpgradePoints(stack), drain);
	}

	public int getTotalUpgradePoints(ItemStack stack)
	{
		return stack.getCount();
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

	@Override
	public int getExcessUpgradePoints(ItemStack stack, int drain)
	{
		return getTotalUpgradePoints(stack) - getAvailableUpgradePoints(stack, drain);
	}

	@Override
	public boolean canSyphonPoints(ItemStack stack, int drain)
	{
		return true;
	}
}
