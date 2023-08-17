package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.api.compat.IAltarReader;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.util.Utils;

public class BlockAltar extends Block implements EntityBlock
{
	protected static final VoxelShape BODY = Block.box(0, 0, 0, 16, 12, 16);
	public boolean isRedstoneActive = false;

	public BlockAltar()
	{
		super(Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileAltar(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return TileAltar::tick;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos)
	{
		this.isRedstoneActive = false;
		TileAltar altar = (TileAltar) world.getBlockEntity(pos);
		Block blockdown = world.getBlockState(pos.below()).getBlock();
		int redstoneMode = 0;

		if (blockdown instanceof BloodstoneBlock)
			redstoneMode = 1;

		if (blockdown instanceof RedstoneLampBlock)
		{
			redstoneMode = 2;
			this.isRedstoneActive = true;
		}

		return altar.getAnalogSignalStrength(redstoneMode);
	}

	@Override
	public boolean isSignalSource(BlockState iBlockState)
	{
		return true;
	}

	@Override
	public int getSignal(BlockState blockState, BlockGetter blockReader, BlockPos pos, Direction dir)
	{
		boolean isOutputOn = false;
		BlockEntity tileentity = blockReader.getBlockEntity(pos);
		if (tileentity instanceof TileAltar)
		{
			TileAltar altar = (TileAltar) tileentity;
			isOutputOn = altar.getOutputState();
		}

		final int OUTPUT_POWER_WHEN_ON = 15;
		return isOutputOn ? OUTPUT_POWER_WHEN_ON : 0;
	}

	@Override
	public int getDirectSignal(BlockState blockState, BlockGetter blockReader, BlockPos pos, Direction dir)
	{
		return 0;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		TileAltar altar = (TileAltar) world.getBlockEntity(pos);

		if (altar == null || player.isShiftKeyDown())
			return InteractionResult.FAIL;

		ItemStack playerItem = player.getItemInHand(hand);

		if (playerItem.getItem() instanceof IAltarReader)// || playerItem.getItem() instanceof IAltarManipulator)
		{
			playerItem.getItem().use(world, player, hand);
			return InteractionResult.SUCCESS;
		}

		if (Utils.insertItemToTile(altar, player))
			altar.startCycle();
		else
			altar.setActive();

		world.sendBlockUpdated(pos, state, state, 3);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileAltar altar = (TileAltar) world.getBlockEntity(blockPos);
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
			if (tileentity instanceof TileAltar)
			{
				((TileAltar) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
