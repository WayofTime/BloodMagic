package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.IDemonWillViewer;
import wayoftime.bloodmagic.util.handler.event.GenericHandler;

public class ItemDemonWillGauge extends Item implements IDemonWillViewer
{
	public ItemDemonWillGauge()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.willGauge"));
	}

	@Override
	public boolean canSeeDemonWillAura(World world, ItemStack stack, PlayerEntity player)
	{
		return true;
	}

	@Override
	public int getDemonWillAuraResolution(World world, ItemStack stack, PlayerEntity player)
	{
		return 100;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (entityIn instanceof PlayerEntity && entityIn.ticksExisted % 50 == 0)
		{
			GenericHandler.sendPlayerDemonWillAura((PlayerEntity) entityIn);
		}
	}
}