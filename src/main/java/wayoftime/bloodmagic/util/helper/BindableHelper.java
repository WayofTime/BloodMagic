package wayoftime.bloodmagic.util.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.util.Constants;

public class BindableHelper
{

	public static void applyBinding(ItemStack stack, Player player)
	{
		Binding binding = new Binding(player.getGameProfile().getId(), player.getGameProfile().getName());
		applyBinding(stack, binding);
	}

	public static void applyBinding(ItemStack stack, Binding binding)
	{
		if (!stack.hasTag())
			stack.setTag(new CompoundTag());

		stack.getTag().put("binding", binding.serializeNBT());
	}

	/**
	 * Sets the Owner Name of the item without checking if it is already bound. Also
	 * bypasses {@link ItemBindEvent}.
	 *
	 * @param stack     - The ItemStack to bind
	 * @param ownerName - The username to bind the ItemStack to
	 */
	public static void setItemOwnerName(ItemStack stack, String ownerName)
	{
		stack = NBTHelper.checkNBT(stack);

		stack.getTag().putString(Constants.NBT.OWNER_NAME, ownerName);
	}

	/**
	 * Sets the Owner UUID of the item without checking if it is already bound. Also
	 * bypasses {@link ItemBindEvent}.
	 *
	 * @param stack     - The ItemStack to bind
	 * @param ownerUUID - The UUID to bind the ItemStack to
	 */
	public static void setItemOwnerUUID(ItemStack stack, String ownerUUID)
	{
		stack = NBTHelper.checkNBT(stack);

		stack.getTag().putString(Constants.NBT.OWNER_UUID, ownerUUID);
	}
}