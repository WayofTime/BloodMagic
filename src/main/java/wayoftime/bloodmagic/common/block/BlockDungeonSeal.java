package wayoftime.bloodmagic.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import wayoftime.bloodmagic.common.item.dungeon.IDungeonKey;
import wayoftime.bloodmagic.common.tile.TileDungeonSeal;
import wayoftime.bloodmagic.util.ChatUtil;

import java.util.List;

public class BlockDungeonSeal extends Block implements EntityBlock
{
	public BlockDungeonSeal()
	{
		super(Properties.of().strength(20.0F, 50.0F));
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

		int result = seal.requestRoomFromController(player, playerItem);
		if (result == -1 && !playerItem.isEmpty() && playerItem.getItem() instanceof IDungeonKey)
		{
			// Key didn't work
			List<Component> toSend = Lists.newArrayList();
			toSend.add(Component.translatable("tooltip.bloodmagic.incorrectKey"));
			ChatUtil.sendNoSpam(player, toSend.toArray(new Component[toSend.size()]));
			world.playSound((Player) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
		}

		return InteractionResult.SUCCESS;
	}
}
