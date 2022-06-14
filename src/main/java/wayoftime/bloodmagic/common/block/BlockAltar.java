package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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

import net.minecraft.block.AbstractBlock.Properties;

public class BlockAltar extends Block
{
	protected static final VoxelShape BODY = Block.box(0, 0, 0, 16, 12, 16);
	public boolean isRedstoneActive = false;

	public BlockAltar()
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
		return new TileAltar();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos)
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
	public int getSignal(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction dir)
	{
		boolean isOutputOn = false;
		TileEntity tileentity = blockReader.getBlockEntity(pos);
		if (tileentity instanceof TileAltar)
		{
			TileAltar altar = (TileAltar) tileentity;
			isOutputOn = altar.getOutputState();
		}

		final int OUTPUT_POWER_WHEN_ON = 15;
		return isOutputOn ? OUTPUT_POWER_WHEN_ON : 0;
	}

	@Override
	public int getDirectSignal(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction dir)
	{
		return 0;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileAltar altar = (TileAltar) world.getBlockEntity(pos);

		if (altar == null || player.isShiftKeyDown())
			return ActionResultType.FAIL;

		ItemStack playerItem = player.getItemInHand(hand);

		if (playerItem.getItem() instanceof IAltarReader)// || playerItem.getItem() instanceof IAltarManipulator)
		{
			playerItem.getItem().use(world, player, hand);
			return ActionResultType.SUCCESS;
		}

		if (Utils.insertItemToTile(altar, player))
			altar.startCycle();
		else
			altar.setActive();

		world.sendBlockUpdated(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileAltar altar = (TileAltar) world.getBlockEntity(blockPos);
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
			if (tileentity instanceof TileAltar)
			{
				((TileAltar) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
