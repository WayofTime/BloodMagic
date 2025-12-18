package wayoftime.bloodmagic.common.data;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ConfiguredModel.Builder;
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
		for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
		{
			buildCubeAll(block.get());
		}

		for (RegistryObject<Block> block : BloodMagicBlocks.DUNGEONBLOCKS.getEntries())
		{
			buildDungeonBlock(block.get());
		}

        buildRandomStone(BloodMagicBlocks.DUNGEON_STONE.get());
        buildRandomStone(BloodMagicBlocks.CORROSIVE_DUNGEON_STONE.get());
        buildRandomStone(BloodMagicBlocks.DESTRUCTIVE_DUNGEON_STONE.get());
        buildRandomStone(BloodMagicBlocks.STEADFAST_DUNGEON_STONE.get());
        buildRandomStone(BloodMagicBlocks.VENGEFUL_DUNGEON_STONE.get());

        for (RegistryObject<Block> block : BloodMagicBlocks.DECORATIVE_STAIR.getEntries())
        {
            String basePath = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
            ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "block/dungeon/" + basePath.replaceAll("_stairs", ""));
            stairsBlock((StairBlock) block.get(), texture);
        }
        stairsBlock((StairBlock) BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get(), BloodMagic.rl("block/dungeon/dungeon_brick1"));

        for (RegistryObject<Block> block : BloodMagicBlocks.DECORATIVE_SLAB.getEntries())
        {
            String basePath = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
            ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "block/dungeon/" + basePath.replaceAll("_slab", ""));
            ResourceLocation fullBlock = new ResourceLocation(BloodMagic.MODID, "block/" + basePath.replaceAll("_slab", ""));
            slabBlock((SlabBlock) block.get(), fullBlock, texture);
        }

        for (RegistryObject<Block> block : BloodMagicBlocks.DECORATIVE_WALL.getEntries())
        {
            String basePath = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
            ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "block/dungeon/" + basePath.replaceAll("_wall", ""));
            wallBlock((WallBlock) block.get(), texture);
            ModelFile file = models().wallInventory(basePath + "_inventory", texture);
            file.assertExistence();
        }

        for (RegistryObject<Block> block : BloodMagicBlocks.DECORATIVE_GATE.getEntries())
        {
            String basePath = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
            ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "block/dungeon/" + basePath.replaceAll("_gate", ""));
            fenceGateBlock((FenceGateBlock) block.get(), texture);
        }

        buildWillPillarCap(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom"), BloodMagic.rl("block/dungeon/dungeon_pillartop"));
        buildWillPillarCenter(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar"), BloodMagic.rl("block/dungeon/dungeon_pillarheart"));
        buildWillPillarCenter(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial"), BloodMagic.rl("block/dungeon/dungeon_pillarheart"));

        buildWillPillarCap(BloodMagicBlocks.CORROSIVE_DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart_corrosive"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom_corrosive"), BloodMagic.rl("block/dungeon/dungeon_pillartop_corrosive"));
        buildWillPillarCenter(BloodMagicBlocks.CORROSIVE_DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar_corrosive"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_corrosive"));
        buildWillPillarCenter(BloodMagicBlocks.CORROSIVE_DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial_corrosive"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_corrosive"));

        buildWillPillarCap(BloodMagicBlocks.DESTRUCTIVE_DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart_destructive"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom_destructive"), BloodMagic.rl("block/dungeon/dungeon_pillartop_destructive"));
        buildWillPillarCenter(BloodMagicBlocks.DESTRUCTIVE_DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar_destructive"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_destructive"));
        buildWillPillarCenter(BloodMagicBlocks.DESTRUCTIVE_DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial_destructive"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_destructive"));

        buildWillPillarCap(BloodMagicBlocks.STEADFAST_DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart_steadfast"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom_steadfast"), BloodMagic.rl("block/dungeon/dungeon_pillartop_steadfast"));
        buildWillPillarCenter(BloodMagicBlocks.STEADFAST_DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar_steadfast"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_steadfast"));
        buildWillPillarCenter(BloodMagicBlocks.STEADFAST_DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial_steadfast"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_steadfast"));

        buildWillPillarCap(BloodMagicBlocks.VENGEFUL_DUNGEON_PILLAR_CAP.get(), BloodMagic.rl("block/dungeon/dungeon_pillarheart_vengeful"), BloodMagic.rl("block/dungeon/dungeon_pillarbottom_vengeful"), BloodMagic.rl("block/dungeon/dungeon_pillartop_vengeful"));
        buildWillPillarCenter(BloodMagicBlocks.VENGEFUL_DUNGEON_PILLAR_CENTER.get(), BloodMagic.rl("block/dungeon/dungeon_pillar_vengeful"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_vengeful"));
        buildWillPillarCenter(BloodMagicBlocks.VENGEFUL_DUNGEON_PILLAR_SPECIAL.get(), BloodMagic.rl("block/dungeon/dungeon_pillarspecial_vengeful"), BloodMagic.rl("block/dungeon/dungeon_pillarheart_vengeful"));

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

	private void buildRandomStone(Block block)
	{
		ResourceLocation modelLoc = ForgeRegistries.BLOCKS.getKey(block).withPrefix("block/");
        ResourceLocation textureLoc = ForgeRegistries.BLOCKS.getKey(block).withPrefix("block/dungeon/");

		VariantBlockStateBuilder variantBuilder = getVariantBuilder(block);

		variantBuilder.forAllStates(state -> {
			ModelFile modelFile = models().cubeAll(modelLoc.getPath(), textureLoc);
			ModelFile modelFile_mirrored = models().withExistingParent(modelLoc + "_mirrored", "cube_mirrored_all").texture("all", textureLoc);

			return ConfiguredModel.builder()
					.modelFile(modelFile).nextModel()
					.modelFile(modelFile_mirrored).nextModel()
					.modelFile(modelFile).rotationY(180).nextModel()
					.modelFile(modelFile_mirrored).rotationY(180).build();
		});
	}

	private void buildDungeonBlock(Block block)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile modelFile = models().cubeAll(basePath, BloodMagic.rl("block/dungeon/" + basePath));
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFile).build());
	}

	private void buildCubeTop(Block block, ResourceLocation side, ResourceLocation top)
	{
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();
		ModelFile model = models().cubeBottomTop(basePath, side, side, top);

		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
	}

	private void buildWillPillarCenter(Block block, ResourceLocation side, ResourceLocation end) {
		String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();

		getVariantBuilder(block).forAllStates(state -> {
			Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
			ModelFile modelFile = models().withExistingParent(basePath, "cube_column")
					.texture("side", side)
					.texture("end", end);
			return ConfiguredModel.builder()
					.modelFile(modelFile)
					.rotationX(axis != Direction.Axis.Y ? 270 : 0)
					.rotationY(axis == Direction.Axis.X ? 270 : 0)
					.build();
		});
	}

    private void buildWillPillarCap(Block block, ResourceLocation end, ResourceLocation bottom, ResourceLocation top) {
        String basePath = ForgeRegistries.BLOCKS.getKey(block).getPath();

        VariantBlockStateBuilder builder = getVariantBuilder(block);
        ModelFile upModel = models().cubeBottomTop(basePath, top, end, end);
        ModelFile downModel = models().cubeBottomTop(basePath + "_down", bottom, end, end);

        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.UP), ConfiguredModel.builder().modelFile(upModel).build());
        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.SOUTH), ConfiguredModel.builder().modelFile(upModel).rotationX(270).build());
        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.EAST), ConfiguredModel.builder().modelFile(upModel).rotationX(90).rotationY(90).build());

        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.DOWN), ConfiguredModel.builder().modelFile(downModel).build());
        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.NORTH), ConfiguredModel.builder().modelFile(downModel).rotationX(270).build());
        builder.addModels(builder.partialState().with(BlockPillarCap.FACING, Direction.WEST), ConfiguredModel.builder().modelFile(downModel).rotationX(90).rotationY(90).build());
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

	private static Map<EnumDemonWillType, String> suffixMap = Map.of(
			EnumDemonWillType.DEFAULT, "",
			EnumDemonWillType.CORROSIVE, "_corrosive",
			EnumDemonWillType.DESTRUCTIVE, "_destructive",
			EnumDemonWillType.STEADFAST, "_steadfast",
			EnumDemonWillType.VENGEFUL, "_vengeful"
	);

	private void buildFurnace(Block block)
	{
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for (Entry<EnumDemonWillType, String> entry : suffixMap.entrySet())
		{
			EnumDemonWillType type = entry.getKey();
			String suffix = entry.getValue();

			ModelFile furnace_off = models().orientableWithBottom("alchemicalreactionchamber" + suffix, BloodMagic.rl("block/arc_side" + suffix), BloodMagic.rl("block/arc_front" + suffix), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top" + suffix));
			ModelFile furnace_on = models().orientableWithBottom("alchemicalreactionchamber" + suffix + "_lit", BloodMagic.rl("block/arc_side" + suffix + "_lit"), BloodMagic.rl("block/arc_front" + suffix + "_lit"), BloodMagic.rl("block/arc_bottom"), BloodMagic.rl("block/arc_top" + suffix));

			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(90).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(180).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, false).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_off).rotationY(270).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.NORTH).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.EAST).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(90).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.SOUTH).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(180).addModel();
			builder.partialState().with(BlockAlchemicalReactionChamber.FACING, Direction.WEST).with(BlockAlchemicalReactionChamber.LIT, true).with(BlockAlchemicalReactionChamber.TYPE, type).modelForState().modelFile(furnace_on).rotationY(270).addModel();
		}
	}
}
