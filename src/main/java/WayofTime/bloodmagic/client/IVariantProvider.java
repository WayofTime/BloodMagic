package WayofTime.bloodmagic.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;

import javax.annotation.Nonnull;

public interface IVariantProvider {

    default void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, this instanceof Block ? "normal" : "inventory");
    }
}
