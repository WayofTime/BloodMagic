package WayofTime.alchemicalWizardry.api.rituals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import WayofTime.alchemicalWizardry.api.event.RitualRunEvent;
import WayofTime.alchemicalWizardry.api.event.RitualStopEvent;
import WayofTime.alchemicalWizardry.api.renderer.MRSRenderer;
import cpw.mods.fml.common.eventhandler.Event;

public class Rituals
{
    private int crystalLevel;
    private int actCost;
    private RitualEffect effect;
    private String name;

    private MRSRenderer customRenderer;

    public static Map<String, Rituals> ritualMap = new HashMap();
    public static List<String> keyList = new LinkedList();

    public Rituals(int crystalLevel, int actCost, RitualEffect effect, String name, MRSRenderer renderer)
    {
        this.crystalLevel = crystalLevel;
        this.actCost = actCost;
        this.effect = effect;
        this.name = name;
        keyList.add(name);
        ritualMap.put(name, this);
        this.customRenderer = renderer;
    }

    public Rituals(int crystalLevel, int actCost, RitualEffect effect, String name)
    {
        this(crystalLevel, actCost, effect, name, null);
    }

    /**
     * Static method to register a ritual to the Ritual Registry
     *
     * @param key          Unique identification key - must be different from all others to properly register
     * @param crystalLevel Crystal level required to activate
     * @param actCost      LP amount required to activate
     * @param effect       The effect that will be ticked
     * @param name         The name of the ritual
     * @return Returns true if properly registered, or false if the key is already used
     */
    public static boolean registerRitual(String key, int crystalLevel, int actCost, RitualEffect effect, String name, MRSRenderer renderer)
    {
        if (ritualMap.containsKey(key))
        {
            return false;
        } else
        {
            Rituals ritual = new Rituals(crystalLevel, actCost, effect, name, renderer);
            ritual.removeRitualFromList();
            ritualMap.put(key, ritual);
            keyList.add(key);
            return true;
        }
    }

    public static boolean registerRitual(String key, int crystalLevel, int actCost, RitualEffect effect, String name)
    {
        if (ritualMap.containsKey(key))
        {
            return false;
        } else
        {
            Rituals ritual = new Rituals(crystalLevel, actCost, effect, name);
            ritual.removeRitualFromList();
            ritualMap.put(key, ritual);
            keyList.add(key);
            return true;
        }
    }

    public void removeRitualFromList()
    {
        if (ritualMap.containsValue(this))
        {
            ritualMap.remove(ritualMap.remove(this.name));
        }
        if (keyList.contains(this.name))
        {
            keyList.remove(this.name);
        }
    }

    public static String checkValidRitual(World world, int x, int y, int z)
    {
        for (String key : ritualMap.keySet())
        {
            if (checkRitualIsValid(world, x, y, z, key))
            {
                return key;
            }
        }

        return "";
    }

    public static boolean canCrystalActivate(String ritualID, int crystalLevel)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null)
            {
                return ritual.getCrystalLevel() <= crystalLevel;
            }
        }

        return false;
    }

    public static boolean checkRitualIsValid(World world, int x, int y, int z, String ritualID)
    {
        int direction = Rituals.getDirectionOfRitual(world, x, y, z, ritualID);

        if (direction != -1)
        {
            return true;
        }

        return false;
    }

    /**
     * 1 - NORTH
     * 2 - EAST
     * 3 - SOUTH
     * 4 - WEST
     */
    public static boolean checkDirectionOfRitualValid(World world, int x, int y, int z, String ritualID, int direction)
    {
        List<RitualComponent> ritual = Rituals.getRitualList(ritualID);

        if (ritual == null)
        {
            return false;
        }

        Block test = null;

        switch (direction)
        {
            case 1:
                for (RitualComponent rc : ritual)
                {
                    test = world.getBlock(x + rc.getX(), y + rc.getY(), z + rc.getZ());

                    if (!(test instanceof IRitualStone))
                    {
                        return false;
                    }

                    if (((IRitualStone)test).getRuneType(world, x, y, z, world.getBlockMetadata(x + rc.getX(), y + rc.getY(), z + rc.getZ())) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 2:
                for (RitualComponent rc : ritual)
                {
                    test = world.getBlock(x - rc.getZ(), y + rc.getY(), z + rc.getX());

                    if (!(test instanceof IRitualStone))
                    {
                        return false;
                    }

                    if (((IRitualStone)test).getRuneType(world, x, y, z, world.getBlockMetadata(x - rc.getZ(), y + rc.getY(), z + rc.getX())) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 3:
                for (RitualComponent rc : ritual)
                {
                    test = world.getBlock(x - rc.getX(), y + rc.getY(), z - rc.getZ());

                    if (!(test instanceof IRitualStone))
                    {
                        return false;
                    }

                    if (((IRitualStone)test).getRuneType(world, x, y, z, world.getBlockMetadata(x - rc.getX(), y + rc.getY(), z - rc.getZ())) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 4:
                for (RitualComponent rc : ritual)
                {
                    test = world.getBlock(x + rc.getZ(), y + rc.getY(), z - rc.getX());

                    if (!(test instanceof IRitualStone))
                    {
                        return false;
                    }

                    if (((IRitualStone)test).getRuneType(world, x, y, z, world.getBlockMetadata(x + rc.getZ(), y + rc.getY(), z - rc.getX())) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;
        }

        return false;
    }

    public static int getDirectionOfRitual(World world, int x, int y, int z, String ritualID)
    {
        for (int i = 1; i <= 4; i++)
        {
            if (Rituals.checkDirectionOfRitualValid(world, x, y, z, ritualID, i))
            {
                return i;
            }
        }

        return -1;
    }

    public static int getCostForActivation(String ritualID)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null)
            {
                return ritual.actCost;
            }
        }

        return 0;
    }

    public static int getInitialCooldown(String ritualID)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null && ritual.effect != null)
            {
                return ritual.effect.getInitialCooldown();
            }
        }

        return 0;
    }

    public static List<RitualComponent> getRitualList(String ritualID)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null)
            {
                return ritual.obtainComponents();
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }

    private List<RitualComponent> obtainComponents()
    {
        return this.effect.getRitualComponentList();
    }

    private int getCrystalLevel()
    {
        return this.crystalLevel;
    }

    private MRSRenderer getRenderer()
    {
        return this.customRenderer;
    }

    public static void performEffect(IMasterRitualStone ritualStone, String ritualID)
    {
    	String ownerName = ritualStone.getOwner();
    	
    	RitualRunEvent event = new RitualRunEvent(ritualStone, ownerName, ritualID);
    	
    	if(MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
    	{
    		return;
    	}
    	
        if (ritualMap.containsKey(event.ritualKey))
        {
            Rituals ritual = ritualMap.get(event.ritualKey);
            if (ritual != null && ritual.effect != null)
            {
                ritual.effect.performEffect(ritualStone);
            }
        }
    }

    public static boolean startRitual(IMasterRitualStone ritualStone, String ritualID, EntityPlayer player)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null && ritual.effect != null)
            {
                return ritual.effect.startRitual(ritualStone, player);
            }
        }

        return false;
    }

    public static void onRitualBroken(IMasterRitualStone ritualStone, String ritualID, RitualBreakMethod method)
    {
    	String ownerName = ritualStone.getOwner();
    	RitualStopEvent event = new RitualStopEvent(ritualStone, ownerName, ritualID, method);
    	MinecraftForge.EVENT_BUS.post(event);
    	
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null && ritual.effect != null)
            {
                ritual.effect.onRitualBroken(ritualStone, method);
            }
        }
        
        System.out.println(method);
    }

    public static int getNumberOfRituals()
    {
        return ritualMap.size();
    }

    public String getRitualName()
    {
        return this.name;
    }

    public static String getNameOfRitual(String id)
    {
        if (ritualMap.containsKey(id))
        {
            Rituals ritual = ritualMap.get(id);
            if (ritual != null)
            {
                return ritual.getRitualName();
            }
        }

        return "";
    }

    public static String getNextRitualKey(String key)
    {
        boolean hasSpotted = false;
        String firstKey = "";

        for (String str : keyList)
        {
            if (firstKey.equals(""))
            {
                firstKey = str;
            }
            if (hasSpotted)
            {
                return str;
            }
            if (str.equals(key))
            {
                hasSpotted = true;
            }
        }

        return firstKey;
    }

    public static String getPreviousRitualKey(String key)
    {
        boolean hasSpotted = false;
        String lastKey = keyList.get(keyList.size() - 1);

        for (String str : keyList)
        {
            if (str.equals(key))
            {
                hasSpotted = true;
            }
            if (hasSpotted)
            {
                return lastKey;
            }
            lastKey = str;
        }

        return lastKey;
    }

    public static MRSRenderer getRendererForKey(String ritualID)
    {
        if (ritualMap.containsKey(ritualID))
        {
            Rituals ritual = ritualMap.get(ritualID);
            if (ritual != null)
            {
                return ritual.getRenderer();
            }
        }

        return null;
    }
}
