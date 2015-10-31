package WayofTime.alchemicalWizardry.api.ritual;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraftforge.common.config.Configuration;
import sun.misc.Launcher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RitualRegistry {

    public static final Map<RitualEffect, Boolean> enabledRituals = new HashMap<RitualEffect, Boolean>();
    private static BiMap<String, Ritual> registry = HashBiMap.create();

    /**
     * The safe way to register a new Ritual.
     *
     * @param ritual - The ritual to register.
     * @param id     - The ID for the ritual. Cannot be duplicated.
     */
    public static void registerRitual(Ritual ritual, String id) {
        if (ritual != null) {
            if (registry.containsKey(id))
                AlchemicalWizardry.instance.getLogger().error("Duplicate ritual id: " + id);
            else
                registry.put(id, ritual);
        }
    }

    public static Ritual getRitualForId(String id) {
        return registry.get(id);
    }

    public static String getIdForRitual(Ritual ritual) {
        return registry.inverse().get(ritual);
    }

    public static boolean isMapEmpty() {
        return registry.isEmpty();
    }

    public static int getMapSize() {
        return registry.size();
    }

    public static boolean ritualEnabled(Ritual ritual) {
        return enabledRituals.get(ritual.getRitualEffect());
    }

    public static ArrayList<String> getIds() {
        return new ArrayList<String>(registry.keySet());
    }

    public static ArrayList<Ritual> getRituals() {
        return new ArrayList<Ritual>(registry.values());
    }

    /**
     * Adds your Ritual to the {@link RitualRegistry#enabledRituals} Map.
     * This is used to determine whether your effect is enabled or not.
     *
     * The config option will be created as {@code B:ClassName=true} with a comment of
     * {@code Enables the ClassName ritual}.
     *
     * Should be safe to modify at any point.
     *
     * @param config      - Your mod's Forge {@link Configuration} object.
     * @param packageName - The package your Rituals are located in.
     * @param category    - The config category to write to.
     */
    public static void checkRituals(Configuration config, String packageName, String category) {
        String name = packageName;
        if (!name.startsWith("/"))
            name = "/" + name;

        name = name.replace('.', '/');
        URL url = Launcher.class.getResource(name);
        File directory = new File(url.getFile());

        if (directory.exists()) {
            String[] files = directory.list();

            for (String file : files) {
                if (file.endsWith(".class")) {
                    String className = file.substring(0, file.length() - 6);

                    try {
                        Object o = Class.forName(packageName + "." + className).newInstance();

                        if (o instanceof RitualEffect)
                            RitualRegistry.enabledRituals.put((RitualEffect) o, config.get(category, className, true).getBoolean());

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
