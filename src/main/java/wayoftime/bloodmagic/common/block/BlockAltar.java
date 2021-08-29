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
import wayoftime.bloodmagic.api.compat.IAltarReader;
import wayoftime.bloodmagic.tile.TileAltar;
import wayoftime.bloodmagic.util.Utils;

public class BlockAltar extends Block
{
	protected static final VoxelShape BODY = Block.makeCuboidShape(0, 0, 0, 16, 12, 16);

	public BlockAltar()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
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
		return new TileAltar();
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
	{
		TileAltar altar = (TileAltar) world.getTileEntity(pos);
		return altar.getAnalogSignalStrength();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileAltar altar = (TileAltar) world.getTileEntity(pos);

		if (altar == null || player.isSneaking())
			return ActionResultType.FAIL;

		ItemStack playerItem = player.getHeldItem(hand);

		if (playerItem.getItem() instanceof IAltarReader)// || playerItem.getItem() instanceof IAltarManipulator)
		{
			playerItem.getItem().onItemRightClick(world, player, hand);
			return ActionResultType.SUCCESS;
		}

		if (Utils.insertItemToTile(altar, player))
			altar.startCycle();
		else
			altar.setActive();

		world.notifyBlockUpdate(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileAltar altar = (TileAltar) world.getTileEntity(blockPos);
		if (altar != null)
			altar.dropItems();

		super.onPlayerDestroy(world, blockPos, blockState);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileAltar)
			{
				((TileAltar) tileentity).dropItems();
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
