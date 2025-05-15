package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import wayoftime.bloodmagic.BloodMagic;

public class BMDataMaps {
    public static final DataMapType<Item, Double> TARTARIC_GEM_MAX_AMOUNTS = DataMapType.builder(
            BloodMagic.rl("tartaric_gem_max"),
            Registries.ITEM,
            Codec.DOUBLE
    ).synced(Codec.DOUBLE, true).build();
}
