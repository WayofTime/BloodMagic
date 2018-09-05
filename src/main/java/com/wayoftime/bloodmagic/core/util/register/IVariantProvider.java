package com.wayoftime.bloodmagic.core.util.register;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface IVariantProvider {

    /**
     * A mapping of meta -> state variant
     *
     * @param variants A map to populate with all variants
     */
    default void collectVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "inventory");
    }
}
