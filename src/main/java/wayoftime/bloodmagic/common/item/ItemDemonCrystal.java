package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDiscreteDemonWill;

public class ItemDemonCrystal extends Item implements IDiscreteDemonWill
{
	private EnumDemonWillType type;

	public ItemDemonCrystal(EnumDemonWillType type)
	{
		super(new Item.Properties());
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
