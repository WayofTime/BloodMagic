package wayoftime.bloodmagic.tile;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class TileFungalCharge extends TileVeinMineCharge
{
	@ObjectHolder("bloodmagic:fungal_charge")
	public static TileEntityType<TileFungalCharge> TYPE;

	public TileFungalCharge(TileEntityType<?> type, int maxBlocks)
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
