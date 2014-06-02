package WayofTime.alchemicalWizardry.api.rituals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import scala.reflect.internal.Trees.This;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class Rituals
{
    private int crystalLevel;
    private int actCost;
    private RitualEffect effect;
    private String name;

    public static Map<String,Rituals> ritualMap = new HashMap();
    @Deprecated
    public static List<Rituals> ritualList = new LinkedList();
   	public static List<String> keyList = new LinkedList();

    public Rituals(int crystalLevel, int actCost, RitualEffect effect, String name)
    {
        this.crystalLevel = crystalLevel; //For a test commit
        this.actCost = actCost;
        this.effect = effect;
        this.name = name;
        keyList.add(name);
        ritualMap.put(name, this);
    }
    
    /**
     * Static method to register a ritual to the Ritual Registry
     * @param key	Unique identification key - must be different from all others to properly register
     * @param crystalLevel	Crystal level required to activate
     * @param actCost	LP amount required to activate
     * @param effect	The effect that will be ticked
     * @param name	The name of the ritual
     * @return	Returns true if properly registered, or false if the key is already used
     */
    public static boolean registerRitual(String key, int crystalLevel, int actCost, RitualEffect effect, String name)
    {
    	if(ritualMap.containsKey(key))
    	{
    		return false;
    	}
    	else
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
    	if(ritualMap.containsValue(this))
    	{
    		ritualMap.remove(ritualMap.remove(this.name));
    	}
    	if(keyList.contains(this.name))
    	{
    		keyList.remove(this.name);
    	}
    }

    public static String checkValidRitual(World world, int x, int y, int z)
    {
    	for(String key : ritualMap.keySet())
    	{
    		if(checkRitualIsValid(world,x,y,z,key))
    		{
    			return key;
    		}
    	}
    	
    	return "";
    }

    public static boolean canCrystalActivate(String ritualID, int crystalLevel)
    {
    	if(ritualMap.containsKey(ritualID))
    	{
    		Rituals ritual = ritualMap.get(ritualID);
    		if(ritual != null)
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

                    if (world.getBlockMetadata(x + rc.getX(), y + rc.getY(), z + rc.getZ()) != rc.getStoneType())
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

                    if (world.getBlockMetadata(x - rc.getZ(), y + rc.getY(), z + rc.getX()) != rc.getStoneType())
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

                    if (world.getBlockMetadata(x - rc.getX(), y + rc.getY(), z - rc.getZ()) != rc.getStoneType())
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

                    if (world.getBlockMetadata(x + rc.getZ(), y + rc.getY(), z - rc.getX()) != rc.getStoneType())
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
    	if(ritualMap.containsKey(ritualID))
    	{
    		Rituals ritual = ritualMap.get(ritualID);
    		if(ritual != null)
    		{
    			return ritual.actCost;
    		}
    	}
    	
    	return 0;
    }

    public static int getInitialCooldown(String ritualID)
    {
    	if(ritualMap.containsKey(ritualID))
    	{
    		Rituals ritual = ritualMap.get(ritualID);
    		if(ritual != null && ritual.effect != null)
    		{
    			return ritual.effect.getInitialCooldown();
    		}
    	}
    	
    	return 0;
    }

    public static List<RitualComponent> getRitualList(String ritualID)
    {
    	if(ritualMap.containsKey(ritualID))
    	{
    		Rituals ritual = ritualMap.get(ritualID);
    		if(ritual != null)
    		{
    			return ritual.obtainComponents();
    		}else
    		{
    			return null;
    		}
    	}else
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

    public static void performEffect(IMasterRitualStone ritualStone, String ritualID)
    {
    	if(ritualMap.containsKey(ritualID))
    	{
    		Rituals ritual = ritualMap.get(ritualID);
    		if(ritual != null && ritual.effect != null)
    		{
    			ritual.effect.performEffect(ritualStone);
    		}
    	}
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
    	if(ritualMap.containsKey(id))
    	{
    		Rituals ritual = ritualMap.get(id);
    		if(ritual != null)
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

    	for(String str : keyList)
    	{
    		if(firstKey.equals(""))
    		{
    			firstKey = str;
    		}
    		if(hasSpotted)
    		{
    			return str;
    		}
    		if(str.equals(key))
    		{
    			hasSpotted = true;
    		}
    	}

    	return firstKey;
    }
}
