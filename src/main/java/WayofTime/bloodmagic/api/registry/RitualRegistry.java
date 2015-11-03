package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;

public class RitualRegistry {

    private static final BiMap<Ritual, Boolean> enabledRituals = HashBiMap.create();
    private static final BiMap<String, Ritual> registry = HashBiMap.create();

    /**
     * The safe way to register a new Ritual.
     *
     * @param ritual - The ritual to register.
     * @param id     - The ID for the ritual. Cannot be duplicated.
     */
    public static void registerRitual(Ritual ritual, String id) {
        if (ritual != null) {
            if (registry.containsKey(id))
                BloodMagicAPI.getLogger().error("Duplicate ritual id: " + id);
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
        try {
            return enabledRituals.get(ritual);
        } catch (NullPointerException e) {
            BloodMagicAPI.getLogger().error("Invalid Ritual was called");
            return false;
        }
    }

    public static BiMap<String, Ritual> getRegistry() {
        return HashBiMap.create(registry);
    }

    public static BiMap<Ritual, Boolean> getEnabledMap() {
        return HashBiMap.create(enabledRituals);
    }

    public static ArrayList<String> getIds() {
        return new ArrayList<String>(registry.keySet());
    }

    public static ArrayList<Ritual> getRituals() {
        return new ArrayList<Ritual>(registry.values());
    }
}
