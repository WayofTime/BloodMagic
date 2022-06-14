package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDemonCrucible;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;
import wayoftime.bloodmagic.api.compat.IDiscreteDemonWill;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockDemonCrucible extends Block
{
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 12, 15);

	public BlockDemonCrucible()
	{
		super(Properties.of(Material.STONE).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
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
		return new TileDemonCrucible();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileDemonCrucible crucible = (TileDemonCrucible) world.getBlockEntity(pos);

		if (crucible == null || player.isShiftKeyDown())
			return ActionResultType.FAIL;

		ItemStack playerItem = player.getItemInHand(hand);

		if (!playerItem.isEmpty())
		{
			if (!(playerItem.getItem() instanceof IDiscreteDemonWill)
					&& !(playerItem.getItem() instanceof IDemonWillGem))
			{
				return ActionResultType.SUCCESS;
			}
		}

		Utils.insertItemToTile(crucible, player);

		world.sendBlockUpdated(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileDemonCrucible altar = (TileDemonCrucible) world.getBlockEntity(blockPos);
		if (altar != null)
			altar.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileDemonCrucible)
			{
				((TileDemonCrucible) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
