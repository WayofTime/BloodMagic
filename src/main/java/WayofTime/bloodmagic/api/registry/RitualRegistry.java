package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.ritual.Ritual;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class RitualRegistry
{
    public static final Map<Ritual, Boolean> enabledRituals = new HashMap<Ritual, Boolean>();
    private static final BiMap<String, Ritual> registry = HashBiMap.create();
    /**
     * Ordered list for actions that depend on the order that the rituals were
     * registered in
     */
    private static final ArrayList<String> orderedIdList = new ArrayList<String>();

    /**
     * The safe way to register a new Ritual.
     * 
     * @param ritual
     *        - The ritual to register.
     * @param id
     *        - The ID for the ritual. Cannot be duplicated.
     */
    public static void registerRitual(Ritual ritual, String id, boolean enabled)
    {
        if (ritual != null)
        {
            if (registry.containsKey(id))
                BloodMagicAPI.getLogger().error("Duplicate ritual id: %s", id);
            else
            {
                registry.put(id, ritual);
                enabledRituals.put(ritual, enabled);
                orderedIdList.add(id);
            }
        }
    }

    public static void registerRitual(Ritual ritual, boolean enabled)
    {
        registerRitual(ritual, ritual.getName(), enabled);
    }

    public static void registerRitual(Ritual ritual, String id)
    {
        registerRitual(ritual, id, true);
    }

    public static void registerRitual(Ritual ritual)
    {
        registerRitual(ritual, ritual.getName());
    }

    public static Ritual getRitualForId(String id)
    {
        Ritual ritual = registry.get(id);
        return ritual != null ? ritual.getNewCopy() : null;
    }

    public static String getIdForRitual(Ritual ritual)
    {
        return registry.inverse().get(ritual);
    }

    public static boolean isMapEmpty()
    {
        return registry.isEmpty();
    }

    public static int getMapSize()
    {
        return registry.size();
    }

    public static boolean ritualEnabled(Ritual ritual)
    {
        try
        {
            return enabledRituals.get(ritual);
        } catch (NullPointerException e)
        {
            BloodMagicAPI.getLogger().error("Invalid Ritual was called");
            return false;
        }
    }

    public static boolean ritualEnabled(String id)
    {
        return ritualEnabled(getRitualForId(id));
    }

    public static BiMap<String, Ritual> getRegistry()
    {
        return HashBiMap.create(registry);
    }

    public static Map<Ritual, Boolean> getEnabledMap()
    {
        return new HashMap<Ritual, Boolean>(enabledRituals);
    }

    public static ArrayList<String> getIds()
    {
        return new ArrayList<String>(registry.keySet());
    }

    public static ArrayList<String> getOrderedIds()
    {
        return orderedIdList;
    }

    public static ArrayList<Ritual> getRituals()
    {
        return new ArrayList<Ritual>(registry.values());
    }
}
