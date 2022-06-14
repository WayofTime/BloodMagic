package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.tile.TileAlchemyTable;

public class BlockAlchemyTable extends Block// implements IBMBlock
{
	public static final DirectionProperty DIRECTION = DirectionProperty.create("direction", Direction.Plane.HORIZONTAL);
	public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 15, 15);

	public BlockAlchemyTable()
	{
		super(AbstractBlock.Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).noOcclusion().isRedstoneConductor(BlockAlchemyTable::isntSolid).isViewBlocking(BlockAlchemyTable::isntSolid));
	}

	private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BODY;
	}

	public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
	{
		return VoxelShapes.empty();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileAlchemyTable();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return ActionResultType.SUCCESS;

		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemyTable)
		{
			if (((TileAlchemyTable) tile).isSlave())
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) world.getBlockEntity(((TileAlchemyTable) tile).getConnectedPos()), ((TileAlchemyTable) tile).getConnectedPos());
			} else
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
			}

			return ActionResultType.SUCCESS;
		}

//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return ActionResultType.FAIL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(DIRECTION, INVISIBLE);
	}

	@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
	{
		TileAlchemyTable tile = (TileAlchemyTable) world.getBlockEntity(pos);
		if (tile != null)
		{
			BlockPos connectedPos = tile.getConnectedPos();
			TileEntity connectedTile = world.getBlockEntity(connectedPos);
			if (!(connectedTile instanceof TileAlchemyTable
					&& ((TileAlchemyTable) connectedTile).getConnectedPos().equals(pos)))
			{
				this.destroy(tile.getLevel(), pos, state);
				this.removedByPlayer(state, tile.getLevel(), pos, null, true, this.getFluidState(state));
			}
		}
	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemyTable forge = (TileAlchemyTable) world.getBlockEntity(blockPos);

		if (forge != null && !forge.isSlave())
		{
			forge.dropItems();
		}

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
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