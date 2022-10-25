package wayoftime.bloodmagic.core.living;

import java.util.List;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemLivingArmor;
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
			stack.setTag(new CompoundTag());

		stack.getTag().put("livingStats", stats.serialize());
	}

	static void setDisplayIfZero(ItemStack stack, boolean doDisplay)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
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
	static void appendLivingTooltip(ItemStack stack, LivingStats stats, List<Component> tooltip, boolean trainable)
	{
		if (stats != null)
		{
			if (trainable)
				tooltip.add(new TranslatableComponent("tooltip.bloodmagic.livingarmour.upgrade.points", stats.getUsedPoints(), stats.getMaxPoints()).withStyle(ChatFormatting.GOLD));

			String color = "Red";
			Item item = stack.getItem();
			if(item == BloodMagicItems.LIVING_PLATE.get()){
				color = ((ItemLivingArmor) item).getDyeColor(stack);
			}
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.livingarmour.color", I18n.get(String.format("color.minecraft.%s", color))).withStyle(ChatFormatting.GOLD));

			stats.getUpgrades().forEach((k, v) -> {
				if (k.getLevel(v.intValue()) <= 0 && !displayIfLevelZero(stack))
					return;

				boolean sneaking = Screen.hasShiftDown();
//				if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || k.getNextRequirement(v) == 0)
				if (!sneaking || k.getNextRequirement(v.intValue()) == 0)
				{
					int level = k.getLevel(v.intValue());
					if (level > 0)
						tooltip.add(new TranslatableComponent("%s %s", new TranslatableComponent(k.getTranslationKey()), new TranslatableComponent("enchantment.level." + level)).withStyle(ChatFormatting.GRAY));
					else
						tooltip.add(new TranslatableComponent(k.getTranslationKey()).withStyle(ChatFormatting.GRAY));
				} else
					tooltip.add(new TranslatableComponent("%s %s", new TranslatableComponent(k.getTranslationKey()), (": " + v.intValue() + "/" + k.getNextRequirement(v.intValue()))).withStyle(ChatFormatting.GRAY));
			});
		}
	}
}
