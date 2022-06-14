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
	public static final ITag.INamedTag<Item> ARC_TOOL = ItemTags.bind("bloodmagic:arc/tool");
	public static final ITag.INamedTag<Item> ARC_TOOL_FURNACE = ItemTags.bind("bloodmagic:arc/furnace");
	public static final ITag.INamedTag<Item> ARC_TOOL_SIEVE = ItemTags.bind("bloodmagic:arc/sieve");
	public static final ITag.INamedTag<Item> ARC_TOOL_REVERTER = ItemTags.bind("bloodmagic:arc/reverter");
	public static final ITag.INamedTag<Item> ARC_TOOL_EXPLOSIVE = ItemTags.bind("bloodmagic:arc/explosive");
	public static final ITag.INamedTag<Item> ARC_TOOL_HYDRATE = ItemTags.bind("bloodmagic:arc/hydrate");
	public static final ITag.INamedTag<Item> ARC_TOOL_RESONATOR = ItemTags.bind("bloodmagic:arc/resonator");
	public static final ITag.INamedTag<Item> ARC_TOOL_CUTTINGFLUID = ItemTags.bind("bloodmagic:arc/cuttingfluid");

	public static final ITag.INamedTag<Item> CRYSTAL_DEMON = ItemTags.bind("bloodmagic:crystals/demon");

	public static final ITag.INamedTag<Fluid> LIFE_ESSENCE = FluidTags.bind("forge:life");

	public static final ITag.INamedTag<Item> MUSHROOM_STEM = ItemTags.bind("minecraft:mushroom_stem");
	public static final ITag.INamedTag<Item> MUSHROOM_HYPHAE = ItemTags.bind("minecraft:mushroom_hyphae");

	// Ores
	public static final ITag.INamedTag<Item> ORE_COPPER = getForgeOreTag("copper");
	public static final ITag.INamedTag<Item> ORE_TIN = getForgeOreTag("tin");
	public static final ITag.INamedTag<Item> ORE_LEAD = getForgeOreTag("lead");
	public static final ITag.INamedTag<Item> ORE_OSMIUM = getForgeOreTag("osmium");
	public static final ITag.INamedTag<Item> ORE_SILVER = getForgeOreTag("silver");

	public static final ITag.INamedTag<Block> BLOCK_ORE_COPPER = getForgeBlockOreTag("copper");
	public static final ITag.INamedTag<Block> BLOCK_ORE_TIN = getForgeBlockOreTag("tin");
	public static final ITag.INamedTag<Block> BLOCK_ORE_LEAD = getForgeBlockOreTag("lead");
	public static final ITag.INamedTag<Block> BLOCK_ORE_OSMIUM = getForgeBlockOreTag("osmium");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SILVER = getForgeBlockOreTag("silver");
	public static final ITag.INamedTag<Block> BLOCK_ORE_APATITE = getForgeBlockOreTag("apatite");
	public static final ITag.INamedTag<Block> BLOCK_ORE_CINNABAR = getForgeBlockOreTag("cinnabar");
	public static final ITag.INamedTag<Block> BLOCK_ORE_RUBY = getForgeBlockOreTag("ruby");
	public static final ITag.INamedTag<Block> BLOCK_ORE_SAPPHIRE = getForgeBlockOreTag("sapphire");

	public static final ITag.INamedTag<Block> BLOCK_STONE_UNPOLISHED = BlockTags.bind("minecraft:stone_unpolished");

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
	public static final ITag.INamedTag<Item> SWORDS = ItemTags.bind("forge:swords");
	public static final ITag.INamedTag<Item> AXES = ItemTags.bind("forge:axes");
	public static final ITag.INamedTag<Item> SHOVELS = ItemTags.bind("forge:shovels");
	public static final ITag.INamedTag<Item> PICKAXES = ItemTags.bind("forge:pickaxes");
	public static final ITag.INamedTag<Item> HOES = ItemTags.bind("forge:hoes");

	public static class Blocks
	{
		public static final ITag.INamedTag<Block> MUSHROOM_STEM = BlockTags.bind("minecraft:mushroom_stem");
		public static final ITag.INamedTag<Block> MUSHROOM_HYPHAE = BlockTags.bind("minecraft:mushroom_hyphae");
	}

	public static ITag.INamedTag<Item> getForgeOreTag(String name)
	{
		return ItemTags.bind("forge:ores/" + name);
	}

	public static ITag.INamedTag<Block> getForgeBlockOreTag(String name)
	{
		return BlockTags.bind("forge:ores/" + name);
	}

	public static ITag.INamedTag<Item> getForgeIngotTag(String name)
	{
		return ItemTags.bind("forge:ingots/" + name);
	}

	public static ITag.INamedTag<Item> getBMDustTag(String name)
	{
		return ItemTags.bind("bloodmagic:dusts/" + name);
	}

	public static ITag.INamedTag<Item> getBMTinyDustTag(String name)
	{
		return ItemTags.bind("bloodmagic:tiny_dusts/" + name);
	}

	public static ITag.INamedTag<Item> getForgeDustTag(String name)
	{
		return ItemTags.bind("forge:dusts/" + name);
	}

	public static ITag.INamedTag<Item> getFragmentTag(String name)
	{
		return ItemTags.bind("bloodmagic:fragments/" + name);
	}

	public static ITag.INamedTag<Item> getGravelTag(String name)
	{
		return ItemTags.bind("bloodmagic:gravels/" + name);
	}
}
