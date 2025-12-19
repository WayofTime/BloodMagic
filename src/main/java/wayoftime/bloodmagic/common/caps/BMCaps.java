package wayoftime.bloodmagic.common.caps;

import net.neoforged.neoforge.capabilities.BlockCapability;
import wayoftime.bloodmagic.BloodMagic;

public class BMCaps {
    public static final BlockCapability<IRunePowers, Void> RUNE_POWERS = BlockCapability.createVoid(BloodMagic.rl("rune_powers"), IRunePowers.class);
}
