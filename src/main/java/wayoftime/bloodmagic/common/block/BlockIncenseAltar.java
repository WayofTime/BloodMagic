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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileIncenseAltar;
import wayoftime.bloodmagic.tile.TileSoulForge;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockIncenseAltar extends Block
{
	protected static final VoxelShape BODY = Block.box(5, 0, 5, 12, 16, 12);

	public BlockIncenseAltar()
	{
		super(Properties.of(Material.METAL).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileSoulForge forge = (TileSoulForge) world.getBlockEntity(blockPos);
		if (forge != null)
			forge.dropItems();

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
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
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileIncenseAltar();
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

}