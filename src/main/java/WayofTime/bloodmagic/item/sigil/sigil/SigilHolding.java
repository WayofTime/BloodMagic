package WayofTime.bloodmagic.item.sigil.sigil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class SigilHolding implements ISigil.Holding {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public int getSize(ItemStack stack) {
        return 5;
    }

    @Override
    public int getEquippedSigil(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("current"))
            return -1;

        return stack.getTagCompound().getInteger("current");
    }

    @Override
    public NonNullList<ItemStack> getHeldSigils(ItemStack stack) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(getSize(stack), ItemStack.EMPTY);
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("inv"))
            return inventory;

        NBTTagList invTag = stack.getTagCompound().getTagList("inv", 10);
        for (int i = 0; i < invTag.tagCount(); i++)
            inventory.set(i, new ItemStack(invTag.getCompoundTagAt(i)));

        return inventory;
    }

    @Override
    public void setHeldSigils(ItemStack stack, NonNullList<ItemStack> inventory) {
        NBTTagList invTag = new NBTTagList();
        for (ItemStack invStack : inventory)
            if (!invStack.isEmpty())
                invTag.appendTag(invStack.writeToNBT(new NBTTagCompound()));

        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setTag("inv", invTag);
    }
}
