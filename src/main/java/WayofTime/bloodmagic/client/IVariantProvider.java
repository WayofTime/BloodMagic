package WayofTime.bloodmagic.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IVariantProvider {

    /**
     * A mapping of meta -> state variant
     *
     * @param variants A map to populate with all variants
     */
    void populateVariants(Int2ObjectMap<String> variants);
}
