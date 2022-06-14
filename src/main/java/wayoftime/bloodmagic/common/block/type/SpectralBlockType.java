package wayoftime.bloodmagic.common.block.type;

import net.minecraft.util.StringRepresentable;

public enum SpectralBlockType implements StringRepresentable
{
	SOLID("solid"),
	LEAKING("leaking");

	private final String name;

	private SpectralBlockType(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return this.name;
	}

	public String getSerializedName()
	{
		return this.name;
	}
}