package WayofTime.bloodmagic.item.block.base;

import WayofTime.bloodmagic.block.base.BlockInteger;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockInteger extends ItemBlock {

    public ItemBlockInteger(BlockInteger block) {
        super(block);

        if (block.getMaxMeta() > 1)
            setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockInteger getBlock() {
        return (BlockInteger) super.getBlock();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return getBlock().getTranslationKey() + "." + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
