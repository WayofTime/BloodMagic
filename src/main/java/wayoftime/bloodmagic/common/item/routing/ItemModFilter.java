package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemModFilter extends ItemRouterFilter implements INestableItemFilterProvider
{
	public ItemModFilter()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.modfilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

//		super.addInformation(filterStack, world, tooltip, flag);
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		String namespace = ghostStack.getItem().getRegistryName().getNamespace();

		return new ModFilterKey(namespace, amount);
	}
}
