package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.types.ISubItem;
import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemEnum<T extends Enum<T> & ISubItem> extends Item implements IVariantProvider {

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
        return types[MathHelper.clamp(stack.getItemDamage(), 0, types.length)];
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> variants = Lists.newArrayList();
        for (int i = 0; i < types.length; i++)
            variants.add(Pair.of(i, "type=" + types[i].getInternalName()));

        return variants;
    }
}