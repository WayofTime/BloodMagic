package wayoftime.bloodmagic.common.item.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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
import wayoftime.bloodmagic.api.compat.IDemonWillGem;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

import java.util.List;
import java.util.Locale;

public class ItemSoulGem extends Item implements IDemonWillGem, IMultiWillTool
{
	private final int maxWill;
	private final String name;

	public ItemSoulGem(String name, int maxWill)
	{
		super(new Item.Properties().stacksTo(1));
		this.name = name;
		this.maxWill = maxWill;
	}
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		EnumDemonWillType type = this.getCurrentType(stack);
		double drain = Math.min(this.getWill(type, stack), this.getMaxWill(type, stack) / 10);

		double filled = PlayerDemonWillHandler.addDemonWill(type, player, drain, stack);
		this.drainWill(type, stack, filled, true);

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		EnumDemonWillType type = this.getCurrentType(stack);
		tooltip.add(Component.translatable("tooltip.bloodmagic.soulGem." + name).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bloodmagic.will", ChatUtil.DECIMAL_FORMAT.format(getWill(type, stack))).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bloodmagic.currentType." + getCurrentType(stack).name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		EnumDemonWillType type = this.getCurrentType(stack);
		double maxWill = getMaxWill(type, stack);
		if (maxWill <= 0)
		{
			return 0;
		}
		return (int) ((getWill(type, stack) * 13.0F / maxWill));
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		EnumDemonWillType type = this.getCurrentType(stack);
		double maxWill = getMaxWill(type, stack);
		if (maxWill <= 0)
		{
			return 1;
		}

		return Mth.hsvToRgb(Math.max(0.0F, (float) (getWill(type, stack)) / (float) maxWill) / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public ItemStack fillDemonWillGem(ItemStack soulGemStack, ItemStack soulStack)
	{
		if (soulStack != null && soulStack.getItem() instanceof IDemonWill)
		{
			EnumDemonWillType thisType = this.getCurrentType(soulGemStack);
			if (thisType != ((IDemonWill) soulStack.getItem()).getType(soulStack))
			{
				return soulStack;
			}
			IDemonWill soul = (IDemonWill) soulStack.getItem();
			double soulsLeft = getWill(thisType, soulGemStack);

			if (soulsLeft < getMaxWill(thisType, soulGemStack))
			{
				double newSoulsLeft = Math.min(soulsLeft + soul.getWill(thisType, soulStack), getMaxWill(thisType, soulGemStack));
				soul.drainWill(thisType, soulStack, newSoulsLeft - soulsLeft);

				setWill(thisType, soulGemStack, newSoulsLeft);
				if (soul.getWill(thisType, soulStack) <= 0)
				{
					return ItemStack.EMPTY;
				}
			}
		}

		return soulStack;
	}

	@Override
	public double getWill(EnumDemonWillType type, ItemStack soulGemStack)
	{
		if (!type.equals(getCurrentType(soulGemStack)))
		{
			return 0;
		}

		CompoundTag tag = soulGemStack.getTag();

		return tag.getDouble(Constants.NBT.SOULS);
	}

	@Override
	public void setWill(EnumDemonWillType type, ItemStack soulGemStack, double souls)
	{
		setCurrentType(type, soulGemStack);

		CompoundTag tag = soulGemStack.getTag();

		tag.putDouble(Constants.NBT.SOULS, souls);
	}

	@Override
	public double drainWill(EnumDemonWillType type, ItemStack soulGemStack, double drainAmount, boolean doDrain)
	{
		EnumDemonWillType currentType = this.getCurrentType(soulGemStack);
		if (currentType != type)
		{
			return 0;
		}
		double souls = getWill(type, soulGemStack);

		double soulsDrained = Math.min(drainAmount, souls);

		if (doDrain)
		{
			setWill(type, soulGemStack, souls - soulsDrained);
		}

		return soulsDrained;
	}

	@Override
	public int getMaxWill(EnumDemonWillType type, ItemStack soulGemStack)
	{
		EnumDemonWillType currentType = getCurrentType(soulGemStack);
		if (!type.equals(currentType) && currentType != EnumDemonWillType.DEFAULT)
		{
			return 0;
		}

		return maxWill;
	}

	@Override
	public EnumDemonWillType getCurrentType(ItemStack soulGemStack)
	{
		NBTHelper.checkNBT(soulGemStack);

		CompoundTag tag = soulGemStack.getTag();

		if (!tag.contains(Constants.NBT.WILL_TYPE))
		{
			return EnumDemonWillType.DEFAULT;
		}

		return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ROOT));
	}

	public void setCurrentType(EnumDemonWillType type, ItemStack soulGemStack)
	{
		NBTHelper.checkNBT(soulGemStack);

		CompoundTag tag = soulGemStack.getTag();

		if (type == EnumDemonWillType.DEFAULT)
		{
			if (tag.contains(Constants.NBT.WILL_TYPE))
			{
				tag.remove(Constants.NBT.WILL_TYPE);
			}

			return;
		}

		tag.putString(Constants.NBT.WILL_TYPE, type.toString());
	}

	@Override
	public double fillWill(EnumDemonWillType type, ItemStack stack, double fillAmount, boolean doFill)
	{
		if (!type.equals(getCurrentType(stack)) && this.getWill(getCurrentType(stack), stack) > 0)
		{
			return 0;
		}

		double current = this.getWill(type, stack);
		double maxWill = this.getMaxWill(type, stack);

		double filled = Math.min(fillAmount, maxWill - current);

		if (doFill)
		{
			this.setWill(type, stack, filled + current);
		}

		return filled;
	}
}