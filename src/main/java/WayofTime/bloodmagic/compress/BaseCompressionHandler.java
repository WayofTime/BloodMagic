package WayofTime.bloodmagic.compress;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class BaseCompressionHandler extends CompressionHandler {
    public final ItemStack required;
    private final ItemStack result;
    private final int leftover;

    public BaseCompressionHandler(ItemStack input, ItemStack output, int remainder) {
        super();
        this.required = input;
        this.result = output;
        this.leftover = remainder;
    }

    public ItemStack getResultStack() {
        return this.result.copy();
    }

    public ItemStack getRequiredStack() {
        return this.required.copy();
    }

    @Override
    public ItemStack compressInventory(ItemStack[] inv, World world) {
        int remaining = this.getRemainingNeeded(inv);
        if (remaining <= 0) {
            this.drainInventory(inv);
            return this.getResultStack();
        }

        return ItemStack.EMPTY;
    }

    protected void compress(InventoryPlayer inv, Tuple<ItemStack, Integer> stackAndAmount) {
        /* Calculates how many compressed items can be added and how much should be left.
            Proceeds to first add the items that should be left over and then the compressed items to the inventory.
         */
        int keptAmount = this.getLeftover() + stackAndAmount.getSecond() % this.required.getCount();
        int resultAmount = (stackAndAmount.getSecond() - keptAmount) / this.required.getCount();
        ItemStack kept = stackAndAmount.getFirst();
        while (keptAmount > 64) {
            kept.setCount(64);
            inv.addItemStackToInventory(kept);
            keptAmount -= 64;
        }
        kept.setCount(keptAmount);
        inv.addItemStackToInventory(kept);

        for (int i = 0; i < resultAmount; i++) {
            inv.addItemStackToInventory(this.result);
        }
    }

    public int getRemainingNeeded(ItemStack[] inv) {
        int needed = this.required.getCount();
        int kept = this.getLeftover();
        return iterateThroughInventory(this.required, kept, inv, needed, true);
    }

    public int drainInventory(ItemStack[] inv) {
        int needed = this.required.getCount();
        int kept = this.getLeftover();
        return iterateThroughInventory(this.required, kept, inv, needed, true);
    }

    public int getLeftover() {
        return this.leftover;
    }
}