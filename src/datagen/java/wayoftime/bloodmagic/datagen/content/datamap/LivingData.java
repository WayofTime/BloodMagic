package wayoftime.bloodmagic.datagen.content.datamap;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.LivingArmorData;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.function.Function;

public class LivingSet {
    public static void bootstrap(Function<DataMapType<Item, LivingArmorData>, DataMapProvider.Builder<LivingArmorData, Item>> setup) {
        setup.apply(BMDataMaps.LIVING_ARMOUR_DATA)
                .add(BMItems.LIVING_PLATE, new LivingArmorData(BMTags.Items.LIVING_SET, BMTags.Living.LIVING_START, BMTags.Living.LIVING_BLACKLIST), false);
    }
}
