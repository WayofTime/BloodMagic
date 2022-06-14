package wayoftime.bloodmagic.demonaura;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ChunkAccess;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class WorldDemonWillHandler
{
	public static ConcurrentHashMap<ResourceLocation, ConcurrentLinkedQueue<PosXY>> dirtyChunks = new ConcurrentHashMap<>();
	static ConcurrentHashMap<ResourceLocation, WillWorld> containedWills = new ConcurrentHashMap<>();

	@Nullable
	public static DemonWillHolder getWillHolder(ResourceLocation resourceLocation, int x, int y)
	{
		WillChunk chunk = getWillChunk(resourceLocation, x, y);
		if (chunk != null)
		{
			return chunk.getCurrentWill();
		}

		return null;
	}

	public static DemonWillHolder getWillHolder(Level world, BlockPos pos)
	{
		return getWillHolder(getDimensionResourceLocation(world), pos.getX() >> 4, pos.getZ() >> 4);
	}

	public static WillWorld getWillWorld(ResourceLocation rl)
	{
		return containedWills.get(rl);
	}

	@Nullable
	public static WillChunk getWillChunk(ResourceLocation resourceLocation, int x, int y)
	{
		if (!containedWills.containsKey(resourceLocation))
		{
			addWillWorld(resourceLocation);
		}

		return (containedWills.get(resourceLocation)).getWillChunkAt(x, y);
	}

	public static void addWillWorld(ResourceLocation resourceLocation)
	{
		if (!containedWills.containsKey(resourceLocation))
		{
			containedWills.put(resourceLocation, new WillWorld(resourceLocation));
			BMLog.DEBUG.info("Creating demon will cache for world {}", resourceLocation);
		}
	}

	public static void removeWillWorld(ResourceLocation rl)
	{
		containedWills.remove(rl);
		BMLog.DEBUG.info("Removing demon will cache for world {}", rl);
	}

	public static void addWillChunk(ResourceLocation resourceLocation, ChunkAccess chunk, short base, DemonWillHolder currentWill)
	{
		WillWorld aw = containedWills.get(resourceLocation);
		if (aw == null)
		{
			aw = new WillWorld(resourceLocation);
		}
		aw.getWillChunks().put(new PosXY(chunk.getPos().x, chunk.getPos().z), new WillChunk(chunk, base, currentWill));

		containedWills.put(resourceLocation, aw);
	}

	public static void removeWillChunk(ResourceLocation resourceLocation, int x, int y)
	{
		WillWorld aw = containedWills.get(resourceLocation);
		if (aw != null)
		{
			WillChunk chunk = aw.getWillChunks().remove(new PosXY(x, y));
			if (chunk != null)
			{
				markChunkAsDirty(chunk, resourceLocation);
			}
		}
	}

	public static EnumDemonWillType getHighestDemonWillType(Level world, BlockPos pos)
	{
		double currentMax = 0;
		EnumDemonWillType currentHighest = EnumDemonWillType.DEFAULT;

		WillChunk willChunk = getWillChunk(world, pos);

		DemonWillHolder currentWill = willChunk.getCurrentWill();
		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			if (currentWill.getWill(type) > currentMax)
			{
				currentMax = currentWill.getWill(type);
				currentHighest = type;
			}
		}

		return currentHighest;
	}

	public static double drainWill(Level world, BlockPos pos, EnumDemonWillType type, double amount, boolean doDrain)
	{
		WillChunk willChunk = getWillChunk(world, pos);

		DemonWillHolder currentWill = willChunk.getCurrentWill();
		double drain = Math.min(currentWill.getWill(type), amount);
		if (!doDrain)
		{
			return drain;
		}

		drain = currentWill.drainWill(type, drain);
		markChunkAsDirty(willChunk, getDimensionResourceLocation(world));

		return drain;
	}

	public static double fillWillToMaximum(Level world, BlockPos pos, EnumDemonWillType type, double amount, double max, boolean doFill)
	{
		WillChunk willChunk = getWillChunk(world, pos);

		DemonWillHolder currentWill = willChunk.getCurrentWill();
		double fill = Math.min(amount, max - currentWill.getWill(type));
		if (!doFill || fill <= 0)
		{
			return fill > 0 ? fill : 0;
		}

		fill = currentWill.addWill(type, amount, max);
		markChunkAsDirty(willChunk, getDimensionResourceLocation(world));

		return fill;
	}

	public static double fillWill(Level world, BlockPos pos, EnumDemonWillType type, double amount, boolean doFill)
	{
		WillChunk willChunk = getWillChunk(world, pos);

		DemonWillHolder currentWill = willChunk.getCurrentWill();
		if (!doFill)
		{
			return amount;
		}

		currentWill.addWill(type, amount);
		markChunkAsDirty(willChunk, getDimensionResourceLocation(world));

		return amount;
	}

	public static WillChunk getWillChunk(Level world, BlockPos pos)
	{
		WillChunk willChunk = getWillChunk(getDimensionResourceLocation(world), pos.getX() >> 4, pos.getZ() >> 4);
		if (willChunk == null)
		{
			LevelChunk chunk = world.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
			generateWill(chunk, world);

			willChunk = getWillChunk(getDimensionResourceLocation(world), pos.getX() >> 4, pos.getZ() >> 4);
		}

		return willChunk;
	}

	public static double getCurrentWill(Level world, BlockPos pos, EnumDemonWillType type)
	{
		WillChunk willChunk = getWillChunk(world, pos);

		if (willChunk == null)
		{
			return 0;
		}

		DemonWillHolder currentWill = willChunk.getCurrentWill();
		return currentWill.getWill(type);
	}

	private static void markChunkAsDirty(WillChunk chunk, ResourceLocation resourceLocation)
	{
		if (chunk.isModified())
		{
			return;
		}
		PosXY pos = new PosXY(chunk.loc.x, chunk.loc.y);
		if (!dirtyChunks.containsKey(resourceLocation))
		{
			dirtyChunks.put(resourceLocation, new ConcurrentLinkedQueue<>());
		}
		ConcurrentLinkedQueue<PosXY> dc = dirtyChunks.get(resourceLocation);
		if (!dc.contains(pos))
		{
			dc.add(pos);
		}
	}

	public static void generateWill(ChunkAccess chunk, Level world)
	{
		addWillChunk(getDimensionResourceLocation(world), chunk, (short) 1, new DemonWillHolder());
	}

	public static ResourceLocation getDimensionResourceLocation(Level world)
	{
		return world.dimension().location();
	}

}