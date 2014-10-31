package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.world.World;

public class DemonPacketRegistry 
{
	public static Map<String, DemonHoardPacket> packetMap = new HashMap();
	
	public static boolean registerDemonPacket(String string, DemonHoardPacket packet)
	{
		if(!packetMap.containsValue(string) && packet != null)
		{
			packetMap.put(string, packet);
			
			return true;
		}
		
		return false;
	}
	
	public static String getDemonPacketName(DemonType type, int tier)
	{ 
		float totalChance = 0;
		
		for(Entry<String, DemonHoardPacket> entry : packetMap.entrySet())
		{
			DemonHoardPacket packet = entry.getValue();
			
			if(packet == null)
			{
				continue;
			}
			
			totalChance += packet.getRelativeChance(type, tier);
		}
		
		for(Entry<String, DemonHoardPacket> entry : packetMap.entrySet())
		{
			DemonHoardPacket packet = entry.getValue();
			
			if(packet == null)
			{
				continue;
			}
			
			float relativeChance = packet.getRelativeChance(type, tier);
			
			if(relativeChance >= totalChance)
			{
				return entry.getKey();
			}else
			{
				totalChance -= relativeChance;
			}
		}
		
		return "";
	}
	
	public static int spawnDemons(World world, int x, int y, int z, DemonType type, int tier)
	{
		return spawnDemons(world, x, y, z, getDemonPacketName(type, tier), type, tier);
	}
	
	public static int spawnDemons(World world, int x, int y, int z, String name, DemonType type, int tier)
	{
		DemonHoardPacket packet = packetMap.get(name);
		
		if(packet != null)
		{
			return packet.summonDemons(world, x, y, z, type, tier);
		}
		
		return 0;
	}
}