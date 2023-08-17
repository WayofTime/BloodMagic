package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileDungeonController;

public class BlockDungeonController extends Block implements EntityBlock
{
	public BlockDungeonController()
	{
		super(Properties.of().strength(20.0F, 50.0F));
//		.harvestTool(ToolType.PICKAXE).harvestLevel(1)
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileDungeonController(pos, state);
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
//		TileAltar altar = (TileAltar) world.getTileEntity(blockPos);
//		if (altar != null)
//			altar.dropItems();
		// TODO: Spawn particles?

		super.destroy(world, blockPos, blockState);
	}
}
