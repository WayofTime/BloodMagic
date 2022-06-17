package wayoftime.bloodmagic.common.item.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.block.BlockAlchemyTable;
import wayoftime.bloodmagic.common.tile.TileAlchemyTable;

public class ItemBlockAlchemyTable extends BlockItem
{
	public ItemBlockAlchemyTable(Block block, Properties properties)
	{
		super(block, properties);
	}

	@Override
	public InteractionResult place(BlockPlaceContext context)
	{
//		PlayerEntity player = context.getPlayer()
//		float yaw = player.rotationYaw;
		Direction direction = context.getHorizontalDirection();
		Player player = context.getPlayer();

		if (direction.getStepY() != 0)
		{
			return InteractionResult.FAIL;
		}

		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (!world.isEmptyBlock(pos.relative(direction)))
		{
			return InteractionResult.FAIL;
		}

		BlockState thisState = this.getBlock().defaultBlockState().setValue(BlockAlchemyTable.DIRECTION, direction).setValue(BlockAlchemyTable.INVISIBLE, false);
		BlockState newState = this.getBlock().defaultBlockState().setValue(BlockAlchemyTable.DIRECTION, direction).setValue(BlockAlchemyTable.INVISIBLE, true);

		if (!this.canPlace(context, thisState) || !world.setBlock(pos.relative(direction), newState, 3))
		{
			return InteractionResult.FAIL;
		}

		if (!world.setBlock(pos, thisState, 3))
		{
			return InteractionResult.FAIL;
		}

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.getBlock())
		{
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).setInitialTableParameters(direction, false, pos.relative(direction));
			}

			BlockEntity slaveTile = world.getBlockEntity(pos.relative(direction));
			if (slaveTile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) slaveTile).setInitialTableParameters(direction, true, pos);
			}

			updateCustomBlockEntityTag(world, context.getPlayer(), pos, context.getItemInHand());
			this.getBlock().setPlacedBy(world, pos, state, context.getPlayer(), context.getItemInHand());
			if (player instanceof ServerPlayer)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, context.getItemInHand());
			}
		}

		SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
		world.playSound(player, pos, this.getPlaceSound(state, world, pos, context.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		if (player == null || !player.getAbilities().instabuild)
		{
			context.getItemInHand().shrink(1);
		}

		return InteractionResult.sidedSuccess(world.isClientSide);

//		return ActionResultType.SUCCESS;
	}
}