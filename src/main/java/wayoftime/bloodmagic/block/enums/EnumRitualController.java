package wayoftime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

//TODO: Will want to probably discontinue this due to The Flattening
public enum EnumRitualController implements StringRepresentable
{
	MASTER,
	IMPERFECT,
	INVERTED,;

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
}
