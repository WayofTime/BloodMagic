package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Base class for all (static) sigils.
 */
public class ItemSigil extends Item implements IBindable, ISigil {
    private int lpUsed;

    public ItemSigil(int lpUsed) {
        super();

        this.lpUsed = lpUsed;

        setMaxStackSize(1);
    }

    public boolean isUnusable(ItemStack stack) {
        NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getBoolean(Constants.NBT.UNUSABLE);
    }

    public ItemStack setUnusable(ItemStack stack, boolean unusable) {
        NBTHelper.checkNBT(stack);

        stack.getTagCompound().setBoolean(Constants.NBT.UNUSABLE, unusable);
        return stack;
    }

    public int getLpUsed() {
        return lpUsed;
    }
}
