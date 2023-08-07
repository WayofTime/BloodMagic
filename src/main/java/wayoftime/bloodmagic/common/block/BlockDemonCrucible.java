package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;
import wayoftime.bloodmagic.api.compat.IDiscreteDemonWill;
import wayoftime.bloodmagic.common.tile.TileDemonCrucible;
import wayoftime.bloodmagic.util.Utils;

public class BlockDemonCrucible extends Block implements EntityBlock
{
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 12, 15);

	public BlockDemonCrucible()
	{
		super(Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops());
//		.harvestTool(ToolType.PICKAXE).harvestLevel(1)
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileDemonCrucible(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileDemonCrucible)
			{
				((TileDemonCrucible) tile).tick();
			}
		};
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		TileDemonCrucible crucible = (TileDemonCrucible) world.getBlockEntity(pos);

		if (crucible == null || player.isShiftKeyDown())
			return InteractionResult.FAIL;

		ItemStack playerItem = player.getItemInHand(hand);

		if (!playerItem.isEmpty())
		{
			if (!(playerItem.getItem() instanceof IDiscreteDemonWill) && !(playerItem.getItem() instanceof IDemonWillGem))
			{
				return InteractionResult.SUCCESS;
			}
		}

		Utils.insertItemToTile(crucible, player);

		world.sendBlockUpdated(pos, state, state, 3);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileDemonCrucible altar = (TileDemonCrucible) world.getBlockEntity(blockPos);
		if (altar != null)
			altar.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileDemonCrucible)
			{
				((TileDemonCrucible) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
