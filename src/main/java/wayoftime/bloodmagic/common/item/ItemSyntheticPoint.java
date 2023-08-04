package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;

import java.util.List;

public class ItemSyntheticPoint extends Item implements ILivingUpgradePointsProvider
{
	public ItemSyntheticPoint()
	{
		super(new Item.Properties());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.syntheticpoint.desc").withStyle(ChatFormatting.GRAY));
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

	@Override
	public int getPriority(ItemStack stack)
	{
		return 5;
	}
}
