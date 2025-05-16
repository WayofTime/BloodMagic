package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import wayoftime.bloodmagic.common.damagesource.BMDamageSources;

public class BloodyDamageSources {
    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BMDamageSources.SACRIFICE, new DamageType("sacrifice", DamageScaling.NEVER, 0F));
    }
}
