package WayofTime.bloodmagic.item.types;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ShardType implements ISubItem {

    WEAK,
    DEMONIC,
    ;

    @Nonnull
    @Override
    public String getInternalName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count) {
        return new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD, count, ordinal());
    }
}
