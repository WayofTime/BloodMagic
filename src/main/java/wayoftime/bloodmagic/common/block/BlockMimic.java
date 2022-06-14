package wayoftime.bloodmagic.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileMimic;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockMimic extends Block
{
	private static final VoxelShape SHAPE = VoxelShapes.box(0.01, 0, 0.01, 0.99, 1, 0.99);

	public BlockMimic(Properties prop)
	{
		super(prop);
	}

//	@Override
//	public void addInformation(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flags)
//	{
//		list.add(new TranslationTextComponent("message.fancyblock"));
//	}

//	@Override
//	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
//	{
//		TileEntity te = world.getTileEntity(pos);
//		if (te instanceof TileMimic)
//		{
//			BlockState mimic = ((TileMimic) te).getMimic();
//			if (mimic != null && !(mimic.getBlock() instanceof BlockMimic))
//			{
//				return mimic.getLightValue(world, pos);
//			}
//		}
//		return super.getLightValue(state, world, pos);
//	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
	{
		TileEntity te = reader.getBlockEntity(pos);
		if (te instanceof TileMimic)
		{
			BlockState mimic = ((TileMimic) te).getMimic();
			if (mimic != null && !(mimic.getBlock() instanceof BlockMimic))
			{
				return mimic.getShape(reader, pos, context);
			}
		}
		return SHAPE;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileMimic();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace)
	{
		TileMimic mimic = (TileMimic) world.getBlockEntity(pos);

		return (mimic != null && mimic.onBlockActivated(world, pos, state, player, hand, player.getItemInHand(hand), trace.getDirection()))
				? ActionResultType.SUCCESS
				: ActionResultType.FAIL;
//		ItemStack item = player.getHeldItem(hand);
//		if (!item.isEmpty() && item.getItem() instanceof BlockItem)
//		{
//			if (!world.isRemote)
//			{
//				TileEntity te = world.getTileEntity(pos);
//				if (te instanceof TileMimic)
//				{
//					BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
//					((TileMimic) te).setMimic(mimicState);
//				}
//			}
//			return ActionResultType.SUCCESS;
//		}
//		return super.onBlockActivated(state, world, pos, player, hand, trace);
	}

//	public boolean canMimicBlock(World world, BlockPos pos, BlockState state)
//	{
//		return state.getBlock()
//	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileMimic altar = (TileMimic) world.getBlockEntity(blockPos);
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
			if (tileentity instanceof TileMimic)
			{
				((TileMimic) tileentity).dropItems();
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}