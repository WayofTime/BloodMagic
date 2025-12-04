package wayoftime.bloodmagic.ritual;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;
import net.minecraft.ChatFormatting;

public enum EnumRuneType implements StringRepresentable {
    BLANK(ChatFormatting.GRAY),
    WATER(ChatFormatting.AQUA),
    FIRE(ChatFormatting.RED),
    EARTH(ChatFormatting.GREEN),
    AIR(ChatFormatting.WHITE),
    DUSK(ChatFormatting.DARK_GRAY),
    DAWN(ChatFormatting.GOLD);

    public final ChatFormatting colorCode;
    public final String translationKey = this.name().toLowerCase(Locale.ROOT) + "Rune";
    public final String patchouliColor = "$(" + this.name().toLowerCase(Locale.ROOT) + ")";

    EnumRuneType(ChatFormatting colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getSerializedName() {
        return this.toString();
    }

    public static EnumRuneType byMetadata(int meta) {
        if (meta < 0 || meta >= values().length)
            meta = 0;
        return values()[meta];
    }
}
