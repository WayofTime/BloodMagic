package WayofTime.bloodmagic.api.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.util.Utils;

/**
 * A registry aimed to help compress items in an inventory into its compressible
 * form.
 */
public class CompressionRegistry
{
    public static List<CompressionHandler> compressionRegistry = new ArrayList<CompressionHandler>();
    public static Map<ItemStack, Integer> thresholdMap = new HashMap<ItemStack, Integer>();

    public static void registerHandler(CompressionHandler handler)
    {
        compressionRegistry.add(handler);
    }

    /**
     * Registers an item so that it only compresses while above this threshold
     * 
     * @param stack
     *        item/block to be compressed
     * @param threshold
     *        amount that is to be compressed
     */
    public static void registerItemThreshold(ItemStack stack, int threshold)
    {
        thresholdMap.put(stack, threshold);
    }

    public static ItemStack compressInventory(ItemStack[] inv, World world)
    {
        for (CompressionHandler handler : compressionRegistry)
        {
            ItemStack stack = handler.compressInventory(inv, world);
            if (stack != null)
            {
                return stack;
            }
        }

        return null;
    }

    public static Pair<ItemStack, Boolean> compressInventory(TileEntity tile, World world)
    {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStack[] inventory = new ItemStack[itemHandler.getSlots()]; //THIS MUST NOT BE EDITED!
            ItemStack[] copyInventory = new ItemStack[itemHandler.getSlots()];

            for (int slot = 0; slot < itemHandler.getSlots(); slot++)
            {
                inventory[slot] = itemHandler.extractItem(slot, 64, true);
                copyInventory[slot] = ItemStack.copyItemStack(inventory[slot]);
            }

            for (CompressionHandler handler : compressionRegistry)
            {
                ItemStack stack = handler.compressInventory(copyInventory, world);
                if (stack != null)
                {
                    for (int slot = 0; slot < itemHandler.getSlots(); slot++)
                    {
                        if (inventory[slot] != null && !ItemStack.areItemStacksEqual(inventory[slot], copyInventory[slot]))
                        {
                            itemHandler.extractItem(slot, inventory[slot].stackSize, false);
                            if (copyInventory[slot] != null)
                            {
                                itemHandler.insertItem(slot, copyInventory[slot], false);
                            }
                        }
                    }

                    return Pair.of(Utils.insertStackIntoTile(stack, itemHandler), true);
                }
            }
        }

        return Pair.of(null, false);
    }

    public static int getItemThreshold(ItemStack stack)
    {
        for (Map.Entry<ItemStack, Integer> entry : thresholdMap.entrySet())
        {
            if (areItemStacksEqual(entry.getKey(), stack))
            {
                return entry.getValue();
            }
        }

        return 0;
    }

    public static boolean areItemStacksEqual(ItemStack stack, ItemStack compressedStack)
    {
        return stack.isItemEqual(compressedStack) && (stack.getTagCompound() == null ? compressedStack.getTagCompound() == null : stack.getTagCompound().equals(compressedStack.getTagCompound()));
    }
}
