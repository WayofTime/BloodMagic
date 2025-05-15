package wayoftime.bloodmagic.util.helper;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockWithItemRegister {
    private final DeferredRegister<Block> BLOCK;
    private final DeferredRegister<Item> ITEM;

    public BlockWithItemRegister(DeferredRegister<Block> block, DeferredRegister<Item> item) {
        BLOCK = block;
        ITEM = item;
    }

    public <B extends Block, I extends BlockItem> BlockWithItemHolder<B, I> register(String name, Supplier<B> block, Function<B, I> item) {
        DeferredHolder<Block, B> regBlock = BLOCK.register(name, block);
        DeferredHolder<Item, I> regItem = ITEM.register(name, () -> item.apply(regBlock.get()));
        return new BlockWithItemHolder<B, I>(regBlock, regItem);
    }

    public <B extends Block> BlockWithItemHolder<B, BlockItem> register(String name, Supplier<B> block) {
        return register(name, block, reg -> new BlockItem(reg, new Item.Properties()));
    }

    public <B extends Block> BlockWithItemHolder<B, BlockItem> register(String name, Supplier<B> block, Item.Properties properties) {
        return register(name, block, BlockItem::new, properties);
    }

    public <B extends Block, I extends BlockItem> BlockWithItemHolder<B, I> register(String name, Supplier<B> block, BiFunction<B, Item.Properties, I> item, Item.Properties properties) {
        return register(name, block, reg -> item.apply(reg, properties));
    }

    public <B extends Block, I extends BlockItem> BlockWithItemHolder<B, I> register(String name, Function<BlockBehaviour.Properties, B> block, BlockBehaviour.Properties blockProperties, BiFunction<B, Item.Properties, I> item, Item.Properties itemProperties) {
        return register(name, () -> block.apply(blockProperties), reg -> item.apply(reg, itemProperties));
    }

    public BlockWithItemHolder<Block, BlockItem> register(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return register(name, Block::new, blockProperties, BlockItem::new, itemProperties);
    }

    public <I extends BlockItem> BlockWithItemHolder<Block, I> register(String name, BlockBehaviour.Properties blockProperties, BiFunction<Block, Item.Properties, I> item, Item.Properties itemProperties) {
        return register(name, Block::new, blockProperties, item, itemProperties);
    }

    public <I extends BlockItem> BlockWithItemHolder<Block, I> register(String name, BlockBehaviour.Properties blockProperties, BiFunction<Block, Item.Properties, I> item) {
        return register(name, Block::new, blockProperties, item, new Item.Properties());
    }

    public <I extends BlockItem> BlockWithItemHolder<Block, I> register(String name, BlockBehaviour.Properties blockProperties, Function<Block, I> item) {
        return register(name, () -> new Block(blockProperties), item);
    }
}
