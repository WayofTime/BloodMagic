package wayoftime.bloodmagic.datagen.content.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.util.blockitem.BlockWithItemHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MineBlock extends BlockLootSubProvider {
    public MineBlock(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        BMBlocks.BASIC_BLOCKS.getEntries().forEach(holder -> dropSelfList.add(holder.get()));
        addDropSelf(BMBlocks.ARC_BLOCK); // TODO maybe let it keep fluids?
        addDropSelf(BMBlocks.BLOOD_ALTAR);
        addDropSelf(BMBlocks.HELLFIRE_FORGE);
    }

    private void addDropSelf(BlockWithItemHolder<? extends Block, ? extends BlockItem> toAdd) {
        dropSelfList.add(toAdd.block().get());
    }

    private final List<Block> specialDropList = List.of(BMBlocks.BLOOD_TANK.block().get(), BMBlocks.LIVING_STATION.block().get());
    private List<Block> dropSelfList = new ArrayList<>();

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> list = new ArrayList<>();
        list.addAll(specialDropList);
        list.addAll(dropSelfList);
        return list;
    }

    @Override
    protected void generate() {
        dropSelfList.forEach(this::dropSelf);
        copyComponents(BMBlocks.BLOOD_TANK);
        copyComponents(BMBlocks.LIVING_STATION);
        ritualStone(BMBlocks.RITUAL_STONE_WATER);
        ritualStone(BMBlocks.RITUAL_STONE_FIRE);
        ritualStone(BMBlocks.RITUAL_STONE_EARTH);
        ritualStone(BMBlocks.RITUAL_STONE_AIR);
        ritualStone(BMBlocks.RITUAL_STONE_DUSK);
        ritualStone(BMBlocks.RITUAL_STONE_DAWN);
    }

    private void ritualStone(BlockWithItemHolder<? extends Block, ? extends BlockItem> holder) {
        add(
                holder.block().get(),
                in -> this.createSingleItemTableWithSilkTouch(in, BMBlocks.RITUAL_STONE_BLANK)
        );
    }

    private void copyComponents(BlockWithItemHolder<? extends Block, ? extends BlockItem> holder) {
        add(
                holder.block().get(),
                LootTable.lootTable().withPool(
                        this.applyExplosionCondition(holder.block().get(), LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(holder)
                                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY))
                                )
                        )
                )
        );
    }
}
