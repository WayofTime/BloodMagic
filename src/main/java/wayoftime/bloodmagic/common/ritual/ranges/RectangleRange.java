package wayoftime.bloodmagic.common.ritual.ranges;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import wayoftime.bloodmagic.api.ritual.Range;

import java.util.ArrayList;
import java.util.List;

public class RectangleRange implements Range {

    private BlockPos start;
    private BlockPos end;
    private final List<BlockPos> containedPositions = new ArrayList<>();
    private int currentIndex;
    private final int maxHorizontal;
    private final int maxVertical;

    public static final MapCodec<RectangleRange> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BlockPos.CODEC.fieldOf("start").forGetter(RectangleRange::start),
            BlockPos.CODEC.fieldOf("end").forGetter(RectangleRange::end),
            Codec.INT.fieldOf("index").forGetter(RectangleRange::index),
            Codec.INT.fieldOf("max_horizontal").forGetter(RectangleRange::maxHorizontal),
            Codec.INT.fieldOf("max_vertical").forGetter(RectangleRange::maxVertical)
    ).apply(builder, RectangleRange::new));

    public RectangleRange(BlockPos start, BlockPos end, int currentIndex, int maxHorizontal, int maxVertical) {
        this.currentIndex = currentIndex;
        this.maxHorizontal = maxHorizontal;
        this.maxVertical = maxVertical;

        setRange(start, end);
    }

    public RectangleRange(BlockPos start, BlockPos end, int maxHorizontal, int maxVertical) {
        this(start, end, 0, maxHorizontal, maxVertical);
    }

    public void setRange(BlockPos start, BlockPos end) {
        int startX = Math.min(start.getX(), end.getX());
        int startY = Math.min(start.getY(), end.getY());
        int startZ = Math.min(start.getZ(), end.getZ());

        int endX = Math.min(startX + maxHorizontal, Math.max(start.getX(), end.getX()));
        int endY = Math.min(startY + maxVertical, Math.max(start.getY(), end.getY()));
        int endZ = Math.min(startZ + maxHorizontal, Math.max(start.getZ(), end.getZ()));

        start = new BlockPos(startX, startY, startZ);
        end = new BlockPos(endX, endY, endZ);

        containedPositions.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    containedPositions.add(new BlockPos(x, y, z));
                }
            }
        }
    }

    public BlockPos start() {
        return start;
    }

    public BlockPos end() {
        return end;
    }

    public int index() {
        return currentIndex;
    }

    public int maxHorizontal() {
        return maxHorizontal;
    }

    public int maxVertical() {
        return maxVertical;
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    @Override
    public Boolean hasNext() {
        return currentIndex < containedPositions.size();
    }

    @Override
    public BlockPos next() {
        return containedPositions.get(currentIndex++);
    }

    @Override
    public BlockPos getFirst() {
        return containedPositions.getFirst();
    }

    @Override
    public MapCodec<? extends Range> codec() {
        return CODEC;
    }
}
