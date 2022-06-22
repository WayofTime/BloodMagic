package wayoftime.bloodmagic.core.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.gson.Serializers;
import wayoftime.bloodmagic.ritual.AreaDescriptor.Rectangle;
import wayoftime.bloodmagic.structures.DungeonRoom;

public class DungeonRoomProvider implements DataProvider
{
	DataGenerator generator;

	public DungeonRoomProvider(DataGenerator gen)
	{
		this.generator = gen;
	}

	public void loadDungeons(HashCache cache)
	{
		String basePath = "bloodmagic:schematics";

		DungeonRoom miniArmoury = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/armoury", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 8, 17)));
		miniArmoury.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniArmoury.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");
		DungeonRoom miniCrypt = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/crypt", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 10, 17)));
		miniCrypt.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniCrypt.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");
		DungeonRoom miniFarm = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/farm", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 9, 17)));
		miniFarm.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniFarm.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");
		DungeonRoom miniLibrary = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/library", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 7, 17)));
		miniLibrary.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniLibrary.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");
		DungeonRoom miniPortalRoom = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/portal_nether", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 7, 17)));
		miniPortalRoom.addDoor(new BlockPos(8, 5, 0), Direction.NORTH, "default", 1);
		miniPortalRoom.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");

		DungeonRoom fourWayCorridor = new DungeonRoom().addStructure("bloodmagic:four_way_corridor", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 6, 11)));
		fourWayCorridor.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0));
		fourWayCorridor.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 10));
		fourWayCorridor.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 0, 5));
		fourWayCorridor.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 0, 5));

		DungeonRoom fourWayCorridorLoot = new DungeonRoom().addStructure("bloodmagic:four_way_corridor_loot", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 1, 11)));
		fourWayCorridorLoot.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 5, 0));
		fourWayCorridorLoot.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 5, 10));
		fourWayCorridorLoot.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 5));
		fourWayCorridorLoot.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 5, 5));

		DungeonRoom overlapped_corridor = new DungeonRoom().addStructure("bloodmagic:overlapped_corridor", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(3, 0, 0), new BlockPos(8, 5, 11))).addAreaDescriptor(new Rectangle(new BlockPos(0, 4, 3), new BlockPos(11, 9, 8)));
		overlapped_corridor.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0));
		overlapped_corridor.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 10));
		overlapped_corridor.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 4, 5));
		overlapped_corridor.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 4, 5));

		DungeonRoom oreHold = new DungeonRoom().addStructure("bloodmagic:ore_hold_1", new BlockPos(0, 0, 0));
		oreHold.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0), new BlockPos(2, 5, 0), new BlockPos(12, 5, 0));
		oreHold.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 14), new BlockPos(2, 5, 14), new BlockPos(12, 5, 14));
		oreHold.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 7));
		oreHold.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(15, 12, 15)));
		oreHold.addRoomPool(1, "bloodmagic:room_pools/tier2/standard_rooms");
		oreHold.addRoomPool(1, "#bloodmagic:room_pools/tier2/mine_entrances");
//		Serializers.GSON.toJson(room, DungeonRoom.class);

		addDungeonRoom(cache, miniArmoury, BloodMagic.rl("mini_dungeon/armoury"));
		addDungeonRoom(cache, miniCrypt, BloodMagic.rl("mini_dungeon/crypt"));
		addDungeonRoom(cache, miniFarm, BloodMagic.rl("mini_dungeon/farm"));
		addDungeonRoom(cache, miniLibrary, BloodMagic.rl("mini_dungeon/library"));
		addDungeonRoom(cache, miniPortalRoom, BloodMagic.rl("mini_dungeon/portal_nether"));

		addDungeonRoom(cache, fourWayCorridor, BloodMagic.rl("four_way_corridor"));
		addDungeonRoom(cache, fourWayCorridorLoot, BloodMagic.rl("four_way_corridor_loot"));
		addDungeonRoom(cache, overlapped_corridor, BloodMagic.rl("overlapped_corridor"));

		addDungeonRoom(cache, oreHold, BloodMagic.rl("standard/ore_hold_1"));
//		DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);

		registerStarterRooms(cache);
	}

	public void registerStarterRooms(HashCache cache)
	{
		DungeonRoom miniDungeon = new DungeonRoom().addStructure("bloodmagic:t3_entrance", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 8, 17)));
		miniDungeon.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 1, 0));
		miniDungeon.addDoors(Direction.SOUTH, "default", 1, new BlockPos(8, 1, 16));
		miniDungeon.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 1, 8));
		miniDungeon.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 1, 8));
		miniDungeon.addRoomPool(1, "bloodmagic:room_pools/tier1/mini_dungeon");
		miniDungeon.spawnLocation = new BlockPos(8, 2, 4);
		miniDungeon.controllerOffset = new BlockPos(8, 6, 8);
		miniDungeon.portalOffset = new BlockPos(8, 4, 8);

		addDungeonRoom(cache, miniDungeon, BloodMagic.rl("t3_entrance"));

	}

	public void addDungeonRoom(HashCache cache, DungeonRoom room, ResourceLocation schematicName)
	{
		String json = Serializers.GSON.toJson(room);

		Path mainOutput = generator.getOutputFolder();
		String pathSuffix = "assets/" + schematicName.getNamespace() + "/schematics/" + schematicName.getPath() + ".json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		try
		{
			save(cache, json, outputPath);
		} catch (IOException e)
		{
			BloodMagic.LOGGER.error("Couldn't save schematic to {}", outputPath, e);
		}
	}

	static void save(HashCache p_123922_, String json, Path p_123924_)
			throws IOException
	{
		String s = json;
		String s1 = SHA1.hashUnencodedChars(s).toString();
		if (!Objects.equals(p_123922_.getHash(p_123924_), s1) || !Files.exists(p_123924_))
		{
			Files.createDirectories(p_123924_.getParent());
			BufferedWriter bufferedwriter = Files.newBufferedWriter(p_123924_);

			try
			{
				bufferedwriter.write(s);
			} catch (Throwable throwable1)
			{
				if (bufferedwriter != null)
				{
					try
					{
						bufferedwriter.close();
					} catch (Throwable throwable)
					{
						throwable1.addSuppressed(throwable);
					}
				}

				throw throwable1;
			}

			if (bufferedwriter != null)
			{
				bufferedwriter.close();
			}
		}

		p_123922_.putNew(p_123924_, s1);
	}

	@Override
	public void run(HashCache cache)
			throws IOException
	{
		loadDungeons(cache);
	}

	@Override
	public String getName()
	{
		return "DungeonGenerator";
	}
}
