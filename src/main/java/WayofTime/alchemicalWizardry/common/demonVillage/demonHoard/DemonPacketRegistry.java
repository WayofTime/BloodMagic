package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
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
	
	public static String getDemonPacketName(DemonType type, int tier, boolean spawnGuardian)
	{ 
		float totalChance = 0;
		
		for(Entry<String, DemonHoardPacket> entry : packetMap.entrySet())
		{
			DemonHoardPacket packet = entry.getValue();
			
			if(packet == null)
			{
				continue;
			}
			
			totalChance += packet.getRelativeChance(type, tier, spawnGuardian);
		}
		
		for(Entry<String, DemonHoardPacket> entry : packetMap.entrySet())
		{
			DemonHoardPacket packet = entry.getValue();
			
			if(packet == null)
			{
				continue;
			}
			
			float relativeChance = packet.getRelativeChance(type, tier, spawnGuardian);
			
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
	
	public static int spawnDemons(TEDemonPortal teDemonPortal, World world, int x, int y, int z, DemonType type, int tier, boolean spawnGuardian)
	{
		return spawnDemons(teDemonPortal, world, x, y, z, getDemonPacketName(type, tier, spawnGuardian), type, tier, spawnGuardian);
	}
	
	public static int spawnDemons(TEDemonPortal teDemonPortal, World world, int x, int y, int z, String name, DemonType type, int tier, boolean spawnGuardian)
	{
		DemonHoardPacket packet = packetMap.get(name);
		
		if(packet != null)
		{
			return packet.summonDemons(teDemonPortal, world, x, y, z, type, tier, spawnGuardian);
		}
		
		return 0;
	}
}