package WayofTime.bloodmagic.item.alchemy;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.iface.ICustomAlchemyConsumable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemCuttingFluid extends Item implements IVariantProvider, ICustomAlchemyConsumable {
    public static final String BASIC = "basicCuttingFluid";
    public static final String EXPLOSIVE = "explosive";
    private static ArrayList<String> names = new ArrayList<String>();

    public ItemCuttingFluid() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".cuttingFluid.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);

        buildItemList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        int max = getMaxUsesForFluid(stack);
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.cuttingFluidRatio", max - getDamageOfFluid(stack), max));
    }

    private void buildItemList() {
        names.add(0, BASIC);
        names.add(1, EXPLOSIVE);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public void populateVariants(Int2ObjectMap<String> variants) {
        for (String name : names)
            variants.put(names.indexOf(name), "type=" + name);
    }

    public int getDamageOfFluid(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getInteger("used");
    }

    public void applyDamageToFluid(ItemStack stack) {
        int damage = Math.min(getDamageOfFluid(stack) + 1, getMaxUsesForFluid(stack));
        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger("used", damage);
    }

    public int getMaxUsesForFluid(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0:
                return 16;
            case 1:
                return 64;
            default:
                return 1;
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) (getDamageOfFluid(stack)) / (double) (getMaxUsesForFluid(stack));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDamageOfFluid(stack) > 0;
    }

    @Override
    public ItemStack drainUseOnAlchemyCraft(ItemStack stack) {
        applyDamageToFluid(stack);
        if (getDamageOfFluid(stack) >= getMaxUsesForFluid(stack)) {
            return ItemStack.EMPTY;
        }

        return stack;
    }

    public static ItemStack getStack(String name) {
        return new ItemStack(RegistrarBloodMagicItems.CUTTING_FLUID, 1, names.indexOf(name));
    }

    public static ArrayList<String> getNames() {
        return names;
    }
}
