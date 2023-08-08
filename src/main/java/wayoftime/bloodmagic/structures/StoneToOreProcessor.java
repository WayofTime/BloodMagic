package wayoftime.bloodmagic.structures;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader p_230386_1_, BlockPos p_230386_2_, BlockPos p_230386_3_, StructureTemplate.StructureBlockInfo p_230386_4_, StructureTemplate.StructureBlockInfo p_230386_5_, StructurePlaceSettings p_230386_6_)
	{
		if (p_230386_5_.state().getBlock() != BloodMagicBlocks.DUNGEON_STONE.get())
		{
			return p_230386_5_;
		}
		RandomSource random = p_230386_6_.getRandom(p_230386_5_.pos());
		return !(this.integrity >= 1.0F) && !(random.nextFloat() >= this.integrity)
				? new StructureTemplate.StructureBlockInfo(p_230386_5_.pos(), BloodMagicBlocks.DUNGEON_ORE.get().defaultBlockState(), p_230386_5_.nbt())
				: p_230386_5_;
	}

	protected StructureProcessorType<?> getType()
	{
		return StructureProcessorType.BLOCK_ROT;
	}
}