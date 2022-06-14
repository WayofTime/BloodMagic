package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileIncenseAltar;
import wayoftime.bloodmagic.tile.TileSoulForge;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockIncenseAltar extends Block
{
	protected static final VoxelShape BODY = Block.box(5, 0, 5, 12, 16, 12);

	public BlockIncenseAltar()
	{
		super(Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BODY;
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

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileIncenseAltar();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

}