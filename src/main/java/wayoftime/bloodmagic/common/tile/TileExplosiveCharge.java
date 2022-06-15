package wayoftime.bloodmagic.common.tile;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.tile.base.TileTicking;

public class TileExplosiveCharge extends TileTicking
{
	public AnointmentHolder anointmentHolder = new AnointmentHolder();

	public TileExplosiveCharge(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	protected static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos)
	{
		int i = dropPositionArray.size();

		for (int j = 0; j < i; ++j)
		{
			Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
			ItemStack itemstack = pair.getFirst();
			if (ItemEntity.areMergable(itemstack, stack))
			{
				ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
				dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
				if (stack.isEmpty())
				{
					return;
				}
			}
		}

		dropPositionArray.add(Pair.of(stack, pos));
	}

	public ItemStack getHarvestingTool()
	{
		ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
		if (anointmentHolder != null)
			anointmentHolder.toItemStack(stack);
		return stack;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		if (tag.contains("holder"))
		{
			anointmentHolder = AnointmentHolder.fromNBT(tag.getCompound("holder"));
		}

	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		if (anointmentHolder != null)
		{
			tag.put("holder", anointmentHolder.serialize());
		}

		return tag;
	}

	public void setAnointmentHolder(AnointmentHolder holder)
	{
		this.anointmentHolder = holder;
	}

	public void dropSelf()
	{
		ItemStack stack = new ItemStack(getBlockState().getBlock());
		if (anointmentHolder != null && !anointmentHolder.isEmpty())
		{
			anointmentHolder.toItemStack(stack);
		}

		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
	}

	@Override
	public void onUpdate()
	{

	}
}