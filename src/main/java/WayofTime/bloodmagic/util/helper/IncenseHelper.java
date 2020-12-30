package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IncenseHelper {

    public static double getCurrentIncense(EntityPlayer player) {
        NBTTagCompound data = player.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_INCENSE)) {
            return data.getDouble(Constants.NBT.CURRENT_INCENSE);
        }

        return 0;
    }

    public static void setCurrentIncense(EntityPlayer player, double amount) {
        NBTTagCompound data = player.getEntityData();
        data.setDouble(Constants.NBT.CURRENT_INCENSE, amount);
    }

    public static void setMaxIncense(EntityPlayer player, double amount) {
        NBTTagCompound data = player.getEntityData();
        data.setDouble(Constants.NBT.MAX_INCENSE, amount);
    }

    public static double getMaxIncense(EntityPlayer player) {
        NBTTagCompound data = player.getEntityData();
        if (data.hasKey(Constants.NBT.MAX_INCENSE)) {
            return data.getDouble(Constants.NBT.MAX_INCENSE);
        }
        return 0;
    }

    public static void setHasMaxIncense(ItemStack stack, EntityPlayer player, boolean isMax) {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.HAS_MAX_INCENSE, isMax);
    }

    public static boolean getHasMaxIncense(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.HAS_MAX_INCENSE);
    }
}