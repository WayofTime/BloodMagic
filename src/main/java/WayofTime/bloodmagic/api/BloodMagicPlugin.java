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

}
