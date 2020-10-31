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

public class ItemBlockAlchemyTable extends BlockItem
{
	public ItemBlockAlchemyTable(Block block, Properties properties)
	{
		super(block, properties);
	}

	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
//		PlayerEntity player = context.getPlayer()
//		float yaw = player.rotationYaw;
		Direction direction = context.getPlacementHorizontalFacing();
		PlayerEntity player = context.getPlayer();

		if (direction.getYOffset() != 0)
		{
			return ActionResultType.FAIL;
		}

		World world = context.getWorld();
		BlockPos pos = context.getPos();

		if (!world.isAirBlock(pos.offset(direction)))
		{
			return ActionResultType.FAIL;
		}

		BlockState thisState = this.getBlock().getDefaultState().with(BlockAlchemyTable.DIRECTION, direction).with(BlockAlchemyTable.INVISIBLE, false);
		BlockState newState = this.getBlock().getDefaultState().with(BlockAlchemyTable.DIRECTION, direction).with(BlockAlchemyTable.INVISIBLE, true);

		if (!this.canPlace(context, thisState) || !world.setBlockState(pos.offset(direction), newState, 3))
		{
			return ActionResultType.FAIL;
		}

		if (!world.setBlockState(pos, thisState, 3))
		{
			return ActionResultType.FAIL;
		}

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.getBlock())
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).setInitialTableParameters(direction, false, pos.offset(direction));
			}

			TileEntity slaveTile = world.getTileEntity(pos.offset(direction));
			if (slaveTile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) slaveTile).setInitialTableParameters(direction, true, pos);
			}

			setTileEntityNBT(world, context.getPlayer(), pos, context.getItem());
			this.getBlock().onBlockPlacedBy(world, pos, state, context.getPlayer(), context.getItem());
			if (player instanceof ServerPlayerEntity)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
			}
		}

		SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
		world.playSound(player, pos, this.getPlaceSound(state, world, pos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume()
				+ 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		if (player == null || !player.abilities.isCreativeMode)
		{
			context.getItem().shrink(1);
		}

		return ActionResultType.func_233537_a_(world.isRemote);

//		return ActionResultType.SUCCESS;
	}
}