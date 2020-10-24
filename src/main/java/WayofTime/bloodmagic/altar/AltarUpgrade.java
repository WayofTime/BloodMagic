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

	public AltarUpgrade upgrade(BloodRuneType rune)
	{
		upgradeLevels.compute(rune, (r, l) -> l == null ? 1 : l + 1);
		return this;
	}

	public int getLevel(BloodRuneType rune)
	{
		return upgradeLevels.getOrDefault(rune, 0);
	}
}
