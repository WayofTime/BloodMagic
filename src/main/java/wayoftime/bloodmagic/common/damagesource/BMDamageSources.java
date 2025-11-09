package wayoftime.bloodmagic.common.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import wayoftime.bloodmagic.BloodMagic;

public class BMDamageSources {
    public static final ResourceKey<DamageType> SACRIFICE = key("sacrifice"); // used by soul network to forcibly receive needed LP. TODO Potentially by Dagger of Sacrifice?
    public static final ResourceKey<DamageType> SELF_SACRIFICE = key("self_sacrifice");

    private static ResourceKey<DamageType> key(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, BloodMagic.rl(path));
    }
}
