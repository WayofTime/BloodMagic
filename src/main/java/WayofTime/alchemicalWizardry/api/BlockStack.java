package WayofTime.alchemicalWizardry.api;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;

/**
 * A Block with a set metadata. Similar to an ItemStack.
 */
public class BlockStack {

    private Block block;
    private int meta;

    public BlockStack(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public BlockStack(Block block) {
        this(block, 0);
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return GameData.getBlockRegistry().getNameForObject(block) + ":" + meta;
    }

    @Override
    public boolean equals(Object obj) {
        BlockStack blockStack = (BlockStack) obj;

        return blockStack.block == this.getBlock() && blockStack.meta == this.getMeta();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
