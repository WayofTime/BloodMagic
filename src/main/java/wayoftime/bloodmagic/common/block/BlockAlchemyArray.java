package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
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
import wayoftime.bloodmagic.tile.TileAlchemyArray;
import wayoftime.bloodmagic.util.Utils;

public class BlockAlchemyArray extends Block
{
	protected static final VoxelShape BODY = Block.makeCuboidShape(1, 0, 1, 15, 1, 15);

	public BlockAlchemyArray()
	{
		super(Properties.create(Material.WOOL).hardnessAndResistance(1.0F, 0).doesNotBlockMovement());
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
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileAlchemyArray array = (TileAlchemyArray) world.getTileEntity(pos);

		if (array == null || player.isSneaking())
			return ActionResultType.FAIL;

		ItemStack playerItem = player.getHeldItem(hand);

		if (!playerItem.isEmpty())
		{
			if (array.getStackInSlot(0).isEmpty())
			{
				Utils.insertItemToTile(array, player, 0);
				world.notifyBlockUpdate(pos, state, state, 3);
			} else if (!array.getStackInSlot(0).isEmpty())
			{
				Utils.insertItemToTile(array, player, 1);
				array.attemptCraft();
				world.notifyBlockUpdate(pos, state, state, 3);
			} else
			{
				return ActionResultType.SUCCESS;
			}
		}

		world.notifyBlockUpdate(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getTileEntity(blockPos);
		if (alchemyArray != null)
			alchemyArray.dropItems();

		super.onPlayerDestroy(world, blockPos, blockState);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileAlchemyArray)
			{
				((TileAlchemyArray) tileentity).dropItems();
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

}
