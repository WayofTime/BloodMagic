package wayoftime.bloodmagic.common.block;

import java.util.EnumMap;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemDemonCrystal;
import wayoftime.bloodmagic.tile.TileDemonCrystal;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class BlockDemonCrystal extends Block
{
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
//	public static final EnumProperty<EnumDemonWillType> TYPE = EnumProperty.create("type", EnumDemonWillType.class);
	public static final EnumProperty<Direction> ATTACHED = EnumProperty.create("attached", Direction.class);
	private static final EnumMap<Direction, VoxelShape> bounds = new EnumMap<>(Direction.class);

	public final EnumDemonWillType type;

	// Bounding / Collision boxes
	private static final VoxelShape[] UP = { Block.makeCuboidShape(6, 0, 5, 10, 13, 9),
			Block.makeCuboidShape(7, 0, 0, 13, 6, 5), Block.makeCuboidShape(9, 0, 9, 13, 5, 14),
			Block.makeCuboidShape(2, 0, 1, 7, 6, 7), Block.makeCuboidShape(5, 0, 9, 9, 7, 15),
			Block.makeCuboidShape(0, 0, 7, 6, 6, 10), Block.makeCuboidShape(10, 0, 6, 15, 6, 9) };
	private static final VoxelShape[] DOWN = { Block.makeCuboidShape(6, 3, 7, 10, 16, 11),
			Block.makeCuboidShape(7, 10, 11, 13, 16, 16), Block.makeCuboidShape(9, 11, 2, 13, 16, 7),
			Block.makeCuboidShape(2, 9, 11, 7, 16, 15), Block.makeCuboidShape(5, 9, 1, 9, 16, 7),
			Block.makeCuboidShape(0, 10, 6, 6, 16, 9), Block.makeCuboidShape(10, 11, 7, 15, 16, 10) };
	private static final VoxelShape[] NORTH = { Block.makeCuboidShape(6, 5, 3, 10, 9, 16),
			Block.makeCuboidShape(9, 0, 6, 13, 5, 16), Block.makeCuboidShape(8, 9, 11, 13, 14, 16),
			Block.makeCuboidShape(2, 1, 9, 7, 7, 16), Block.makeCuboidShape(5, 9, 9, 9, 15, 16),
			Block.makeCuboidShape(0, 7, 10, 6, 10, 16), Block.makeCuboidShape(10, 7, 10, 15, 9, 15), };
	private static final VoxelShape[] SOUTH = { Block.makeCuboidShape(6, 7, 0, 10, 11, 13),
			Block.makeCuboidShape(7, 11, 0, 13, 16, 6), Block.makeCuboidShape(8, 2, 9, 13, 7, 14),
			Block.makeCuboidShape(2, 9, 1, 7, 14, 7), Block.makeCuboidShape(5, 1, 9, 9, 7, 9),
			Block.makeCuboidShape(0, 6, 1, 6, 9, 7), Block.makeCuboidShape(10, 8, 1, 15, 10, 6) };
	private static final VoxelShape[] EAST = { Block.makeCuboidShape(0, 6, 5, 13, 10, 9),
			Block.makeCuboidShape(0, 3, 0, 6, 9, 5), Block.makeCuboidShape(0, 3, 9, 5, 8, 14),
			Block.makeCuboidShape(1, 9, 1, 7, 13, 7), Block.makeCuboidShape(1, 0, 9, 7, 11, 15),
			Block.makeCuboidShape(0, 10, 7, 6, 16, 10), Block.makeCuboidShape(0, 1, 6, 5, 6, 9) };
	private static final VoxelShape[] WEST = { Block.makeCuboidShape(3, 6, 5, 16, 10, 9),
			Block.makeCuboidShape(9, 7, 0, 16, 12, 5), Block.makeCuboidShape(11, 4, 9, 16, 13, 14),
			Block.makeCuboidShape(9, 3, 1, 16, 8, 7), Block.makeCuboidShape(9, 6, 9, 16, 8, 15),
			Block.makeCuboidShape(10, 1, 7, 16, 6, 10), Block.makeCuboidShape(10, 6, 6, 15, 15, 9) };

	public BlockDemonCrystal(EnumDemonWillType type)
	{
		super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2));
		this.type = type;

		this.setDefaultState(this.stateContainer.getBaseState().with(ATTACHED, Direction.UP).with(AGE, Integer.valueOf(0)));
//		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumDemonWillType.DEFAULT).withProperty(ATTACHED, Direction.UP));

//		setTranslationKey(BloodMagic.MODID + ".demonCrystal.");
//		setCreativeTab(BloodMagic.TAB_BM);
//		setHardness(2.0F);
//		setResistance(5.0F);
//		setHarvestLevel("pickaxe", 2);
	}

	public static ItemStack getItemStackDropped(EnumDemonWillType type, int crystalNumber)
	{
		ItemStack stack = ItemStack.EMPTY;
		switch (type)
		{
		case CORROSIVE:
			stack = new ItemStack(BloodMagicItems.CORROSIVE_CRYSTAL.get());
			break;
		case DEFAULT:
			stack = new ItemStack(BloodMagicItems.RAW_CRYSTAL.get());
			break;
		case DESTRUCTIVE:
			stack = new ItemStack(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
			break;
		case STEADFAST:
			stack = new ItemStack(BloodMagicItems.STEADFAST_CRYSTAL.get());
			break;
		case VENGEFUL:
			stack = new ItemStack(BloodMagicItems.VENGEFUL_CRYSTAL.get());
			break;
		}

		stack.setCount(crystalNumber);
		return stack;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		switch (state.get(ATTACHED))
		{
		case DOWN:
			return DOWN[0];
		case NORTH:
			return NORTH[0];
		case SOUTH:
			return SOUTH[0];
		case EAST:
			return EAST[0];
		case WEST:
			return WEST[0];
		case UP:
		default:
			return UP[0];
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileDemonCrystal)
			{
				TileDemonCrystal crystal = (TileDemonCrystal) tile;
				boolean isCreative = player.isCreative();
				boolean holdsCrystal = player.getHeldItem(hand).getItem() instanceof ItemDemonCrystal;

				if (PlayerDemonWillHandler.getTotalDemonWill(PlayerDemonWillHandler.getLargestWillType(player), player) > 1024 && !(holdsCrystal && isCreative))
				{
					crystal.dropSingleCrystal();

				}
				if (!crystal.getWorld().isRemote && isCreative && holdsCrystal)
				{
					if (crystal.getCrystalCount() < 7)
					{
						crystal.internalCounter = 0;
						if (crystal.progressToNextCrystal > 0)
							crystal.progressToNextCrystal--;
						crystal.setCrystalCount(crystal.getCrystalCount() + 1);
						crystal.markDirty();
						crystal.notifyUpdate();
					}
				}
			}
		}

		return ActionResultType.SUCCESS;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		Direction direction = state.get(ATTACHED);
		BlockPos blockpos = pos.offset(direction.getOpposite());
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return blockstate.isSolidSide(worldIn, blockpos, direction);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState blockstate = this.getDefaultState();
		IWorldReader iworldreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		Direction[] adirection = context.getNearestLookingDirections();

		for (Direction direction : adirection)
		{
//			if (direction.getAxis().isHorizontal())
			{
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.with(ATTACHED, direction1);
				if (blockstate.isValidPosition(iworldreader, blockpos))
				{
					return blockstate;
				}
			}
		}

		return null;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return facing.getOpposite() == stateIn.get(ATTACHED) && !stateIn.isValidPosition(worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: stateIn;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ATTACHED, AGE);
	}

//	@Override
//	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
//	{
//
//		super.onPlayerDestroy(world, blockPos, blockState);
//	}
//
//	@Override
//	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//	{
//		TorchBlock d;
//		if (!state.isIn(newState.getBlock()))
//		{
//
//			super.onReplaced(state, worldIn, pos, newState, isMoving);
//		}
//	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileDemonCrystal(type);
	}
}
