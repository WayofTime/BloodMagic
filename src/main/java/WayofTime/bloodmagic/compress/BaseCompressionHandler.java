package WayofTime.bloodmagic.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseCompressionHandler extends CompressionHandler {
    private final ItemStack required;
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