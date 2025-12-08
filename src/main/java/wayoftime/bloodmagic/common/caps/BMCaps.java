package wayoftime.bloodmagic.common.caps;

import net.neoforged.neoforge.capabilities.BlockCapability;
import wayoftime.bloodmagic.BloodMagic;

public class BMCaps {
    public static final BlockCapability<IBloodRune, Void> BLOOD_RUNE = BlockCapability.createVoid(BloodMagic.rl("blood_rune"), IBloodRune.class);
}
