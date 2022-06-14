package wayoftime.bloodmagic.structures;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class StoneToOreProcessor extends StructureProcessor
{
	public static final Codec<StoneToOreProcessor> CODEC = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(StoneToOreProcessor::new, (p_237078_0_) -> {
		return p_237078_0_.integrity;
	}).codec();
	private final float integrity;

	public StoneToOreProcessor(float integrity)
	{
		this.integrity = integrity;
	}

	@Nullable
	public Template.BlockInfo processBlock(IWorldReader p_230386_1_, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings p_230386_6_)
	{
		if (p_230386_5_.state.getBlock() != BloodMagicBlocks.DUNGEON_STONE.get())
		{
			return p_230386_5_;
		}
		Random random = p_230386_6_.getRandom(p_230386_5_.pos);
		return !(this.integrity >= 1.0F) && !(random.nextFloat() >= this.integrity)
				? new Template.BlockInfo(p_230386_5_.pos, BloodMagicBlocks.DUNGEON_ORE.get().defaultBlockState(), p_230386_5_.nbt)
				: p_230386_5_;
	}

	protected IStructureProcessorType<?> getType()
	{
		return IStructureProcessorType.BLOCK_ROT;
	}
}