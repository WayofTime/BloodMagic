package WayofTime.alchemicalWizardry.api.registry;

import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import WayofTime.alchemicalWizardry.api.ritual.imperfect.ImperfectRitual;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;

public class ImperfectRitualRegistry {

    public static final BiMap<ImperfectRitual, Boolean> enabledRituals = HashBiMap.create();
    private static final BiMap<String, ImperfectRitual> registry = HashBiMap.create();

    /**
     * The safe way to register a new Ritual.
     *
     * @param imperfectRitual - The imperfect ritual to register.
     * @param id              - The ID for the imperfect ritual. Cannot be duplicated.
     */
    public static void registerRitual(ImperfectRitual imperfectRitual, String id) {
        if (imperfectRitual != null) {
            if (registry.containsKey(id))
                AlchemicalWizardryAPI.getLogger().error("Duplicate imperfect ritual id: " + id);
            else
                registry.put(id, imperfectRitual);
        }
    }

    public static ImperfectRitual getRitualForId(String id) {
        return registry.get(id);
    }

    public static String getIdForRitual(ImperfectRitual imperfectRitual) {
        return registry.inverse().get(imperfectRitual);
    }

    public static boolean isMapEmpty() {
        return registry.isEmpty();
    }

    public static int getMapSize() {
        return registry.size();
    }

    public static boolean ritualEnabled(ImperfectRitual imperfectRitual) {
        return enabledRituals.get(imperfectRitual);
    }

    public static BiMap<String, ImperfectRitual> getRegistry() {
        return HashBiMap.create(registry);
    }

    public static ArrayList<String> getIds() {
        return new ArrayList<String>(registry.keySet());
    }

    public static ArrayList<ImperfectRitual> getRituals() {
        return new ArrayList<ImperfectRitual>(registry.values());
    }
}
