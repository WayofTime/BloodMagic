package wayoftime.bloodmagic.will;

import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public enum EnumDemonWillType implements IStringSerializable
{
	DEFAULT("default"),
	CORROSIVE("corrosive"),
	DESTRUCTIVE("destructive"),
	VENGEFUL("vengeful"),
	STEADFAST("steadfast");

	public final String name;

	EnumDemonWillType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String getString()
	{
		return this.toString();
	}

	public ItemStack getStack()
	{
		switch (this)
		{
		case CORROSIVE:
			return new ItemStack(BloodMagicItems.CORROSIVE_CRYSTAL.get());
		case DEFAULT:
			return new ItemStack(BloodMagicItems.RAW_CRYSTAL.get());
		case DESTRUCTIVE:
			return new ItemStack(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
		case STEADFAST:
			return new ItemStack(BloodMagicItems.STEADFAST_CRYSTAL.get());
		case VENGEFUL:
			return new ItemStack(BloodMagicItems.VENGEFUL_CRYSTAL.get());
		default:
			return ItemStack.EMPTY;
		}
	}
}