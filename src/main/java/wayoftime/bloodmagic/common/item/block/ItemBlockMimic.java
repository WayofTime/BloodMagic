package wayoftime.bloodmagic.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileMimic;

import net.minecraft.item.Item.Properties;

public class ItemBlockMimic extends BlockItem
{
	public ItemBlockMimic(Block block, Properties prop)
	{
		super(block, prop);
	}

	@Override
	public ActionResultType place(BlockItemUseContext context)
	{
		PlayerEntity player = context.getPlayer();
		ItemStack stack = player.getItemInHand(context.getHand());

		// If not sneaking, do normal item use
		if (!player.isShiftKeyDown())
		{
			return super.place(context);
		}

		BlockPos pos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
		World world = context.getLevel();
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
			TileEntity tileReplaced = world.getBlockEntity(pos);
			if (!canReplaceTile(tileReplaced))
			{
				return ActionResultType.FAIL;
			}

			// If tile can be replaced, store info about the tile
			CompoundNBT tileTag = getTagFromTileEntity(tileReplaced);
			if (tileReplaced != null)
			{
				CompoundNBT voidTag = new CompoundNBT();
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
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

			// Replace the tile entity
			TileEntity tile = world.getBlockEntity(pos);
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
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;

	}

	public boolean canReplaceTile(TileEntity tile)
	{
		if (tile instanceof ChestTileEntity)
		{
			return true;
		}

		return tile == null;
	}

	public boolean canReplaceBlock(World world, BlockPos pos, BlockState state)
	{
		return state.getDestroySpeed(world, pos) != -1.0F;
	}

	public CompoundNBT getTagFromTileEntity(TileEntity tile)
	{
		CompoundNBT tag = new CompoundNBT();

		if (tile != null)
		{
			return tile.save(tag);
		}

		return tag;
	}

}
