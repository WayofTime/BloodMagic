package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDungeonController;

public class BlockDungeonController extends Block
{
	public BlockDungeonController()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(20.0F, 50.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileDungeonController();
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
//		TileAltar altar = (TileAltar) world.getTileEntity(blockPos);
//		if (altar != null)
//			altar.dropItems();
		// TODO: Spawn particles?

		super.onPlayerDestroy(world, blockPos, blockState);
	}
}
