package wayoftime.bloodmagic.common.item.arc;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.ChatUtil;

public class ItemARCToolBase extends Item implements IARCTool
{
	private final double craftingMultiplier;
	private final double additionalOutputChance;

	public ItemARCToolBase(int maxDamage, double craftingMultiplier)
	{
		this(maxDamage, craftingMultiplier, 1);
	}

	public ItemARCToolBase(int maxDamage, double craftingMultiplier, double additionalOutputChance)
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB).durability(maxDamage));
		this.craftingMultiplier = craftingMultiplier;
		this.additionalOutputChance = additionalOutputChance;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.arctool.uses", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));

		if (getCraftingSpeedMultiplier(stack) != 1)
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.arctool.craftspeed", ChatUtil.DECIMAL_FORMAT.format(getCraftingSpeedMultiplier(stack))).withStyle(ChatFormatting.GRAY));

		if (getAdditionalOutputChanceMultiplier(stack) != 1)
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.arctool.additionaldrops", ChatUtil.DECIMAL_FORMAT.format(getAdditionalOutputChanceMultiplier(stack))).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}

	@Override
	public double getCraftingSpeedMultiplier(ItemStack stack)
	{
		return craftingMultiplier;
	}

	@Override
	public double getAdditionalOutputChanceMultiplier(ItemStack stack)
	{
		return additionalOutputChance;
	}
}
