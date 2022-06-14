package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.tile.TileSoulForge;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockSoulForge extends Block// implements IBMBlock
{
	protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 12, 15);

	public BlockSoulForge()
	{
		super(Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
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
		return new TileSoulForge();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return ActionResultType.SUCCESS;

		TileEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileSoulForge))
			return ActionResultType.FAIL;

		NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return ActionResultType.SUCCESS;
	}

	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileSoulForge forge = (TileSoulForge) world.getBlockEntity(blockPos);
		if (forge != null)
			forge.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileSoulForge)
			{
				((TileSoulForge) tileentity).dropItems();
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
