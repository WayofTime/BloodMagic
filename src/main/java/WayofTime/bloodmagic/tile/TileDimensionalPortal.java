package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.ritual.types.RitualPortal;
import WayofTime.bloodmagic.tile.base.TileBase;
import com.google.common.base.Strings;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class TileDimensionalPortal extends TileBase {
    public String portalID = "";
    public int masterStoneX;
    public int masterStoneY;
    public int masterStoneZ;

    public void deserialize(CompoundNBT tagCompound) {
        portalID = tagCompound.getString(RitualPortal.PORTAL_ID_TAG);

        masterStoneX = tagCompound.getInt("masterStoneX");
        masterStoneY = tagCompound.getInt("masterStoneY");
        masterStoneZ = tagCompound.getInt("masterStoneZ");
    }

    public CompoundNBT serialize(CompoundNBT tagCompound) {
        tagCompound.putString(RitualPortal.PORTAL_ID_TAG, Strings.isNullOrEmpty(portalID) ? "" : portalID);

        tagCompound.putInt("masterStoneX", masterStoneX);
        tagCompound.putInt("masterStoneY", masterStoneY);
        tagCompound.putInt("masterStoneZ", masterStoneZ);
        return tagCompound;
    }

    public BlockPos getMasterStonePos() {
        return new BlockPos(masterStoneX, masterStoneY, masterStoneZ);
    }

    public void setMasterStonePos(BlockPos blockPos) {
        this.masterStoneX = blockPos.getX();
        this.masterStoneY = blockPos.getY();
        this.masterStoneZ = blockPos.getZ();
    }
}
