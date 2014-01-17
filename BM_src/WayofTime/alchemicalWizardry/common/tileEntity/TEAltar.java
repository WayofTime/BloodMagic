package WayofTime.alchemicalWizardry.common.tileEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.AltarUpgradeComponent;
import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.UpgradedAltars;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TEAltar extends TileEntity implements IInventory, IFluidTank, IFluidHandler
{
	private ItemStack[] inv;
	private int resultID;
	private int resultDamage;
	private int upgradeLevel;
	//public final LiquidTank tank = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 10);
	protected FluidStack fluid;
	public int capacity;
	private boolean isActive;
	private int liquidRequired; //mB
	private boolean canBeFilled;
	private int consumptionRate;
	private int drainRate;
	private float consumptionMultiplier;
	private float efficiencyMultiplier;
	private float sacrificeEfficiencyMultiplier;
	private float selfSacrificeEfficiencyMultiplier;
	private float capacityMultiplier;
	private float orbCapacityMultiplier;
	private float dislocationMultiplier;
	private boolean isUpgraded;
	private boolean isResultBlock;
	private int bufferCapacity;
	protected FluidStack fluidOutput;
	protected FluidStack fluidInput;
	private int progress;

	public TEAltar()
	{
		inv = new ItemStack[1];
		resultID = 0;
		resultDamage = 0;
		capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;
		fluid = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 0);
		fluidOutput = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 0);
		fluidInput = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 0);
		bufferCapacity = FluidContainerRegistry.BUCKET_VOLUME;
		isActive = false;
		consumptionRate = 0;
		drainRate = 0;
		consumptionMultiplier = 0;
		efficiencyMultiplier = 0;
		capacityMultiplier = 1;
		isUpgraded = false;
		upgradeLevel = 0;
		isResultBlock = false;
		progress = 0;
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

		if (!par1NBTTagCompound.hasKey("Empty"))
		{
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(par1NBTTagCompound);

			if (fluid != null)
			{
				setMainFluid(fluid);
			}

			FluidStack fluidOut = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, par1NBTTagCompound.getInteger("outputAmount"));

			if (fluidOut != null)
			{
				setOutputFluid(fluidOut);
			}

			FluidStack fluidIn = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, par1NBTTagCompound.getInteger("inputAmount"));

			if (fluidIn != null)
			{
				setInputFluid(fluidIn);
			}
		}

		upgradeLevel = par1NBTTagCompound.getInteger("upgradeLevel");
		isActive = par1NBTTagCompound.getBoolean("isActive");
		liquidRequired = par1NBTTagCompound.getInteger("liquidRequired");
		canBeFilled = par1NBTTagCompound.getBoolean("canBeFilled");
		isUpgraded = par1NBTTagCompound.getBoolean("isUpgraded");
		consumptionRate = par1NBTTagCompound.getInteger("consumptionRate");
		drainRate = par1NBTTagCompound.getInteger("drainRate");
		consumptionMultiplier = par1NBTTagCompound.getFloat("consumptionMultiplier");
		efficiencyMultiplier = par1NBTTagCompound.getFloat("efficiencyMultiplier");
		selfSacrificeEfficiencyMultiplier = par1NBTTagCompound.getFloat("selfSacrificeEfficiencyMultiplier");
		sacrificeEfficiencyMultiplier = par1NBTTagCompound.getFloat("sacrificeEfficiencyMultiplier");
		capacityMultiplier = par1NBTTagCompound.getFloat("capacityMultiplier");
		orbCapacityMultiplier = par1NBTTagCompound.getFloat("orbCapacityMultiplier");
		dislocationMultiplier = par1NBTTagCompound.getFloat("dislocationMultiplier");
		capacity = par1NBTTagCompound.getInteger("capacity");
		bufferCapacity = par1NBTTagCompound.getInteger("bufferCapacity");
		progress = par1NBTTagCompound.getInteger("progress");
		isResultBlock = par1NBTTagCompound.getBoolean("isResultBlock");
	}

	public void setMainFluid(FluidStack fluid)
	{
		this.fluid = fluid;
	}

	public void setOutputFluid(FluidStack fluid)
	{
		fluidOutput = fluid;
	}

	public void setInputFluid(FluidStack fluid)
	{
		fluidInput = fluid;
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

		if (fluid != null)
		{
			fluid.writeToNBT(par1NBTTagCompound);
		}
		else
		{
			par1NBTTagCompound.setString("Empty", "");
		}

		if (fluidOutput != null)
		{
			par1NBTTagCompound.setInteger("outputAmount", fluidOutput.amount);
		}

		if (fluidInput != null)
		{
			par1NBTTagCompound.setInteger("inputAmount", fluidInput.amount);
		}

		par1NBTTagCompound.setInteger("upgradeLevel", upgradeLevel);
		par1NBTTagCompound.setBoolean("isActive", isActive);
		par1NBTTagCompound.setInteger("liquidRequired", liquidRequired);
		par1NBTTagCompound.setBoolean("canBeFilled", canBeFilled);
		par1NBTTagCompound.setBoolean("isUpgraded", isUpgraded);
		par1NBTTagCompound.setInteger("consumptionRate", consumptionRate);
		par1NBTTagCompound.setInteger("drainRate", drainRate);
		par1NBTTagCompound.setFloat("consumptionMultiplier", consumptionMultiplier);
		par1NBTTagCompound.setFloat("efficiencyMultiplier", efficiencyMultiplier);
		par1NBTTagCompound.setFloat("sacrificeEfficiencyMultiplier", sacrificeEfficiencyMultiplier);
		par1NBTTagCompound.setFloat("selfSacrificeEfficiencyMultiplier", selfSacrificeEfficiencyMultiplier);
		par1NBTTagCompound.setBoolean("isResultBlock", isResultBlock);
		par1NBTTagCompound.setFloat("capacityMultiplier", capacityMultiplier);
		par1NBTTagCompound.setFloat("orbCapacityMultiplier", orbCapacityMultiplier);
		par1NBTTagCompound.setFloat("dislocationMultiplier", dislocationMultiplier);
		par1NBTTagCompound.setInteger("capacity", capacity);
		par1NBTTagCompound.setInteger("progress", progress);
		par1NBTTagCompound.setInteger("bufferCapacity", bufferCapacity);
	}

	@Override
	public int getSizeInventory()
	{
		return inv.length;
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
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "TEAltar";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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

	//IFluidTank methods
	@Override
	public FluidStack getFluid()
	{
		return fluid;
	}

	public FluidStack getInputFluid()
	{
		return fluidInput;
	}

	public FluidStack getOutputFluid()
	{
		return fluidOutput;
	}

	@Override
	public int getFluidAmount()
	{
		if (fluid == null)
		{
			return 0;
		}

		return fluid.amount;
	}

	@Override
	public int getCapacity()
	{
		return capacity;
	}

	@Override
	public FluidTankInfo getInfo()
	{
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		TileEntity tile = this;

		if (resource == null)
		{
			return 0;
		}

		if (resource.fluidID != new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 1).fluidID)
		{
			return 0;
		}

		if (!doFill)
		{
			if (fluidInput == null)
			{
				return Math.min(bufferCapacity, resource.amount);
			}

			if (!fluidInput.isFluidEqual(resource))
			{
				return 0;
			}

			return Math.min(bufferCapacity - fluidInput.amount, resource.amount);
		}

		if (fluidInput == null)
		{
			fluidInput = new FluidStack(resource, Math.min(bufferCapacity, resource.amount));

			if (tile != null)
			{
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidInput, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord, this));
			}

			return fluidInput.amount;
		}

		if (!fluidInput.isFluidEqual(resource))
		{
			return 0;
		}

		int filled = bufferCapacity - fluidInput.amount;

		if (resource.amount < filled)
		{
			fluidInput.amount += resource.amount;
			filled = resource.amount;
		}
		else
		{
			fluidInput.amount = bufferCapacity;
		}

		if (tile != null)
		{
			FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidInput, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord, this));
		}

		return filled;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (fluidOutput == null)
		{
			return null;
		}

		int drained = maxDrain;

		if (fluidOutput.amount < drained)
		{
			drained = fluidOutput.amount;
		}

		FluidStack stack = new FluidStack(fluidOutput, drained);

		if (doDrain)
		{
			fluidOutput.amount -= drained;

			if (fluidOutput.amount <= 0)
			{
				fluidOutput = null;
			}

			if (this != null)
			{
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluidOutput, worldObj, xCoord, yCoord, zCoord, this));
			}
		}

		if (fluidOutput == null)
		{
			fluidOutput = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 0);
		}

		if (worldObj != null)
		{
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		return stack;
	}

	//Logic for the actual block is under here
	@Override
	public void updateEntity()
	{
		//this.capacity=(int) (10000*this.capacityMultiplier);
		if (!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0)
		{
			//TODO
			int syphonMax = (int)(20 * dislocationMultiplier);
			int fluidInputted = 0;
			int fluidOutputted = 0;
			fluidInputted = Math.min(syphonMax, -fluid.amount + capacity);
			fluidInputted = Math.min(fluidInput.amount, fluidInputted);
			fluid.amount += fluidInputted;
			fluidInput.amount -= fluidInputted;
			fluidOutputted = Math.min(syphonMax, bufferCapacity - fluidOutput.amount);
			fluidOutputted = Math.min(fluid.amount, fluidOutputted);
			fluidOutput.amount += fluidOutputted;
			fluid.amount -= fluidOutputted;
		}

		if (worldObj.getWorldTime() % 150 == 0)
		{
			startCycle();
		}

		if (!isActive)
		{
			return;
		}

		if (getStackInSlot(0) == null)
		{
			return;
		}

		int worldTime = (int)(worldObj.getWorldTime() % 24000);

		if (worldObj.isRemote)
		{
			return;
		}

		if (worldTime % 1 == 0)
		{
			if (!canBeFilled)
			{
				if (fluid != null && fluid.amount >= 1)
				{
					int stackSize = getStackInSlot(0).stackSize;
					int liquidDrained = Math.min((int)(upgradeLevel >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

					if (liquidDrained > liquidRequired * stackSize - progress)
					{
						liquidDrained = liquidRequired * stackSize - progress;
					}

					fluid.amount = fluid.amount - liquidDrained;
					progress += liquidDrained;
					//getStackInSlot(0).setItemDamage(getStackInSlot(0).getItemDamage() + liquidDrained);

					if (worldTime % 4 == 0)
					{
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short)1));
					}

					if (progress >= liquidRequired * stackSize)
					{
						ItemStack result = null;

						if (!isResultBlock)
						{
							result = new ItemStack(resultID, stackSize, resultDamage);
						}
						else
						{
							result = new ItemStack(Block.blocksList[resultID], stackSize, 0);
						}

						setInventorySlotContents(0, result);
						progress = 0;

						for (int i = 0; i < 8; i++)
						{
							PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord + 0.5, yCoord + 1, zCoord + 0.5, (short)4));
						}

						//setInventorySlotContents(1, null);
						isActive = false;
					}
				}
				else if (progress > 0)
				{
					progress -= (int)(efficiencyMultiplier * drainRate);

					if (worldTime % 2 == 0)
					{
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short)2));
					}
				}
			}
			else
			{
				ItemStack returnedItem = getStackInSlot(0);

				if (!(returnedItem.getItem() instanceof EnergyBattery))
				{
					return;
				}

				EnergyBattery item = (EnergyBattery)returnedItem.getItem();
				NBTTagCompound itemTag = returnedItem.stackTagCompound;

				if (itemTag == null)
				{
					return;
				}

				String ownerName = itemTag.getString("ownerName");

				if (ownerName.equals(""))
				{
					return;
				}

				//EntityPlayer owner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(itemTag.getString("ownerName"));
				World world = MinecraftServer.getServer().worldServers[0];
				LifeEssenceNetwork data = (LifeEssenceNetwork)world.loadItemData(LifeEssenceNetwork.class, ownerName);

				if (data == null)
				{
					data = new LifeEssenceNetwork(ownerName);
					world.setItemData(ownerName, data);
				}

				int currentEssence = data.currentEssence;
				//                if(owner==null){return;}
				//                NBTTagCompound playerTag = owner.getEntityData();
				//                if(playerTag==null){return;}
				//int currentEssence=playerTag.getInteger("currentEssence");

				if (fluid != null && fluid.amount >= 1)
				{
					int liquidDrained = Math.min((int)(upgradeLevel >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

					if (liquidDrained > item.getMaxEssence() * orbCapacityMultiplier - currentEssence)
					{
						liquidDrained = (int)(item.getMaxEssence() * orbCapacityMultiplier - currentEssence);
					}

					if (liquidDrained <= 0)
					{
						return;
					}

					fluid.amount = fluid.amount - liquidDrained;
					//                    int maxAmount = (int) Math.min(item.getMaxEssence() - consumptionRate * (1 + consumptionMultiplier), consumptionRate * (1 + consumptionMultiplier));
					//                    fluid.amount -= maxAmount;
					data.currentEssence = liquidDrained + data.currentEssence;
					data.markDirty();
					//                    playerTag.setInteger("currentEssence", currentEssence+maxAmount);

					if (worldTime % 4 == 0)
					{
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short)3));
					}
				}
			}

			if (worldObj != null)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}

			//AlchemicalWizardry.proxy.getClientWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			//PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 10, 1, getDescriptionPacket());
			/*
            progress++;

            if(progress>=liquidRequired)
            {
            	setActive();
            	setInventorySlotContents(0, new ItemStack(AlchemicalWizardry.weakBloodOrb));
            }
			 */
		}
	}

	public void setActive()
	{
		isActive = false;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void sacrificialDaggerCall(int amount, boolean isSacrifice)
	{
		fluid.amount += Math.min(capacity - fluid.amount, (isSacrifice ? 1 + sacrificeEfficiencyMultiplier : 1 + selfSacrificeEfficiencyMultiplier) * amount);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacket(this);
	}

	public static Packet250CustomPayload getParticlePacket(double x, double y, double z, short particleType)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			dos.writeDouble(x);
			dos.writeDouble(y);
			dos.writeDouble(z);
			dos.writeShort(particleType);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return new Packet250CustomPayload("particle", bos.toByteArray());
	}

	public void handlePacketData(int[] intData, FluidStack flMain, FluidStack flOut, FluidStack flIn, int capacity)
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

		setMainFluid(flMain);
		setOutputFluid(flOut);
		setInputFluid(flIn);
		this.capacity = capacity;
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

	public void startCycle()
	{
		if (worldObj != null)
		{
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		checkAndSetAltar();

		if (fluid == null || fluid.amount <= 0)
		{
			return;
		}

		if (getStackInSlot(0) == null)
		{
			return;
		}

		if (!isActive)
		{
			progress = 0;
		}

		if (getStackInSlot(0).getItem() instanceof ItemBlock)
		{
			//        	if(!getStackInSlot(0).isItemDamaged()&&getStackInSlot(0).getItemDamage()>16&&!isActive)
				//        	{
				//        		getStackInSlot(0).setItemDamage(0);
				//        	}
			if (upgradeLevel >= 4)
			{
				if (getStackInSlot(0).itemID == Block.coalBlock.blockID)
				{
					isActive = true;
					liquidRequired = 2000;
					canBeFilled = false;
					consumptionRate = 20;
					drainRate = 10;
					resultID = AlchemicalWizardry.duskScribeTool.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (upgradeLevel >= 3)
			{
				if (getStackInSlot(0).itemID == Block.blockGold.blockID)
				{
					isActive = true;
					liquidRequired = 25000;
					canBeFilled = false;
					consumptionRate = 20;
					drainRate = 20;
					//ItemStack bloodOrb = new ItemStack(AlchemicalWizardry.magicianBloodOrb);
					resultID = AlchemicalWizardry.magicianBloodOrb.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.emptySocket.blockID)
				{
					isActive = true;
					liquidRequired = 30000;
					canBeFilled = false;
					consumptionRate = 40;
					drainRate = 10;
					//ItemStack bloodOrb = new ItemStack(AlchemicalWizardry.weakBloodOrb);
					resultID = AlchemicalWizardry.bloodSocket.blockID;
					resultDamage = 0;
					isResultBlock = true;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == Block.obsidian.blockID)
				{
					isActive = true;
					liquidRequired = 1000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.earthScribeTool.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == Block.blockLapis.blockID)
				{
					isActive = true;
					liquidRequired = 1000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.waterScribeTool.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (upgradeLevel >= 2)
			{
				if (getStackInSlot(0).itemID == Block.glass.blockID)
				{
					isActive = true;
					liquidRequired = 1000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.blankSpell.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (getStackInSlot(0).itemID == Block.stone.blockID)
			{
				isActive = true;
				liquidRequired = 1000;
				canBeFilled = false;
				consumptionRate = 5;
				drainRate = 5;
				resultID = AlchemicalWizardry.blankSlate.itemID;
				resultDamage = 0;
				isResultBlock = false;
				//setInventorySlotContents(1, bloodOrb);
				return;
			}
		}
		else
		{
			//        	if(!getStackInSlot(0).isItemDamaged()&&getStackInSlot(0).getItemDamage()>16&&!isActive)
			//        	{
			//        		getStackInSlot(0).setItemDamage(0);
			//        	}
			if (upgradeLevel >= 5)
			{
				if (getStackInSlot(0).itemID == AlchemicalWizardry.demonBloodShard.itemID)
				{
					isActive = true;
					liquidRequired = 75000;
					canBeFilled = false;
					consumptionRate = 50;
					drainRate = 100;
					resultID = AlchemicalWizardry.archmageBloodOrb.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.archmageBloodOrb.itemID)
				{
					ItemStack item = getStackInSlot(0);

					if (item.stackTagCompound == null || item.stackTagCompound.getString("ownerName").equals(""))
					{
						return;
					}

					isActive = true;
					canBeFilled = true;
					consumptionRate = 50;
					isResultBlock = false;
					return;
				}
			}

			if (upgradeLevel >= 4)
			{
				if (getStackInSlot(0).itemID == AlchemicalWizardry.weakBloodShard.itemID)
				{
					isActive = true;
					liquidRequired = 40000;
					canBeFilled = false;
					consumptionRate = 30;
					drainRate = 50;
					resultID = AlchemicalWizardry.masterBloodOrb.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.masterBloodOrb.itemID)
				{
					ItemStack item = getStackInSlot(0);

					if (item.stackTagCompound == null || item.stackTagCompound.getString("ownerName").equals(""))
					{
						return;
					}

					isActive = true;
					canBeFilled = true;
					consumptionRate = 25;
					isResultBlock = false;
					return;
				}

				if (getStackInSlot(0).itemID == Item.enderPearl.itemID)
				{
					isActive = true;
					liquidRequired = 5000;
					canBeFilled = false;
					consumptionRate = 10;
					drainRate = 10;
					resultID = AlchemicalWizardry.telepositionFocus.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.telepositionFocus.itemID)
				{
					isActive = true;
					liquidRequired = 10000;
					canBeFilled = false;
					consumptionRate = 25;
					drainRate = 15;
					resultID = AlchemicalWizardry.enhancedTelepositionFocus.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.imbuedSlate.itemID)
				{
					isActive = true;
					liquidRequired = 15000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					new ItemStack(AlchemicalWizardry.apprenticeBloodOrb);
					resultID = AlchemicalWizardry.demonicSlate.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (upgradeLevel >= 3)
			{
				if (getStackInSlot(0).itemID == AlchemicalWizardry.magicianBloodOrb.itemID)
				{
					ItemStack item = getStackInSlot(0);

					if (item.stackTagCompound == null || item.stackTagCompound.getString("ownerName").equals(""))
					{
						return;
					}

					isActive = true;
					canBeFilled = true;
					consumptionRate = 15;
					isResultBlock = false;
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.lavaCrystal.itemID)
				{
					isActive = true;
					liquidRequired = 10000;
					canBeFilled = false;
					consumptionRate = 20;
					drainRate = 10;
					//ItemStack bloodOrb = new ItemStack(AlchemicalWizardry.weakBloodOrb);
					resultID = AlchemicalWizardry.activationCrystal.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == Item.magmaCream.itemID)
				{
					isActive = true;
					liquidRequired = 1000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.fireScribeTool.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == Item.ghastTear.itemID)
				{
					isActive = true;
					liquidRequired = 1000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.airScribeTool.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.reinforcedSlate.itemID)
				{
					isActive = true;
					liquidRequired = 7000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					new ItemStack(AlchemicalWizardry.apprenticeBloodOrb);
					resultID = AlchemicalWizardry.imbuedSlate.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (upgradeLevel >= 2)
			{
				if (getStackInSlot(0).itemID == Item.emerald.itemID)
				{
					isActive = true;
					liquidRequired = 5000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					new ItemStack(AlchemicalWizardry.apprenticeBloodOrb);
					resultID = AlchemicalWizardry.apprenticeBloodOrb.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.apprenticeBloodOrb.itemID)
				{
					ItemStack item = getStackInSlot(0);

					if (item.stackTagCompound == null || item.stackTagCompound.getString("ownerName").equals(""))
					{
						return;
					}

					isActive = true;
					canBeFilled = true;
					consumptionRate = 5;
					isResultBlock = false;
					return;
				}

				if (getStackInSlot(0).itemID == Item.swordIron.itemID)
				{
					isActive = true;
					liquidRequired = 3000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.daggerOfSacrifice.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == Item.glassBottle.itemID)
				{
					isActive = true;
					liquidRequired = 2000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					resultID = AlchemicalWizardry.alchemyFlask.itemID;
					resultDamage = AlchemicalWizardry.alchemyFlask.getMaxDamage();
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}

				if (getStackInSlot(0).itemID == AlchemicalWizardry.blankSlate.itemID)
				{
					isActive = true;
					liquidRequired = 2000;
					canBeFilled = false;
					consumptionRate = 5;
					drainRate = 5;
					new ItemStack(AlchemicalWizardry.apprenticeBloodOrb);
					resultID = AlchemicalWizardry.reinforcedSlate.itemID;
					resultDamage = 0;
					isResultBlock = false;
					//setInventorySlotContents(1, bloodOrb);
					return;
				}
			}

			if (getStackInSlot(0).itemID == Item.diamond.itemID)
			{
				isActive = true;
				liquidRequired = 2000;
				canBeFilled = false;
				consumptionRate = 2;
				drainRate = 1;
				new ItemStack(AlchemicalWizardry.weakBloodOrb);
				resultID = AlchemicalWizardry.weakBloodOrb.itemID;
				resultDamage = 0;
				isResultBlock = false;
				//setInventorySlotContents(1, bloodOrb);
				return;
			}

			if (getStackInSlot(0).itemID == AlchemicalWizardry.weakBloodOrb.itemID)
			{
				ItemStack item = getStackInSlot(0);

				if (item.stackTagCompound == null || item.stackTagCompound.getString("ownerName").equals(""))
				{
					return;
				}

				isActive = true;
				canBeFilled = true;
				consumptionRate = 2;
				isResultBlock = false;
				return;
			}

			if (getStackInSlot(0).itemID == Item.bucketEmpty.itemID)
			{
				isActive = true;
				liquidRequired = 1000;
				canBeFilled = false;
				consumptionRate = 5;
				drainRate = 0;
				//ItemStack bloodOrb = new ItemStack(AlchemicalWizardry.weakBloodOrb);
				resultID = AlchemicalWizardry.bucketLife.itemID;
				resultDamage = 0;
				isResultBlock = false;
				//setInventorySlotContents(1, bloodOrb);
				return;
			}
		}

		isActive = false;
	}

	public void checkAndSetAltar()
	{
		boolean checkUpgrade = true;
		int upgradeState = UpgradedAltars.isAltarValid(worldObj, xCoord, yCoord, zCoord);

		if (upgradeState <= 1)
		{
			upgradeLevel = 1;
			isUpgraded = false;
			consumptionMultiplier = 0;
			efficiencyMultiplier = 1;
			sacrificeEfficiencyMultiplier = 0;
			selfSacrificeEfficiencyMultiplier = 0;
			capacityMultiplier = 1;
			orbCapacityMultiplier = 1;
			dislocationMultiplier = 1;
			return;
		}

		AltarUpgradeComponent upgrades = UpgradedAltars.getUpgrades(worldObj, xCoord, yCoord, zCoord, upgradeState);

		if (upgrades == null)
		{
			upgradeLevel = 1;
			isUpgraded = false;
			consumptionMultiplier = 0;
			efficiencyMultiplier = 1;
			sacrificeEfficiencyMultiplier = 0;
			selfSacrificeEfficiencyMultiplier = 0;
			capacityMultiplier = 1;
			orbCapacityMultiplier = 1;
			dislocationMultiplier = 1;
			upgradeLevel = upgradeState;
			return;
		}

		isUpgraded = checkUpgrade;
		upgradeLevel = upgradeState;
		consumptionMultiplier = (float)(0.15 * upgrades.getSpeedUpgrades());
		efficiencyMultiplier = (float)Math.pow(0.85, upgrades.getSpeedUpgrades());
		sacrificeEfficiencyMultiplier = (float)(0.10 * upgrades.getSacrificeUpgrades());
		selfSacrificeEfficiencyMultiplier = (float)(0.10 * upgrades.getSelfSacrificeUpgrades());
		capacityMultiplier = (float)(1 + 0.15 * upgrades.getAltarCapacitiveUpgrades());
		//TODO finalize values
		dislocationMultiplier = (float)Math.pow(1.2, upgrades.getDisplacementUpgrades());
		orbCapacityMultiplier = (float)(1 + 0.02 * upgrades.getOrbCapacitiveUpgrades());
		capacity = (int)(FluidContainerRegistry.BUCKET_VOLUME * 10 * capacityMultiplier);
		bufferCapacity = (int)(FluidContainerRegistry.BUCKET_VOLUME * 1 * capacityMultiplier);

		if (fluid.amount > capacity)
		{
			fluid.amount = capacity;
		}

		if (fluidOutput.amount > bufferCapacity)
		{
			fluidOutput.amount = bufferCapacity;
		}

		if (fluidInput.amount > bufferCapacity)
		{
			fluidInput.amount = bufferCapacity;
		}

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		//        for (int x = -1; x <= 1; x++)
			//        {
			//            for (int z = -1; z <= 1; z++)
		//            {
		//                if (!(x == 0 && z == 0))
		//                {
		//                    Block block = Block.blocksList[worldObj.getBlockId(xCoord + x, yCoord - 1, zCoord + z)];
		//
		//                    if (!(block instanceof BloodRune))
		//                    {
		//                        checkUpgrade = false;
		//                        this.isUpgraded = false;
		//                        return;
		//                    }
		//
		//                    if ((z == 0 && (x == -1 || x == 1)) || (x == 0 && (z == -1 || z == 1)))
		//                    {
		//                        switch (((BloodRune)block).getRuneEffect())
		//                        {
		//                            case 1:
		//                                speedUpgrades++;
		//
		//                            case 2:
		//                                efficiencyUpgrades++;
		//
		//                            case 3:
		//                                sacrificeUpgrades++;
		//
		//                            case 4:
		//                                selfSacrificeUpgrades++;
		//                        }
		//                    }
		//                }
		//            }
		//        }
		//        this.isUpgraded = checkUpgrade;
		//        this.consumptionMultiplier = (float)(0.20 * speedUpgrades);
		//        this.efficiencyMultiplier = (float)Math.pow(0.80, efficiencyUpgrades);
		//        this.sacrificeEfficiencyMultiplier = (float)(0.10 * sacrificeUpgrades);
		//        this.selfSacrificeEfficiencyMultiplier = (float)(0.10 * sacrificeUpgrades);
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

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		//TODO
		if (resource == null)
		{
			return 0;
		}

		resource = resource.copy();
		int totalUsed = 0;
		//TileTank tankToFill = getBottomTank();
		int used = this.fill(resource, doFill);
		resource.amount -= used;
		totalUsed += used;
		//FluidStack liquid = tankToFill.tank.getFluid();
		//		if (liquid != null && liquid.amount > 0 && !liquid.isFluidEqual(resource))
		//		{
		//			return 0;
		//		}
		//		while (tankToFill != null && resource.amount > 0)
		//		{
		//			int used = tankToFill.tank.fill(resource, doFill);
		//			resource.amount -= used;
		//			if (used > 0)
		//			{
		//				tankToFill.hasUpdate = true;
		//			}
		//
		//
		//			totalUsed += used;
		//			tankToFill = getTankAbove(tankToFill);
		//		}
		startCycle();
		return totalUsed;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null)
		{
			return null;
		}

		if (!resource.isFluidEqual(fluidOutput))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxEmpty, boolean doDrain)
	{
		return this.drain(maxEmpty, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		if (fluidInput != null && this.fluid.getFluid().equals(fluidInput))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		FluidTank compositeTank = new FluidTank(capacity);
		compositeTank.setFluid(fluid);
		return new FluidTankInfo[] {compositeTank.getInfo()};
	}
}
