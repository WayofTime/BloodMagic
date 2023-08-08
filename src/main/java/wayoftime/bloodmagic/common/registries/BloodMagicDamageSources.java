package wayoftime.bloodmagic.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicDamageSources {

    public static final BloodMagicDamageSources INSTANCE = new BloodMagicDamageSources();
    private BloodMagicDamageSources(){

    }

    public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, BloodMagic.MODID);
    public static final RegistryObject<DamageType> DEFAULT = DAMAGE_TYPES.register("bloodmagic", () -> new DamageType("bloodmagic", 0.0F ));
    public static final RegistryObject<DamageType> RITUAL = DAMAGE_TYPES.register("bloodmagic", () -> new DamageType("ritual", 0.0F ));

    public DamageSource defaultSource(){
        return new DamageSource(DEFAULT.getHolder().get());
    }
    public DamageSource ritual(){
        return new DamageSource(RITUAL.getHolder().get());
    }
}
