package wayoftime.bloodmagic.common.item.inventory;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.routing.ItemItemRouterFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

public class InventoryFilter extends ItemInventory
{
	public InventoryFilter(ItemStack itemStack)
	{
		super(itemStack, ItemItemRouterFilter.inventorySize, "RoutingFilter");
	}

	public void onGuiSaved(Player entityPlayer)
	{
		masterStack = findParentStack(entityPlayer);

		if (!masterStack.isEmpty())
		{
			save();
		}
	}

	public ItemStack findParentStack(Player entityPlayer)
	{
		if (Utils.hasUUID(masterStack))
		{
			UUID parentStackUUID = new UUID(masterStack.getTag().getLong(Constants.NBT.MOST_SIG), masterStack.getTag().getLong(Constants.NBT.LEAST_SIG));
			for (int i = 0; i < entityPlayer.getInventory().getContainerSize(); i++)
			{
				ItemStack itemStack = entityPlayer.getInventory().getItem(i);

				if (!itemStack.isEmpty() && Utils.hasUUID(itemStack))
				{
					if (itemStack.getTag().getLong(Constants.NBT.MOST_SIG) == parentStackUUID.getMostSignificantBits() && itemStack.getTag().getLong(Constants.NBT.LEAST_SIG) == parentStackUUID.getLeastSignificantBits())
					{
						return itemStack;
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}

	public void save()
	{
		CompoundTag nbtTagCompound = masterStack.getTag();

		if (nbtTagCompound == null)
		{
			nbtTagCompound = new CompoundTag();

			UUID uuid = UUID.randomUUID();
			nbtTagCompound.putLong(Constants.NBT.MOST_SIG, uuid.getMostSignificantBits());
			nbtTagCompound.putLong(Constants.NBT.LEAST_SIG, uuid.getLeastSignificantBits());
		}

		writeToNBT(nbtTagCompound);
		masterStack.setTag(nbtTagCompound);
	}

	@Override
	public boolean canPlaceItem(int slotIndex, ItemStack itemStack)
	{
		return true;
	}

	@Override
	public int getMaxStackSize()
	{
		return 1;
	}
}
