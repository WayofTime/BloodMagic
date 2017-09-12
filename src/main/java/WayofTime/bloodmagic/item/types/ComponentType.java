package WayofTime.bloodmagic.item.types;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum  ComponentType implements ISubItem {

    FRAME_PART,
    SAND_IRON,
    SAND_GOLD,
    SAND_COAL,
    PLANT_OIL,
    SULFUR,
    SALTPETER,
    NEURO_TOXIN,
    ANTISEPTIC,
    CATALYST_LENGTH_1,
    CATALYST_POWER_1,
    ;

    @Nonnull
    @Override
    public String getInternalName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count) {
        return new ItemStack(RegistrarBloodMagicItems.COMPONENT, count, ordinal());
    }
}
