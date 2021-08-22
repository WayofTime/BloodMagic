package wayoftime.bloodmagic.structures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;

import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.gson.Serializers;

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
		String json = Serializers.GSON.toJson(room);

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
//		Map<String, BlockPos> structureMap = new HashMap<>();
//
//		Map<String, Map<Direction, List<BlockPos>>> doorMap = new HashMap<>(); // Map of doors. The EnumFacing
//																				// indicates what way
//		// this door faces.
//		List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();
//		Map<Direction, List<BlockPos>> defaultList = new HashMap<>();
//		List<BlockPos> northList = new ArrayList<>();
//		northList.add(new BlockPos(0, 0, 0));
//		northList.add(new BlockPos(1, 1, 1));
//
//		List<BlockPos> eastList = new ArrayList<>();
//		eastList.add(new BlockPos(1, 2, 3));
//		eastList.add(new BlockPos(4, 5, 6));
//		defaultList.put(Direction.NORTH, northList);
//		defaultList.put(Direction.EAST, eastList);
//
//		doorMap.put("default", defaultList);
//
//		DungeonRoom testRoom = new DungeonRoom(structureMap, doorMap, descriptorList);
//
//		DungeonRoomLoader.saveSingleDungeon(testRoom);
		try
		{
//			System.out.println("LOADING DEMON DUNGEONS");

			URL schematicURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(new ResourceLocation("bloodmagic:schematics")));
			List<String> schematics = Serializers.GSON.fromJson(Resources.toString(schematicURL, Charsets.UTF_8), new TypeToken<List<String>>()
			{
			}.getType());
			for (String schematicKey : schematics)
			{
				ResourceLocation schematic = new ResourceLocation(schematicKey);
				URL dungeonURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(schematic));
				DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);
				DungeonRoomRegistry.registerDungeonRoom(dungeonRoom, Math.max(1, dungeonRoom.dungeonWeight));
			}

			System.out.println("# schematics: " + schematics.size());

			URL starter_schematicURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(new ResourceLocation("bloodmagic:starter_schematics")));
			List<String> starter_schematics = Serializers.GSON.fromJson(Resources.toString(starter_schematicURL, Charsets.UTF_8), new TypeToken<List<String>>()
			{
			}.getType());
			for (String schematicKey : starter_schematics)
			{
				if (!schematicKey.contains(";"))
				{
					continue;
				}

				String[] keys = schematicKey.split(";");
				String key = keys[0];
				String schematicSubkey = keys[1];
				ResourceLocation schematic = new ResourceLocation(schematicSubkey);
				URL dungeonURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(schematic));
				DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);
				DungeonRoomRegistry.registerStarterDungeonRoom(dungeonRoom, key);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void test()
	{
		ResourceLocation id = new ResourceLocation(BloodMagic.MODID, "testGson");
		String s = id.getNamespace();
		String s1 = id.getPath();
		InputStream inputstream = null;

		try
		{
			inputstream = DungeonRoomLoader.class.getResourceAsStream("/assets/" + s + "/schematics/" + s1 + ".nbt");
//            this.readTemplateFromStream(s1, inputstream);
			return;
		} catch (Throwable var10)
		{

		} finally
		{
			IOUtils.closeQuietly(inputstream);
		}
	}

	public static String resLocToResourcePath(ResourceLocation resourceLocation)
	{
		return "/assets/" + resourceLocation.getNamespace() + "/schematics/" + resourceLocation.getPath() + ".json";
	}
}