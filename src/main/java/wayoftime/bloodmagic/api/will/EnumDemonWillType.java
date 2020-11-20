package wayoftime.bloodmagic.api.will;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

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

	public static EnumDemonWillType getType(String type)
	{
		for (EnumDemonWillType t : values())
		{
			if (t.name().equalsIgnoreCase(type))
			{
				return t;
			}
		}

		return null;
	}
}