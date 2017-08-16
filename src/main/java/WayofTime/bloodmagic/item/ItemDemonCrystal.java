package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemDemonCrystal extends Item implements IDiscreteDemonWill, IVariantProvider {
    public static final ArrayList<String> NAMES = Lists.newArrayList();

    public static final String CRYSTAL_DEFAULT = "crystalDefault";
    public static final String CRYSTAL_CORROSIVE = "crystalCorrosive";
    public static final String CRYSTAL_VENGEFUL = "crystalVengeful";
    public static final String CRYSTAL_DESTRUCTIVE = "crystalDestructive";
    public static final String CRYSTAL_STEADFAST = "crystalSteadfast";

    public ItemDemonCrystal() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".demonCrystal.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);

        buildItemList();
    }

    private void buildItemList() {
        NAMES.add(0, CRYSTAL_DEFAULT);
        NAMES.add(1, CRYSTAL_CORROSIVE);
        NAMES.add(2, CRYSTAL_DESTRUCTIVE);
        NAMES.add(3, CRYSTAL_VENGEFUL);
        NAMES.add(4, CRYSTAL_STEADFAST);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + NAMES.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < NAMES.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public double getWill(ItemStack willStack) {
        return getDiscretization(willStack) * willStack.getCount();
    }

    @Override
    public double drainWill(ItemStack willStack, double drainAmount) {
        double discretization = getDiscretization(willStack);
        int drainedNumber = (int) Math.floor(Math.min(willStack.getCount() * discretization, drainAmount) / discretization);

        if (drainedNumber > 0) {
            willStack.shrink(drainedNumber);
            return drainedNumber * discretization;
        }

        return 0;
    }

    @Override
    public double getDiscretization(ItemStack willStack) {
        return 50;
    }

    @Override
    public EnumDemonWillType getType(ItemStack willStack) {
        return EnumDemonWillType.values()[MathHelper.clamp(willStack.getMetadata(), 0, EnumDemonWillType.values().length - 1)];
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : NAMES)
            ret.add(new ImmutablePair<Integer, String>(NAMES.indexOf(name), "type=" + name));
        return ret;
    }

    public static ItemStack getStack(String name) {
        return new ItemStack(RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL, 1, NAMES.indexOf(name));
    }
}
