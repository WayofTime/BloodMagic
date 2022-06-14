package wayoftime.bloodmagic.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class GhostItemHelper
{
	public static void setItemGhostAmount(ItemStack stack, int amount)
	{
		NBTHelper.checkNBT(stack);
		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.GHOST_STACK_SIZE, amount);
	}

	public static int getItemGhostAmount(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);
		CompoundTag tag = stack.getTag();

		return tag.getInt(Constants.NBT.GHOST_STACK_SIZE);
	}

	public static boolean hasGhostAmount(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			return false;
		}

		CompoundTag tag = stack.getTag();
		return tag.contains(Constants.NBT.GHOST_STACK_SIZE);
	}

	public static void incrementGhostAmout(ItemStack stack, int value)
	{
		int amount = getItemGhostAmount(stack);
		amount += value;
		setItemGhostAmount(stack, amount);
	}

	public static void decrementGhostAmount(ItemStack stack, int value)
	{
		int amount = getItemGhostAmount(stack);
		amount -= value;
		setItemGhostAmount(stack, amount);
	}

	public static ItemStack getStackFromGhost(ItemStack ghostStack)
	{
		ItemStack newStack = ghostStack.copy();
		NBTHelper.checkNBT(newStack);
		CompoundTag tag = newStack.getTag();
		int amount = getItemGhostAmount(ghostStack);
		tag.remove(Constants.NBT.GHOST_STACK_SIZE);
		if (tag.isEmpty())
		{
			newStack.setTag(null);
		}
		newStack.setCount(amount);

		return newStack;
	}

	public static ItemStack getSingleStackFromGhost(ItemStack ghostStack)
	{
		ItemStack newStack = ghostStack.copy();
		NBTHelper.checkNBT(newStack);
		CompoundTag tag = newStack.getTag();
		tag.remove(Constants.NBT.GHOST_STACK_SIZE);
		if (tag.isEmpty())
		{
			newStack.setTag(null);
		}
		newStack.setCount(1);

		return newStack;
	}
}
