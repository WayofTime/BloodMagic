package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.NBTHolder;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class BindableHelper {

    /**
     * Bind an item to a player. Handles checking if the player was an instanceof
     * {@link net.minecraftforge.common.util.FakePlayer} or other type of Fake Player.
     *
     * @param stack  - The ItemStack to bind
     * @param player - The Player to bind the ItemStack to
     * @return - Whether binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, EntityPlayer player) {
        return !PlayerHelper.isFakePlayer(player) && checkAndSetItemOwner(stack, player.getGameProfile().getName());
    }

    /**
     * Bind an item to a username.
     *
     * Requires the Item contained in the ItemStack to be an instanceof {@link IBindable}
     *
     * Fires {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     * @return - Whether the binding was successful
     */
    public static boolean checkAndSetItemOwner(ItemStack stack, String ownerName) {
        stack = NBTHolder.checkNBT(stack);

        if (!(stack.getItem() instanceof IBindable))
            return false;

        if (Strings.isNullOrEmpty(stack.getTagCompound().getString(NBTHolder.NBT_OWNER))) {
            MinecraftForge.EVENT_BUS.post(new ItemBindEvent(PlayerHelper.getPlayerFromUsername(ownerName), ownerName, stack));
            ((IBindable) stack.getItem()).onBind(PlayerHelper.getPlayerFromUsername(ownerName), stack);
            stack.getTagCompound().setString(NBTHolder.NBT_OWNER, ownerName);
            return true;
        }

        return false;
    }

    /**
     * Sets the Owner of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     */
    public static void setItemOwner(ItemStack stack, String ownerName) {
        stack = NBTHolder.checkNBT(stack);

        stack.getTagCompound().setString(NBTHolder.NBT_OWNER, ownerName);
    }

    /**
     * Used to safely obtain the username of the ItemStack's owner
     *
     * @param stack - The ItemStack to check the owner of
     * @return - The username of the ItemStack's owner
     */
    public static String getOwnerName(ItemStack stack) {
        stack = NBTHolder.checkNBT(stack);

        return stack.getTagCompound().getString(NBTHolder.NBT_OWNER);
    }
}
