package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.BloodMagicCorePlugin;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PluginUtil {

    @SuppressWarnings("unchecked")
    @Nonnull
    public static List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> gatherPlugins(ASMDataTable dataTable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> discoveredAnnotations = Lists.newArrayList();
        Set<ASMDataTable.ASMData> discoveredPlugins = dataTable.getAll(BloodMagicPlugin.class.getName());

        for (ASMDataTable.ASMData data : discoveredPlugins) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                Class<? extends IBloodMagicPlugin> pluginClass = asmClass.asSubclass(IBloodMagicPlugin.class);

                IBloodMagicPlugin instance = pluginClass.newInstance();

                BMLog.API.info("Discovered plugin at {}", data.getClassName());
                discoveredAnnotations.add(Pair.of(instance, pluginClass.getAnnotation(BloodMagicPlugin.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Bring core plugin up to top
        discoveredAnnotations.sort((o1, o2) -> {
            if (o1.getLeft().getClass() == BloodMagicCorePlugin.class)
                return -1;

            return o1.getClass().getCanonicalName().compareToIgnoreCase(o2.getClass().getCanonicalName());
        });
        BMLog.API.info("Discovered {} potential plugin(s) in {}", discoveredAnnotations.size(), stopwatch.stop());
        return discoveredAnnotations;
    }

    @Nonnull
    public static List<Field> gatherInjections(ASMDataTable dataTable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Field> injectees = Lists.newArrayList();
        Set<ASMDataTable.ASMData> discoveredInjectees = dataTable.getAll(BloodMagicPlugin.Inject.class.getName());

        for (ASMDataTable.ASMData data : discoveredInjectees) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                Field toInject = asmClass.getDeclaredField(data.getObjectName());
                if (toInject.getType() != IBloodMagicAPI.class) {
                    BMLog.API.error("Mod requested API injection on field {}.{} which is an invalid type.", data.getClassName(), data.getObjectName());
                    continue;
                }

                BMLog.API.info("Discovered injection request at {}.{}", data.getClassName(), data.getObjectName());
                injectees.add(toInject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BMLog.API.info("Discovered {} potential API injection(s) in {}", injectees.size(), stopwatch.stop());
        return injectees;
    }

    public static void handlePluginStep(RegistrationStep step) {
        Stopwatch total = Stopwatch.createStarted();
        int errors = 0;
        for (Pair<IBloodMagicPlugin, BloodMagicPlugin> plugin : BloodMagic.PLUGINS) {
            Stopwatch per = Stopwatch.createStarted();
            try {
                step.getConsumer().accept(plugin);
            } catch (Exception e) {
                errors++;
                BMLog.DEFAULT.error("Error handling plugin step {} at {}: {}: {}", step, plugin.getLeft().getClass(), e.getClass().getSimpleName(), e.getMessage());
            }
            BMLog.API.info("Handled plugin step {} at {} in {}", step, plugin.getLeft().getClass(), per.stop());
        }

        BMLog.API.info("Handled {} plugin(s) at step {} with {} errors in {}", BloodMagic.PLUGINS.size() - errors, step, errors, total.stop());
    }

    public static void injectAPIInstances(List<Field> injectees) {
        Stopwatch total = Stopwatch.createStarted();
        int errors = 0;

        for (Field injectee : injectees) {
            Stopwatch per = Stopwatch.createStarted();
            if (!Modifier.isStatic(injectee.getModifiers()))
                continue;

            try {
                EnumHelper.setFailsafeFieldValue(injectee, null, BloodMagicAPI.INSTANCE);
            } catch (Exception e) {
                errors++;
                BMLog.DEFAULT.error("Error injecting API instance at {}.{}", injectee.getDeclaringClass().getCanonicalName(), injectee.getName());
            }
            BMLog.API.info("Injected API instance at {}.{} in {}", injectee.getDeclaringClass().getCanonicalName(), injectee.getName(), per.stop());
        }

        BMLog.API.info("Injected API {} times with {} errors in {}", injectees.size() - errors, errors, total.stop());
    }

    public enum RegistrationStep {

        PLUGIN_REGISTER(p -> p.getLeft().register(BloodMagicAPI.INSTANCE)),
        RECIPE_REGISTER(p -> p.getLeft().registerRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar()));

        private final Consumer<Pair<IBloodMagicPlugin, BloodMagicPlugin>> consumer;

        RegistrationStep(Consumer<Pair<IBloodMagicPlugin, BloodMagicPlugin>> consumer) {
            this.consumer = consumer;
        }

        @Nonnull
        public Consumer<Pair<IBloodMagicPlugin, BloodMagicPlugin>> getConsumer() {
            return consumer;
        }
    }
}
