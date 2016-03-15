package WayofTime.bloodmagic.demonAura;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

public class WorldDemonWillHandler
{
    static ConcurrentHashMap<Integer, WillWorld> containedWills = new ConcurrentHashMap<Integer, WillWorld>();
    public static ConcurrentHashMap<Integer, CopyOnWriteArrayList<PosXY>> dirtyChunks = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<PosXY>>();
    public static ConcurrentHashMap<Integer, BlockPos> taintTrigger = new ConcurrentHashMap<Integer, BlockPos>();

    public static WillWorld getWillWorld(int dim)
    {
        return containedWills.get(dim);
    }

    public static WillChunk getWillChunk(int dim, int x, int y)
    {
        if (!containedWills.containsKey(dim))
        {
            addWillWorld(dim);
        }

        return (containedWills.get(dim)).getWillChunkAt(x, y);
    }

    public static void addWillWorld(int dim)
    {
        if (!containedWills.containsKey(dim))
        {
            containedWills.put(dim, new WillWorld(dim));
            BloodMagicAPI.getLogger().info("Creating demon will cache for world " + dim);
        }
    }

    public static void removeWillWorld(int dim)
    {
        containedWills.remove(dim);
        BloodMagicAPI.getLogger().info("Removing demon will cache for world " + dim);
    }

    public static void addWillChunk(int dim, Chunk chunk, short base, DemonWillHolder currentWill)
    {
        WillWorld aw = containedWills.get(dim);
        if (aw == null)
        {
            aw = new WillWorld(dim);
        }
        aw.getWillChunks().put(new PosXY(chunk.xPosition, chunk.zPosition), new WillChunk(chunk, base, currentWill));

        containedWills.put(dim, aw);
    }

    public static void removeWillChunk(int dim, int x, int y)
    {
        WillWorld aw = containedWills.get(dim);
        if (aw != null)
        {
            WillChunk chunk = aw.getWillChunks().remove(new PosXY(x, y));
            if (chunk != null)
            {
                markChunkAsDirty(chunk, dim);
            }
        }
    }

    public static EnumDemonWillType getHighestDemonWillType(World world, BlockPos pos)
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

    public static double drainWill(World world, BlockPos pos, EnumDemonWillType type, double amount, boolean doDrain)
    {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        double drain = Math.min(currentWill.getWill(type), amount);
        if (!doDrain)
        {
            return drain;
        }

        drain = currentWill.drainWill(type, drain);
        markChunkAsDirty(willChunk, world.provider.getDimensionId());

        return drain;
    }

    public static double fillWillToMaximum(World world, BlockPos pos, EnumDemonWillType type, double amount, double max, boolean doFill)
    {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        double fill = Math.min(amount, max - currentWill.getWill(type));
        if (!doFill)
        {
            return fill;
        }

        fill = currentWill.addWill(type, amount, max);
        markChunkAsDirty(willChunk, world.provider.getDimensionId());

        return fill;
    }

    public static double fillWill(World world, BlockPos pos, EnumDemonWillType type, double amount, boolean doFill)
    {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        if (!doFill)
        {
            return amount;
        }

        currentWill.addWill(type, amount);
        markChunkAsDirty(willChunk, world.provider.getDimensionId());

        return amount;
    }

    public static WillChunk getWillChunk(World world, BlockPos pos)
    {
        WillChunk willChunk = getWillChunk(world.provider.getDimensionId(), pos.getX() >> 4, pos.getZ() >> 4);
        if (willChunk == null)
        {
            Chunk chunk = world.getChunkFromBlockCoords(pos);
            generateWill(chunk);

            willChunk = getWillChunk(world.provider.getDimensionId(), pos.getX() >> 4, pos.getZ() >> 4);
        }

        return willChunk;
    }

    public static double getCurrentWill(World world, BlockPos pos, EnumDemonWillType type)
    {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        return currentWill.getWill(type);
    }

    private static void markChunkAsDirty(WillChunk chunk, int dim)
    {
        if (chunk.isModified())
        {
            return;
        }
        PosXY pos = new PosXY(chunk.loc.x, chunk.loc.y);
        if (!dirtyChunks.containsKey(dim))
        {
            dirtyChunks.put(dim, new CopyOnWriteArrayList<PosXY>());
        }
        CopyOnWriteArrayList<PosXY> dc = dirtyChunks.get(dim);
        if (!dc.contains(pos))
        {
            dc.add(pos);
        }
    }

    public static void generateWill(Chunk chunk)
    {
        addWillChunk(chunk.getWorld().provider.getDimensionId(), chunk, (short) 1, new DemonWillHolder());
    }
}
