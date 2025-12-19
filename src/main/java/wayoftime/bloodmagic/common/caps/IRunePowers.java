package wayoftime.bloodmagic.common.caps;

import wayoftime.bloodmagic.util.EnumRuneType;

import java.util.Map;

@FunctionalInterface
public interface IRunePowers {
    Map<EnumRuneType, Integer> get();
}
