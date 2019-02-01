package WayofTime.bloodmagic.structures;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.gson.Serializers;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class DungeonRoomLoader {
    public static void saveDungeons() {
        for (DungeonRoom room : DungeonRoomRegistry.dungeonWeightMap.keySet()) {
            saveSingleDungeon(room);
        }
    }

    public static void saveSingleDungeon(DungeonRoom room) {
        String json = Serializers.GSON.toJson(room);

        Writer writer;
        try {
            File file = new File("config/BloodMagic/schematics");
            file.mkdirs();

            writer = new FileWriter("config/BloodMagic/schematics/" + new Random().nextInt() + ".json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDungeons() {
        try {
            URL schematicURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(new ResourceLocation("bloodmagic:schematics")));
            List<String> schematics = Serializers.GSON.fromJson(Resources.toString(schematicURL, Charsets.UTF_8), new TypeToken<List<String>>() {
            }.getType());
            for (String schematicKey : schematics) {
                ResourceLocation schematic = new ResourceLocation(schematicKey);
                URL dungeonURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(schematic));
                DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);
                DungeonRoomRegistry.registerDungeonRoom(dungeonRoom, Math.max(1, dungeonRoom.dungeonWeight));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() {
        ResourceLocation id = new ResourceLocation(BloodMagic.MODID, "testGson");
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;

        try {
            inputstream = DungeonRoomLoader.class.getResourceAsStream("/assets/" + s + "/schematics/" + s1 + ".nbt");
//            this.readTemplateFromStream(s1, inputstream);
            return;
        } catch (Throwable var10) {

        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public static String resLocToResourcePath(ResourceLocation resourceLocation) {
        return "/assets/" + resourceLocation.getNamespace() + "/schematics/" + resourceLocation.getPath() + ".json";
    }
}
