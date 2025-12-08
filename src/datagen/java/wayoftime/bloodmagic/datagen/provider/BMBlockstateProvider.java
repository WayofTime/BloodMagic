package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.ARCBlock;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.block.RoutingNodeBlock;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

public class BMBlockstateProvider extends BlockStateProvider {
    public BMBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BloodMagic.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BMBlocks.BASIC_BLOCKS.getEntries().forEach(block -> {
            simpleBlockWithItem(block.get(), cubeAll(block.get()));
        });

        buildArc();

        buildRoutingNode(BMBlocks.ROUTING_NODE.block().get(), "");
        buildRoutingNode(BMBlocks.MASTER_NODE.block().get(), "_master");
        buildRoutingNode(BMBlocks.INPUT_ROUTING_NODE.block().get(), "_input");
        buildRoutingNode(BMBlocks.OUTPUT_ROUTING_NODE.block().get(), "_output");
    }

    private void buildRoutingNode(Block node, String type) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(node);

        ModelFile base = models().getExistingFile(bm("block/routing_node_base" + type));
        ModelFile core = models().getExistingFile(bm("block/routing_node_core" + type));

        builder.part().modelFile(core).addModel().end();

        builder.part().modelFile(base).addModel().condition(RoutingNodeBlock.DOWN, true).end();
        builder.part().modelFile(base).rotationX(180).addModel().condition(RoutingNodeBlock.UP, true).end();
        builder.part().modelFile(base).rotationX(270).addModel().condition(RoutingNodeBlock.NORTH, true).end();
        builder.part().modelFile(base).rotationX(90).addModel().condition(RoutingNodeBlock.SOUTH, true).end();
        builder.part().modelFile(base).rotationY(90).rotationX(90).addModel().condition(RoutingNodeBlock.WEST, true).end();
        builder.part().modelFile(base).rotationY(90).rotationX(270).addModel().condition(RoutingNodeBlock.EAST, true).end();
    }

    private void buildArc() {
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
