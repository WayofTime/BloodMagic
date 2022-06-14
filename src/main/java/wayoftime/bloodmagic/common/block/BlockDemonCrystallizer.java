package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDemonCrystallizer;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDemonCrystallizer extends Block
{
	protected static final VoxelShape BODY = Block.box(2, 2, 2, 14, 16, 14);

	public BlockDemonCrystallizer()
	{
		super(Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileDemonCrystallizer();
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
}
