package WayofTime.bloodmagic.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileInventory;

public class Utils
{
    public static boolean isInteger(String integer)
    {
        try
        {
            Integer.parseInt(integer);
        } catch (NumberFormatException e)
        {
            return false;
        } catch (NullPointerException e)
        {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * @see #insertItemToTile(TileInventory, EntityPlayer, int)
     * 
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * 
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player)
    {
        return insertItemToTile(tile, player, 0);
    }

    /**
     * Used for inserting an ItemStack with a stacksize of 1 to a tile's
     * inventory at slot 0
     * 
     * EG: Block Altar
     * 
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * @param slot
     *        - The slot to attempt to insert to
     * 
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player, int slot)
    {
        if (tile.getStackInSlot(slot) == null && player.getHeldItem() != null)
        {
            ItemStack input = player.getHeldItem().copy();
            input.stackSize = 1;
            player.getHeldItem().stackSize--;
            tile.setInventorySlotContents(slot, input);
            return true;
        } else if (tile.getStackInSlot(slot) != null && player.getHeldItem() == null)
        {
            if (!tile.getWorld().isRemote)
            {
                EntityItem invItem = new EntityItem(tile.getWorld(), player.posX, player.posY + 0.25, player.posZ, tile.getStackInSlot(slot));
                tile.getWorld().spawnEntityInWorld(invItem);
            }
            tile.clear();
            return false;
        }

        return false;
    }

    /**
     * Gets a default block for each type of {@link EnumAltarComponent}
     * 
     * @param component
     *        - The Component to provide a block for.
     * 
     * @return The default Block for the EnumAltarComponent
     */
    public static Block getBlockForComponent(EnumAltarComponent component)
    {
        switch (component)
        {
        case GLOWSTONE:
            return Blocks.glowstone;
        case BLOODSTONE:
            return ModBlocks.bloodStoneBrick;
        case BEACON:
            return Blocks.beacon;
        case BLOODRUNE:
            return ModBlocks.bloodRune;
        case CRYSTAL:
            return ModBlocks.crystal;
        case NOTAIR:
            return Blocks.stonebrick;
        default:
            return Blocks.air;
        }
    }
}
