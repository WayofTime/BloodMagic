package wayoftime.bloodmagic.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

import java.util.concurrent.CompletableFuture;

public class GeneratorFluidTags extends IntrinsicHolderTagsProvider<Fluid>
{
	public GeneratorFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper)
	{
		super(output, Registries.FLUID, future, block -> block.builtInRegistryHolder().key(), BloodMagic.MODID, helper);
	}

	@Override
	public void addTags(HolderLookup.Provider provider)
	{
		this.tag(BloodMagicTags.LIFE_ESSENCE).add(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), BloodMagicFluids.LIFE_ESSENCE_FLUID_FLOWING.get());
	}
}
