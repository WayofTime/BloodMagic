package WayofTime.bloodmagic.api.ritual;

import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import WayofTime.bloodmagic.api.Constants;

public enum EnumRuneType implements IStringSerializable
{
    BLANK,
    WATER,
    FIRE,
    EARTH,
    AIR,
    DUSK,
    DAWN;

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
