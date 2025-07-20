package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class GeneratorBlockTags extends IntrinsicHolderTagsProvider<Block>
{
	public GeneratorBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper)
	{
		super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), BloodMagic.MODID, helper);
	}

	@Override
	public void addTags(HolderLookup.Provider pProvider)
	{
		this.tag(BloodMagicTags.Blocks.ANDESITE).add(Blocks.ANDESITE);
		this.tag(BloodMagicTags.Blocks.SOUL_SAND).add(Blocks.SOUL_SAND);
		this.tag(BloodMagicTags.Blocks.SOUL_SOIL).add(Blocks.SOUL_SOIL);
		this.tag(BlockTags.WALLS).add(BloodMagicBlocks.DUNGEON_BRICK_WALL.get());
		this.tag(BlockTags.WALLS).add(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get());
		this.tag(BloodMagicTags.Blocks.MUSHROOM_STEM).add(Blocks.MUSHROOM_STEM).add(Blocks.CRIMSON_STEM).add(Blocks.WARPED_STEM);
		this.tag(BloodMagicTags.Blocks.MUSHROOM_HYPHAE).add(Blocks.BROWN_MUSHROOM_BLOCK).add(Blocks.RED_MUSHROOM_BLOCK).add(Blocks.CRIMSON_HYPHAE).add(Blocks.WARPED_HYPHAE).add(Blocks.STRIPPED_CRIMSON_HYPHAE).add(Blocks.STRIPPED_WARPED_HYPHAE).add(Blocks.NETHER_WART_BLOCK).add(Blocks.WARPED_WART_BLOCK).add(Blocks.SHROOMLIGHT);

		tag(BloodMagicTags.BLOCK_ORE_ALUMINUM);
		tag(BloodMagicTags.BLOCK_ORE_APATITE);
		tag(BloodMagicTags.BLOCK_ORE_CERTUS_QUARTZ);
		tag(BloodMagicTags.BLOCK_ORE_CINNABAR);
		tag(BloodMagicTags.BLOCK_ORE_COPPER);
		tag(BloodMagicTags.BLOCK_ORE_FLUORITE);
		tag(BloodMagicTags.BLOCK_ORE_INFERIUM);
		tag(BloodMagicTags.BLOCK_ORE_LEAD);
		tag(BloodMagicTags.BLOCK_ORE_NICKEL);
		tag(BloodMagicTags.BLOCK_ORE_NITER);
		tag(BloodMagicTags.BLOCK_ORE_OSMIUM);
		tag(BloodMagicTags.BLOCK_ORE_PROSPERITY);
		tag(BloodMagicTags.BLOCK_ORE_RUBY);
		tag(BloodMagicTags.BLOCK_ORE_SAPPHIRE);
		tag(BloodMagicTags.BLOCK_ORE_SILVER);
		tag(BloodMagicTags.BLOCK_ORE_SOULIUM);
		tag(BloodMagicTags.BLOCK_ORE_SULFUR);
		tag(BloodMagicTags.BLOCK_ORE_TIN);
		tag(BloodMagicTags.BLOCK_ORE_URANIUM);
		tag(BloodMagicTags.BLOCK_ORE_ZINC);

		tag(BloodMagicTags.BLOCK_SLAG);
		tag(BloodMagicTags.BLOCK_URANIUM);
		tag(BloodMagicTags.BLOCK_QUARTZ);
		tag(BloodMagicTags.BLOCK_FLUIX);
		tag(BloodMagicTags.BLOCK_SKY_STONE);

		tag(BloodMagicTags.BLOCK_STONE_UNPOLISHED).add(Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE);

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodMagicBlocks.SOUL_FORGE.get(), BloodMagicBlocks.INCENSE_ALTAR.get(), BloodMagicBlocks.BLANK_RUNE.get(), BloodMagicBlocks.SPEED_RUNE.get(), BloodMagicBlocks.SACRIFICE_RUNE.get(), BloodMagicBlocks.SELF_SACRIFICE_RUNE.get(), BloodMagicBlocks.DISPLACEMENT_RUNE.get(), BloodMagicBlocks.CAPACITY_RUNE.get(), BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get(), BloodMagicBlocks.ORB_RUNE.get(), BloodMagicBlocks.ACCELERATION_RUNE.get(), BloodMagicBlocks.CHARGING_RUNE.get(), BloodMagicBlocks.BLOOD_ALTAR.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get(), BloodMagicBlocks.AIR_RITUAL_STONE.get(), BloodMagicBlocks.WATER_RITUAL_STONE.get(), BloodMagicBlocks.FIRE_RITUAL_STONE.get(), BloodMagicBlocks.EARTH_RITUAL_STONE.get(), BloodMagicBlocks.DUSK_RITUAL_STONE.get(), BloodMagicBlocks.DAWN_RITUAL_STONE.get(), BloodMagicBlocks.BLOODSTONE.get(), BloodMagicBlocks.BLOODSTONE_BRICK.get(), BloodMagicBlocks.MASTER_RITUAL_STONE.get(), BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get(), BloodMagicBlocks.ALCHEMY_TABLE.get(), BloodMagicBlocks.DEMON_CRUCIBLE.get(), BloodMagicBlocks.DEMON_CRYSTALLIZER.get(), BloodMagicBlocks.DEMON_PYLON.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.ETHEREAL_MIMIC.get(), BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.TELEPOSER.get(), BloodMagicBlocks.STONE_PATH.get(), BloodMagicBlocks.STONE_TILE_PATH.get(), BloodMagicBlocks.WORN_STONE_PATH.get(), BloodMagicBlocks.WORN_STONE_TILE_PATH.get(), BloodMagicBlocks.OBSIDIAN_PATH.get(), BloodMagicBlocks.OBSIDIAN_TILE_PATH.get(), BloodMagicBlocks.MIMIC.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodMagicBlocks.DUNGEON_BRICK_1.get(), BloodMagicBlocks.DUNGEON_BRICK_2.get(), BloodMagicBlocks.DUNGEON_BRICK_3.get(), BloodMagicBlocks.DUNGEON_ORE.get(), BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get(), BloodMagicBlocks.DUNGEON_STONE.get(), BloodMagicBlocks.DUNGEON_EYE.get(), BloodMagicBlocks.DUNGEON_EMITTER.get(), BloodMagicBlocks.DUNGEON_ALTERNATOR.get(), BloodMagicBlocks.DUNGEON_POLISHED_STONE.get(), BloodMagicBlocks.DUNGEON_TILE.get(), BloodMagicBlocks.DUNGEON_SMALL_BRICK.get(), BloodMagicBlocks.DUNGEON_TILE_SPECIAL.get(), BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get(), BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get(), BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get(), BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get(), BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get(), BloodMagicBlocks.DUNGEON_PILLAR_CAP.get(), BloodMagicBlocks.DUNGEON_BRICK_WALL.get(), BloodMagicBlocks.DUNGEON_POLISHED_WALL.get(), BloodMagicBlocks.DUNGEON_BRICK_GATE.get(), BloodMagicBlocks.DUNGEON_POLISHED_GATE.get(), BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), BloodMagicBlocks.HELLFORGED_BLOCK.get(), BloodMagicBlocks.DUNGEON_CRACKED_BRICK_1.get(), BloodMagicBlocks.DUNGEON_GLOWING_CRACKED_BRICK_1.get(), BloodMagicBlocks.DUNGEON_CONTROLLER.get(), BloodMagicBlocks.DUNGEON_SEAL.get(), BloodMagicBlocks.SPIKES.get(), BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get(), BloodMagicBlocks.INVERSION_PILLAR_CAP.get(), BloodMagicBlocks.INVERSION_PILLAR.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodMagicBlocks.DEFORESTER_CHARGE.get()).add(BloodMagicBlocks.VEINMINE_CHARGE.get()).add(BloodMagicBlocks.VEINMINE_CHARGE_2.get()).add(BloodMagicBlocks.DEFORESTER_CHARGE_2.get()).add(BloodMagicBlocks.FUNGAL_CHARGE_2.get()).add(BloodMagicBlocks.SHAPED_CHARGE.get()).add(BloodMagicBlocks.FUNGAL_CHARGE.get()).add(BloodMagicBlocks.AUG_SHAPED_CHARGE.get()).add(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodMagicBlocks.SPEED_RUNE_2.get(), BloodMagicBlocks.SACRIFICE_RUNE_2.get(), BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get(), BloodMagicBlocks.DISPLACEMENT_RUNE_2.get(), BloodMagicBlocks.CAPACITY_RUNE_2.get(), BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get(), BloodMagicBlocks.ORB_RUNE_2.get(), BloodMagicBlocks.ACCELERATION_RUNE_2.get(), BloodMagicBlocks.CHARGING_RUNE_2.get());

		tag(BlockTags.MINEABLE_WITH_AXE).add(BloodMagicBlocks.WOOD_PATH.get(), BloodMagicBlocks.WOOD_TILE_PATH.get());

		tag(BlockTags.NEEDS_STONE_TOOL).add(BloodMagicBlocks.SOUL_FORGE.get(), BloodMagicBlocks.INCENSE_ALTAR.get(), BloodMagicBlocks.BLANK_RUNE.get(), BloodMagicBlocks.SPEED_RUNE.get(), BloodMagicBlocks.SACRIFICE_RUNE.get(), BloodMagicBlocks.SELF_SACRIFICE_RUNE.get(), BloodMagicBlocks.DISPLACEMENT_RUNE.get(), BloodMagicBlocks.CAPACITY_RUNE.get(), BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get(), BloodMagicBlocks.ORB_RUNE.get(), BloodMagicBlocks.ACCELERATION_RUNE.get(), BloodMagicBlocks.CHARGING_RUNE.get(), BloodMagicBlocks.BLOOD_ALTAR.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get(), BloodMagicBlocks.AIR_RITUAL_STONE.get(), BloodMagicBlocks.WATER_RITUAL_STONE.get(), BloodMagicBlocks.FIRE_RITUAL_STONE.get(), BloodMagicBlocks.EARTH_RITUAL_STONE.get(), BloodMagicBlocks.DUSK_RITUAL_STONE.get(), BloodMagicBlocks.DAWN_RITUAL_STONE.get(), BloodMagicBlocks.BLOODSTONE.get(), BloodMagicBlocks.BLOODSTONE_BRICK.get(), BloodMagicBlocks.MASTER_RITUAL_STONE.get(), BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get(), BloodMagicBlocks.ALCHEMY_TABLE.get(), BloodMagicBlocks.DEMON_CRUCIBLE.get(), BloodMagicBlocks.DEMON_CRYSTALLIZER.get(), BloodMagicBlocks.DEMON_PYLON.get());
		tag(BlockTags.NEEDS_STONE_TOOL).add(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.ETHEREAL_MIMIC.get(), BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get(), BloodMagicBlocks.TELEPOSER.get(), BloodMagicBlocks.STONE_PATH.get(), BloodMagicBlocks.STONE_TILE_PATH.get(), BloodMagicBlocks.WORN_STONE_PATH.get(), BloodMagicBlocks.WORN_STONE_TILE_PATH.get(), BloodMagicBlocks.MIMIC.get());
		tag(BlockTags.NEEDS_STONE_TOOL).add(BloodMagicBlocks.DUNGEON_BRICK_1.get(), BloodMagicBlocks.DUNGEON_BRICK_2.get(), BloodMagicBlocks.DUNGEON_BRICK_3.get(), BloodMagicBlocks.DUNGEON_ORE.get(), BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get(), BloodMagicBlocks.DUNGEON_STONE.get(), BloodMagicBlocks.DUNGEON_EYE.get(), BloodMagicBlocks.DUNGEON_EMITTER.get(), BloodMagicBlocks.DUNGEON_ALTERNATOR.get(), BloodMagicBlocks.DUNGEON_POLISHED_STONE.get(), BloodMagicBlocks.DUNGEON_TILE.get(), BloodMagicBlocks.DUNGEON_SMALL_BRICK.get(), BloodMagicBlocks.DUNGEON_TILE_SPECIAL.get(), BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get(), BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get(), BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get(), BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get(), BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get(), BloodMagicBlocks.DUNGEON_PILLAR_CAP.get(), BloodMagicBlocks.DUNGEON_BRICK_WALL.get(), BloodMagicBlocks.DUNGEON_POLISHED_WALL.get(), BloodMagicBlocks.DUNGEON_BRICK_GATE.get(), BloodMagicBlocks.DUNGEON_POLISHED_GATE.get(), BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), BloodMagicBlocks.HELLFORGED_BLOCK.get(), BloodMagicBlocks.DUNGEON_CRACKED_BRICK_1.get(), BloodMagicBlocks.DUNGEON_GLOWING_CRACKED_BRICK_1.get(), BloodMagicBlocks.DUNGEON_CONTROLLER.get(), BloodMagicBlocks.DUNGEON_SEAL.get(), BloodMagicBlocks.SPIKES.get(), BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get(), BloodMagicBlocks.INVERSION_PILLAR_CAP.get(), BloodMagicBlocks.INVERSION_PILLAR.get());
		tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BloodMagicBlocks.SPEED_RUNE_2.get(), BloodMagicBlocks.SACRIFICE_RUNE_2.get(), BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get(), BloodMagicBlocks.DISPLACEMENT_RUNE_2.get(), BloodMagicBlocks.CAPACITY_RUNE_2.get(), BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get(), BloodMagicBlocks.ORB_RUNE_2.get(), BloodMagicBlocks.ACCELERATION_RUNE_2.get(), BloodMagicBlocks.CHARGING_RUNE_2.get());
		tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BloodMagicBlocks.OBSIDIAN_PATH.get(), BloodMagicBlocks.OBSIDIAN_TILE_PATH.get());
		tag(BlockTags.BEACON_BASE_BLOCKS).add(BloodMagicBlocks.HELLFORGED_BLOCK.get());

		tag(BloodMagicTags.Blocks.MUNDANE_BLOCK).addTag(Tags.Blocks.COBBLESTONE).addTag(Tags.Blocks.STONE).addTag(BlockTags.SAND).addTag(BlockTags.DIRT).add(Blocks.GRAVEL).add(Blocks.NETHERRACK);
	}

	/**
	 * Resolves a Path for the location to save the given tag.
	 */
//	@Override
//	protected Path getPath(ResourceLocation id)
//	{
//		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
//	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName()
	{
		return "Block Tags";
	}
}
