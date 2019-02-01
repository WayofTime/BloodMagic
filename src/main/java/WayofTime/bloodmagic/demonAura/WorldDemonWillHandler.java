package WayofTime.bloodmagic.demonAura;

import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldDemonWillHandler {
    public static ConcurrentHashMap<Integer, CopyOnWriteArrayList<PosXY>> dirtyChunks = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer, WillWorld> containedWills = new ConcurrentHashMap<>();

    @Nullable
    public static DemonWillHolder getWillHolder(int dim, int x, int y) {
        WillChunk chunk = getWillChunk(dim, x, y);
        if (chunk != null) {
            return chunk.getCurrentWill();
        }

        return null;
    }

    public static DemonWillHolder getWillHolder(World world, BlockPos pos) {
        return getWillHolder(world.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4);
    }

    public static WillWorld getWillWorld(int dim) {
        return containedWills.get(dim);
    }

    @Nullable
    public static WillChunk getWillChunk(int dim, int x, int y) {
        if (!containedWills.containsKey(dim)) {
            addWillWorld(dim);
        }

        return (containedWills.get(dim)).getWillChunkAt(x, y);
    }

    public static void addWillWorld(int dim) {
        if (!containedWills.containsKey(dim)) {
            containedWills.put(dim, new WillWorld(dim));
            BMLog.DEBUG.info("Creating demon will cache for world {}", dim);
        }
    }

    public static void removeWillWorld(int dim) {
        containedWills.remove(dim);
        BMLog.DEBUG.info("Removing demon will cache for world {}", dim);
    }

    public static void addWillChunk(int dim, Chunk chunk, short base, DemonWillHolder currentWill) {
        WillWorld aw = containedWills.get(dim);
        if (aw == null) {
            aw = new WillWorld(dim);
        }
        aw.getWillChunks().put(new PosXY(chunk.x, chunk.z), new WillChunk(chunk, base, currentWill));

        containedWills.put(dim, aw);
    }

    public static void removeWillChunk(int dim, int x, int y) {
        WillWorld aw = containedWills.get(dim);
        if (aw != null) {
            WillChunk chunk = aw.getWillChunks().remove(new PosXY(x, y));
            if (chunk != null) {
                markChunkAsDirty(chunk, dim);
            }
        }
    }

    public static EnumDemonWillType getHighestDemonWillType(World world, BlockPos pos) {
        double currentMax = 0;
        EnumDemonWillType currentHighest = EnumDemonWillType.DEFAULT;

        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            if (currentWill.getWill(type) > currentMax) {
                currentMax = currentWill.getWill(type);
                currentHighest = type;
            }
        }

        return currentHighest;
    }

    public static double drainWill(World world, BlockPos pos, EnumDemonWillType type, double amount, boolean doDrain) {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        double drain = Math.min(currentWill.getWill(type), amount);
        if (!doDrain) {
            return drain;
        }

        drain = currentWill.drainWill(type, drain);
        markChunkAsDirty(willChunk, world.provider.getDimension());

        return drain;
    }

    public static double fillWillToMaximum(World world, BlockPos pos, EnumDemonWillType type, double amount, double max, boolean doFill) {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        double fill = Math.min(amount, max - currentWill.getWill(type));
        if (!doFill || fill <= 0) {
            return fill > 0 ? fill : 0;
        }

        fill = currentWill.addWill(type, amount, max);
        markChunkAsDirty(willChunk, world.provider.getDimension());

        return fill;
    }

    public static double fillWill(World world, BlockPos pos, EnumDemonWillType type, double amount, boolean doFill) {
        WillChunk willChunk = getWillChunk(world, pos);

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        if (!doFill) {
            return amount;
        }

        currentWill.addWill(type, amount);
        markChunkAsDirty(willChunk, world.provider.getDimension());

        return amount;
    }

    public static WillChunk getWillChunk(World world, BlockPos pos) {
        WillChunk willChunk = getWillChunk(world.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4);
        if (willChunk == null) {
            Chunk chunk = world.getChunk(pos);
            generateWill(chunk);

            willChunk = getWillChunk(world.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4);
        }

        return willChunk;
    }

    public static double getCurrentWill(World world, BlockPos pos, EnumDemonWillType type) {
        WillChunk willChunk = getWillChunk(world, pos);

        if (willChunk == null) {
            return 0;
        }

        DemonWillHolder currentWill = willChunk.getCurrentWill();
        return currentWill.getWill(type);
    }

    private static void markChunkAsDirty(WillChunk chunk, int dim) {
        if (chunk.isModified()) {
            return;
        }
        PosXY pos = new PosXY(chunk.loc.x, chunk.loc.y);
        if (!dirtyChunks.containsKey(dim)) {
            dirtyChunks.put(dim, new CopyOnWriteArrayList<>());
        }
        CopyOnWriteArrayList<PosXY> dc = dirtyChunks.get(dim);
        if (!dc.contains(pos)) {
            dc.add(pos);
        }
    }

    public static void generateWill(Chunk chunk) {
        addWillChunk(chunk.getWorld().provider.getDimension(), chunk, (short) 1, new DemonWillHolder());
    }
}
