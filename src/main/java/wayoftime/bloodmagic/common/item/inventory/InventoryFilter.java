package wayoftime.bloodmagic.common.item.inventory;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import wayoftime.bloodmagic.common.item.routing.ItemRouterFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

public class InventoryFilter extends ItemInventory
{
	public InventoryFilter(ItemStack itemStack)
	{
		super(itemStack, ItemRouterFilter.inventorySize, "RoutingFilter");
	}

	public void onGuiSaved(PlayerEntity entityPlayer)
	{
		masterStack = findParentStack(entityPlayer);

		if (!masterStack.isEmpty())
		{
			save();
		}
	}

	public ItemStack findParentStack(PlayerEntity entityPlayer)
	{
		if (Utils.hasUUID(masterStack))
		{
			UUID parentStackUUID = new UUID(masterStack.getTag().getLong(Constants.NBT.MOST_SIG), masterStack.getTag().getLong(Constants.NBT.LEAST_SIG));
			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);

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
		CompoundNBT nbtTagCompound = masterStack.getTag();

		if (nbtTagCompound == null)
		{
			nbtTagCompound = new CompoundNBT();

			UUID uuid = UUID.randomUUID();
			nbtTagCompound.putLong(Constants.NBT.MOST_SIG, uuid.getMostSignificantBits());
			nbtTagCompound.putLong(Constants.NBT.LEAST_SIG, uuid.getLeastSignificantBits());
		}

		writeToNBT(nbtTagCompound);
		masterStack.setTag(nbtTagCompound);
	}

	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
}
