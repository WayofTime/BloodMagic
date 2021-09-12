package wayoftime.bloodmagic.core.living;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.util.Constants;

public interface ILivingContainer
{
	default LivingStats getLivingStats(ItemStack stack)
	{
		if (!stack.hasTag() || !stack.getTag().contains("livingStats"))
			return null;

		return LivingStats.fromNBT(stack.getTag().getCompound("livingStats"));
	}

	default void updateLivingStats(ItemStack stack, LivingStats stats)
	{
		if (stats == null)
		{
			if (stack.hasTag())
				stack.getTag().remove("livingStats");
			return;
		}

		if (!stack.hasTag())
			stack.setTag(new CompoundNBT());

		stack.getTag().put("livingStats", stats.serialize());
	}

	static void setDisplayIfZero(ItemStack stack, boolean doDisplay)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		stack.getTag().putBoolean(Constants.NBT.UPGRADE_ZERO_DISPLAY, doDisplay);
	}

	static boolean displayIfLevelZero(ItemStack stack)
	{
		if (stack.hasTag())
		{
			return stack.getTag().getBoolean(Constants.NBT.UPGRADE_ZERO_DISPLAY);
		}

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	static void appendLivingTooltip(ItemStack stack, LivingStats stats, List<ITextComponent> tooltip, boolean trainable)
	{
		if (stats != null)
		{
			if (trainable)
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.livingarmour.upgrade.points", stats.getUsedPoints(), stats.getMaxPoints()).mergeStyle(TextFormatting.GOLD));

			stats.getUpgrades().forEach((k, v) -> {
				if (k.getLevel(v.intValue()) <= 0 && !displayIfLevelZero(stack))
					return;

				boolean sneaking = Screen.hasShiftDown();
//				if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || k.getNextRequirement(v) == 0)
				if (!sneaking || k.getNextRequirement(v.intValue()) == 0)
				{
					int level = k.getLevel(v.intValue());
					if (level > 0)
						tooltip.add(new TranslationTextComponent("%s %s", new TranslationTextComponent(k.getTranslationKey()), new TranslationTextComponent("enchantment.level." + level)).mergeStyle(TextFormatting.GRAY));
					else
						tooltip.add(new TranslationTextComponent(k.getTranslationKey()));
				} else
					tooltip.add(new TranslationTextComponent("%s %s", new TranslationTextComponent(k.getTranslationKey()), (": " + v.intValue() + "/" + k.getNextRequirement(v.intValue()))).mergeStyle(TextFormatting.GRAY));
			});
		}
	}
}
