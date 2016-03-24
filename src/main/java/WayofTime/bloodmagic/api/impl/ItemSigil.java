package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import lombok.Getter;
import net.minecraft.item.ItemStack;

/**
 * Base class for all (static) sigils.
 */
public class ItemSigil extends ItemBindable implements ISigil
{
    @Getter
    private int lpUsed;

    public ItemSigil(int lpUsed)
    {
        super();

        this.lpUsed = lpUsed;
    }

    public boolean isUnusable(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getBoolean(Constants.NBT.UNUSABLE);
    }

    public ItemStack setUnusable(ItemStack stack, boolean unusable)
    {
        NBTHelper.checkNBT(stack);

        stack.getTagCompound().setBoolean(Constants.NBT.UNUSABLE, unusable);
        return stack;
    }
}
