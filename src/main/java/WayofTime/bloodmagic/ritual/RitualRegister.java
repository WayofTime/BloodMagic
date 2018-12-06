package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RitualRegister {

    String value();

    Class<? extends Function<Class<? extends Ritual>, Ritual>> factory() default DefaultRitualFactory.class;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Imperfect {

        String value();

        Class<? extends Function<Class<? extends ImperfectRitual>, ImperfectRitual>> factory() default DefaultImperfectRitualFactory.class;
    }

    class DefaultRitualFactory implements Function<Class<? extends Ritual>, Ritual> {
        @Override
        public Ritual apply(Class<? extends Ritual> aClass) {
            try {
                return aClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
    }

    class DefaultImperfectRitualFactory implements Function<Class<? extends ImperfectRitual>, ImperfectRitual> {
        @Override
        public ImperfectRitual apply(Class<? extends ImperfectRitual> aClass) {
            try {
                return aClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
