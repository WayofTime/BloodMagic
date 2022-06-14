package wayoftime.bloodmagic.common.block.type;

import net.minecraft.util.StringRepresentable;

public enum PillarCapType implements StringRepresentable
{
	TOP("top"),
	BOTTOM("bottom");

	private final String name;

	private PillarCapType(String name)
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
