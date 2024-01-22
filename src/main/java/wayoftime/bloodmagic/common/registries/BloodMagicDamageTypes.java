package wayoftime.bloodmagic.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicDamageTypes {

    public static final ResourceKey<DamageType> SACRIFICE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BloodMagic.MODID, "sacrifice"));
    public static final ResourceKey<DamageType> RITUAL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BloodMagic.MODID, "ritual"));

}
