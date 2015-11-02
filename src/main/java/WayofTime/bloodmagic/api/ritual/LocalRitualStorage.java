package WayofTime.bloodmagic.api.ritual;

import WayofTime.bloodmagic.api.NBTHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

/**
 * This class is used to pass ritual-specific data into the RitualEffect from the containing Master Ritual Stone. This is basically used as auxiliary storage,
 * for when simply storing to NBT becomes... difficult.
 */
@Getter
@Setter
public class LocalRitualStorage {

    private BlockPos pos;

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger(NBTHolder.NBT_COORDX, pos.getX());
        tagCompound.setInteger(NBTHolder.NBT_COORDY, pos.getY());
        tagCompound.setInteger(NBTHolder.NBT_COORDZ, pos.getZ());
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        this.pos = new BlockPos(tagCompound.getInteger(NBTHolder.NBT_COORDX), tagCompound.getInteger(NBTHolder.NBT_COORDY), tagCompound.getInteger(NBTHolder.NBT_COORDZ));
    }
}
