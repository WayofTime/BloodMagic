package WayofTime.bloodmagic.soul;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ISubItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum EnumDemonWillType implements IStringSerializable, ISubItem {
    DEFAULT("default"),
    CORROSIVE("corrosive"),
    DESTRUCTIVE("destructive"),
    VENGEFUL("vengeful"),
    STEADFAST("steadfast");

    public final String name;

    EnumDemonWillType(String name) {
        this.name = name;
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
        return getName();
    }

    @Nonnull
    @Override
    public ItemStack getStack(int count) {
        return new ItemStack(RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL, count, ordinal());
    }
}
