package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileInversionPillar;

public class BlockInversionPillar extends Block
{
	protected static final VoxelShape BODY = Block.makeCuboidShape(2, 0.001, 2, 14, 15.999, 14);

	public BlockInversionPillar(Properties properties)
	{
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BODY;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileInversionPillar();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (world.isRemote)
			return ActionResultType.SUCCESS;

		TileEntity tile = world.getTileEntity(pos);
		if (tile != null)
		{
			((TileInversionPillar) tile).handlePlayerInteraction(player);
		}
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return ActionResultType.FAIL;
	}

//	@Override
//	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
//	{
//		TileInversionPillar pillar = (TileInversionPillar) world.getTileEntity(blockPos);
////		if (altar != null)
////			altar.dropItems();
//
//		super.onPlayerDestroy(world, blockPos, blockState);
//	}
//
//	@Override
//	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//	{
//		if (!state.isIn(newState.getBlock()))
//		{
//			TileEntity tileentity = worldIn.getTileEntity(pos);
//			if (tileentity instanceof TileAltar)
//			{
//				((TileAltar) tileentity).dropItems();
//				worldIn.updateComparatorOutputLevel(pos, this);
//			}
//
//			super.onReplaced(state, worldIn, pos, newState, isMoving);
//		}
//	}
}
