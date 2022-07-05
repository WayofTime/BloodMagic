package wayoftime.bloodmagic.altar;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import wayoftime.bloodmagic.block.enums.BloodRuneType;

public class AltarUpgrade
{

	private final EnumMap<BloodRuneType, Integer> upgradeLevels;

	public AltarUpgrade()
	{
		this.upgradeLevels = Maps.newEnumMap(BloodRuneType.class);
	}

	public AltarUpgrade upgrade(BloodRuneType rune, int count)
	{
		upgradeLevels.compute(rune, (r, l) -> l == null ? count : l + count);
		return this;
	}

	public int getLevel(BloodRuneType rune)
	{
		return upgradeLevels.getOrDefault(rune, 0);
	}
}
