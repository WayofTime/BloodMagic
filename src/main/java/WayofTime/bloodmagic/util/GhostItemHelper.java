package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.util.helper.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GhostItemHelper {
    public static void setItemGhostAmount(ItemStack stack, int amount) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger(Constants.NBT.GHOST_STACK_SIZE, amount);
    }

    public static int getItemGhostAmount(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getInteger(Constants.NBT.GHOST_STACK_SIZE);
    }

    public static boolean hasGhostAmount(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return false;
        }

        NBTTagCompound tag = stack.getTagCompound();
        return tag.hasKey(Constants.NBT.GHOST_STACK_SIZE);
    }

    public static void incrementGhostAmout(ItemStack stack, int value) {
        int amount = getItemGhostAmount(stack);
        amount += value;
        setItemGhostAmount(stack, amount);
    }

    public static void decrementGhostAmount(ItemStack stack, int value) {
        int amount = getItemGhostAmount(stack);
        amount -= value;
        setItemGhostAmount(stack, amount);
    }

    public static ItemStack getStackFromGhost(ItemStack ghostStack) {
        ItemStack newStack = ghostStack.copy();
        NBTHelper.checkNBT(newStack);
        NBTTagCompound tag = newStack.getTagCompound();
        int amount = getItemGhostAmount(ghostStack);
        tag.removeTag(Constants.NBT.GHOST_STACK_SIZE);
        if (tag.isEmpty()) {
            newStack.setTagCompound(null);
        }
        newStack.setCount(amount);

        return newStack;
    }
}
