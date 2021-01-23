package wayoftime.bloodmagic.tile;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.tile.base.TileTicking;

public class TileExplosiveCharge extends TileTicking
{

	public AnointmentHolder anointmentHolder = new AnointmentHolder();

	public TileExplosiveCharge(TileEntityType<?> type)
	{
		super(type);
	}

	protected static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos)
	{
		int i = dropPositionArray.size();

		for (int j = 0; j < i; ++j)
		{
			Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
			ItemStack itemstack = pair.getFirst();
			if (ItemEntity.canMergeStacks(itemstack, stack))
			{
				ItemStack itemstack1 = ItemEntity.mergeStacks(itemstack, stack, 16);
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
	public void deserialize(CompoundNBT tag)
	{
		if (tag.contains("holder"))
		{
			anointmentHolder = AnointmentHolder.fromNBT(tag.getCompound("holder"));
		}

	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
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

		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	@Override
	public void onUpdate()
	{

	}
}