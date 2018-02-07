package WayofTime.bloodmagic.item.types;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ComponentTypes implements ISubItem {

    REAGENT_WATER,
    REAGENT_LAVA,
    REAGENT_AIR,
    REAGENT_FAST_MINER,
    REAGENT_VOID,
    REAGENT_GROWTH,
    REAGENT_AFFINITY,
    REAGENT_SIGHT,
    REAGENT_BINDING,
    REAGENT_SUPPRESSION,
    FRAME_PART,
    REAGENT_BLOOD_LIGHT,
    REAGENT_MAGNETISM,
    REAGENT_HASTE,
    REAGENT_COMPRESSION,
    REAGENT_BRIDGE,
    REAGENT_SEVERANCE,
    REAGENT_TELEPOSITION,
    REAGENT_TRANSPOSITION,
    SAND_IRON,
    SAND_GOLD,
    SAND_COAL,
    PLANT_OIL,
    SULFUR,
    SALTPETER,
    NEURO_TOXIN,
    ANTISEPTIC,
    REAGENT_HOLDING,
    CATALYST_LENGTH_1,
    CATALYST_POWER_1,
    REAGENT_CLAW,
    REAGENT_BOUNCE,
    REAGENT_FROST,
    ;

    @Nonnull
    @Override
    public String getInternalName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nonnull
    @Override
    public ItemStack getStack() {
        return getStack(1);
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count) {
        return new ItemStack(RegistrarBloodMagicItems.COMPONENT, count, ordinal());
    }
}

