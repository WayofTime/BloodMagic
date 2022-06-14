package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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
import wayoftime.bloodmagic.tile.TileAlchemyArray;
import wayoftime.bloodmagic.util.Utils;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockAlchemyArray extends Block
{
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 1, 15);

	public BlockAlchemyArray()
	{
		super(Properties.of(Material.WOOL).strength(1.0F, 0).noCollission());
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
		return new TileAlchemyArray();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
	{
		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemyArray)
		{
			((TileAlchemyArray) tile).onEntityCollidedWithBlock(state, entity);
		}
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileAlchemyArray array = (TileAlchemyArray) world.getBlockEntity(pos);

		if (array == null || player.isShiftKeyDown())
			return ActionResultType.FAIL;

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
				return ActionResultType.SUCCESS;
			}
		}

		world.sendBlockUpdated(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getBlockEntity(blockPos);
		if (alchemyArray != null)
			alchemyArray.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileAlchemyArray)
			{
				((TileAlchemyArray) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

}
