package wayoftime.bloodmagic.datagen.content.datamap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.BloodRune;
import wayoftime.bloodmagic.api.altar.EnumRuneType;
import wayoftime.bloodmagic.util.blockitem.BlockWithItemHolder;

import java.util.List;
import java.util.function.Function;

public class BloodRuneData {
    public static void bootstrap(Function<DataMapType<Block, List<BloodRune>>, DataMapProvider.Builder<List<BloodRune>, Block>> setup) {
        setup.apply(BMDataMaps.BLOOD_RUNES)
                .add(key(BMBlocks.RUNE_SACRIFICE), List.of(new BloodRune(EnumRuneType.SACRIFICE, 1)), false)
                .add(key(BMBlocks.RUNE_SELF_SACRIFICE), List.of(new BloodRune(EnumRuneType.SELF_SACRIFICE, 1)), false)
                .add(key(BMBlocks.RUNE_CAPACITY), List.of(new BloodRune(EnumRuneType.CAPACITY, 1)), false)
                .add(key(BMBlocks.RUNE_CAPACITY_AUGMENTED), List.of(new BloodRune(EnumRuneType.AUGMENTED_CAPACITY, 1)), false)
                .add(key(BMBlocks.RUNE_CHARGING), List.of(new BloodRune(EnumRuneType.CHARGING, 1)), false)
                .add(key(BMBlocks.RUNE_SPEED), List.of(new BloodRune(EnumRuneType.SPEED, 1)), false)
                .add(key(BMBlocks.RUNE_ACCELERATION), List.of(new BloodRune(EnumRuneType.ACCELERATION, 1)), false)
                .add(key(BMBlocks.RUNE_DISLOCATION), List.of(new BloodRune(EnumRuneType.DISPLACEMENT, 1)), false)
                .add(key(BMBlocks.RUNE_ORB), List.of(new BloodRune(EnumRuneType.ORB, 1)), false)
                .add(key(BMBlocks.RUNE_EFFICIENCY), List.of(new BloodRune(EnumRuneType.EFFICIENCY, 1)), false)

                .add(key(BMBlocks.RUNE_2_SACRIFICE), List.of(new BloodRune(EnumRuneType.SACRIFICE, 2)), false)
                .add(key(BMBlocks.RUNE_2_SELF_SACRIFICE), List.of(new BloodRune(EnumRuneType.SELF_SACRIFICE, 2)), false)
                .add(key(BMBlocks.RUNE_2_CAPACITY), List.of(new BloodRune(EnumRuneType.CAPACITY, 2)), false)
                .add(key(BMBlocks.RUNE_2_CAPACITY_AUGMENTED), List.of(new BloodRune(EnumRuneType.AUGMENTED_CAPACITY, 2)), false)
                .add(key(BMBlocks.RUNE_2_CHARGING), List.of(new BloodRune(EnumRuneType.CHARGING, 2)), false)
                .add(key(BMBlocks.RUNE_2_SPEED), List.of(new BloodRune(EnumRuneType.SPEED, 2)), false)
                .add(key(BMBlocks.RUNE_2_ACCELERATION), List.of(new BloodRune(EnumRuneType.ACCELERATION, 2)), false)
                .add(key(BMBlocks.RUNE_2_DISLOCATION), List.of(new BloodRune(EnumRuneType.DISPLACEMENT, 2)), false)
                .add(key(BMBlocks.RUNE_2_ORB), List.of(new BloodRune(EnumRuneType.ORB, 2)), false)
                .add(key(BMBlocks.RUNE_2_EFFICIENCY), List.of(new BloodRune(EnumRuneType.EFFICIENCY, 2)), false)
                .build();
    }

    private static ResourceKey<Block> key(BlockWithItemHolder<Block, BlockItem> holder) {
        return holder.block().getKey();
    }
}
