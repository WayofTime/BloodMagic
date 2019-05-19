package WayofTime.bloodmagic.item.soulBreath;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.soul.ISoulBreathContainer;
import WayofTime.bloodmagic.util.Constants;

public abstract class ItemSoulBreathContainer extends Item implements ISoulBreathContainer {
    @Override
    public double getBreath(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble(Constants.NBT.BREATH);
    }

    @Override
    public void setBreath(ItemStack stack, double amount) {
        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.BREATH, amount);
    }

    @Override
    public double drainBreath(ItemStack stack, double drainAmount, boolean doDrain) {
        double breath = getBreath(stack);

        double breathDrained = Math.min(drainAmount, breath);

        if (doDrain) {
            setBreath(stack, breath - breathDrained);
        }

        return breathDrained;
    }

    @Override
    public double fillBreath(ItemStack stack, double fillAmount, boolean doFill) {
        double current = this.getBreath(stack);
        double maxBreath = this.getMaxBreath(stack);

        double filled = Math.min(fillAmount, maxBreath - current);

        if (doFill) {
            this.setBreath(stack, filled + current);
        }

        return filled;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double maxWill = getMaxBreath(stack);
        if (maxWill <= 0) {
            return 1;
        }
        return 1.0 - (getBreath(stack) / maxWill);
    }

}
