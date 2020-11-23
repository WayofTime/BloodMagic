package wayoftime.bloodmagic.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class GeneratorFluidTags extends FluidTagsProvider
{
	public GeneratorFluidTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, BloodMagic.MODID, existingFileHelper);
	}

	@Override
	public void registerTags()
	{
		this.getOrCreateBuilder(BloodMagicTags.LIFE_ESSENCE).add(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get(), BloodMagicBlocks.LIFE_ESSENCE_FLUID_FLOWING.get());
	}
}
