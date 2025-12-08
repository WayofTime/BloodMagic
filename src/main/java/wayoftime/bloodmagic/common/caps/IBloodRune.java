package wayoftime.bloodmagic.common.caps;

import wayoftime.bloodmagic.util.EnumRuneType;

import java.util.Map;

@FunctionalInterface
public interface IBloodRune {
    Map<EnumRuneType, Integer> getUpgrades();
}
