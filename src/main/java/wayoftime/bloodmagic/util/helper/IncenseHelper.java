package wayoftime.bloodmagic.util.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wayoftime.bloodmagic.util.Constants;

public class IncenseHelper
{

	public static double getCurrentIncense(Player player)
	{
		CompoundTag data = player.getPersistentData();
		if (data.contains(Constants.NBT.CURRENT_INCENSE))
		{
			return data.getDouble(Constants.NBT.CURRENT_INCENSE);
		}

		return 0;
	}

	public static void setCurrentIncense(Player player, double amount)
	{
		CompoundTag data = player.getPersistentData();
		data.putDouble(Constants.NBT.CURRENT_INCENSE, amount);
	}

	public static void setMaxIncense(Player player, double amount)
	{
		CompoundTag data = player.getPersistentData();
		data.putDouble(Constants.NBT.MAX_INCENSE, amount);
	}

	public static double getMaxIncense(Player player)
	{
		CompoundTag data = player.getPersistentData();
		if (data.contains(Constants.NBT.MAX_INCENSE))
		{
			return data.getDouble(Constants.NBT.MAX_INCENSE);
		}
		return 0;
	}

	public static void setHasMaxIncense(ItemStack stack, Player player, boolean isMax)
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
