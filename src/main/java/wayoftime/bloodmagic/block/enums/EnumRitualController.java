package wayoftime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

//TODO: Will want to probably discontinue this due to The Flattening
public enum EnumRitualController implements IStringSerializable
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
