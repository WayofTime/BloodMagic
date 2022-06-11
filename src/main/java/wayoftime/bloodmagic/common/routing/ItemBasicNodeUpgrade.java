package wayoftime.bloodmagic.common.routing;

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
import wayoftime.bloodmagic.common.item.routing.IRouterUpgrade;

public class ItemBasicNodeUpgrade extends Item implements IRouterUpgrade
{
	public ItemBasicNodeUpgrade()
	{
		super(new Item.Properties().maxStackSize(16).group(BloodMagic.TAB));
	}

	@Override
	public int getMaxTransferIncrease(ItemStack stack)
	{
		return getTransferIncreasePerItem(stack) * stack.getCount();
	}

	protected int getTransferIncreasePerItem(ItemStack stack)
	{
		return 8;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.basicnodeupgrade").mergeStyle(TextFormatting.GRAY).mergeStyle(TextFormatting.ITALIC));
	}
}
