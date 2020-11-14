package wayoftime.bloodmagic.util.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import wayoftime.bloodmagic.util.Constants;

public class IncenseHelper
{

	public static double getCurrentIncense(PlayerEntity player)
	{
		CompoundNBT data = player.getPersistentData();
		if (data.contains(Constants.NBT.CURRENT_INCENSE))
		{
			return data.getDouble(Constants.NBT.CURRENT_INCENSE);
		}

		return 0;
	}

	public static void setCurrentIncense(PlayerEntity player, double amount)
	{
		CompoundNBT data = player.getPersistentData();
		data.putDouble(Constants.NBT.CURRENT_INCENSE, amount);
	}

	public static void setMaxIncense(PlayerEntity player, double amount)
	{
		CompoundNBT data = player.getPersistentData();
		data.putDouble(Constants.NBT.MAX_INCENSE, amount);
	}

	public static double getMaxIncense(PlayerEntity player)
	{
		CompoundNBT data = player.getPersistentData();
		if (data.contains(Constants.NBT.MAX_INCENSE))
		{
			return data.getDouble(Constants.NBT.MAX_INCENSE);
		}
		return 0;
	}

	public static void setHasMaxIncense(ItemStack stack, PlayerEntity player, boolean isMax)
	{
		stack = NBTHelper.checkNBT(stack);
		stack.getTag().putBoolean(Constants.NBT.HAS_MAX_INCENSE, isMax);
	}

	public static boolean getHasMaxIncense(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return stack.getTag().getBoolean(Constants.NBT.HAS_MAX_INCENSE);
	}
}
