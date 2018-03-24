package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.types.ISubItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnum<T extends Enum<T> & ISubItem> extends Item {

    protected final T[] types;

    public ItemEnum(Class<T> enumClass, String baseName) {
        super();

        this.types = enumClass.getEnumConstants();

        setUnlocalizedName(BloodMagic.MODID + "." + baseName);
        setHasSubtypes(types.length > 1);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + getItemType(stack).getInternalName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (!isInCreativeTab(tab))
            return;

        for (T type : types)
            subItems.add(new ItemStack(this, 1, type.ordinal()));
    }

    public T getItemType(ItemStack stack) {
        return types[MathHelper.clamp(stack.getItemDamage(), 0, types.length - 1)];
    }

    public static class Variant<T extends Enum<T> & ISubItem> extends ItemEnum<T> implements IVariantProvider {

        public Variant(Class<T> enumClass, String baseName) {
            super(enumClass, baseName);
        }

        @Override
        public void gatherVariants(Int2ObjectMap<String> variants) {
            for (int i = 0; i < types.length; i++)
                variants.put(i, "type=" + types[i].getInternalName());
        }
    }
}
