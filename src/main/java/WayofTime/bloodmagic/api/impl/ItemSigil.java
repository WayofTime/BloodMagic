package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for all (static) sigils.
 */
public class ItemSigil extends ItemBindable implements ISigil {
    private int lpUsed;

    public ItemSigil(int lpUsed) {
        super();

        this.lpUsed = lpUsed;
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

    @Override
    public boolean performArrayEffect(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean hasArrayEffect() {
        return false;
    }

    public int getLpUsed() {
        return lpUsed;
    }
}
