package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class GeneratorItemTags extends ItemTagsProvider
{
	public GeneratorItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagProvider, BloodMagic.MODID, existingFileHelper);
	}

	@Override
	public void registerTags()
	{
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).add(Items.DIAMOND);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
//		this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

	}

	/**
	 * Resolves a Path for the location to save the given tag.
	 */
	@Override
	protected Path makePath(ResourceLocation id)
	{
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName()
	{
		return "Item Tags";
	}
}
