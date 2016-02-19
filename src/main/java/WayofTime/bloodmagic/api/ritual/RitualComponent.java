package WayofTime.bloodmagic.api.ritual;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Used to set a {@link EnumRuneType} type to a given {@link BlockPos} for usage
 * in Ritual creation.
 */
@Getter
@RequiredArgsConstructor
public class RitualComponent
{
    private final BlockPos offset;
    private final EnumRuneType runeType;

    public int getX(EnumFacing direction)
    {
        switch (direction)
        {
        case EAST:
            return -this.getOffset().getZ();
        case SOUTH:
            return -this.getOffset().getX();
        case WEST:
            return this.getOffset().getZ();
        default:
            return this.getOffset().getX();
        }
    }

    public int getY()
    {
        return this.getOffset().getY();
    }

    public int getZ(EnumFacing direction)
    {
        switch (direction)
        {
        case EAST:
            return this.getOffset().getX();
        case SOUTH:
            return -this.getOffset().getZ();
        case WEST:
            return -this.getOffset().getX();
        default:
            return this.getOffset().getZ();
        }
    }

    public BlockPos getOffset(EnumFacing direction)
    {
        return new BlockPos(getX(direction), offset.getY(), getZ(direction));
    }
}
