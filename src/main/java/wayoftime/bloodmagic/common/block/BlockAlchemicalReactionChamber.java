package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockAlchemicalReactionChamber extends Block
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BlockAlchemicalReactionChamber()
	{
		super(Properties.of(Material.STONE).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.STONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileAlchemicalReactionChamber();
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemicalReactionChamber arc = (TileAlchemicalReactionChamber) world.getBlockEntity(blockPos);
		if (arc != null)
			arc.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileAlchemicalReactionChamber)
			{
				((TileAlchemicalReactionChamber) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileAlchemicalReactionChamber))
			return InteractionResult.FAIL;

		NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if (stack.hasCustomHoverName())
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof AbstractFurnaceBlockEntity)
			{
				((AbstractFurnaceBlockEntity) tileentity).setCustomName(stack.getHoverName());
			}
		}

	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param)
	{
		super.triggerEvent(state, worldIn, pos, id, param);
		BlockEntity tileentity = worldIn.getBlockEntity(pos);
		return tileentity == null ? false : tileentity.triggerEvent(id, param);
	}

}
