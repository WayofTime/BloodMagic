package WayofTime.alchemicalWizardry.api.soulNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;


/**
 * Temporary class to hash-out how to create a network not completely tied to the player. 
 */
public class ComplexNetworkHandler 
{
	public static String fileName = "config/BloodMagic/soulnetworkKeys";
	static HashMap<UUID, String> keyMap = new HashMap();
	public static UUID getUUIDFromPlayer(EntityPlayer player)
    {
        return player.getPersistentID();
    }

    public static EntityPlayer getPlayerFromUUID(UUID uuid)
    {
        MinecraftServer server = MinecraftServer.getServer();
        GameProfile gameProfile;
        gameProfile = server.func_152358_ax().func_152652_a(uuid);
        String str = uuid.toString();
        //TODO ServerConfigurationManager d.createPlayerForUser
        UUID.fromString(str);
        return null;
    }
    
    public static String getKeyForPlayer(EntityPlayer player)
    {
    	return "";
    }
    
    public static UUID getUUIDForKey(String key)
    {
//    	if (MinecraftServer.getServer() == null)
//        {
//            return null;
//        }
//
//        World world = MinecraftServer.getServer().worldServers[0];
//        UUIDKeyMap data = (UUIDKeyMap) world.loadItemData(UUIDKeyMap.class, key);
//
//        if (data == null)
//        {
//            data = new UUIDKeyMap(key);
//            world.setItemData(key, data);
//        }
    	return null;
    }
    
    public static String assignKeyToPlayer(EntityPlayer player)
    {
    	return "";
    }
    
    public static void save()
    {
    	keyMap.put(new UUID(0, 0), "test");
    	
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(keyMap);
        Writer writer;
        try
        {
            writer = new FileWriter(fileName + ".json");
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void load()
    {
    	File save = new File(fileName + ".json");
    	
    	if(save.canRead())
    	{
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();

            BufferedReader br;

            try
            {
                br = new BufferedReader(new FileReader(save));
                HashMap schema = gson.fromJson(br, keyMap.getClass());
                
                keyMap = schema;
                
                if(keyMap != null)
                {
                	for(Entry<UUID, String> entry : keyMap.entrySet())
                	{
                		System.out.println("" + entry.getValue() + " gave: "+ entry.getKey());
                	}
                }
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
    	}
    	
    	else
    	{
    		keyMap = null;
    	}
    }
}
