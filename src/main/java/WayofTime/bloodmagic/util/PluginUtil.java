package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.BloodMagicCorePlugin;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class PluginUtil {

    @SuppressWarnings("unchecked")
    @Nonnull
    public static List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> gatherPlugins(ASMDataTable dataTable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> discoveredAnnotations = Lists.newArrayList();
        Set<ASMDataTable.ASMData> discoveredPlugins = dataTable.getAll(BloodMagicPlugin.class.getCanonicalName());

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

    public static void registerPlugins(List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> plugins) {
        Stopwatch total = Stopwatch.createStarted();
        int errors = 0;
        for (Pair<IBloodMagicPlugin, BloodMagicPlugin> plugin : plugins) {
            Stopwatch per = Stopwatch.createStarted();
            try {
                plugin.getLeft().register(BloodMagicAPI.INSTANCE);
            } catch (Exception e) {
                errors++;
                BMLog.DEFAULT.error("Error loading plugin at {}: {}: {}", plugin.getLeft().getClass(), e.getClass().getSimpleName(), e.getMessage());
            }
            BMLog.API.info("Registered plugin at {} in {}", plugin.getLeft().getClass(), per.stop());
        }

        BMLog.API.info("Registered {} plugins with {} errors in {}", plugins.size() - errors, errors, total.stop());
    }
}
