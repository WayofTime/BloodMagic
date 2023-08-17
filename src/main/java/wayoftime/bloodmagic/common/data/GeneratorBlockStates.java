package wayoftime.bloodmagic.common.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ConfiguredModel.Builder;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder.PartBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.common.block.BlockInversionPillarEnd;
import wayoftime.bloodmagic.common.block.BlockRoutingNode;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;
import wayoftime.bloodmagic.common.block.BlockSpecialDungeonSeal;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.block.base.BlockPillarCap;
import wayoftime.bloodmagic.common.block.type.PillarCapType;
import wayoftime.bloodmagic.common.block.type.SpecialSealType;

public class GeneratorBlockStates extends BlockStateProvider
{
	public GeneratorBlockStates(PackOutput packOutput, ExistingFileHelper exFileHelper)
	{
		super(packOutput, BloodMagic.MODID, exFileHelper);
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

		for (RegistryObject<Block> block : BloodMagicBlocks.DUNGEONBLOCKS.getEntries())
		{
			buildDungeonBlock(block.get());
		}

		buildCubeAll(BloodMagicBlocks.BLOOD_LIGHT.get());
		buildCubeAll(BloodMagicBlocks.BLANK_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.AIR_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.WATER_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.FIRE_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.EARTH_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.DUSK_RITUAL_STONE.get());
		buildCubeAll(BloodMagicBlocks.DAWN_RITUAL_STONE.get());
		buildFarmland(BloodMagicBlocks.NETHER_SOIL.get(), BloodMagic.rl("block/nether_soil"), new ResourceLocation("block/netherrack"));

		buildFurnace(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get());

		buildCrystal(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), "defaultcrystal");
		buildCrystal(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), "corrosivecrystal");
		buildCrystal(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), "destructivecrystal");
		buildCrystal(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), "vengefulcrystal");
		buildCrystal(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), "steadfastcrystal");

		buildRoutingNode(BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), "routingnode");
		buildRoutingNode(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), "inputroutingnode");
		buildRoutingNode(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), "outputroutingnode");
		buildMasterRoutingNode(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get());

		buildRandomStone(BloodMagicBlocks.DUNGEON_STONE.get(), BloodMagic.rl("block/dungeon/dungeon_stone"));
		stairsBlock((StairBlock) BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get(), BloodMagic.rl("block/dungeon/dungeon_brick1"));
		stairsBlock((StairBlock) BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get(), BloodMagic.rl("block/dungeon/dungeon_polished"));
		buildPillarCenter(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar"), BloodMagic.rl("block/dungeon/dungeon_pillarheart"));
		buildPillarCenter(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial"), BloodMagic.rl("block/dungeon/dungeon_pillarheart"));
		buildWallInventory((WallBlock) BloodMagicBlocks.DUNGEON_BRICK_WALL.get(), BloodMagic.rl("block/dungeon/dungeon_brick1"));
		buildWallInventory((WallBlock) BloodMagicBlocks.DUNGEON_POLISHED_WALL.get(), BloodMagic.rl("block/dungeon/dungeon_polished"));
		fenceGateBlock((FenceGateBlock) BloodMagicBlocks.DUNGEON_BRICK_GATE.get(), BloodMagic.rl("block/dungeon/dungeon_brick1"));
		fenceGateBlock((FenceGateBlock) BloodMagicBlocks.DUNGEON_POLISHED_GATE.get(), BloodMagic.rl("block/dungeon/dungeon_polished"));

		slabBlock((SlabBlock) BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), BloodMagic.rl("dungeon_brick1"), BloodMagic.rl("block/dungeon/dungeon_brick1"));
		slabBlock((SlabBlock) BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), BloodMagic.rl("dungeon_tile"), BloodMagic.rl("block/dungeon/dungeon_tile"));

		buildPillarCap(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom"), BloodMagic.rl("block/dungeon/dungeon_pillartop"));

		buildAssortedBlock(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get(), modLoc("dungeon_brick1"), modLoc("dungeon_brick2"), modLoc("dungeon_brick3"));
		buildDungeonBlock(BloodMagicBlocks.DUNGEON_ORE.get());

		buildCubeAllWithTextureName("etherealopaquemimic");
		buildCubeAllWithTextureName("sentientmimic");
		buildCubeAllWithTextureName("solidclearmimic");
		buildCubeAllWithTextureName("solidlightmimic");
		buildCubeAllWithTextureName("solidopaquemimic");

		buildCubeAll(BloodMagicBlocks.SPECTRAL.get());
		buildCubeAll(BloodMagicBlocks.ALCHEMY_ARRAY.get());

		buildCubeTop(BloodMagicBlocks.TELEPOSER.get(), modLoc("block/teleposer_side"), modLoc("block/teleposer_top"));

		buildCubeAll(BloodMagicBlocks.DUNGEON_CONTROLLER.get());
		buildCubeAll(BloodMagicBlocks.DUNGEON_SEAL.get());
		buildSpecialDungeonSeal(BloodMagicBlocks.SPECIAL_DUNGEON_SEAL.get());

		buildCrop(BloodMagicBlocks.GROWING_DOUBT.get(), CropBlock.AGE, 7, BloodMagic.rl("block/creeping_doubt_1"), BloodMagic.rl("block/creeping_doubt_2"), BloodMagic.rl("block/creeping_doubt_3"), BloodMagic.rl("block/creeping_doubt_4"), BloodMagic.rl("block/creeping_doubt_5"), BloodMagic.rl("block/creeping_doubt_6"), BloodMagic.rl("block/creeping_doubt_7"), BloodMagic.rl("block/creeping_doubt_8"));
		buildCrossCrop(BloodMagicBlocks.WEAK_TAU.get(), CropBlock.AGE, 7, modLoc("block/weak_tau_1"), modLoc("block/weak_tau_2"), modLoc("block/weak_tau_3"), modLoc("block/weak_tau_4"), modLoc("block/weak_tau_5"), modLoc("block/weak_tau_6"), modLoc("block/weak_tau_7"), modLoc("block/weak_tau_8"));
		buildCrossCrop(BloodMagicBlocks.STRONG_TAU.get(), CropBlock.AGE, 7, modLoc("block/weak_tau_1"), modLoc("block/strong_tau_2"), modLoc("block/strong_tau_3"), modLoc("block/strong_tau_4"), modLoc("block/strong_tau_5"), modLoc("block/strong_tau_6"), modLoc("block/strong_tau_7"), modLoc("block/strong_tau_8"));

//		buildOrientable(BloodMagicBlocks.SHAPED_CHARGE.get(), "shaped_charge", modLoc("block/sub/shaped_charge"), modLoc("block/dungeon/dungeon_stone"), modLoc("block/dungeon/dungeon_tile"), new ResourceLocation("block/copper_block"), modLoc("block/largebloodstonebrick"), modLoc("models/defaultcrystal"));
		buildShapedCharge(BloodMagicBlocks.SHAPED_CHARGE.get(), "shaped_charge", modLoc("block/sub/shaped_charge"), modLoc("block/dungeon/dungeon_stone"), modLoc("block/dungeon/dungeon_tile"), modLoc("block/blankrune"), modLoc("block/largebloodstonebrick"), modLoc("models/defaultcrystal"));
		buildShapedCharge(BloodMagicBlocks.DEFORESTER_CHARGE.get(), "deforester_charge", modLoc("block/sub/shaped_charge"), new ResourceLocation("block/oak_log_top"), new ResourceLocation("block/oak_log_top"), modLoc("block/blankrune"), new ResourceLocation("block/oak_planks"), modLoc("models/defaultcrystal"));
		buildShapedCharge(BloodMagicBlocks.VEINMINE_CHARGE.get(), "veinmine_charge", modLoc("block/sub/shaped_charge"), new ResourceLocation("block/sandstone_bottom"), new ResourceLocation("block/sandstone_bottom"), modLoc("block/blankrune"), new ResourceLocation("block/sand"), modLoc("models/defaultcrystal"));
		buildShapedCharge(BloodMagicBlocks.FUNGAL_CHARGE.get(), "fungal_charge", modLoc("block/sub/shaped_charge"), new ResourceLocation("block/nether_wart_block"), new ResourceLocation("block/crimson_planks"), modLoc("block/blankrune"), new ResourceLocation("block/crimson_stem"), modLoc("models/defaultcrystal"));

		buildAugmentedShapedCharge(BloodMagicBlocks.AUG_SHAPED_CHARGE.get(), "aug_shaped_charge", modLoc("block/sub/augment_shaped_charge"), modLoc("block/dungeon/dungeon_stone"), modLoc("block/dungeon/dungeon_tile"), modLoc("block/blankrune"), modLoc("block/largebloodstonebrick"), modLoc("models/defaultcrystal"), modLoc("models/copper_trim"));
		buildAugmentedShapedCharge(BloodMagicBlocks.DEFORESTER_CHARGE_2.get(), "deforester_charge_2", modLoc("block/sub/augment_shaped_charge"), new ResourceLocation("block/oak_log_top"), new ResourceLocation("block/oak_log_top"), modLoc("block/blankrune"), new ResourceLocation("block/oak_planks"), modLoc("models/defaultcrystal"), modLoc("models/copper_trim"));
		buildAugmentedShapedCharge(BloodMagicBlocks.VEINMINE_CHARGE_2.get(), "veinmine_charge_2", modLoc("block/sub/augment_shaped_charge"), new ResourceLocation("block/sandstone_bottom"), new ResourceLocation("block/sandstone_bottom"), modLoc("block/blankrune"), new ResourceLocation("block/sand"), modLoc("models/defaultcrystal"), modLoc("models/copper_trim"));
		buildAugmentedShapedCharge(BloodMagicBlocks.FUNGAL_CHARGE_2.get(), "fungal_charge_2", modLoc("block/sub/augment_shaped_charge"), new ResourceLocation("block/nether_wart_block"), new ResourceLocation("block/crimson_planks"), modLoc("block/blankrune"), new ResourceLocation("block/crimson_stem"), modLoc("models/defaultcrystal"), modLoc("models/copper_trim"));

		buildAugmentedShapedCharge(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get(), "shaped_charge_deep", modLoc("block/sub/augment_shaped_charge"), modLoc("block/dungeon/dungeon_stone"), modLoc("block/dungeon/dungeon_tile"), modLoc("block/blankrune"), modLoc("block/largebloodstonebrick"), new ResourceLocation("block/iron_block"), modLoc("models/copper_trim"));

		getVariantBuilder(BloodMagicBlocks.INVERSION_PILLAR.get()).forAllStates(state -> {
			Builder builder = ConfiguredModel.builder();
			ModelFile model = models().withExistingParent("inversion_pillar", modLoc("pillar_mid")).texture("texture", modLoc("models/pillar_mid"));

			return builder.modelFile(model).build();
		});

		buildInversionPillarCap(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());
	}

	private void buildSpecialDungeonSeal(Block block)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for (SpecialSealType type : SpecialSealType.values())
		{
			ModelFile modelFile = models().cubeAll(basePath + "_" + type.name().toLowerCase(), modLoc("block/" + basePath + "_" + type.name().toLowerCase()));
			builder.partialState().with(BlockSpecialDungeonSeal.SEAL, type).modelForState().modelFile(modelFile).addModel();
		}
	}

	private void buildInversionPillarCap(Block block)
	{
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		ModelFile bottomModel = models().withExistingParent("inversion_pillar_bottom", modLoc("pillar_bottom")).texture("texture", modLoc("models/pillar_base"));
		ModelFile topModel = models().withExistingParent("inversion_pillar_top", modLoc("pillar_top")).texture("texture", modLoc("models/pillar_base"));
		builder.partialState().with(BlockInversionPillarEnd.TYPE, PillarCapType.BOTTOM).modelForState().modelFile(bottomModel).addModel();
		builder.partialState().with(BlockInversionPillarEnd.TYPE, PillarCapType.TOP).modelForState().modelFile(topModel).addModel();
	}

	private void buildShapedCharge(Block block, String name, ResourceLocation modelPath, ResourceLocation base, ResourceLocation edges, ResourceLocation centerCap, ResourceLocation binding, ResourceLocation core)
	{
//		ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber", BloodMagic.rl("block/arc_side"), BloodMagic.rl("block/arc_front"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top"));
//		getVariantBuilder(block).addModels(block.getDefaultState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false), furnaceModel);
		ModelFile model = models().withExistingParent(name, modelPath).texture("1", edges).texture("3", base).texture("4", centerCap).texture("5", binding).texture("6", core).texture("particle", core);

		VariantBlockStateBuilder builder = getVariantBuilder(block);

		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.UP).modelForState().modelFile(model).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.DOWN).modelForState().modelFile(model).rotationX(180).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.EAST).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.WEST).modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.NORTH).modelForState().modelFile(model).rotationX(90).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.SOUTH).modelForState().modelFile(model).rotationX(270).addModel();
	}

	private void buildAugmentedShapedCharge(Block block, String name, ResourceLocation modelPath, ResourceLocation base, ResourceLocation edges, ResourceLocation centerCap, ResourceLocation binding, ResourceLocation core, ResourceLocation bracket)
	{
//		ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber", BloodMagic.rl("block/arc_side"), BloodMagic.rl("block/arc_front"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top"));
//		getVariantBuilder(block).addModels(block.getDefaultState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false), furnaceModel);
		ModelFile model = models().withExistingParent(name, modelPath).texture("1", edges).texture("3", base).texture("4", centerCap).texture("5", binding).texture("2", core).texture("7", bracket).texture("particle", core);

		VariantBlockStateBuilder builder = getVariantBuilder(block);

		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.UP).modelForState().modelFile(model).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.DOWN).modelForState().modelFile(model).rotationX(180).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.EAST).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.WEST).modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.NORTH).modelForState().modelFile(model).rotationX(90).addModel();
		builder.partialState().with(BlockShapedExplosive.ATTACHED, Direction.SOUTH).modelForState().modelFile(model).rotationX(270).addModel();
	}

	private void buildCrop(Block block, IntegerProperty prop, int maxAge, ResourceLocation... textures)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for (int i = 0; i <= maxAge; i++)
		{
			ModelFile modelFile = models().crop(basePath + "_" + (i + 1), textures[i]);
			builder.partialState().with(prop, i).modelForState().modelFile(modelFile).addModel();
		}
	}

	private void buildCrossCrop(Block block, IntegerProperty prop, int maxAge, ResourceLocation... textures)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for (int i = 0; i <= maxAge; i++)
		{
			ModelFile modelFile = models().cross(basePath + "_" + (i + 1), textures[i]);
			builder.partialState().with(prop, i).modelForState().modelFile(modelFile).addModel();
		}
	}

	private void buildFarmland(Block block, ResourceLocation top, ResourceLocation side)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		getVariantBuilder(block).forAllStates(state -> {
			Builder builder = ConfiguredModel.builder();

			ModelFile file = models().withExistingParent(basePath, "template_farmland").texture("top", top).texture("dirt", side);

			return builder.modelFile(file).build();
		});
	}

	private void buildCubeAllWithTextureName(String texture)
	{
		models().cubeAll(texture, BloodMagic.rl("block/" + texture)).assertExistence();
	}

	private void buildAssortedBlock(Block block, ResourceLocation... modelResources)
	{
		getVariantBuilder(block).forAllStates(state -> {
			Builder builder = ConfiguredModel.builder();

			for (int i = 0; i < modelResources.length; i++)
			{
				ResourceLocation location = modelResources[i];
				ModelFile file = models().getExistingFile(location);
				if (i < modelResources.length - 1)
				{
					builder = builder.modelFile(file).nextModel();
				} else
				{
					builder = builder.modelFile(file);
				}
			}

			return builder.build();
		});
	}

	private void buildRandomStone(Block block, ResourceLocation texture)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile modelFile = models().cubeAll(basePath, texture);
		ModelFile modelFile_mirrored = models().withExistingParent(basePath + "_mirrored", "cube_mirrored_all").texture("all", texture);
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFile).nextModel().modelFile(modelFile_mirrored).nextModel().modelFile(modelFile).rotationY(180).nextModel().modelFile(modelFile_mirrored).rotationY(180).build());
	}

	private void buildWallInventory(WallBlock block, ResourceLocation texture)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		wallBlock(block, texture);
		ModelFile file = models().wallInventory(basePath + "_inventory", texture);
		file.assertExistence();
	}

	private void buildDungeonBlock(Block block)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile modelFile = models().cubeAll(basePath, BloodMagic.rl("block/dungeon/" + basePath));
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFile).build());
//		ModelFile furnace_off = models().cubeAll(block.getRegistryName().toString(), texture)\
	}

	private void buildCubeTop(Block block, ResourceLocation side, ResourceLocation top)
	{
//		this.top
//		String basePath = block.getRegistryName().getPath();
//		models().cubeAll(texture, BloodMagic.rl("block/" + texture)).assertExistence();
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile model = models().cubeBottomTop(basePath, side, side, top);

		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
	}

//	private void buildCubeTop(Block block, ResourceLocation side, ResourceLocation top)
//	{
//		String basePath = block.getRegistryName().getPath();
//		ModelFile yModel = models().cubeBottomTop(basePath, side, side, top);
//
//		ElementBuilder xElementBuilder = models().withExistingParent(basePath + "_x", "cube").texture("particle", side).texture("end", pillarEnd).texture("side", side).element();
//		xElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		xElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		xElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		xElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		xElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#end").end();
//		xElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#end").end();
//		ModelFile xModel = xElementBuilder.end();
//
//		ElementBuilder zElementBuilder = models().withExistingParent(basePath + "_z", "cube").texture("particle", side).texture("end", pillarEnd).texture("side", side).element();
//		zElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#side").end();
//		zElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#side").end();
//		zElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#end").end();
//		zElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#end").end();
//		zElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		zElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
//		ModelFile zModel = zElementBuilder.end();
//
//		VariantBlockStateBuilder builder = getVariantBuilder(block);
//		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).modelForState().modelFile(xModel).addModel();
//		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).modelForState().modelFile(yModel).addModel();
//		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(zModel).addModel();
//	}

	private void buildPillarCenter(Block block, ResourceLocation side, ResourceLocation pillarEnd)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile yModel = models().cubeColumn(basePath, side, pillarEnd);

		ElementBuilder xElementBuilder = models().withExistingParent(basePath + "_x", "cube").texture("particle", side).texture("end", pillarEnd).texture("side", side).element();
		xElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		xElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		xElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		xElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		xElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#end").end();
		xElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#end").end();
		ModelFile xModel = xElementBuilder.end();

		ElementBuilder zElementBuilder = models().withExistingParent(basePath + "_z", "cube").texture("particle", side).texture("end", pillarEnd).texture("side", side).element();
		zElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#side").end();
		zElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#side").end();
		zElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#end").end();
		zElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#end").end();
		zElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		zElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		ModelFile zModel = zElementBuilder.end();

		VariantBlockStateBuilder builder = getVariantBuilder(block);
		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).modelForState().modelFile(xModel).addModel();
		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).modelForState().modelFile(yModel).addModel();
		builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(zModel).addModel();
	}

	private void buildPillarCap(Block block, ResourceLocation pillarEnd, ResourceLocation sideBottom, ResourceLocation sideTop)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile upModel = models().cubeBottomTop(basePath, sideTop, pillarEnd, pillarEnd);
		ModelFile downModel = models().cubeBottomTop(basePath + "_down", sideBottom, pillarEnd, pillarEnd);

		ElementBuilder northElementBuilder = models().withExistingParent(basePath + "_north", "cube").texture("particle", pillarEnd).texture("sideBottom", sideBottom).texture("end", pillarEnd).texture("sideTop", sideTop).element();
		northElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#sideTop").end();
		northElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#sideBottom").end();
		northElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#end").end();
		northElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#end").end();
		northElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		northElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		ModelFile northModel = northElementBuilder.end();

		ElementBuilder southElementBuilder = models().withExistingParent(basePath + "_south", "cube").texture("particle", pillarEnd).texture("sideBottom", sideBottom).texture("end", pillarEnd).texture("sideTop", sideTop).element();
		southElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#sideBottom").end();
		southElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#sideTop").end();
		southElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#end").end();
		southElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#end").end();
		southElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		southElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		ModelFile southModel = southElementBuilder.end();

		ElementBuilder westElementBuilder = models().withExistingParent(basePath + "_west", "cube").texture("particle", pillarEnd).texture("sideBottom", sideBottom).texture("end", pillarEnd).texture("sideTop", sideTop).element();
		westElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		westElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		westElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		westElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		westElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#end").end();
		westElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#end").end();
		ModelFile westModel = westElementBuilder.end();

		ElementBuilder eastElementBuilder = models().withExistingParent(basePath + "_east", "cube").texture("particle", pillarEnd).texture("sideBottom", sideBottom).texture("end", pillarEnd).texture("sideTop", sideTop).element();
		eastElementBuilder.face(Direction.UP).uvs(0, 0, 16, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		eastElementBuilder.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		eastElementBuilder.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#sideTop").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		eastElementBuilder.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#sideBottom").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
		eastElementBuilder.face(Direction.WEST).uvs(16, 0, 0, 16).texture("#end").end();
		eastElementBuilder.face(Direction.EAST).uvs(16, 0, 0, 16).texture("#end").end();
		ModelFile eastModel = eastElementBuilder.end();

		VariantBlockStateBuilder builder = getVariantBuilder(block);
		builder.partialState().with(BlockPillarCap.FACING, Direction.UP).modelForState().modelFile(upModel).addModel();
		builder.partialState().with(BlockPillarCap.FACING, Direction.DOWN).modelForState().modelFile(downModel).addModel();
		builder.partialState().with(BlockPillarCap.FACING, Direction.NORTH).modelForState().modelFile(northModel).addModel();
		builder.partialState().with(BlockPillarCap.FACING, Direction.SOUTH).modelForState().modelFile(southModel).addModel();
		builder.partialState().with(BlockPillarCap.FACING, Direction.WEST).modelForState().modelFile(westModel).addModel();
		builder.partialState().with(BlockPillarCap.FACING, Direction.EAST).modelForState().modelFile(eastModel).addModel();
	}

	private BlockModelBuilder rotateTextureFace(BlockModelBuilder file, Direction face, FaceRotation rotation, String texture)
	{
//		BiConsumer<Direction, ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder> biCon = (fc, rot) -> {
//			rot.rotation(rotation).texture(texture);
//		};
		return file.element().face(face).uvs(16, 0, 0, 16).rotation(rotation).texture("#east").end().end();
//		return file.element().faces(biCon).texture("#east").end();
	}

	private void buildCubeAll(Block block)
	{
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(cubeAll(block)).build());
	}

	private void buildCubeTop(Block block)
	{
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(cubeAll(block)).build());
	}

//	private void buildStairs(StairsBlock block, ResourceLocation texture)
//	{
//		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(stairsBlock(block, texture)).build());
//	}

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

	private void buildRoutingNode(Block block, String name)
	{
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

		ModelFile nodeModel = models().withExistingParent("block/routing/" + name + "core", modLoc("routingnodecore")).texture("core", modLoc("models/model" + name));
		ModelFile baseModel = models().withExistingParent("block/routing/" + name + "base", modLoc("routingnodebase")).texture("base", modLoc("models/model" + name));

		builder.part().modelFile(nodeModel).addModel().end();
		for (Direction direction : Direction.values())
		{
			Builder<PartBuilder> partBuilder = builder.part().modelFile(baseModel);
			BooleanProperty prop = BlockRoutingNode.UP;

			switch (direction)
			{
			case UP:
				prop = BlockRoutingNode.UP;
				partBuilder = partBuilder.rotationX(180);
				break;
			case DOWN:
				prop = BlockRoutingNode.DOWN;
				break;
			case EAST:
				prop = BlockRoutingNode.EAST;
				partBuilder = partBuilder.rotationX(90).rotationY(270);
				break;
			case WEST:
				prop = BlockRoutingNode.WEST;
				partBuilder = partBuilder.rotationX(90).rotationY(90);
				break;
			case NORTH:
				prop = BlockRoutingNode.NORTH;
				partBuilder = partBuilder.rotationX(270);
				break;
			case SOUTH:
				prop = BlockRoutingNode.SOUTH;
				partBuilder = partBuilder.rotationX(90);
				break;
			}

			partBuilder.addModel().condition(prop, true).end();
		}
	}

	private void buildMasterRoutingNode(Block block)
	{
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

		ModelFile nodeModel = models().withExistingParent("block/routing/" + "modelmasterroutingnodecore", modLoc("masterroutingnodecore"));
		ModelFile baseModel = models().withExistingParent("block/routing/" + "modelmasterroutingnodebase", modLoc("masterroutingnodebase"));

		builder.part().modelFile(nodeModel).addModel().end();
		for (Direction direction : Direction.values())
		{
			Builder<PartBuilder> partBuilder = builder.part().modelFile(baseModel);
			BooleanProperty prop = BlockRoutingNode.UP;

			switch (direction)
			{
			case UP:
				prop = BlockRoutingNode.UP;
				partBuilder = partBuilder.rotationX(180);
				break;
			case DOWN:
				prop = BlockRoutingNode.DOWN;
				break;
			case EAST:
				prop = BlockRoutingNode.EAST;
				partBuilder = partBuilder.rotationX(90).rotationY(270);
				break;
			case WEST:
				prop = BlockRoutingNode.WEST;
				partBuilder = partBuilder.rotationX(90).rotationY(90);
				break;
			case NORTH:
				prop = BlockRoutingNode.NORTH;
				partBuilder = partBuilder.rotationX(270);
				break;
			case SOUTH:
				prop = BlockRoutingNode.SOUTH;
				partBuilder = partBuilder.rotationX(90);
				break;
			}

			partBuilder.addModel().condition(prop, true).end();
		}
	}

	private void buildFurnace(Block block)
	{
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		Map<EnumDemonWillType, String> suffixMap = new HashMap<>();

		suffixMap.put(EnumDemonWillType.DEFAULT, "");
		suffixMap.put(EnumDemonWillType.CORROSIVE, "_c");
		suffixMap.put(EnumDemonWillType.VENGEFUL, "_v");
		suffixMap.put(EnumDemonWillType.DESTRUCTIVE, "_d");
		suffixMap.put(EnumDemonWillType.STEADFAST, "_s");

		for (Entry<EnumDemonWillType, String> entry : suffixMap.entrySet())
		{
			EnumDemonWillType type = entry.getKey();
			String suffix = entry.getValue();

			ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber" + suffix, BloodMagic.rl("block/arc_side" + suffix), BloodMagic.rl("block/arc_front" + suffix), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top" + suffix));
			ModelFile furnace_on = models().orientableWithBottom("alchemicalreactionchamber" + suffix + "_lit", BloodMagic.rl("block/arc_side" + suffix + "_lit"), BloodMagic.rl("block/arc_front" + suffix + "_lit"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top" + suffix));
//			getVariantBuilder(block).addModels(block.getDefaultState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false), furnaceModel);

			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(90).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(180).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(270).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(90).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(180).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(270).addModel();

		}
////		ConfiguredModel[] furnaceModel = ConfiguredModel.builder().modelFile().build();
//		ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber", BloodMagic.rl("block/arc_side"), BloodMagic.rl("block/arc_front"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top"));
//		ModelFile furnace_on = models().orientableWithBottom("alchemicalreactionchamber_lit", BloodMagic.rl("block/arc_side_lit"), BloodMagic.rl("block/arc_front_lit"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top"));
////		getVariantBuilder(block).addModels(block.getDefaultState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false), furnaceModel);
//
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(90).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(180).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, false).modelForState().modelFile(furnace_off).rotationY(270).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_on).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_on).rotationY(90).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_on).rotationY(180).addModel();
//		builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, true).modelForState().modelFile(furnace_on).rotationY(270).addModel();

//		getVariantBuilder(block).
	}
}
