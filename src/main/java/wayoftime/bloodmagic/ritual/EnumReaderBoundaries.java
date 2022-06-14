package wayoftime.bloodmagic.ritual;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum EnumReaderBoundaries implements StringRepresentable
{
	SUCCESS, VOLUME_TOO_LARGE, NOT_WITHIN_BOUNDARIES;

	@Override
	public String toString()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public String getSerializedName()
	{
		return toString();
	}
}