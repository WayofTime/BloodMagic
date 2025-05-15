package wayoftime.bloodmagic.util.helper;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public record BlockWithItemHolder<B extends Block, I extends BlockItem>(DeferredHolder<Block, B> block, DeferredHolder<Item, I> item) implements ItemLike {
    @Override
    public Item asItem() {
        return item.get();
    }
}
