package wayoftime.bloodmagic.common.item.arc;

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
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.uses", stack.getMaxDamage() - stack.getDamageValue()).withStyle(TextFormatting.GRAY));

		if (getCraftingSpeedMultiplier(stack) != 1)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.craftspeed", ChatUtil.DECIMAL_FORMAT.format(getCraftingSpeedMultiplier(stack))).withStyle(TextFormatting.GRAY));

		if (getAdditionalOutputChanceMultiplier(stack) != 1)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.additionaldrops", ChatUtil.DECIMAL_FORMAT.format(getAdditionalOutputChanceMultiplier(stack))).withStyle(TextFormatting.GRAY));

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
