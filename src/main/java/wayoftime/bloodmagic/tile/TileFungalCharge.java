package wayoftime.bloodmagic.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class TileFungalCharge extends TileVeinMineCharge
{
	@ObjectHolder("bloodmagic:fungal_charge")
	public static BlockEntityType<TileFungalCharge> TYPE;

	public TileFungalCharge(BlockEntityType<?> type, int maxBlocks)
	{
		super(type, maxBlocks);
	}

	public TileFungalCharge()
	{
		this(TYPE, 64 * 3);
	}

	@Override
	public boolean isValidBlock(BlockState originalBlockState, BlockState testState)
	{
		return isValidStartingBlock(testState);
	}

	@Override
	public boolean isValidStartingBlock(BlockState originalBlockState)
	{
		return BloodMagicTags.Blocks.MUSHROOM_HYPHAE.contains(originalBlockState.getBlock()) || BloodMagicTags.Blocks.MUSHROOM_STEM.contains(originalBlockState.getBlock());
	}

	@Override
	public boolean checkDiagonals()
	{
		return true;
	}
}
