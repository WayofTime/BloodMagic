package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public class BindableHelper {

    /**
     * Bind an item to a player. Handles checking if the player was an instanceof
     * {@link net.minecraftforge.common.util.FakePlayer} or other type of Fake Player.
     *
     * @param stack  - The ItemStack to bind
     * @param player - The Player to bind the ItemStack to
     *
     * @return - Whether binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, EntityPlayer player) {
        return !PlayerHelper.isFakePlayer(player) && checkAndSetItemOwner(stack, PlayerHelper.getUUIDFromPlayer(player));
    }

    /**
     * Bind an item to a username.
     *
     * Requires the Item contained in the ItemStack to be an instanceof {@link IBindable}
     *
     * Fires {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param uuid - The username to bind the ItemStack to
     *
     * @return - Whether the binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, String uuid) {
        stack = NBTHelper.checkNBT(stack);

        if (!(stack.getItem() instanceof IBindable))
            return false;

        if (Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID))) {
            MinecraftForge.EVENT_BUS.post(new ItemBindEvent(PlayerHelper.getPlayerFromUUID(uuid), uuid, stack));
            ((IBindable) stack.getItem()).onBind(PlayerHelper.getPlayerFromUUID(uuid), stack);
            stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, uuid);
            return true;
        }

        return true;
    }

    /**
     * @see BindableHelper#checkAndSetItemOwner(ItemStack, String)
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, UUID uuid) {
        return checkAndSetItemOwner(stack, uuid.toString());
    }

    /**
     * Sets the Owner of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     */
    public static void setItemOwner(ItemStack stack, String ownerName) {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, ownerName);
    }

    /**
     * Used to safely obtain the username of the ItemStack's owner
     *
     * @param stack - The ItemStack to check the owner of
     *
     * @return - The username of the ItemStack's owner
     */
    public static String getOwnerName(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        return PlayerHelper.getUsernameFromStack(stack);
    }

    /**
     * Used to safely obtain the UUID of the ItemStack's owner
     *
     * @param stack - The ItemStack to check the owner of
     *
     * @return - The UUID of the ItemStack's owner
     */
    public static String getOwnerUUID(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);
    }
}
