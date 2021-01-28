package wayoftime.bloodmagic.common.item.soul;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;

public class ItemMonsterSoul extends Item implements IDemonWill
{
	private final EnumDemonWillType type;

	public ItemMonsterSoul(EnumDemonWillType type)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.will", ChatUtil.DECIMAL_FORMAT.format(getWill(getType(stack), stack))).mergeStyle(TextFormatting.GRAY));

		super.addInformation(stack, world, tooltip, flag);
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

		CompoundNBT tag = soulStack.getTag();

		return tag.getDouble(Constants.NBT.SOULS);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (this.isInGroup(group))
		{
			ItemStack stack = new ItemStack(this);
			this.setWill(type, stack, 5);
			items.add(stack);
		}
	}

	@Override
	public boolean setWill(EnumDemonWillType type, ItemStack soulStack, double souls)
	{
		if (type != this.getType(soulStack))
		{
			return false;
		}

		NBTHelper.checkNBT(soulStack);
		CompoundNBT tag = soulStack.getTag();
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