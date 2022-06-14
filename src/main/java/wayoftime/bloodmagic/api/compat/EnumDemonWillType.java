package wayoftime.bloodmagic.api.compat;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum EnumDemonWillType implements StringRepresentable
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
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public String getSerializedName()
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