package wayoftime.bloodmagic.api.capability;

import wayoftime.bloodmagic.api.altar.EnumRuneType;

import java.util.Map;

@FunctionalInterface
public interface IRunePowers {
    Map<EnumRuneType, Integer> get();
}
