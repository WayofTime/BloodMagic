package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.util.PleaseStopUsingMe;
import WayofTime.bloodmagic.ritual.data.Ritual;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.annotation.Nullable;
import java.util.*;

public class RitualRegistry {
    public static final Map<Ritual, Boolean> enabledRituals = new HashMap<Ritual, Boolean>();
    private static final BiMap<String, Ritual> registry = HashBiMap.create();
    private static final List<String> lookupList = new ArrayList<String>();
    /**
     * Ordered list for actions that depend on the order that the rituals were
     * registered in
     */
    private static final ArrayList<String> orderedIdList = new ArrayList<String>();

    private static boolean locked;

    /**
     * The safe way to register a new Ritual.
     *
     * @param ritual - The ritual to register.
     * @param id     - The ID for the ritual. Cannot be duplicated.
     */
    public static void registerRitual(Ritual ritual, String id, boolean enabled) {
        if (locked) {
            PleaseStopUsingMe.logger.error("This registry has been locked. Please register your ritual earlier.");
            PleaseStopUsingMe.logger.error("If you reflect this, I will hunt you down. - TehNut");
            return;
        }

        if (ritual != null) {
            if (registry.containsKey(id))
                PleaseStopUsingMe.logger.error("Duplicate ritual id: %s", id);
            else {
                registry.put(id, ritual);
                enabledRituals.put(ritual, enabled);
                orderedIdList.add(id);
            }
        }
    }

    public static void registerRitual(Ritual ritual, boolean enabled) {
        registerRitual(ritual, ritual.getName(), enabled);
    }

    public static void registerRitual(Ritual ritual, String id) {
        registerRitual(ritual, id, true);
    }

    public static void registerRitual(Ritual ritual) {
        registerRitual(ritual, ritual.getName());
    }

    @Nullable
    public static Ritual getRitualForId(String id) {
        Ritual ritual = registry.get(id);
        return ritual != null ? ritual.getNewCopy() : null;
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
            PleaseStopUsingMe.logger.error("Invalid Ritual was called");
            return false;
        }
    }

    public static boolean ritualEnabled(String id) {
        return ritualEnabled(getRitualForId(id));
    }

    public static BiMap<String, Ritual> getRegistry() {
        return HashBiMap.create(registry);
    }

    public static Map<Ritual, Boolean> getEnabledMap() {
        return new HashMap<Ritual, Boolean>(enabledRituals);
    }

    public static ArrayList<String> getIds() {
        return new ArrayList<String>(lookupList);
    }

    public static ArrayList<String> getOrderedIds() {
        return orderedIdList;
    }

    public static ArrayList<Ritual> getRituals() {
        return new ArrayList<Ritual>(registry.values());
    }

    public static void orderLookupList() {
        locked = true; // Lock registry so no no rituals can be registered
        lookupList.clear(); // Make sure it's empty
        lookupList.addAll(registry.keySet());
        Collections.sort(lookupList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Ritual ritual1 = registry.get(o1);
                Ritual ritual2 = registry.get(o2);
                return ritual1.getComponents().size() > ritual2.getComponents().size() ? -1 : 0; // Put earlier if bigger
            }
        });
    }
}
