package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public class TEAlchemicCalcinator extends TEReagentConduit implements IInventory
{
    protected ItemStack[] inv;
    protected ReagentContainer bufferTank = new ReagentContainer(Reagent.REAGENT_SIZE * 2);

    protected int bufferTransferRate = 20;

    private int lpPerTick = 10;
    private int ticksPerReagent = 200;

    public int progress;

    public TEAlchemicCalcinator()
    {
        super(1, Reagent.REAGENT_SIZE * 4);
        this.inv = new ItemStack[2];
        this.tickRate = 20;
        this.maxConnextions = 1;
        this.progress = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        bufferTransferRate = tag.getInteger("bufferTransferRate");
        progress = tag.getInteger("progress");

        NBTTagCompound bufferTankTag = tag.getCompoundTag("bufferTank");

        this.bufferTank = ReagentContainer.readFromNBT(bufferTankTag);

        NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = (NBTTagCompound) tagList.getCompoundTagAt(i);

            if (savedTag.getBoolean("Empty"))
            {
                inv[i] = null;
            } else
            {
                inv[i] = ItemStack.loadItemStackFromNBT(savedTag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("bufferTransferRate", bufferTransferRate);
        tag.setInteger("progress", progress);

        NBTTagCompound bufferTankTag = new NBTTagCompound();

        this.bufferTank.writeToNBT(bufferTankTag);

        tag.setTag("bufferTank", bufferTankTag);

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];
            NBTTagCompound savedTag = new NBTTagCompound();

            if (inv[i] != null)
            {
                inv[i].writeToNBT(savedTag);
            } else
            {
                savedTag.setBoolean("Empty", true);
            }

            itemList.appendTag(savedTag);
        }

        tag.setTag("Inventory", itemList);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            moveBufferToMain();
            tickProgress();
        }
    }

    public void moveBufferToMain()
    {
        ReagentStack amountStack = this.bufferTank.drain(bufferTransferRate, false);
        int drainAmount = this.fill(ForgeDirection.UNKNOWN, amountStack, false);

        if (drainAmount > 0)
        {
            ReagentStack drainedStack = this.bufferTank.drain(drainAmount, true);
            this.fill(ForgeDirection.UNKNOWN, drainedStack, true);
        }
    }

    public void tickProgress()
    {
        ItemStack reagentItemStack = this.getStackInSlot(1);
        if (reagentItemStack == null)
        {
            progress = 0;
            return;
        }

        ReagentStack possibleReagent = ReagentRegistry.getReagentStackForItem(reagentItemStack);
        if (possibleReagent == null || !this.canReagentFitBuffer(possibleReagent))
        {
            return;
        }

        ItemStack orbStack = this.getStackInSlot(0);
        if (orbStack == null || !(orbStack.getItem() instanceof IBloodOrb))
        {
            return;
        }

        if (!SoulNetworkHandler.canSyphonFromOnlyNetwork(orbStack, lpPerTick))
        {
            SoulNetworkHandler.causeNauseaToPlayer(orbStack);
            return;
        }

        SoulNetworkHandler.syphonFromNetwork(orbStack, lpPerTick);
        progress++;

        if (worldObj.getWorldTime() % 4 == 0)
        {
            SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);
        }

        if (progress >= this.ticksPerReagent)
        {
            progress = 0;
            this.bufferTank.fill(possibleReagent, true);
            this.decrStackSize(1, 1);
        }
    }

    public boolean canReagentFitBuffer(ReagentStack stack)
    {
        int amount = this.bufferTank.fill(stack, false);

        return amount >= stack.amount;
    }

    @Override
    public void readClientNBT(NBTTagCompound tag)
    {
        super.readClientNBT(tag);

        NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = tagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }

        NBTTagList invTagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < invTagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = (NBTTagCompound) invTagList.getCompoundTagAt(i);

            if (savedTag.getBoolean("Empty"))
            {
                inv[i] = null;
            } else
            {
                inv[i] = ItemStack.loadItemStackFromNBT(savedTag);
            }
        }
    }

    @Override
    public void writeClientNBT(NBTTagCompound tag)
    {
        super.writeClientNBT(tag);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.tanks.length; i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (this.tanks[i] != null)
            {
                this.tanks[i].writeToNBT(savedTag);
            }
            tagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", tagList);

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];
            NBTTagCompound savedTag = new NBTTagCompound();

            if (inv[i] != null)
            {
                inv[i].writeToNBT(savedTag);
            } else
            {
                savedTag.setBoolean("Empty", true);
            }

            itemList.appendTag(savedTag);
        }

        tag.setTag("Inventory", itemList);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeClientNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readClientNBT(packet.func_148857_g());
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
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inv[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
            } else
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
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public String getInventoryName()
    {
        return "AlchemicCalcinator";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, ReagentStack resource, boolean doFill)
    {
        if (doFill && !worldObj.isRemote)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        return super.fill(from, resource, doFill);
    }
}
