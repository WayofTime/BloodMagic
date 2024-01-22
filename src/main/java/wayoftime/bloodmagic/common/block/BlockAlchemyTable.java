package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tile.TileAlchemyTable;

public class BlockAlchemyTable extends Block implements EntityBlock// implements IBMBlock
{
	public static final DirectionProperty DIRECTION = DirectionProperty.create("direction", Direction.Plane.HORIZONTAL);
	public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 15, 15);

	public BlockAlchemyTable()
	{
		super(BlockBehaviour.Properties.of().strength(2.0F, 5.0F).noOcclusion().isRedstoneConductor(BlockAlchemyTable::isntSolid).isViewBlocking(BlockAlchemyTable::isntSolid).requiresCorrectToolForDrops());
//		.harvestTool(ToolType.PICKAXE).harvestLevel(1)
	}

	private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		return Shapes.empty();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileAlchemyTable(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		if (state.getValue(INVISIBLE))
		{
			return null;
		}

		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).tick();
			}
		};
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemyTable)
		{
			if (((TileAlchemyTable) tile).isSlave())
			{
				NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) world.getBlockEntity(((TileAlchemyTable) tile).getConnectedPos()), ((TileAlchemyTable) tile).getConnectedPos());
			} else
			{
				NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tile, pos);
			}

			return InteractionResult.SUCCESS;
		}

//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return InteractionResult.FAIL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(DIRECTION, INVISIBLE);
	}

	@Override
	public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor)
	{
		TileAlchemyTable tile = (TileAlchemyTable) world.getBlockEntity(pos);
		if (tile != null)
		{
			BlockPos connectedPos = tile.getConnectedPos();
			BlockEntity connectedTile = world.getBlockEntity(connectedPos);
			if (!(connectedTile instanceof TileAlchemyTable && ((TileAlchemyTable) connectedTile).getConnectedPos().equals(pos)))
			{
				tile.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemyTable forge = (TileAlchemyTable) world.getBlockEntity(blockPos);

		if (forge != null && !forge.isSlave())
		{
			forge.dropItems();
		}

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileAlchemyTable && !((TileAlchemyTable) tileentity).isSlave())
			{
				((TileAlchemyTable) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

//	@Override
//	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//	{
//		if (!state.isIn(newState.getBlock()))
//		{
//			TileEntity tileentity = worldIn.getTileEntity(pos);
//			if (tileentity instanceof TileSoulForge)
//			{
//				((TileSoulForge) tileentity).dropItems();
//				worldIn.updateComparatorOutputLevel(pos, this);
//			}
//
//			super.onReplaced(state, worldIn, pos, newState, isMoving);
//		}
//	}

	@Override
	public Item asItem()
	{
		return BloodMagicItems.ALCHEMY_TABLE_ITEM.get();
	}
}