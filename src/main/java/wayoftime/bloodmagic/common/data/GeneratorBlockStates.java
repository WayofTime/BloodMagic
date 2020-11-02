package wayoftime.bloodmagic.common.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ConfiguredModel.Builder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder.PartBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
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

		buildCrystal(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), "defaultcrystal");
		buildCrystal(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), "corrosivecrystal");
		buildCrystal(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), "destructivecrystal");
		buildCrystal(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), "vengefulcrystal");
		buildCrystal(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), "steadfastcrystal");
	}

	private void buildCubeAll(Block block)
	{
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(cubeAll(block)).build());
	}

	private void buildCrystal(Block block, String name)
	{
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

		ModelFile[] crystalModels = new ModelFile[7];
		for (int i = 0; i < 7; i++)
		{
			crystalModels[i] = models().withExistingParent("block/crystal/" + name + (i + 1), modLoc("crystal" + (i + 1))).texture("crystal", modLoc("models/" + name));
		}

		for (int i = 0; i < 7; i++)
		{
			Integer[] intArray = new Integer[7 - i];
			for (int j = i; j < 7; j++)
			{
				intArray[j - i] = j;
			}

			for (Direction direction : Direction.values())
			{
				Builder<PartBuilder> partBuilder = builder.part().modelFile(crystalModels[i]);
				switch (direction)
				{
				case UP:
					break;
				case DOWN:
					partBuilder = partBuilder.rotationX(180);
					break;
				case EAST:
					partBuilder = partBuilder.rotationX(90).rotationY(90);
					break;
				case WEST:
					partBuilder = partBuilder.rotationX(90).rotationY(270);
					break;
				case NORTH:
					partBuilder = partBuilder.rotationX(90);
					break;
				case SOUTH:
					partBuilder = partBuilder.rotationX(270);
					break;
				}

				partBuilder.addModel().condition(BlockDemonCrystal.AGE, intArray).condition(BlockDemonCrystal.ATTACHED, direction).end();
			}
		}
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
