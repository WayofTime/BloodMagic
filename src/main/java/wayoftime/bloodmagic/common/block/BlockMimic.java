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

public class BlockMimic extends Block
{
	private static final VoxelShape SHAPE = VoxelShapes.create(0.01, 0, 0.01, 0.99, 1, 0.99);

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
		TileEntity te = reader.getTileEntity(pos);
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
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace)
	{
		TileMimic mimic = (TileMimic) world.getTileEntity(pos);

		return (mimic != null && mimic.onBlockActivated(world, pos, state, player, hand, player.getHeldItem(hand), trace.getFace()))
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
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileMimic altar = (TileMimic) world.getTileEntity(blockPos);
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
			if (tileentity instanceof TileMimic)
			{
				((TileMimic) tileentity).dropItems();
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}