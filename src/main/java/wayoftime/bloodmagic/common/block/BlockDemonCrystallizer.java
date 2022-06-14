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
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDemonCrystallizer;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockDemonCrystallizer extends Block
{
	protected static final VoxelShape BODY = Block.box(2, 2, 2, 14, 16, 14);

	public BlockDemonCrystallizer()
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
		return new TileDemonCrystallizer();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
}
