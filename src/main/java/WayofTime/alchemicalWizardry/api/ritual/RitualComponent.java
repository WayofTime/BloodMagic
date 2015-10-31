package WayofTime.alchemicalWizardry.api.ritual;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Getter
@RequiredArgsConstructor
public class RitualComponent {

    private final BlockPos offset;
    private final EnumRuneType runeType;

    public int getX(EnumFacing direction) {
        switch (direction) {
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

    public int getZ(EnumFacing direction) {
        switch (direction) {
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
}
