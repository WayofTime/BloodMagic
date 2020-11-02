package wayoftime.bloodmagic.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.will.EnumDemonWillType;
import wayoftime.bloodmagic.will.IDiscreteDemonWill;

public class ItemDemonCrystal extends Item implements IDiscreteDemonWill
{
	private EnumDemonWillType type;

	public ItemDemonCrystal(EnumDemonWillType type)
	{
		super(new Item.Properties().group(BloodMagic.TAB));
		this.type = type;
	}

	@Override
	public double getWill(ItemStack willStack)
	{
		return getDiscretization(willStack) * willStack.getCount();
	}

	@Override
	public double drainWill(ItemStack willStack, double drainAmount)
	{
		double discretization = getDiscretization(willStack);
		int drainedNumber = (int) Math.floor(Math.min(willStack.getCount() * discretization, drainAmount)
				/ discretization);

		if (drainedNumber > 0)
		{
			willStack.shrink(drainedNumber);
			return drainedNumber * discretization;
		}

		return 0;
	}

	@Override
	public double getDiscretization(ItemStack willStack)
	{
		return 50;
	}

	@Override
	public EnumDemonWillType getType(ItemStack willStack)
	{
		return type;
	}
}
