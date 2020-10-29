package wayoftime.bloodmagic.common.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class GeneratorBlockStates extends BlockStateProvider
{
	public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, BloodMagic.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
//		buildCubeAll(BloodMagicBlocks.TARTARICFORGE.get());
//		buildCubeAll(BloodMagicBlocks.SPEED_RUNE.get());

		for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
		{
			buildCubeAll(block.get());
		}

		buildCubeAll(BloodMagicBlocks.BLOOD_LIGHT.get());
		buildCubeAll(BloodMagicBlocks.BLANK_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.AIR_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.WATER_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.FIRE_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.EARTH_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.DUSK_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.DAWN_RITUAL_STONE.get());

		buildFurnace(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get());
	}

	private void buildCubeAll(Block block)
	{
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(cubeAll(block)).build());
	}

	private void buildFurnace(Block block)
	{
//		ConfiguredModel[] furnaceModel = ConfiguredModel.builder().modelFile().build();
		ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber", BloodMagic.rl("block/arc_side"), BloodMagic.rl("block/arc_front"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top"));
//		getVariantBuilder(block).addModels(block.getDefaultState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false), furnaceModel);

		VariantBlockStateBuilder builder = getVariantBuilder(block);

		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(90).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(180).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(270).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_off).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_off).rotationY(90).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_off).rotationY(180).addModel();
		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_off).rotationY(270).addModel();

//		getVariantBuilder(block).
	}
}
