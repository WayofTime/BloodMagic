package wayoftime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum BloodRuneType implements IStringSerializable
{
	BLANK,
	SPEED,
	EFFICIENCY,
	SACRIFICE,
	SELF_SACRIFICE,
	DISPLACEMENT,
	CAPACITY,
	AUGMENTED_CAPACITY,
	ORB,
	ACCELERATION,
	CHARGING;

	@Override
	public String toString()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	/**
	 * getName()
	 * 
	 * @return
	 */
	@Override
	public String getSerializedName()
	{
		return this.toString();
	}
}