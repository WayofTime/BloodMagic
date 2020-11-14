package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

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
