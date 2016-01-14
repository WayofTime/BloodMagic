package WayofTime.bloodmagic.api.util.helper;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.iface.IBindable;

public class BindableHelper
{
    /**
     * Bind an item to a player. Handles checking if the player was an
     * instanceof {@link net.minecraftforge.common.util.FakePlayer} or other
     * type of Fake Player.
     * 
     * @param stack
     *        - The ItemStack to bind
     * @param player
     *        - The Player to bind the ItemStack to
     * 
     * @return - Whether binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, EntityPlayer player)
    {
        return !PlayerHelper.isFakePlayer(player) && checkAndSetItemOwner(stack, PlayerHelper.getUUIDFromPlayer(player), player.getName());
    }

    /**
     * Bind an item to a username.
     * 
     * Requires the Item contained in the ItemStack to be an instanceof
     * {@link IBindable}
     * 
     * Fires {@link ItemBindEvent}.
     * 
     * @param stack
     *        - The ItemStack to bind
     * @param uuid
     *        - The username to bind the ItemStack to
     * @param currentUsername
     *        - The current name of the player.
     * 
     * @return - Whether the binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, String uuid, String currentUsername)
    {
        stack = NBTHelper.checkNBT(stack);

        if (!(stack.getItem() instanceof IBindable))
            return false;

        String currentOwner = stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);

        if (currentOwner == "") //The player has not been set yet, so set everything.
        {
            MinecraftForge.EVENT_BUS.post(new ItemBindEvent(PlayerHelper.getPlayerFromUUID(uuid), uuid, stack));
            ((IBindable) stack.getItem()).onBind(PlayerHelper.getPlayerFromUUID(uuid), stack);
            stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, uuid);
            stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, currentUsername);
            return true;
        } else if (currentOwner.equals(uuid)) //The player has been set, so this will simply update the display name
        {
            stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, currentUsername);
        }

        return true;
    }

    /**
     * @see BindableHelper#checkAndSetItemOwner(ItemStack, String)
     * 
     * @param stack
     *        - ItemStack to check
     * @param uuid
     *        - UUID of the Player
     * @param currentUsername
     *        - The current name of the player.
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, UUID uuid, String currentUsername)
    {
        return checkAndSetItemOwner(stack, uuid.toString(), currentUsername);
    }

    /**
     * Sets the Owner of the item without checking if it is already bound. Also
     * bypasses {@link ItemBindEvent}.
     * 
     * @param stack
     *        - The ItemStack to bind
     * @param ownerName
     *        - The username to bind the ItemStack to
     */
    public static void setItemOwner(ItemStack stack, String ownerName)
    {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, ownerName);
    }

    /**
     * Used to safely obtain the username of the ItemStack's owner
     * 
     * @param stack
     *        - The ItemStack to check the owner of
     * 
     * @return - The username of the ItemStack's owner
     */
    public static String getOwnerName(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        return PlayerHelper.getUsernameFromStack(stack);
    }

    /**
     * Used to safely obtain the UUID of the ItemStack's owner
     * 
     * @param stack
     *        - The ItemStack to check the owner of
     * 
     * @return - The UUID of the ItemStack's owner
     */
    public static String getOwnerUUID(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);
    }
}
