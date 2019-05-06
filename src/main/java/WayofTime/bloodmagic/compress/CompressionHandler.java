package WayofTime.bloodmagic.compress;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class CompressionHandler {
    public ItemStack compressInventory(ItemStack[] inv, World world) {
        return null;
    }

    public void compressInventory(InventoryPlayer inv, World world) {
        // Assigns existing BaseCompressionHandlers to their standardized ItemStacks with the amount found in the inventory (ex: handler, Snowball, 154) -> 154 Snowballs in the whole inventory
        Map<BaseCompressionHandler, Tuple<ItemStack, Integer>> compressor = new HashMap<>();
        for (ItemStack i : inv.mainInventory) {
            if (!i.isEmpty()) {
                ItemStack standardized = i.copy();
                standardized.setCount(1);
                BaseCompressionHandler handler = CompressionRegistry.compressionRegistry.get(standardized);
                if (handler != null) {
                    compressor.put(handler, new Tuple<>(standardized, compressor.get(handler).getSecond() + i.getCount()));
                }
            }
        }
        for (BaseCompressionHandler i : compressor.keySet()) {
            i.compress(inv, compressor.get(i));
        }

        CompressionRegistry.advancedCompressionHandler.compressInventory(inv, world);
    }


    // got lazy at the end... no advanced compression for this one
    // TODO: Implement correctly
    // TODO: Fix it
    // TODO: Advanced Handler
    // F.M.L.
    public static Pair<ItemStack, Boolean> compressInventory(TileEntity tile, World world) {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStack[] inventory = new ItemStack[itemHandler.getSlots()]; //THIS MUST NOT BE EDITED!
            ItemStack[] copyInventory = new ItemStack[itemHandler.getSlots()];

            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                inventory[slot] = itemHandler.extractItem(slot, 64, true);
                copyInventory[slot] = inventory[slot].copy();
            }

            for (BaseCompressionHandler handler : CompressionRegistry.compressionRegistry.values()) {
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

    public int iterateThroughInventory(ItemStack required, int kept, ItemStack[] inv, int needed, boolean doDrain) {
        int i = -1;

        for (ItemStack invStack : inv) {
            i++;

            if (invStack.isEmpty()) {
                continue;
            }

            if (invStack.isItemEqual(required) && (invStack.getTagCompound() == null ? required.getTagCompound() == null : invStack.getTagCompound().equals(required.getTagCompound()))) {
                int stackSize = invStack.getCount();
                int used = 0;
                if (kept > 0) {
                    int remainingFromStack = Math.max(stackSize - kept, 0);
                    used += stackSize - remainingFromStack;
                }

                kept -= used; // 0

                if (kept <= 0 && needed > 0) {
                    int remainingFromStack = Math.max(stackSize - used - needed, 0);
                    if (doDrain) {
                        invStack.setCount(remainingFromStack + used);
                        if (invStack.isEmpty()) {
                            inv[i] = ItemStack.EMPTY;
                        }
                    }

                    needed -= (stackSize - used - remainingFromStack);
                }

                if (needed <= 0) {
                    return 0;
                }
            }
        }

        return needed;


    }

}