package wayoftime.bloodmagic.util;

import net.minecraft.util.StringRepresentable;

public enum TablePart implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    TablePart(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
