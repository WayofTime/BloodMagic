package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.tile.TileMimic;

public class BlockMimic extends Block implements EntityBlock
{
	private static final VoxelShape SHAPE = Shapes.box(0.01, 0, 0.01, 0.99, 1, 0.99);

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
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		BlockEntity te = reader.getBlockEntity(pos);
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileMimic(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace)
	{
		TileMimic mimic = (TileMimic) world.getBlockEntity(pos);

		return (mimic != null && mimic.onBlockActivated(world, pos, state, player, hand, player.getItemInHand(hand), trace.getDirection()))
				? InteractionResult.SUCCESS
				: InteractionResult.FAIL;
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
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileMimic altar = (TileMimic) world.getBlockEntity(blockPos);
		if (altar != null)
			altar.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileMimic)
			{
				((TileMimic) tileentity).dropItems();
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}