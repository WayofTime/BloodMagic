package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import wayoftime.bloodmagic.common.tile.TileDungeonSeal;

public class BlockDungeonSeal extends Block implements EntityBlock
{
	public BlockDungeonSeal()
	{
		super(Properties.of(Material.STONE).strength(20.0F, 50.0F));
//		.harvestTool(ToolType.PICKAXE).harvestLevel(1)
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileDungeonSeal(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		TileDungeonSeal seal = (TileDungeonSeal) world.getBlockEntity(pos);

		if (seal == null || player.isShiftKeyDown())
			return InteractionResult.FAIL;

//
		ItemStack playerItem = player.getItemInHand(hand);

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
		return InteractionResult.SUCCESS;
	}
}
