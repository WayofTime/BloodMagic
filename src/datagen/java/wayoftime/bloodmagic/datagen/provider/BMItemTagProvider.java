package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.concurrent.CompletableFuture;

public class BMItemTagProvider extends ItemTagsProvider {
    public BMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, BloodMagic.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(BMTags.Blocks.STORAGE_BLOCKS_HELLFORGED, BMTags.Items.STORAGE_BLOCKS_HELLFORGED);

        tag(BMTags.Items.LIVING_SET)
                .add(BMItems.LIVING_HELMET.get(), BMItems.LIVING_PLATE.get(), BMItems.LIVING_LEGGINGS.get(), BMItems.LIVING_BOOTS.get());

        tag(BMTags.Items.LIVING_UPGRADE_SET)
                .addTag(BMTags.Items.LIVING_SET);

        tag(BMTags.Items.REVERTER);
        tag(BMTags.Items.EXPLOSIVES);
        tag(BMTags.Items.RESONATOR);
        tag(BMTags.Items.CUTTING_FLUIDS);
        tag(BMTags.Items.HYDRATION);

        tag(BMTags.Items.ARC_BLASTING);
        tag(BMTags.Items.ARC_SMELTING);
        tag(BMTags.Items.ARC_SMOKING);

        tag(BMTags.Items.ARC_FURNACE)
                .addTag(BMTags.Items.ARC_BLASTING)
                .addTag(BMTags.Items.ARC_SMELTING)
                .addTag(BMTags.Items.ARC_SMOKING);

        tag(BMTags.Items.ARC_TOOL)
                .addTag(BMTags.Items.REVERTER)
                .addTag(BMTags.Items.EXPLOSIVES)
                .addTag(BMTags.Items.RESONATOR)
                .addTag(BMTags.Items.CUTTING_FLUIDS)
                .addTag(BMTags.Items.HYDRATION)
                .addTag(BMTags.Items.ARC_FURNACE);

        tag(BMTags.Items.SOUL_GEM)
                .add(BMItems.SOUL_GEM_PETTY.get())
                .add(BMItems.SOUL_GEM_LESSER.get())
                .add(BMItems.SOUL_GEM_COMMON.get())
                .add(BMItems.SOUL_GEM_GREATER.get())
                .add(BMItems.SOUL_GEM_GRAND.get());
    }
}
