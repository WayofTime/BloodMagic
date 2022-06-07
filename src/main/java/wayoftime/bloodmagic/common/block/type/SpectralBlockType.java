package wayoftime.bloodmagic.common.block.type;

import net.minecraft.util.IStringSerializable;

public enum SpectralBlockType implements IStringSerializable
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

	public String getString()
	{
		return this.name;
	}
}