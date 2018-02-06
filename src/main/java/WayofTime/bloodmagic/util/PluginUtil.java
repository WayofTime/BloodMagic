package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import WayofTime.bloodmagic.api.impl.BloodMagicCorePlugin;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class PluginUtil {

    @SuppressWarnings("unchecked")
    @Nonnull
    public static List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> getPlugins(ASMDataTable dataTable) {
        List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> discoveredAnnotations = Lists.newArrayList();
        Set<ASMDataTable.ASMData> discoveredPlugins = dataTable.getAll(BloodMagicPlugin.class.getCanonicalName());

        for (ASMDataTable.ASMData data : discoveredPlugins) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                Class<? extends IBloodMagicPlugin> pluginClass = asmClass.asSubclass(IBloodMagicPlugin.class);

                IBloodMagicPlugin instance = pluginClass.newInstance();

                discoveredAnnotations.add(Pair.of(instance, pluginClass.getAnnotation(BloodMagicPlugin.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Bring core plugin up to top
        discoveredAnnotations.sort((o1, o2) -> o1.getLeft().getClass() == BloodMagicCorePlugin.class ? 1 : 0);
        return discoveredAnnotations;
    }
}
