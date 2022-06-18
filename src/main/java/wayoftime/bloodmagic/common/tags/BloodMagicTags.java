package wayoftime.bloodmagic.common.tags;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class BloodMagicTags
{
	public static final ITag.INamedTag<Item> ARC_TOOL = ItemTags.makeWrapperTag("bloodmagic:arc/tool");
	public static final ITag.INamedTag<Item> ARC_TOOL_FURNACE = ItemTags.makeWrapperTag("bloodmagic:arc/furnace");
	public static final ITag.INamedTag<Item> ARC_TOOL_SIEVE = ItemTags.makeWrapperTag("bloodmagic:arc/sieve");
	public static final ITag.INamedTag<Item> ARC_TOOL_REVERTER = ItemTags.makeWrapperTag("bloodmagic:arc/reverter");
	public static final ITag.INamedTag<Item> ARC_TOOL_EXPLOSIVE = ItemTags.makeWrapperTag("bloodmagic:arc/explosive");
	public static final ITag.INamedTag<Item> ARC_TOOL_HYDRATE = ItemTags.makeWrapperTag("bloodmagic:arc/hydrate");
	public static final ITag.INamedTag<Item> ARC_TOOL_RESONATOR = ItemTags.makeWrapperTag("bloodmagic:arc/resonator");
	public static final ITag.INamedTag<Item> ARC_TOOL_CUTTINGFLUID = ItemTags.makeWrapperTag("bloodmagic:arc/cuttingfluid");

	public static final ITag.INamedTag<Item> CRYSTAL_DEMON = ItemTags.makeWrapperTag("bloodmagic:crystals/demon");

	public static final ITag.INamedTag<Fluid> LIFE_ESSENCE = FluidTags.makeWrapperTag("forge:life");

	public static final ITag.INamedTag<Item> MUSHROOM_STEM = ItemTags.makeWrapperTag("minecraft:mushroom_stem");
	public static final ITag.INamedTag<Item> MUSHROOM_HYPHAE = ItemTags.makeWrapperTag("minecraft:mushroom_hyphae");

	// Storage Blocks
	public static final ITag.INamedTag<Block> BLOCK_QUARTZ = getForgeBlockStorageTag("quartz");
	public static final ITag.INamedTag<Block> BLOCK_URANIUM = getForgeBlockStorageTag("uranium");
	public static final ITag.INamedTag<Block> BLOCK_SLAG = getForgeBlockStorageTag("slag");
	public static final ITag.INamedTag<Block> BLOCK_FLUIX = BlockTags.makeWrapperTag("appliedenergistics2:fluix_block"); //ae2 forgot to tag this one
	public static final ITag.INamedTag<Block> BLOCK_SKY_STONE = BlockTags.makeWrapperTag("appliedenergistics2:sky_stone_block"); //ae2 forgot to tag this one

	// Ores
	public static final ITag.INamedTag<Item> ORE_ALUMINUM = getForgeOreTag("aluminum");
	public static final ITag.INamedTag<Item> ORE_APATITE = getForgeOreTag("apatite");
	public static final ITag.INamedTag<Item> ORE_CERTUS_QUARTZ = getForgeOreTag("certus_quartz");
	public static final ITag.INamedTag<Item> ORE_CINNABAR = getForgeOreTag("cinnabar");
	public static final ITag.INamedTag<Item> ORE_COPPER = getForgeOreTag("copper");
	public static final ITag.INamedTag<Item> ORE_FLUORITE = getForgeOreTag("fluorite");
	public static final ITag.INamedTag<Item> ORE_INFERIUM = getForgeOreTag("inferium");
	public static final ITag.INamedTag<Item> ORE_LEAD = getForgeOreTag("lead");
	public static final ITag.INamedTag<Item> ORE_NICKEL = getForgeOreTag("nickel");
	public static final ITag.INamedTag<Item> ORE_NITER = getForgeOreTag("niter");
	public static final ITag.INamedTag<Item> ORE_OSMIUM = getForgeOreTag("osmium");
	public static final ITag.INamedTag<Item> ORE_PROSPERITY = getForgeOreTag("propserity");
	public static final ITag.INamedTag<Item> ORE_RUBY = getForgeOreTag("ruby");
	public static final ITag.INamedTag<Item> ORE_SAPPHIRE = getForgeOreTag("sapphire");
	public static final ITag.INamedTag<Item> ORE_SILVER = getForgeOreTag("silver");
	public static final ITag.INamedTag<Item> ORE_SOULIUM = getForgeOreTag("soulium");
	public static final ITag.INamedTag<Item> ORE_SULFUR = getForgeOreTag("sulfur");
	public static final ITag.INamedTag<Item> ORE_TIN = getForgeOreTag("tin");
	public static final ITag.INamedTag<Item> ORE_URANIUM = getForgeOreTag("uranium");
	public static final ITag.INamedTag<Item> ORE_ZINC = getForgeOreTag("zinc");

	public static final ITag.INamedTag<Block> BLOCK_ORE_ALUMINUM = getForgeBlockOreTag("aluminum");
	public static final ITag.INamedTag<Block> BLOCK_ORE_APATITE = getForgeBlockOreTag("apatite");
	public static final ITag.INamedTag<Block> BLOCK_ORE_CERTUS_QUARTZ = getForgeBlockOreTag("certus_quartz");
	public static final ITag.INamedTag<Block> BLOCK_ORE_CINNABAR = getForgeBlockOreTag("cinnabar");
	public static final ITag.INamedTag<Block> BLOCK_ORE_COPPER = getForgeBlockOreTag("copper");
	public static final ITag.INamedTag<Block> BLOCK_ORE_FLUORITE = getForgeBlockOreTag("fluorite");
	public static final ITag.INamedTag<Block> BLOCK_ORE_INFERIUM = getForgeBlockOreTag("inferium");
	public static final ITag.INamedTag<Block> BLOCK_ORE_LEAD = getForgeBlockOreTag("lead");
	public static final ITag.INamedTag<Block> BLOCK_ORE_NICKEL = getForgeBlockOreTag("nickel");
	public static final ITag.INamedTag<Block> BLOCK_ORE_NITER = getForgeBlockOreTag("niter");
	public static final ITag.INamedTag<Block> BLOCK_ORE_OSMIUM = getForgeBlockOreTag("osmium");
	public static final ITag.INamedTag<Block> BLOCK_ORE_PROSPERITY = getForgeBlockOreTag("propserity");
	public static final ITag.INamedTag<Block> BLOCK_ORE_RUBY = getForgeBlockOreTag("ruby");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SAPPHIRE = getForgeBlockOreTag("sapphire");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SILVER = getForgeBlockOreTag("silver");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SOULIUM = getForgeBlockOreTag("soulium");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SULFUR = getForgeBlockOreTag("sulfur");
	public static final ITag.INamedTag<Block> BLOCK_ORE_TIN = getForgeBlockOreTag("tin");
	public static final ITag.INamedTag<Block> BLOCK_ORE_URANIUM = getForgeBlockOreTag("uranium");
	public static final ITag.INamedTag<Block> BLOCK_ORE_ZINC = getForgeBlockOreTag("zinc");
	public static final ITag.INamedTag<Block> BLOCK_STONE_UNPOLISHED = BlockTags.makeWrapperTag("minecraft:stone_unpolished");

	// Ingots
	public static final ITag.INamedTag<Item> INGOT_HELLFORGED = getForgeIngotTag("hellforged");

	// Dusts (/Sands)
	public static final ITag.INamedTag<Item> DUST_IRON = getForgeDustTag("iron");
	public static final ITag.INamedTag<Item> DUST_GOLD = getForgeDustTag("gold");
	public static final ITag.INamedTag<Item> DUST_COAL = getForgeDustTag("coal");
	public static final ITag.INamedTag<Item> DUST_SALTPETER = getForgeDustTag("saltpeter");
	public static final ITag.INamedTag<Item> DUST_SULFUR = getForgeDustTag("sulfur");
	public static final ITag.INamedTag<Item> DUST_NETHERITE_SCRAP = getForgeDustTag("netherite_scrap");
	public static final ITag.INamedTag<Item> DUST_HELLFORGED = getForgeDustTag("hellforged");
	public static final ITag.INamedTag<Item> DUST_CORRUPTED = getBMDustTag("corrupted");
	public static final ITag.INamedTag<Item> TINYDUST_CORRUPTED = getBMTinyDustTag("corrupted");

	// Fragments
	public static final ITag.INamedTag<Item> FRAGMENT_IRON = getFragmentTag("iron");
	public static final ITag.INamedTag<Item> FRAGMENT_GOLD = getFragmentTag("gold");
	public static final ITag.INamedTag<Item> FRAGMENT_NETHERITE_SCRAP = getFragmentTag("netherite_scrap");

	// Gravels
	public static final ITag.INamedTag<Item> GRAVEL_IRON = getGravelTag("iron");
	public static final ITag.INamedTag<Item> GRAVEL_GOLD = getGravelTag("gold");
	public static final ITag.INamedTag<Item> GRAVEL_NETHERITE_SCRAP = getGravelTag("netherite_scrap");

	// Vanilla
	public static final ITag.INamedTag<Item> SWORDS = ItemTags.makeWrapperTag("forge:swords");
	public static final ITag.INamedTag<Item> AXES = ItemTags.makeWrapperTag("forge:axes");
	public static final ITag.INamedTag<Item> SHOVELS = ItemTags.makeWrapperTag("forge:shovels");
	public static final ITag.INamedTag<Item> PICKAXES = ItemTags.makeWrapperTag("forge:pickaxes");
	public static final ITag.INamedTag<Item> HOES = ItemTags.makeWrapperTag("forge:hoes");

	//Modded (for meteor rituals)
	public static final ITag.INamedTag<Item> ADVANCED_ALLOY = getForgeAlloyTag("advanced");
	public static final ITag.INamedTag<Item> ANDESITE_ALLOY = ItemTags.makeWrapperTag("create:andesite_alloy");
	public static final ITag.INamedTag<Item> DRAGON_BONE = getForgeBoneTag("dragon");
	public static final ITag.INamedTag<Item> GEM_CERTUS_QUARTZ = getForgeGemTag("certus_quartz");
	public static final ITag.INamedTag<Item> PROSPERITY_SHARD = ItemTags.makeWrapperTag("mysticalagriculture:prosperity_shard");
	public static final ITag.INamedTag<Item> RF_COIL = ItemTags.makeWrapperTag("thermal:rf_coil");
	public static final ITag.INamedTag<Item> WIRECOIL_COPPER = ItemTags.makeWrapperTag("immersiveengineering:wirecoil_copper");

	public static class Blocks
	{
		public static final ITag.INamedTag<Block> ANDESITE = BlockTags.makeWrapperTag("minecraft:andesite");
		public static final ITag.INamedTag<Block> SOUL_SAND = BlockTags.makeWrapperTag("minecraft:soul_sand");
		public static final ITag.INamedTag<Block> SOUL_SOIL = BlockTags.makeWrapperTag("minecraft:soul_soil");
		public static final ITag.INamedTag<Block> MUSHROOM_STEM = BlockTags.makeWrapperTag("minecraft:mushroom_stem");
		public static final ITag.INamedTag<Block> MUSHROOM_HYPHAE = BlockTags.makeWrapperTag("minecraft:mushroom_hyphae");
		public static final ITag.INamedTag<Block> BLOCK_TELEPOSER_BLACKLIST = BlockTags.makeWrapperTag("bloodmagic:teleposer_blacklist");
	}

	public static ITag.INamedTag<Item> getForgeOreTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:ores/" + name);
	}

	public static ITag.INamedTag<Block> getForgeBlockOreTag(String name)
	{
		return BlockTags.makeWrapperTag("forge:ores/" + name);
	}

	public static ITag.INamedTag<Block> getForgeBlockStorageTag(String name)
	{
		return BlockTags.makeWrapperTag("forge:storage_blocks/" + name);
	}

	public static ITag.INamedTag<Item> getForgeGemTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:gems/" + name);
	}

	public static ITag.INamedTag<Item> getForgeAlloyTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:alloys/" + name);
	}

	public static ITag.INamedTag<Item> getForgeBoneTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:bones/" + name);
	}


	public static ITag.INamedTag<Item> getForgeIngotTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:ingots/" + name);
	}

	public static ITag.INamedTag<Item> getBMDustTag(String name)
	{
		return ItemTags.makeWrapperTag("bloodmagic:dusts/" + name);
	}

	public static ITag.INamedTag<Item> getBMTinyDustTag(String name)
	{
		return ItemTags.makeWrapperTag("bloodmagic:tiny_dusts/" + name);
	}

	public static ITag.INamedTag<Item> getForgeDustTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:dusts/" + name);
	}

	public static ITag.INamedTag<Item> getFragmentTag(String name)
	{
		return ItemTags.makeWrapperTag("bloodmagic:fragments/" + name);
	}

	public static ITag.INamedTag<Item> getGravelTag(String name)
	{
		return ItemTags.makeWrapperTag("bloodmagic:gravels/" + name);
	}
}
