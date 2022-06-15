package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.tile.TileInversionPillar;

public class BlockInversionPillar extends Block implements EntityBlock
{
	protected static final VoxelShape BODY = Block.box(2, 1, 2, 14, 15, 14);

	public BlockInversionPillar(Properties properties)
	{
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileInversionPillar(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		BlockEntity tile = world.getBlockEntity(pos);
		if (tile != null)
		{
			((TileInversionPillar) tile).handlePlayerInteraction(player);
		}
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return InteractionResult.FAIL;
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
