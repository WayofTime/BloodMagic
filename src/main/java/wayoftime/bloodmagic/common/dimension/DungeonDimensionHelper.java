package wayoftime.bloodmagic.common.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class DungeonDimensionHelper
{
	public static void test(World world)
	{
		RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, BloodMagic.rl("dungeon"));
		World testWorld = world.getServer().getWorld(key);
		System.out.println("Testing! Key is: " + key + ", World is: " + testWorld);
//		world.getServer().getCommandManager().handleCommand(source, command)
		testWorld.setBlockState(new BlockPos(0, 100, 0), BloodMagicBlocks.ACCELERATION_RUNE.get().getDefaultState());
	}

	public static ServerWorld getDungeonWorld(World world)
	{
		RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, BloodMagic.rl("dungeon"));
		return world.getServer().getWorld(key);
	}
}
