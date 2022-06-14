package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.IDemonWillViewer;
import wayoftime.bloodmagic.util.handler.event.GenericHandler;

public class ItemDemonWillGauge extends Item implements IDemonWillViewer
{
	public ItemDemonWillGauge()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.willGauge").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean canSeeDemonWillAura(Level world, ItemStack stack, Player player)
	{
		return true;
	}

	@Override
	public int getDemonWillAuraResolution(Level world, ItemStack stack, Player player)
	{
		return 100;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (entityIn instanceof Player && entityIn.tickCount % 50 == 0)
		{
			GenericHandler.sendPlayerDemonWillAura((Player) entityIn);
		}
	}
}