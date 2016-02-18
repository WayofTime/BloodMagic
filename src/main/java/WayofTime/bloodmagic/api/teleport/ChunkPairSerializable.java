package WayofTime.bloodmagic.api.teleport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.BlockPos;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
public class ChunkPairSerializable implements Serializable
{
    @Getter
    private int chunkXPos;
    @Getter
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

    public BlockPos getChunkCenter(){
        return getChunkCenter(64);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChunkPairSerializable that = (ChunkPairSerializable) o;

        if (chunkXPos != that.chunkXPos) return false;
        return chunkZPos == that.chunkZPos;

    }

    @Override
    public int hashCode()
    {
        int result = chunkXPos;
        result = 31 * result + chunkZPos;
        return result;
    }

    @Override
    public String toString()
    {
        return "ChunkPairSerializable{" +
                "chunkXPos=" + chunkXPos +
                ", chunkZPos=" + chunkZPos +
                '}';
    }
}
