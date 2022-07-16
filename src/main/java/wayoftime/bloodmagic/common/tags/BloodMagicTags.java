package wayoftime.bloodmagic.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicTags
{
	public static final TagKey<Item> ARC_TOOL = ItemTags.create(new ResourceLocation("bloodmagic:arc/tool"));
	public static final TagKey<Item> ARC_TOOL_FURNACE = ItemTags.create(new ResourceLocation("bloodmagic:arc/furnace"));
	public static final TagKey<Item> ARC_TOOL_SIEVE = ItemTags.create(new ResourceLocation("bloodmagic:arc/sieve"));
	public static final TagKey<Item> ARC_TOOL_REVERTER = ItemTags.create(new ResourceLocation("bloodmagic:arc/reverter"));
	public static final TagKey<Item> ARC_TOOL_EXPLOSIVE = ItemTags.create(new ResourceLocation("bloodmagic:arc/explosive"));
	public static final TagKey<Item> ARC_TOOL_HYDRATE = ItemTags.create(new ResourceLocation("bloodmagic:arc/hydrate"));
	public static final TagKey<Item> ARC_TOOL_RESONATOR = ItemTags.create(new ResourceLocation("bloodmagic:arc/resonator"));
	public static final TagKey<Item> ARC_TOOL_CUTTINGFLUID = ItemTags.create(new ResourceLocation("bloodmagic:arc/cuttingfluid"));

	public static final TagKey<Item> CRYSTAL_DEMON = ItemTags.create(new ResourceLocation("bloodmagic:crystals/demon"));

	public static final TagKey<Fluid> LIFE_ESSENCE = FluidTags.create(new ResourceLocation("forge:life"));

	public static final TagKey<Item> MUSHROOM_STEM = ItemTags.create(new ResourceLocation("minecraft:mushroom_stem"));
	public static final TagKey<Item> MUSHROOM_HYPHAE = ItemTags.create(new ResourceLocation("minecraft:mushroom_hyphae"));

	// Storage Blocks
	public static final TagKey<Block> BLOCK_QUARTZ = getForgeBlockStorageTag("quartz");
	public static final TagKey<Block> BLOCK_URANIUM = getForgeBlockStorageTag("uranium");
	public static final TagKey<Block> BLOCK_SLAG = getForgeBlockStorageTag("slag");
	public static final TagKey<Block> BLOCK_FLUIX = BlockTags.create(new ResourceLocation("appliedenergistics2:fluix_block"));
	public static final TagKey<Block> BLOCK_SKY_STONE = BlockTags.create(new ResourceLocation("appliedenergistics2:sky_stone_block"));

	// Ores
	public static final TagKey<Item> ORE_COPPER = getForgeOreTag("copper");
	public static final TagKey<Item> ORE_TIN = getForgeOreTag("tin");
	public static final TagKey<Item> ORE_LEAD = getForgeOreTag("lead");
	public static final TagKey<Item> ORE_OSMIUM = getForgeOreTag("osmium");
	public static final TagKey<Item> ORE_SILVER = getForgeOreTag("silver");
	public static final TagKey<Item> ORE_ALUMINUM = getForgeOreTag("aluminum");
	public static final TagKey<Item> ORE_APATITE = getForgeOreTag("apatite");
	public static final TagKey<Item> ORE_CERTUS_QUARTZ = getForgeOreTag("certus_quartz");
	public static final TagKey<Item> ORE_CINNABAR = getForgeOreTag("cinnabar");
	public static final TagKey<Item> ORE_FLUORITE = getForgeOreTag("fluorite");
	public static final TagKey<Item> ORE_INFERIUM = getForgeOreTag("inferium");
	public static final TagKey<Item> ORE_NICKEL = getForgeOreTag("nickel");
	public static final TagKey<Item> ORE_NITER = getForgeOreTag("niter");
	public static final TagKey<Item> ORE_PROSPERITY = getForgeOreTag("propserity");
	public static final TagKey<Item> ORE_RUBY = getForgeOreTag("ruby");
	public static final TagKey<Item> ORE_SAPPHIRE = getForgeOreTag("sapphire");
	public static final TagKey<Item> ORE_SOULIUM = getForgeOreTag("soulium");
	public static final TagKey<Item> ORE_SULFUR = getForgeOreTag("sulfur");
	public static final TagKey<Item> ORE_URANIUM = getForgeOreTag("uranium");
	public static final TagKey<Item> ORE_ZINC = getForgeOreTag("zinc");

	public static final TagKey<Block> BLOCK_ORE_COPPER = getForgeBlockOreTag("copper");
	public static final TagKey<Block> BLOCK_ORE_TIN = getForgeBlockOreTag("tin");
	public static final TagKey<Block> BLOCK_ORE_LEAD = getForgeBlockOreTag("lead");
	public static final TagKey<Block> BLOCK_ORE_OSMIUM = getForgeBlockOreTag("osmium");
	public static final TagKey<Block> BLOCK_ORE_SILVER = getForgeBlockOreTag("silver");
	public static final TagKey<Block> BLOCK_ORE_APATITE = getForgeBlockOreTag("apatite");
	public static final TagKey<Block> BLOCK_ORE_CINNABAR = getForgeBlockOreTag("cinnabar");
	public static final TagKey<Block> BLOCK_ORE_RUBY = getForgeBlockOreTag("ruby");
	public static final TagKey<Block> BLOCK_ORE_SAPPHIRE = getForgeBlockOreTag("sapphire");
	public static final TagKey<Block> BLOCK_ORE_ALUMINUM = getForgeBlockOreTag("aluminum");
	public static final TagKey<Block> BLOCK_ORE_CERTUS_QUARTZ = getForgeBlockOreTag("certus_quartz");
	public static final TagKey<Block> BLOCK_ORE_FLUORITE = getForgeBlockOreTag("fluorite");
	public static final TagKey<Block> BLOCK_ORE_INFERIUM = getForgeBlockOreTag("inferium");
	public static final TagKey<Block> BLOCK_ORE_NICKEL = getForgeBlockOreTag("nickel");
	public static final TagKey<Block> BLOCK_ORE_NITER = getForgeBlockOreTag("niter");
	public static final TagKey<Block> BLOCK_ORE_PROSPERITY = getForgeBlockOreTag("propserity");
	public static final TagKey<Block> BLOCK_ORE_SOULIUM = getForgeBlockOreTag("soulium");
	public static final TagKey<Block> BLOCK_ORE_SULFUR = getForgeBlockOreTag("sulfur");
	public static final TagKey<Block> BLOCK_ORE_URANIUM = getForgeBlockOreTag("uranium");
	public static final TagKey<Block> BLOCK_ORE_ZINC = getForgeBlockOreTag("zinc");

	public static final TagKey<Block> BLOCK_STONE_UNPOLISHED = BlockTags.create(new ResourceLocation("minecraft:stone_unpolished"));

	// Ore
	public static final TagKey<Item> ORE_HELLFORGED = getForgeOreTag("hellforged");

	// Raw
	public static final TagKey<Item> RAW_HELLFORGED = getForgeRawTag("hellforged");

	// Ingots
	public static final TagKey<Item> INGOT_HELLFORGED = getForgeIngotTag("hellforged");

	// Dusts (/Sands)
	public static final TagKey<Item> DUST_IRON = getForgeDustTag("iron");
	public static final TagKey<Item> DUST_GOLD = getForgeDustTag("gold");
	public static final TagKey<Item> DUST_COPPER = getForgeDustTag("copper");
	public static final TagKey<Item> DUST_COAL = getForgeDustTag("coal");
	public static final TagKey<Item> DUST_SALTPETER = getForgeDustTag("saltpeter");
	public static final TagKey<Item> DUST_SULFUR = getForgeDustTag("sulfur");
	public static final TagKey<Item> DUST_NETHERITE_SCRAP = getForgeDustTag("netherite_scrap");
	public static final TagKey<Item> DUST_HELLFORGED = getForgeDustTag("hellforged");
	public static final TagKey<Item> DUST_CORRUPTED = getBMDustTag("corrupted");
	public static final TagKey<Item> TINYDUST_CORRUPTED = getBMTinyDustTag("corrupted");

	// Fragments
	public static final TagKey<Item> FRAGMENTS = ItemTags.create(new ResourceLocation("bloodmagic:fragments"));
	public static final TagKey<Item> FRAGMENT_IRON = getFragmentTag("iron");
	public static final TagKey<Item> FRAGMENT_GOLD = getFragmentTag("gold");
	public static final TagKey<Item> FRAGMENT_COPPER = getFragmentTag("copper");
	public static final TagKey<Item> FRAGMENT_DEMONITE = getFragmentTag("hellforged");
	public static final TagKey<Item> FRAGMENT_NETHERITE_SCRAP = getFragmentTag("netherite_scrap");

	// Gravels
	public static final TagKey<Item> GRAVELS = ItemTags.create(new ResourceLocation("bloodmagic:gravels"));
	public static final TagKey<Item> GRAVEL_IRON = getGravelTag("iron");
	public static final TagKey<Item> GRAVEL_GOLD = getGravelTag("gold");
	public static final TagKey<Item> GRAVEL_COPPER = getGravelTag("copper");
	public static final TagKey<Item> GRAVEL_DEMONITE = getGravelTag("hellforged");
	public static final TagKey<Item> GRAVEL_NETHERITE_SCRAP = getGravelTag("netherite_scrap");

	// Vanilla
	public static final TagKey<Item> SWORDS = ItemTags.create(new ResourceLocation("forge:swords"));
	public static final TagKey<Item> AXES = ItemTags.create(new ResourceLocation("forge:axes"));
	public static final TagKey<Item> SHOVELS = ItemTags.create(new ResourceLocation("forge:shovels"));
	public static final TagKey<Item> PICKAXES = ItemTags.create(new ResourceLocation("forge:pickaxes"));
	public static final TagKey<Item> HOES = ItemTags.create(new ResourceLocation("forge:hoes"));

	// Modded (for meteor rituals)
	public static final TagKey<Item> ADVANCED_ALLOY = getForgeAlloyTag("advanced");
	public static final TagKey<Item> ANDESITE_ALLOY = ItemTags.create(new ResourceLocation("create:andesite_alloy"));
	public static final TagKey<Item> DRAGON_BONE = getForgeBoneTag("dragon");
	public static final TagKey<Item> GEM_CERTUS_QUARTZ = getForgeGemTag("certus_quartz");
	public static final TagKey<Item> PROSPERITY_SHARD = ItemTags.create(new ResourceLocation("mysticalagriculture:prosperity_shard"));
	public static final TagKey<Item> RF_COIL = ItemTags.create(new ResourceLocation("thermal:rf_coil"));
	public static final TagKey<Item> WIRECOIL_COPPER = ItemTags.create(new ResourceLocation("immersiveengineering:wirecoil_copper"));

	public static class Blocks
	{
		public static final TagKey<Block> MUSHROOM_STEM = BlockTags.create(new ResourceLocation("minecraft:mushroom_stem"));
		public static final TagKey<Block> MUSHROOM_HYPHAE = BlockTags.create(new ResourceLocation("minecraft:mushroom_hyphae"));
		public static final TagKey<Block> ANDESITE = BlockTags.create(new ResourceLocation("minecraft:andesite"));
		public static final TagKey<Block> SOUL_SAND = BlockTags.create(new ResourceLocation("minecraft:soul_sand"));
		public static final TagKey<Block> SOUL_SOIL = BlockTags.create(new ResourceLocation("minecraft:soul_soil"));
		public static final TagKey<Block> BLOCK_TELEPOSER_BLACKLIST = BlockTags.create(new ResourceLocation("bloodmagic:teleposer_blacklist"));

		public static final TagKey<Block> MUNDANE_BLOCK = BlockTags.create(BloodMagic.rl("mundane_block"));
	}

	public static TagKey<Item> getForgeOreTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:ores/" + name));
	}

	public static TagKey<Block> getForgeBlockOreTag(String name)
	{
		return BlockTags.create(new ResourceLocation("forge:ores/" + name));
	}

	public static TagKey<Block> getForgeBlockStorageTag(String name)
	{
		return BlockTags.create(new ResourceLocation("forge:storage_blocks/" + name));
	}

	public static TagKey<Item> getForgeGemTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:gems/" + name));
	}

	public static TagKey<Item> getForgeAlloyTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:alloys/" + name));
	}

	public static TagKey<Item> getForgeBoneTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:bones/" + name));
	}

	public static TagKey<Item> getForgeIngotTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:ingots/" + name));
	}

	public static TagKey<Item> getForgeRawTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:raw_materials/" + name));
	}

	public static TagKey<Item> getBMDustTag(String name)
	{
		return ItemTags.create(new ResourceLocation("bloodmagic:dusts/" + name));
	}

	public static TagKey<Item> getBMTinyDustTag(String name)
	{
		return ItemTags.create(new ResourceLocation("bloodmagic:tiny_dusts/" + name));
	}

	public static TagKey<Item> getForgeDustTag(String name)
	{
		return ItemTags.create(new ResourceLocation("forge:dusts/" + name));
	}

	public static TagKey<Item> getFragmentTag(String name)
	{
		return ItemTags.create(new ResourceLocation("bloodmagic:fragments/" + name));
	}

	public static TagKey<Item> getGravelTag(String name)
	{
		return ItemTags.create(new ResourceLocation("bloodmagic:gravels/" + name));
	}
}
