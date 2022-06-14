package wayoftime.bloodmagic.common.routing;

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
import wayoftime.bloodmagic.common.item.routing.IRouterUpgrade;

public class ItemBasicNodeUpgrade extends Item implements IRouterUpgrade
{
	public ItemBasicNodeUpgrade()
	{
		super(new Item.Properties().stacksTo(16).tab(BloodMagic.TAB));
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
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.basicnodeupgrade").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
	}
}
