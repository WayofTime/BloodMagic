package wayoftime.bloodmagic.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import wayoftime.bloodmagic.BloodMagic;

public class BMTags {
    public static class Items {
        public static final TagKey<Item> SOUL_GEM = tag(bm("soul_gems"));

        private static TagKey<Item> fromBlock(TagKey<Block> input) {
            return tag(input.location());
        }

        private static TagKey<Item> withParent(TagKey<Item> parent, ResourceLocation location) {
            return TagKey.create(Registries.ITEM, location.withPrefix(parent.location().getPath()+"/"));
        }

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }

    private static ResourceLocation c(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
