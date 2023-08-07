package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import wayoftime.bloodmagic.util.Utils;

public class BlockAlchemyArray extends Block implements EntityBlock
{
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 1, 15);

	public BlockAlchemyArray()
	{
		super(Properties.of().strength(1.0F, 0).noCollission().ignitedByLava());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileAlchemyArray(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileAlchemyArray)
			{
				((TileAlchemyArray) tile).tick();
			}
		};
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
	{
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemyArray)
		{
			((TileAlchemyArray) tile).onEntityCollidedWithBlock(state, entity);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		TileAlchemyArray array = (TileAlchemyArray) world.getBlockEntity(pos);

		if (array == null || player.isShiftKeyDown())
			return InteractionResult.FAIL;

		ItemStack playerItem = player.getItemInHand(hand);

		if (!playerItem.isEmpty())
		{
			if (array.getItem(0).isEmpty())
			{
				Utils.insertItemToTile(array, player, 0);
				world.sendBlockUpdated(pos, state, state, 3);
			} else if (!array.getItem(0).isEmpty())
			{
				Utils.insertItemToTile(array, player, 1);
				array.attemptCraft();
				world.sendBlockUpdated(pos, state, state, 3);
			} else
			{
				return InteractionResult.SUCCESS;
			}
		}

		world.sendBlockUpdated(pos, state, state, 3);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getBlockEntity(blockPos);
		if (alchemyArray != null)
			alchemyArray.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileAlchemyArray)
			{
				((TileAlchemyArray) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

}
