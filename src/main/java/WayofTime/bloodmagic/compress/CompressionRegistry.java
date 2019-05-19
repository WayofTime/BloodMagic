package WayofTime.bloodmagic.compress;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry aimed to help compress items in an inventory into its compressible
 * form.
 */
public class CompressionRegistry {
    private static List<CompressionHandler> compressionRegistry = new ArrayList<>();
    public static Map<ItemStack, Integer> thresholdMap = new HashMap<>();
    static Map<ItemStack, Tuple<ItemStack, Integer>> compressionMap = new HashMap<>();

    public static void registerHandler(CompressionHandler handler) {
        compressionRegistry.add(handler);
    }

    /**
     * Registers an item so that it only compresses while above this threshold
     *
     * @param stack     item/block to be compressed
     * @param threshold amount that is to be compressed
     */
    public static void registerItemThreshold(ItemStack stack, int threshold) {
        thresholdMap.put(stack, threshold);
    }

    public static ItemStack compressInventory(ItemStack[] inv, World world) {
        for (CompressionHandler handler : compressionRegistry) {
            ItemStack stack = handler.compressInventory(inv, world);
            if (!stack.isEmpty()) {
                return stack;
            }
        }

        return null;
    }

    public static Pair<ItemStack, Boolean> compressInventory(TileEntity tile, World world) {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStack[] inventory = new ItemStack[itemHandler.getSlots()]; //THIS MUST NOT BE EDITED!
            ItemStack[] copyInventory = new ItemStack[itemHandler.getSlots()];

            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                inventory[slot] = itemHandler.extractItem(slot, 64, true);
                copyInventory[slot] = inventory[slot].copy();
            }

            for (CompressionHandler handler : compressionRegistry) {
                ItemStack stack = handler.compressInventory(copyInventory, world);
                if (!stack.isEmpty()) {
                    for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                        if (inventory[slot] != null && !ItemStack.areItemStacksEqual(inventory[slot], copyInventory[slot])) {
                            itemHandler.extractItem(slot, inventory[slot].getCount(), false);
                            if (copyInventory[slot] != null) {
                                itemHandler.insertItem(slot, copyInventory[slot], false);
                            }
                        }
                    }

                    return Pair.of(Utils.insertStackIntoTile(stack, itemHandler), true);
                }
            }
        }

        return Pair.of(ItemStack.EMPTY, false);
    }


    public static int getItemThreshold(ItemStack stack, int needed) {
        Integer threshold = thresholdMap.get(stack);
        if (threshold != null)
            return threshold;
        else
            return stack.getMaxStackSize() - needed;
    }

    public static int getItemThreshold(ItemStack stack) {
        Integer threshold = thresholdMap.get(stack);
        if (threshold != null)
            return threshold;
        else
            return stack.getMaxStackSize();
    }


    public static boolean areItemStacksEqual(ItemStack stack, ItemStack compressedStack) {
        return stack.isItemEqual(compressedStack) && (stack.getTagCompound() == null ? !compressedStack.hasTagCompound() : stack.getTagCompound().equals(compressedStack.getTagCompound()));
    }
}