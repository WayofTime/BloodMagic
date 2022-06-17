package wayoftime.bloodmagic.common.item.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileMimic;

public class ItemBlockMimic extends BlockItem
{
	public ItemBlockMimic(Block block, Properties prop)
	{
		super(block, prop);
	}

	@Override
	public InteractionResult place(BlockPlaceContext context)
	{
		Player player = context.getPlayer();
		ItemStack stack = player.getItemInHand(context.getHand());

		// If not sneaking, do normal item use
		if (!player.isShiftKeyDown())
		{
			return super.place(context);
		}

		BlockPos pos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
		Level world = context.getLevel();
		Direction direction = context.getClickedFace();

		// IF sneaking and player has permission, replace the targeted block
		if (player.mayUseItemAt(pos, direction, stack))
		{
			// Store information about the block being replaced and its appropriate
			// itemstack
			BlockState replacedBlockstate = world.getBlockState(pos);
			Block replacedBlock = replacedBlockstate.getBlock();
			ItemStack replacedStack = replacedBlock.getCloneItemStack(world, pos, replacedBlockstate);

			// Get the state for the mimic
			BlockState mimicBlockstate = this.getBlock().defaultBlockState();

			// Check if the block can be replaced

			if (!canReplaceBlock(world, pos, replacedBlockstate))
			{
				return super.place(context);
			}

			// Check if the tile entity, if any, can be replaced
			BlockEntity tileReplaced = world.getBlockEntity(pos);
			if (!canReplaceTile(tileReplaced))
			{
				return InteractionResult.FAIL;
			}

			// If tile can be replaced, store info about the tile
			CompoundTag tileTag = getTagFromTileEntity(tileReplaced);
			if (tileReplaced != null)
			{
				CompoundTag voidTag = new CompoundTag();
				voidTag.putInt("x", pos.getX());
				voidTag.putInt("y", pos.getY());
				voidTag.putInt("z", pos.getZ());
				tileReplaced.deserializeNBT(voidTag);
			}

			// Remove one item from stack
			stack.shrink(1);

			// Replace the block
			world.setBlock(pos, mimicBlockstate, 3);
			// Make placing sound
			SoundType soundtype = mimicBlockstate.getSoundType(world, pos, context.getPlayer());
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

			// Replace the tile entity
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TileMimic)
			{
				TileMimic mimic = (TileMimic) tile;
				mimic.tileTag = tileTag;
//				mimic.setReplacedState(replacedBlockstate);
				mimic.setMimic(replacedBlockstate);
				mimic.setItem(0, replacedStack);
				mimic.refreshTileEntity();

				if (player.isCreative())
				{
					mimic.dropItemsOnBreak = false;
				}
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;

	}

	public boolean canReplaceTile(BlockEntity tile)
	{
		if (tile instanceof ChestBlockEntity)
		{
			return true;
		}

		return tile == null;
	}

	public boolean canReplaceBlock(Level world, BlockPos pos, BlockState state)
	{
		return state.getDestroySpeed(world, pos) != -1.0F;
	}

	public CompoundTag getTagFromTileEntity(BlockEntity tile)
	{
		CompoundTag tag = new CompoundTag();

		if (tile != null)
		{
			return tile.saveWithoutMetadata();
		}

		return tag;
	}

}
