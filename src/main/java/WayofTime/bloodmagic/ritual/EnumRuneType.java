package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ISubItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum EnumRuneType implements IStringSerializable, ISubItem {

    BLANK(TextFormatting.GRAY),
    WATER(TextFormatting.AQUA),
    FIRE(TextFormatting.RED),
    EARTH(TextFormatting.GREEN),
    AIR(TextFormatting.WHITE),
    DUSK(TextFormatting.DARK_GRAY),
    DAWN(TextFormatting.GOLD);

    public final TextFormatting colorCode;

    EnumRuneType(TextFormatting colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Nonnull
    @Override
    public String getInternalName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count) {
        return new ItemStack(RegistrarBloodMagicItems.INSCRIPTION_TOOL, count, ordinal());
    }

    public static EnumRuneType byMetadata(int meta) {
        if (meta < 0 || meta >= values().length)
            meta = 0;

        return values()[meta];
    }


}
