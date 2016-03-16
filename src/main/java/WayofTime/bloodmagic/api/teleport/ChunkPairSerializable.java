package WayofTime.bloodmagic.api.teleport;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.BlockPos;

@ToString
@EqualsAndHashCode
@Getter
public class ChunkPairSerializable implements Serializable
{
    private int chunkXPos;
    private int chunkZPos;

    public ChunkPairSerializable(int chunkXPos, int chunkZPos)
    {
        this.chunkXPos = chunkXPos;
        this.chunkZPos = chunkZPos;
    }

    public ChunkPairSerializable(BlockPos blockPos)
    {
        this(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    public BlockPos getChunkCenter(int y)
    {
        return new BlockPos((chunkXPos << 4) + 8, y, (chunkZPos << 4) + 8);
    }

    public BlockPos getChunkCenter()
    {
        return getChunkCenter(64);
    }
}
