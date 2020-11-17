package wayoftime.bloodmagic.common.item.arc;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.item.IARCTool;
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
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB).maxDamage(maxDamage));
		this.craftingMultiplier = craftingMultiplier;
		this.additionalOutputChance = additionalOutputChance;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.uses", stack.getMaxDamage() - stack.getDamage()));

		if (getAdditionalOutputChanceMultiplier(stack) != 1)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.additionaldrops", ChatUtil.DECIMAL_FORMAT.format(getAdditionalOutputChanceMultiplier(stack))));

		super.addInformation(stack, world, tooltip, flag);
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
