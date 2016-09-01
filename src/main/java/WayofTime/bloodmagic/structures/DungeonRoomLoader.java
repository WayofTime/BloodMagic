package WayofTime.bloodmagic.structures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.gson.Adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DungeonRoomLoader
{
    public static void saveDungeons()
    {
        for (DungeonRoom room : DungeonRoomRegistry.dungeonWeightMap.keySet())
        {
            saveSingleDungeon(room);
        }
    }

    public static void saveSingleDungeon(DungeonRoom room)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(room);

        Writer writer;
        try
        {
            File file = new File("config/BloodMagic/schematics");
            file.mkdirs();

            writer = new FileWriter("config/BloodMagic/schematics/" + new Random().nextInt() + ".json");
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadDungeons()
    {
//        String folder = "config/BloodMagic/schematics";
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(EnumFacing.class, Adapters.adapter).create();

        File file = new File(MinecraftServer.class.getResource("/assets/" + "BloodMagic" + "/schematics").getFile());

        File[] files = file.listFiles();
        BufferedReader br;

        try
        {
            for (File f : files)
            {
                System.out.println("File: " + f);
                br = new BufferedReader(new FileReader(f));

                DungeonRoom room = gson.fromJson(br, DungeonRoom.class);
                DungeonRoomRegistry.registerDungeonRoom(room, Math.max(1, room.dungeonWeight));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void test()
    {
        ResourceLocation id = new ResourceLocation(Constants.Mod.MODID, "testGson");
        String s = id.getResourceDomain();
        String s1 = id.getResourcePath();
        InputStream inputstream = null;

        try
        {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/schematics/" + s1 + ".nbt");
//            this.readTemplateFromStream(s1, inputstream);
            return;
        } catch (Throwable var10)
        {

        } finally
        {
            IOUtils.closeQuietly(inputstream);
        }
    }
}
