package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.tile.TileDungeonSeal;

public class BlockDungeonSeal extends Block
{
	public BlockDungeonSeal()
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
		return new TileDungeonSeal();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		TileDungeonSeal seal = (TileDungeonSeal) world.getTileEntity(pos);

		if (seal == null || player.isSneaking())
			return ActionResultType.FAIL;

//
		ItemStack playerItem = player.getHeldItem(hand);

		int result = seal.requestRoomFromController(playerItem);
//
//		if (playerItem.getItem() instanceof IAltarReader)// || playerItem.getItem() instanceof IAltarManipulator)
//		{
//			playerItem.getItem().onItemRightClick(world, player, hand);
//			return ActionResultType.SUCCESS;
//		}
//
//		if (Utils.insertItemToTile(altar, player))
//			altar.startCycle();
//		else
//			altar.setActive();
//
//		world.notifyBlockUpdate(pos, state, state, 3);
		return ActionResultType.SUCCESS;
	}
}
