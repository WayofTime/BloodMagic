package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.datagen.BlockGroups;

import java.util.concurrent.CompletableFuture;

public class BMBlockTagProvider extends BlockTagsProvider {
    public BMBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BloodMagic.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BMTags.Blocks.RUNES)
                .addAll(BlockGroups.RUNE_T1)
                .addAll(BlockGroups.RUNE_T2);

        this.tag(BMTags.Blocks.T3_CAPSTONES)
                .add(Blocks.GLOWSTONE, Blocks.SHROOMLIGHT, Blocks.SEA_LANTERN)
                .add(Blocks.OCHRE_FROGLIGHT, Blocks.PEARLESCENT_FROGLIGHT, Blocks.VERDANT_FROGLIGHT);

        this.tag(BMTags.Blocks.T4_CAPSTONES)
                .addAll(BlockGroups.BLOODSTONE);

        this.tag(BMTags.Blocks.T5_CAPSTONES)
                .addAll(BlockGroups.HELLFORGED_BLOCK);

        this.tag(BMTags.Blocks.T6_CAPSTONES)
                .addAll(BlockGroups.CRYSTAL_CLUSTER);

        this.tag(BMTags.Blocks.PILLARS); // means all solid blocks are viable, has to be added otherwise the tag isnt generated

        this.tag(BMTags.Blocks.SOUL_NETWORK_COMPARATOR)
                .addAll(BlockGroups.BLOODSTONE);

        this.tag(BMTags.Blocks.PULSE_ON_CRAFTING)
                .add(Blocks.REDSTONE_LAMP, Blocks.NOTE_BLOCK);

        this.tag(BMTags.Blocks.STORAGE_BLOCKS_HELLFORGED)
                .addAll(BlockGroups.HELLFORGED_BLOCK);

        this.tag(BlockTags.BEACON_BASE_BLOCKS)
                .addAll(BlockGroups.HELLFORGED_BLOCK);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BMBlocks.BLOOD_ALTAR.block().getKey(), BMBlocks.BLOOD_TANK.block().getKey());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BMBlocks.BLOOD_ALTAR.block().getKey(), BMBlocks.BLOOD_TANK.block().getKey());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addAll(BlockGroups.BLOODSTONE)
                .addAll(BlockGroups.HELLFORGED_BLOCK)
                .addAll(BlockGroups.CRYSTAL_CLUSTER)
                .addAll(BlockGroups.RUNE_T1)
                .addAll(BlockGroups.RUNE_T2);

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .addAll(BlockGroups.BLOODSTONE)
                .addAll(BlockGroups.CRYSTAL_CLUSTER)
                .addAll(BlockGroups.RUNE_T1);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .addAll(BlockGroups.HELLFORGED_BLOCK);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .addAll(BlockGroups.RUNE_T2);
    }
}
