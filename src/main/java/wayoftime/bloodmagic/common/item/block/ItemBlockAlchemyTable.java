package wayoftime.bloodmagic.common.item.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.block.BlockAlchemyTable;
import wayoftime.bloodmagic.tile.TileAlchemyTable;

import net.minecraft.item.Item.Properties;

public class ItemBlockAlchemyTable extends BlockItem
{
	public ItemBlockAlchemyTable(Block block, Properties properties)
	{
		super(block, properties);
	}

	@Override
	public ActionResultType place(BlockItemUseContext context)
	{
//		PlayerEntity player = context.getPlayer()
//		float yaw = player.rotationYaw;
		Direction direction = context.getHorizontalDirection();
		PlayerEntity player = context.getPlayer();

		if (direction.getStepY() != 0)
		{
			return ActionResultType.FAIL;
		}

		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (!world.isEmptyBlock(pos.relative(direction)))
		{
			return ActionResultType.FAIL;
		}

		BlockState thisState = this.getBlock().defaultBlockState().setValue(BlockAlchemyTable.DIRECTION, direction).setValue(BlockAlchemyTable.INVISIBLE, false);
		BlockState newState = this.getBlock().defaultBlockState().setValue(BlockAlchemyTable.DIRECTION, direction).setValue(BlockAlchemyTable.INVISIBLE, true);

		if (!this.canPlace(context, thisState) || !world.setBlock(pos.relative(direction), newState, 3))
		{
			return ActionResultType.FAIL;
		}

		if (!world.setBlock(pos, thisState, 3))
		{
			return ActionResultType.FAIL;
		}

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.getBlock())
		{
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).setInitialTableParameters(direction, false, pos.relative(direction));
			}

			TileEntity slaveTile = world.getBlockEntity(pos.relative(direction));
			if (slaveTile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) slaveTile).setInitialTableParameters(direction, true, pos);
			}

			updateCustomBlockEntityTag(world, context.getPlayer(), pos, context.getItemInHand());
			this.getBlock().setPlacedBy(world, pos, state, context.getPlayer(), context.getItemInHand());
			if (player instanceof ServerPlayerEntity)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItemInHand());
			}
		}

		SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
		world.playSound(player, pos, this.getPlaceSound(state, world, pos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume()
				+ 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		if (player == null || !player.abilities.instabuild)
		{
			context.getItemInHand().shrink(1);
		}

		return ActionResultType.sidedSuccess(world.isClientSide);

//		return ActionResultType.SUCCESS;
	}
}