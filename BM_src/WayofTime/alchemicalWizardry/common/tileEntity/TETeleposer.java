package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;

public class TETeleposer extends TileEntity implements IInventory
{
	private ItemStack[] inv;
	private int resultID;
	private int resultDamage;
	private int previousInput;

	private boolean isActive;

	public TETeleposer()
	{
		inv = new ItemStack[1];
		resultID = 0;
		resultDamage = 0;
		isActive = false;
		previousInput = 0;
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
		previousInput = par1NBTTagCompound.getInteger("previousInput");
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
		par1NBTTagCompound.setInteger("previousInput", previousInput);
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
		return "TETeleposer";
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

		if (worldObj.isRemote)
		{
			return;
		}

		int currentInput = worldObj.getBlockPowerInput(xCoord, yCoord, zCoord);

		if (previousInput == 0 && currentInput != 0)
		{
			ItemStack focus = getStackInSlot(0);

			if (focus != null && focus.getItem()instanceof TelepositionFocus)
			{
				TelepositionFocus focusItem = (TelepositionFocus)focus.getItem();
				int xf = focusItem.xCoord(focus);
				int yf = focusItem.yCoord(focus);
				int zf = focusItem.zCoord(focus);
				World worldF = focusItem.getWorld(focus);
				int damage = (int)(0.5f * Math.sqrt((xCoord - xf) * (xCoord - xf) + (yCoord - yf + 1) * (yCoord - yf + 1) + (zCoord - zf) * (zCoord - zf)));
				int focusLevel = focusItem.getFocusLevel();
				int transportCount = 0;
				int entityCount = 0;

				if (worldF != null && worldF.getBlockTileEntity(xf, yf, zf) instanceof TETeleposer && !worldF.getBlockTileEntity(xf, yf, zf).equals(this))
				{
					//Prime the teleportation
					int d0 = focusLevel - 1;
					AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getAABBPool().getAABB(xCoord, (double)yCoord + d0 + 1, zCoord, xCoord + 1, yCoord + 2 + d0, zCoord + 1).expand(d0, d0, d0);
					axisalignedbb1.maxY = Math.min((double)worldObj.getHeight(), yCoord + 2 + d0 + d0);
					List list1 = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb1);
					Iterator iterator1 = list1.iterator();
					EntityLivingBase entityplayer1;

					while (iterator1.hasNext())
					{
						entityplayer1 = (EntityLivingBase)iterator1.next();
						entityCount++;
					}

					//int d0 = focusLevel-1;
					AxisAlignedBB axisalignedbb2 = AxisAlignedBB.getAABBPool().getAABB(xf, yf + d0 + 1, zf, xf + 1, yf + 2 + d0, zf).expand(d0, d0, d0);
					//axisalignedbb2.maxY = Math.min((double)worldF.getHeight(),yf+1+d0+d0);
					List list2 = worldF.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb2);
					Iterator iterator2 = list2.iterator();
					EntityLivingBase entityplayer2;

					while (iterator2.hasNext())
					{
						entityplayer2 = (EntityLivingBase)iterator2.next();
						entityCount++;
					}

					if (EnergyItems.canSyphonInContainer(focus, damage * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) + damage * entityCount))
					{
						for (int k = 0; k <= focusLevel * 2 - 2; k++)
							//for(int k=(focusLevel*2-1);k>=0;k--)
							{
								for (int i = -(focusLevel - 1); i <= focusLevel - 1; i++)
								{
									for (int j = -(focusLevel - 1); j <= focusLevel - 1; j++)
									{
										{
											if (BlockTeleposer.swapBlocks(worldObj, worldF, xCoord + i, yCoord + 1 + k, zCoord + j, xf + i, yf + 1 + k, zf + j))
											{
												transportCount++;
											}
										}
									}
								}
							}

						if (!worldF.equals(worldObj))
						{
							entityCount = 0;
						}

						EnergyItems.syphonWhileInContainer(focus, damage * transportCount + damage * entityCount);
						//Teleport

						if (worldF.equals(worldObj))
						{
							iterator1 = list1.iterator();
							iterator2 = list2.iterator();

							while (iterator1.hasNext())
							{
								entityplayer1 = (EntityLivingBase)iterator1.next();
								entityplayer1.worldObj = worldF;
								entityplayer1.setPositionAndUpdate(entityplayer1.posX - xCoord + xf, entityplayer1.posY - yCoord + yf, entityplayer1.posZ - zCoord + zf);
							}

							while (iterator2.hasNext())
							{
								entityplayer2 = (EntityLivingBase)iterator2.next();
								entityplayer2.worldObj = worldF;
								entityplayer2.setPositionAndUpdate(entityplayer2.posX + xCoord - xf, entityplayer2.posY + yCoord - yf, entityplayer2.posZ + zCoord - zf);
							}
						}
					}
				}
			}
		}

		previousInput = currentInput;
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
}
