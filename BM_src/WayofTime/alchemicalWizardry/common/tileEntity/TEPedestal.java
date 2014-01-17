package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TEPedestal extends TileEntity implements IInventory
{
	private ItemStack[] inv;
	private int resultID;
	private int resultDamage;

	private boolean isActive;

	public TEPedestal()
	{
		inv = new ItemStack[1];
		resultID = 0;
		resultDamage = 0;
		isActive = false;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			int slot = tag.getByte("Slot");

			if (slot >= 0 && slot < inv.length)
			{
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}

		resultID = par1NBTTagCompound.getInteger("resultID");
		resultDamage = par1NBTTagCompound.getInteger("resultDamage");
		isActive = par1NBTTagCompound.getBoolean("isActive");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inv.length; i++)
		{
			if (inv[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				inv[i].writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		par1NBTTagCompound.setInteger("resultID", resultID);
		par1NBTTagCompound.setInteger("resultDamage", resultDamage);
		par1NBTTagCompound.setTag("Inventory", itemList);
		par1NBTTagCompound.setBoolean("isActive", isActive);
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);

		if (stack != null)
		{
			if (stack.stackSize <= amt)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amt);

				if (stack.stackSize == 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}

		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);

		if (stack != null)
		{
			setInventorySlotContents(slot, null);
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		inv[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "TEPedestal";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void closeChest()
	{
		// TODO Auto-generated method stub
	}

	//Logic for the actual block is under here
	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}

	public void setActive()
	{
		isActive = false;
	}

	public boolean isActive()
	{
		return isActive;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacket(this);
	}

	public void handlePacketData(int[] intData)
	{
		if (intData == null)
		{
			return;
		}

		if (intData.length == 3)
		{
			for (int i = 0; i < 1; i++)
			{
				if (intData[i * 3 + 2] != 0)
				{
					ItemStack is = new ItemStack(intData[i * 3], intData[i * 3 + 2], intData[i * 3 + 1]);
					inv[i] = is;
				}
				else
				{
					inv[i] = null;
				}
			}
		}
	}

	public int[] buildIntDataList()
	{
		int [] sortList = new int[1 * 3];
		int pos = 0;

		for (ItemStack is : inv)
		{
			if (is != null)
			{
				sortList[pos++] = is.itemID;
				sortList[pos++] = is.getItemDamage();
				sortList[pos++] = is.stackSize;
			}
			else
			{
				sortList[pos++] = 0;
				sortList[pos++] = 0;
				sortList[pos++] = 0;
			}
		}

		return sortList;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		if (slot == 0)
		{
			return true;
		}

		return false;
	}

	public void onItemDeletion()
	{
		//worldObj.createExplosion(null, xCoord+0.5, yCoord+0.5, zCoord+0.5, 1, false);
		worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord));

		for (int i = 0; i < 16; i++)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short)2));
		}
	}
}
