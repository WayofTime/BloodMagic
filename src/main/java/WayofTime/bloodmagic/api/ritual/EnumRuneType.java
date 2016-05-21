package WayofTime.bloodmagic.api.ritual;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

public enum EnumRuneType implements IStringSerializable
{
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

    public static EnumRuneType byMetadata(int meta)
    {
        if (meta < 0 || meta >= values().length)
            meta = 0;

        return values()[meta];
    }

    public ItemStack getScribeStack()
    {
        return new ItemStack(Constants.BloodMagicItem.INSCRIPTION_TOOL.getItem(), 1, ordinal());
    }

    @Override
    public String toString()
    {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName()
    {
        return this.toString();
    }

}
