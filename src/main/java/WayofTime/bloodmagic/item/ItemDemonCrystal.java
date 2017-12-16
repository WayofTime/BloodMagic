package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemDemonCrystal extends ItemEnum<EnumDemonWillType> implements IDiscreteDemonWill, IVariantProvider {

    public ItemDemonCrystal() {
        super(EnumDemonWillType.class, "demonCrystal");
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
