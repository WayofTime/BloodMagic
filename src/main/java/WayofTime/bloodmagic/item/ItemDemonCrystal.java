package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.soul.EnumDemonWillType;
import WayofTime.bloodmagic.apibutnotreally.soul.IDiscreteDemonWill;
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

public class ItemDemonCrystal extends ItemEnum<EnumDemonWillType> implements IDiscreteDemonWill, IVariantProvider {

    public ItemDemonCrystal() {
        super(EnumDemonWillType.class, "demonCrystal");

        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);
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
}
