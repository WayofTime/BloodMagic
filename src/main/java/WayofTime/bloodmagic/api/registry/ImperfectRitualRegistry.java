package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ImperfectRitualRegistry
{
    public static final Map<ImperfectRitual, Boolean> enabledRituals = new HashMap<ImperfectRitual, Boolean>();
    private static final BiMap<String, ImperfectRitual> registry = HashBiMap.create();

    /**
     * The safe way to register a new Ritual.
     * 
     * @param imperfectRitual
     *        - The imperfect ritual to register.
     * @param id
     *        - The ID for the imperfect ritual. Cannot be duplicated.
     */
    public static void registerRitual(ImperfectRitual imperfectRitual, String id, boolean enabled)
    {
        if (imperfectRitual != null)
        {
            if (registry.containsKey(id))
                BloodMagicAPI.getLogger().error("Duplicate imperfect ritual id: %s", id);
            else
            {
                registry.put(id, imperfectRitual);
                enabledRituals.put(imperfectRitual, enabled);
            }
        }
    }

    public static void registerRitual(ImperfectRitual imperfectRitual, String id)
    {
        registerRitual(imperfectRitual, id, true);
    }

    public static void registerRitual(ImperfectRitual imperfectRitual, boolean enabled)
    {
        registerRitual(imperfectRitual, imperfectRitual.getName(), enabled);
    }

    public static void registerRitual(ImperfectRitual imperfectRitual)
    {
        registerRitual(imperfectRitual, imperfectRitual.getName());
    }

    public static ImperfectRitual getRitualForBlock(BlockStack blockStack)
    {
        for (ImperfectRitual imperfectRitual : getRegistry().values())
            if (imperfectRitual.getRequiredBlock().equals(blockStack))
                return imperfectRitual;

        return null;
    }

    public static ImperfectRitual getRitualForId(String id)
    {
        return registry.get(id);
    }

    public static String getIdForRitual(ImperfectRitual imperfectRitual)
    {
        return registry.inverse().get(imperfectRitual);
    }

    public static boolean isMapEmpty()
    {
        return registry.isEmpty();
    }

    public static int getMapSize()
    {
        return registry.size();
    }

    public static boolean ritualEnabled(ImperfectRitual imperfectRitual)
    {
        try
        {
            return enabledRituals.get(imperfectRitual);
        } catch (NullPointerException e)
        {
            BloodMagicAPI.getLogger().error("Invalid Imperfect Ritual was called");
            return false;
        }
    }

    public static boolean ritualEnabled(String id)
    {
        return ritualEnabled(getRitualForId(id));
    }

    public static BiMap<String, ImperfectRitual> getRegistry()
    {
        return HashBiMap.create(registry);
    }

    public static BiMap<ImperfectRitual, Boolean> getEnabledMap()
    {
        return HashBiMap.create(enabledRituals);
    }

    public static ArrayList<String> getIds()
    {
        return new ArrayList<String>(registry.keySet());
    }

    public static ArrayList<ImperfectRitual> getRituals()
    {
        return new ArrayList<ImperfectRitual>(registry.values());
    }
}
