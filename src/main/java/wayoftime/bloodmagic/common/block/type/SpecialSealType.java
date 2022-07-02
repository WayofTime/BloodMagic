package wayoftime.bloodmagic.common.block.type;

import net.minecraft.util.StringRepresentable;

public enum SpecialSealType implements StringRepresentable
{
	STANDARD("standard"),
	MINE_ENTRANCE("mine_entrance"),
	MINE_KEY("mine_key");

	private final String name;

	private SpecialSealType(String name)
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