package WayofTime.bloodmagic.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation lets Blood Magic detect mod plugins.
 * All {@link IBloodMagicPlugin} must have this annotation and a constructor with no arguments.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BloodMagicPlugin {

    /**
     * This annotation will inject the active {@link IBloodMagicAPI} into a {@code static} field of the same
     * type. Fields with invalid types will be ignored and an error will be logged.
     *
     * These fields are populated during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}.
     *
     * {@code public static @BloodMagicPlugin.Inject IBloodMagicAPI API_INSTANCE = null;}
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Inject {

    }
}
