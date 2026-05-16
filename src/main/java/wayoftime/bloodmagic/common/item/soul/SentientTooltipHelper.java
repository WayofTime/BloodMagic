package wayoftime.bloodmagic.common.item.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public final class SentientTooltipHelper
{
	private SentientTooltipHelper() {}

	public static ChatFormatting colorFor(EnumDemonWillType type)
	{
		if (type == null)
			return ChatFormatting.GRAY;
		switch (type)
		{
		case CORROSIVE:
			return ChatFormatting.GREEN;
		case DESTRUCTIVE:
			return ChatFormatting.GOLD;
		case VENGEFUL:
			return ChatFormatting.RED;
		case STEADFAST:
			return ChatFormatting.LIGHT_PURPLE;
		case DEFAULT:
		default:
			return ChatFormatting.DARK_AQUA;
		}
	}

	public static void appendSentientTooltip(
			List<Component> tooltip,
			String flavourKey,
			EnumDemonWillType type,
			int level,
			double bonusDamage,
			Double aoeRadius,
			Double digSpeedBonus,
			int corrosiveWitherTimeTicks,
			int corrosiveWitherAmplifier,
			int steadfastAbsorptionTimeTicks,
			double vengefulMovementSpeed)
	{
		tooltip.add(Component.translatable(flavourKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

		ChatFormatting color = colorFor(type);
		String typeKey = "tooltip.bloodmagic.currentBaseType." + type.name().toLowerCase(Locale.ROOT);
		Component typeName = Component.translatable(typeKey).withStyle(color);
		tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.attuned", typeName).withStyle(ChatFormatting.GRAY));

		if (!Screen.hasShiftDown())
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.DARK_GRAY));
			return;
		}

		Player player = Minecraft.getInstance().player;
		if (level >= 0 && type != EnumDemonWillType.DEFAULT && player != null)
		{
			double pool = PlayerDemonWillHandler.getTotalDemonWill(type, player);
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.levelPool", level + 1, formatNumber(pool)).withStyle(color));
		} else
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.inactive").withStyle(ChatFormatting.DARK_GRAY));
			return;
		}

		if (bonusDamage > 0)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.bonusDamage", formatSigned(bonusDamage)).withStyle(ChatFormatting.GRAY));
		}
		if (aoeRadius != null)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.aoe", formatNumber(aoeRadius)).withStyle(ChatFormatting.GRAY));
		}

		switch (type)
		{
		case CORROSIVE:
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.rider.corrosive",
					formatNumber(corrosiveWitherTimeTicks / 20.0),
					corrosiveWitherAmplifier + 1).withStyle(color));
			break;
		case STEADFAST:
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.rider.steadfast",
					formatNumber(steadfastAbsorptionTimeTicks / 20.0)).withStyle(color));
			break;
		case VENGEFUL:
			if (vengefulMovementSpeed > 0)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.rider.vengeful",
						formatSigned(vengefulMovementSpeed)).withStyle(color));
			}
			break;
		default:
			break;
		}
		if (digSpeedBonus != null && digSpeedBonus > 0)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.sentient.rider.digspeed", formatSigned(digSpeedBonus)).withStyle(ChatFormatting.GRAY));
		}
	}

	private static String formatNumber(double value)
	{
		if (value == Math.floor(value) && !Double.isInfinite(value))
		{
			return String.format(Locale.ROOT, "%d", (long) value);
		}
		String s = String.format(Locale.ROOT, "%.2f", value);
		if (s.contains("."))
		{
			s = s.replaceAll("0+$", "");
			if (s.endsWith("."))
				s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	private static String formatSigned(double value)
	{
		String sign = value >= 0 ? "+" : "";
		return sign + formatNumber(value);
	}

}
