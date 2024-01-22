package wayoftime.bloodmagic.common.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class DungeonDimensionHelper
{
	public static void test(Level world)
	{
		ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, BloodMagic.rl("dungeon"));
		Level testWorld = world.getServer().getLevel(key);
		System.out.println("Testing! Key is: " + key + ", World is: " + testWorld);
//		world.getServer().getCommandManager().handleCommand(source, command)
		testWorld.setBlockAndUpdate(new BlockPos(0, 100, 0), BloodMagicBlocks.ACCELERATION_RUNE.get().defaultBlockState());
	}

	public static ServerLevel getDungeonWorld(Level world)
	{
		ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, BloodMagic.rl("dungeon"));
		return world.getServer().getLevel(key);
	}
}
