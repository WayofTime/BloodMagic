package wayoftime.bloodmagic.common.item.soul;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.will.IMultiWillTool;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.api.will.EnumDemonWillType;
import wayoftime.bloodmagic.api.will.IDemonWill;
import wayoftime.bloodmagic.api.will.IDemonWillGem;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class ItemSoulGem extends Item implements IDemonWillGem, IMultiWillTool
{
	private final int maxWill;
	private final String name;

	public ItemSoulGem(String name, int maxWill)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
		this.name = name;
		this.maxWill = maxWill;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (this.isInGroup(group))
		{
			for (EnumDemonWillType type : EnumDemonWillType.values())
			{
				ItemStack stack = new ItemStack(this);
				this.setCurrentType(type, stack);
				this.setWill(type, stack, maxWill);
				items.add(stack);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		EnumDemonWillType type = this.getCurrentType(stack);
		double drain = Math.min(this.getWill(type, stack), this.getMaxWill(type, stack) / 10);

		double filled = PlayerDemonWillHandler.addDemonWill(type, player, drain, stack);
		this.drainWill(type, stack, filled, true);

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		EnumDemonWillType type = this.getCurrentType(stack);
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.soulGem." + name));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.will", ChatUtil.DECIMAL_FORMAT.format(getWill(type, stack))));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.currentType." + getCurrentType(stack).name().toLowerCase()));

		super.addInformation(stack, world, tooltip, flag);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		EnumDemonWillType type = this.getCurrentType(stack);
		double maxWill = getMaxWill(type, stack);
		if (maxWill <= 0)
		{
			return 1;
		}
		return 1.0 - (getWill(type, stack) / maxWill);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		EnumDemonWillType type = this.getCurrentType(stack);
		double maxWill = getMaxWill(type, stack);
		if (maxWill <= 0)
		{
			return 1;
		}

		return MathHelper.hsvToRGB(Math.max(0.0F, (float) (getWill(type, stack)) / (float) maxWill) / 3.0F, 1.0F, 1.0F);
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

		CompoundNBT tag = soulGemStack.getTag();

		return tag.getDouble(Constants.NBT.SOULS);
	}

	@Override
	public void setWill(EnumDemonWillType type, ItemStack soulGemStack, double souls)
	{
		setCurrentType(type, soulGemStack);

		CompoundNBT tag = soulGemStack.getTag();

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

		CompoundNBT tag = soulGemStack.getTag();

		if (!tag.contains(Constants.NBT.WILL_TYPE))
		{
			return EnumDemonWillType.DEFAULT;
		}

		return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
	}

	public void setCurrentType(EnumDemonWillType type, ItemStack soulGemStack)
	{
		NBTHelper.checkNBT(soulGemStack);

		CompoundNBT tag = soulGemStack.getTag();

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