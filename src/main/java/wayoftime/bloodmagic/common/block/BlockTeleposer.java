package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.tile.TileTeleposer;

public class BlockTeleposer extends Block
{
	public BlockTeleposer()
	{
		super(BlockBehaviour.Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2));

//		this.setDefaultState(this.stateContainer.getBaseState().with(DOWN, false).with(UP, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileTeleposer();
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileTeleposer arc = (TileTeleposer) world.getBlockEntity(blockPos);
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
			if (tileentity instanceof TileTeleposer)
			{
				((TileTeleposer) tileentity).dropItems();
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
		if (!(tile instanceof TileTeleposer))
			return InteractionResult.FAIL;

		NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return InteractionResult.SUCCESS;
	}
}
