package WayofTime.bloodmagic.ritual;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

public final class CapabilityRuneType {
    public static class RuneTypeStorage implements Capability.IStorage<IRitualStone.Tile> {
        @Override
        public NBTBase writeNBT(Capability<IRitualStone.Tile> capability, IRitualStone.Tile instance, EnumFacing side) {
            return new NBTTagByte((byte) instance.getRuneType().ordinal());
        }

        @Override
        public void readNBT(Capability<IRitualStone.Tile> capability, IRitualStone.Tile instance, EnumFacing side, NBTBase nbt) {
            instance.setRuneType(EnumRuneType.byMetadata(((NBTTagByte) nbt).getByte()));
        }
    }

    public static class RuneTypeWrapper implements IRitualStone.Tile {
        private EnumRuneType type = EnumRuneType.BLANK;

        @Override
        public boolean isRuneType(EnumRuneType runeType) {
            return type == runeType;
        }

        @Override
        public EnumRuneType getRuneType() {
            return type;
        }

        public void setRuneType(EnumRuneType runeType) {
            type = runeType;
        }
    }

    public static class Factory implements Callable<IRitualStone.Tile> {
        @Override
        public IRitualStone.Tile call() throws Exception {
            return new RuneTypeWrapper();
        }
    }
}
