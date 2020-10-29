package wayoftime.bloodmagic.will;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

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
}