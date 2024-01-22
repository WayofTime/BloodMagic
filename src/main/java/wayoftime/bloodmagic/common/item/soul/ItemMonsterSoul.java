package wayoftime.bloodmagic.common.item.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

import java.util.List;

public class ItemMonsterSoul extends Item implements IDemonWill
{
	private final EnumDemonWillType type;

	public ItemMonsterSoul(EnumDemonWillType type)
	{
		super(new Item.Properties().stacksTo(1));
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;
		tooltip.add(Component.translatable("tooltip.bloodmagic.will", ChatUtil.DECIMAL_FORMAT.format(getWill(getType(stack), stack))).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}

	@Override
	public EnumDemonWillType getType(ItemStack stack)
	{
		return type;
	}

	@Override
	public double getWill(EnumDemonWillType type, ItemStack soulStack)
	{
		if (type != this.getType(soulStack))
		{
			return 0;
		}

		NBTHelper.checkNBT(soulStack);

		CompoundTag tag = soulStack.getTag();

		return tag.getDouble(Constants.NBT.SOULS);
	}


	@Override
	public boolean setWill(EnumDemonWillType type, ItemStack soulStack, double souls)
	{
		if (type != this.getType(soulStack))
		{
			return false;
		}

		NBTHelper.checkNBT(soulStack);
		CompoundTag tag = soulStack.getTag();
		tag.putDouble(Constants.NBT.SOULS, souls);

		return true;
	}

	@Override
	public double drainWill(EnumDemonWillType type, ItemStack soulStack, double drainAmount)
	{
		double souls = getWill(type, soulStack);

		double soulsDrained = Math.min(drainAmount, souls);
		setWill(type, soulStack, souls - soulsDrained);

		return soulsDrained;
	}

	@Override
	public ItemStack createWill(double number)
	{
		ItemStack soulStack = new ItemStack(this);
		setWill(getType(soulStack), soulStack, number);
		return soulStack;
	}

//	@Override
//	public double getWill(ItemStack willStack)
//	{
//		return this.getWill(EnumDemonWillType.DEFAULT, willStack);
//	}
//
//	@Override
//	public void setWill(ItemStack willStack, double will)
//	{
//		this.setWill(EnumDemonWillType.DEFAULT, willStack, will);
//	}
//
//	@Override
//	public double drainWill(ItemStack willStack, double drainAmount)
//	{
//		return this.drainWill(EnumDemonWillType.DEFAULT, willStack, drainAmount);
//	}

}