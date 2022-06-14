package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDungeonController;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDungeonController extends Block
{
	public BlockDungeonController()
	{
		super(Properties.of(Material.STONE).strength(20.0F, 50.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileDungeonController();
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
//		TileAltar altar = (TileAltar) world.getTileEntity(blockPos);
//		if (altar != null)
//			altar.dropItems();
		// TODO: Spawn particles?

		super.destroy(world, blockPos, blockState);
	}
}
