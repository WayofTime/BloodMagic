package wayoftime.bloodmagic.util;

import java.util.Locale;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.tile.TileInventory;

public class Utils
{
	/**
	 * @param tile   - The {@link TileInventory} to input the item to
	 * @param player - The player to take the item from.
	 * @return {@code true} if the ItemStack is inserted, {@code false} otherwise
	 * @see #insertItemToTile(TileInventory, PlayerEntity, int)
	 */
	public static boolean insertItemToTile(TileInventory tile, PlayerEntity player)
	{
		return insertItemToTile(tile, player, 0);
	}

	/**
	 * Used for inserting an ItemStack with a stacksize of 1 to a tile's inventory
	 * at slot 0
	 * <p/>
	 * EG: Block Altar
	 *
	 * @param tile   - The {@link TileInventory} to input the item to
	 * @param player - The player to take the item from.
	 * @param slot   - The slot to attempt to insert to
	 * @return {@code true} if the ItemStack is inserted, {@code false} otherwise
	 */
	public static boolean insertItemToTile(TileInventory tile, PlayerEntity player, int slot)
	{
		ItemStack slotStack = tile.getStackInSlot(slot);
		if (slotStack.isEmpty() && !player.getHeldItemMainhand().isEmpty())
		{
			ItemStack input = player.getHeldItemMainhand().copy();
			input.setCount(1);
			player.getHeldItemMainhand().shrink(1);
			tile.setInventorySlotContents(slot, input);
			return true;
		} else if (!slotStack.isEmpty() && player.getHeldItemMainhand().isEmpty())
		{
			ItemHandlerHelper.giveItemToPlayer(player, slotStack);
			tile.clear();
			return false;
		}

		return false;
	}

	public static String toFancyCasing(String input)
	{
		return String.valueOf(input.charAt(0)).toUpperCase(Locale.ENGLISH) + input.substring(1);
	}
}
