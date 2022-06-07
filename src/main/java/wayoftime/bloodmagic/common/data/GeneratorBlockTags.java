package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class GeneratorBlockTags extends BlockTagsProvider
{
	public GeneratorBlockTags(DataGenerator generatorIn, ExistingFileHelper exFileHelper)
	{
		super(generatorIn, BloodMagic.MODID, exFileHelper);
	}

	@Override
	public void registerTags()
	{
		this.getOrCreateBuilder(BlockTags.WALLS).add(BloodMagicBlocks.DUNGEON_BRICK_WALL.get());
		this.getOrCreateBuilder(BlockTags.WALLS).add(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get());
		this.getOrCreateBuilder(BloodMagicTags.Blocks.MUSHROOM_STEM).add(Blocks.MUSHROOM_STEM).add(Blocks.CRIMSON_STEM).add(Blocks.WARPED_STEM);
		this.getOrCreateBuilder(BloodMagicTags.Blocks.MUSHROOM_HYPHAE).add(Blocks.BROWN_MUSHROOM_BLOCK).add(Blocks.RED_MUSHROOM_BLOCK).add(Blocks.CRIMSON_HYPHAE).add(Blocks.WARPED_HYPHAE).add(Blocks.STRIPPED_CRIMSON_HYPHAE).add(Blocks.STRIPPED_WARPED_HYPHAE).add(Blocks.NETHER_WART_BLOCK).add(Blocks.WARPED_WART_BLOCK).add(Blocks.SHROOMLIGHT);

		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_COPPER);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_LEAD);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_OSMIUM);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_SILVER);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_TIN);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_APATITE);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_CINNABAR);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_RUBY);
		getOrCreateBuilder(BloodMagicTags.BLOCK_ORE_SAPPHIRE);

		getOrCreateBuilder(BloodMagicTags.BLOCK_STONE_UNPOLISHED).add(Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE);

	}

	/**
	 * Resolves a Path for the location to save the given tag.
	 */
	@Override
	protected Path makePath(ResourceLocation id)
	{
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName()
	{
		return "Block Tags";
	}
}
