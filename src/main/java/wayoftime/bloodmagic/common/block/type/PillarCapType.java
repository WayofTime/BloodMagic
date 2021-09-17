package wayoftime.bloodmagic.common.block.type;

import net.minecraft.util.IStringSerializable;

public enum PillarCapType implements IStringSerializable
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

	public String getString()
	{
		return this.name;
	}
}
