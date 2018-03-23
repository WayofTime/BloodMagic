package WayofTime.bloodmagic.item.types;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum AlchemicVialType implements ISubItem
{
    BASE(0x2e35ff);

    final int potionColour;

    AlchemicVialType(int colour1)
    {
        potionColour = colour1;
    }

    @Nonnull
    @Override
    public String getInternalName()
    {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count)
    {
        return new ItemStack(RegistrarBloodMagicItems.ALCHEMIC_VIAL, count, ordinal());
    }

    public int getColourForLayer(int layer)
    {
        if (layer == 0)
        {
            return potionColour;
        }
        return -1;
    }

    public static int getColourForLayer(int variant, int layer)
    {
        if (variant >= AlchemicVialType.values().length)
        {
            return -1;
        }

        return AlchemicVialType.values()[variant].getColourForLayer(layer);
    }
}
