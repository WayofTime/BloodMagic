package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.ARCBlock;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

public class BMBlockStateProvider extends BlockStateProvider {
    public BMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BloodMagic.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BMBlocks.BASIC_BLOCKS.getEntries().forEach(block -> {
            if (block.getId().getPath().contains("tau")) {
                return;
            }
            simpleBlockWithItem(block.get(), cubeAll(block.get()));
        });

        simpleBlockWithItem(BMBlocks.BLANK_RITUAL_STONE.block().get(), cubeAll(BMBlocks.BLANK_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.AIR_RITUAL_STONE.block().get(), cubeAll(BMBlocks.AIR_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.WATER_RITUAL_STONE.block().get(), cubeAll(BMBlocks.WATER_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.FIRE_RITUAL_STONE.block().get(), cubeAll(BMBlocks.FIRE_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.EARTH_RITUAL_STONE.block().get(), cubeAll(BMBlocks.EARTH_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.DUSK_RITUAL_STONE.block().get(), cubeAll(BMBlocks.DUSK_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.DAWN_RITUAL_STONE.block().get(), cubeAll(BMBlocks.DAWN_RITUAL_STONE.block().get()));
        simpleBlockWithItem(BMBlocks.MASTER_RITUAL_STONE.block().get(), cubeAll(BMBlocks.MASTER_RITUAL_STONE.block().get()));

        VariantBlockStateBuilder builder = getVariantBuilder(BMBlocks.ARC_BLOCK.block().get());
        String bottom = "block/arc_bottom";
        String lit = "_lit";
        for (EnumWillType type : EnumWillType.values()) {
            String willName = type.getSerializedName();
            String side = "block/arc_side_" + willName;
            String front = "block/arc_front_" + willName;
            String top = "block/arc_top_" + willName;
            ModelFile on = models().orientableWithBottom("alchemical_reaction_chamber_" + willName + "_lit", bm(side + lit), bm(front + lit), bm(bottom), bm(top));
            ModelFile off = models().orientableWithBottom("alchemical_reaction_chamber_" + willName, bm(side), bm(front), bm(bottom), bm(top));
            if (type == EnumWillType.DEFAULT) {
                simpleBlockItem(BMBlocks.ARC_BLOCK.block().get(), off);
            }

            for (Direction facing : Direction.Plane.HORIZONTAL) {
                builder.partialState().with(ARCBlock.LIT, false).with(ARCBlock.FACING, facing).with(ARCBlock.TYPE, type).modelForState().modelFile(off).rotationY((int) facing.getOpposite().toYRot()).addModel();
                builder.partialState().with(ARCBlock.LIT, true).with(ARCBlock.FACING, facing).with(ARCBlock.TYPE, type).modelForState().modelFile(on).rotationY((int) facing.getOpposite().toYRot()).addModel();
            }
        }
    }

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }
}
