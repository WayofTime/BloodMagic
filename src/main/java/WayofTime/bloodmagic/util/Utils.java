package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.tile.TileInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Utils {

    public static boolean isInteger(String integer) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Used for inserting an ItemStack with a stacksize of 1 to a tile's inventory at slot 0.
     * Returns {@code true} if the ItemStack is inserted, {@code false} otherwise
     * <p/>
     * EG: Block Altar
     *
     * @param tile   - The {@link TileInventory} to input the item to
     * @param player - The player to take the item from.
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player) {
        if (tile.getStackInSlot(0) == null && player.getHeldItem() != null) {
            ItemStack input = player.getHeldItem().copy();
            input.stackSize = 1;
            player.getHeldItem().stackSize--;
            tile.setInventorySlotContents(0, input);
            return true;
        } else if (tile.getStackInSlot(0) != null && player.getHeldItem() == null) {
            if (!tile.getWorld().isRemote) {
                EntityItem invItem = new EntityItem(tile.getWorld(), player.posX, player.posY + 0.25, player.posZ, tile.getStackInSlot(0));
                tile.getWorld().spawnEntityInWorld(invItem);
            }
            tile.clear();
            return false;
        }

        return false;
    }
}
