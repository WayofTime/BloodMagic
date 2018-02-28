package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.event.ItemBindEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BindableHelper {

    public static void applyBinding(ItemStack stack, EntityPlayer player) {
        Binding binding = new Binding(player.getGameProfile().getId(), player.getGameProfile().getName());
        applyBinding(stack, binding);
    }

    public static void applyBinding(ItemStack stack, Binding binding) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setTag("binding", binding.serializeNBT());
    }

    /**
     * Sets the Owner Name of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     */
    public static void setItemOwnerName(ItemStack stack, String ownerName) {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, ownerName);
    }

    /**
     * Sets the Owner UUID of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerUUID - The UUID to bind the ItemStack to
     */
    public static void setItemOwnerUUID(ItemStack stack, String ownerUUID) {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, ownerUUID);
    }
}
