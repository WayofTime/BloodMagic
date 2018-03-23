package WayofTime.bloodmagic.item.types;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum AlchemicTypes implements ISubItem
{
    BASE(0x2e35ff);

    final int potionColour;

    AlchemicTypes(int colour1)
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
    public ItemStack getStack()
    {
        return getStack(1);
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
        if (variant >= AlchemicTypes.values().length)
        {
            return -1;
        }

        return AlchemicTypes.values()[variant].getColourForLayer(layer);
    }
}
