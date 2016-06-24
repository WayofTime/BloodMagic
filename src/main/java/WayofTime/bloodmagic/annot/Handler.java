package WayofTime.bloodmagic.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes annotated with this will automatically be registered to the
 * {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handler
{
}
