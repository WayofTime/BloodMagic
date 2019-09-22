package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class IncenseHelper {

    public static double getCurrentIncense(PlayerEntity player) {
        CompoundNBT data = player.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_INCENSE)) {
            return data.getDouble(Constants.NBT.CURRENT_INCENSE);
        }

        return 0;
    }

    public static void setCurrentIncense(PlayerEntity player, double amount) {
        CompoundNBT data = player.getEntityData();
        data.setDouble(Constants.NBT.CURRENT_INCENSE, amount);
    }

    public static void setMaxIncense(PlayerEntity player, double amount) {
        CompoundNBT data = player.getEntityData();
        data.setDouble(Constants.NBT.MAX_INCENSE, amount);
    }

    public static double getMaxIncense(PlayerEntity player) {
        CompoundNBT data = player.getEntityData();
        if (data.hasKey(Constants.NBT.MAX_INCENSE)) {
            return data.getDouble(Constants.NBT.MAX_INCENSE);
        }
        return 0;
    }

    public static void setHasMaxIncense(ItemStack stack, PlayerEntity player, boolean isMax) {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.HAS_MAX_INCENSE, isMax);
    }

    public static boolean getHasMaxIncense(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.HAS_MAX_INCENSE);
    }
}