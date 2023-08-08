package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.Constants;

import java.util.List;

public class ItemLivingTomeScrap extends Item implements ILivingUpgradePointsProvider
{
	public ItemLivingTomeScrap()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.livingtomescrap.desc").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bloodmagic.livingtomescrap.points", getTotalUpgradePoints(stack)).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public int getAvailableUpgradePoints(ItemStack stack, int drain)
	{
		return Math.min(getTotalUpgradePoints(stack), drain);
	}

	@Override
	public ItemStack getResultingStack(ItemStack stack, int syphonedPoints)
	{
		if (canSyphonPoints(stack, syphonedPoints))
		{
			int totalPoints = getTotalUpgradePoints(stack);
			int remaining = Math.max(0, totalPoints - syphonedPoints);

			if (remaining > 0)
			{
				ItemStack newStack = stack.copy();
				setTotalUpgradePoints(newStack, remaining);

				return newStack;
			} else
			{
				return ItemStack.EMPTY;
			}
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

	public void setTotalUpgradePoints(ItemStack stack, int points)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.POINTS, points);
	}

	@Override
	public int getTotalUpgradePoints(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		return tag.getInt(Constants.NBT.POINTS);
	}

	@Override
	public int getPriority(ItemStack stack)
	{
		return 0;
	}
}
