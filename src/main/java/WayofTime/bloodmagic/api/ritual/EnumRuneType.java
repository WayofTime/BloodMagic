package WayofTime.bloodmagic.api.ritual;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Locale;

public enum EnumRuneType implements IStringSerializable {
    BLANK(TextFormatting.GRAY),
    WATER(TextFormatting.AQUA),
    FIRE(TextFormatting.RED),
    EARTH(TextFormatting.GREEN),
    AIR(TextFormatting.WHITE),
    DUSK(TextFormatting.DARK_GRAY),
    DAWN(TextFormatting.GOLD);

    @GameRegistry.ObjectHolder("bloodmagic:inscription_tool")
    public static final Item INSCRIPTION_TOOL = Items.AIR;

    public final TextFormatting colorCode;

    EnumRuneType(TextFormatting colorCode) {
        this.colorCode = colorCode;
    }

    public ItemStack getScribeStack() {
        return new ItemStack(INSCRIPTION_TOOL, 1, ordinal());
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    public static EnumRuneType byMetadata(int meta) {
        if (meta < 0 || meta >= values().length)
            meta = 0;

        return values()[meta];
    }

}
