package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.*;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TEWritingTable extends TileEntity implements IInventory {
    private ItemStack[] inv;
    private int progress;
    private int progressNeeded = 100;
    private int amountUsed;

    public TEWritingTable()
    {
        inv = new ItemStack[7];
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
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Inventory");

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");

            if (slot >= 0 && slot < inv.length)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        progress = tagCompound.getInteger("progress");
        amountUsed = tagCompound.getInteger("amountUsed");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];

            if (stack != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        tagCompound.setTag("Inventory", itemList);
        tagCompound.setInteger("progress", progress);
        tagCompound.setInteger("amountUsed", amountUsed);
    }

    @Override
    public String getInvName()
    {
        return "aw.TEWritingTable";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        // TODO Auto-generated method stub
        return false;
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Packet250CustomPayload("particle", bos.toByteArray());
    }

    public void handlePacketData(int[] intData)
    {
        if (intData == null)
        {
            return;
        }

        if (intData.length == 3 * 7)
        {
            for (int i = 0; i < 7; i++)
            {
                if (intData[i * 3 + 2] != 0)
                {
                    ItemStack is = new ItemStack(intData[i * 3], intData[i * 3 + 2], intData[i * 3 + 1]);
                    inv[i] = is;
                } else
                {
                    inv[i] = null;
                }
            }
        }
    }

    public int[] buildIntDataList()
    {
        int[] sortList = new int[7 * 3];
        int pos = 0;

        for (ItemStack is : inv)
        {
            if (is != null)
            {
                sortList[pos++] = is.itemID;
                sortList[pos++] = is.getItemDamage();
                sortList[pos++] = is.stackSize;
            } else
            {
                sortList[pos++] = 0;
                sortList[pos++] = 0;
                sortList[pos++] = 0;
            }
        }

        return sortList;
    }

    public ItemStack getResultingItemStack()
    {
        ItemStack[] composedRecipe = new ItemStack[5];

        for (int i = 0; i < 5; i++)
        {
            composedRecipe[i] = inv[i + 1];
        }

        return AlchemyRecipeRegistry.getResult(composedRecipe, inv[0]);
    }

    public boolean isRecipeValid()
    {
        return (getResultingItemStack() != null);
    }

    public int getAmountNeeded(ItemStack bloodOrb)
    {
        ItemStack[] composedRecipe = new ItemStack[5];

        for (int i = 0; i < 5; i++)
        {
            composedRecipe[i] = inv[i + 1];
        }

        return AlchemyRecipeRegistry.getAmountNeeded(composedRecipe, bloodOrb);
    }

    public boolean containsPotionFlask()
    {
        if (getPotionFlaskPosition() != -1)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public int getPotionFlaskPosition()
    {
        for (int i = 1; i <= 5; i++)
        {
            if (inv[i] != null && !(inv[i].getItem() instanceof ItemBlock) && inv[i].itemID == ModItems.alchemyFlask.itemID)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean containsRegisteredPotionIngredient()
    {
        if (getRegisteredPotionIngredientPosition() != -1)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public int getRegisteredPotionIngredientPosition()
    {
        ItemStack[] composedRecipe = new ItemStack[5];

        for (int i = 0; i < 5; i++)
        {
            composedRecipe[i] = inv[i + 1];
        }

        int location = AlchemicalPotionCreationHandler.getRegisteredPotionIngredientPosition(composedRecipe);

        if (location != -1)
        {
            return location + 1;
        }

        return -1;
    }

    public boolean containsCatalyst()
    {
        if (getCatalystPosition() != -1)
        {
            return true;
        }

        return false;
    }

    public int getCatalystPosition()
    {
        for (int i = 0; i < 5; i++)
        {
            if (inv[i + 1] != null && inv[i + 1].getItem() instanceof ICatalyst)
            {
                return i + 1;
            }
        }

        return -1;
    }

    public boolean containsBindingAgent()
    {
        if (getBindingAgentPosition() != -1)
        {
            return true;
        }

        return false;
    }

    public int getBindingAgentPosition()
    {
        for (int i = 0; i < 5; i++)
        {
            if (inv[i + 1] != null && inv[i + 1].getItem() instanceof IBindingAgent)
            {
                return i + 1;
            }
        }

        return -1;
    }

    public boolean containsFillingAgent()
    {
        if (getFillingAgentPosition() != -1)
        {
            return true;
        }

        return false;
    }

    public int getFillingAgentPosition()
    {
        for (int i = 0; i < 5; i++)
        {
            if (inv[i + 1] != null && inv[i + 1].getItem() instanceof IFillingAgent)
            {
                return i + 1;
            }
        }

        return -1;
    }

    public boolean containsBlankSlate()
    {
        if (getBlankSlatePosition() != -1)
        {
            return true;
        }

        return false;
    }

    public int getBlankSlatePosition()
    {
        for (int i = 0; i < 5; i++)
        {
            if (inv[i + 1] != null && inv[i + 1].itemID == ModItems.blankSlate.itemID)
            {
                return i + 1;
            }
        }

        return -1;
    }

    @Override
    public void updateEntity()
    {
        long worldTime = worldObj.getWorldTime();

        if (worldObj.isRemote)
        {
            return;
        }

//    	if(worldTime%100==0)
//    	{
//    		if (worldObj != null)
//            {
//                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
//            }
//    	}

        if (containsPotionFlask() && containsRegisteredPotionIngredient())
        {
            if (containsCatalyst())
            {
                if (getStackInSlot(6) == null)
                {
                    progress++;

                    if (worldTime % 4 == 0)
                    {
                        PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                    }

                    if (progress >= progressNeeded)
                    {
                        ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                        ItemStack ingredientStack = inv[this.getRegisteredPotionIngredientPosition()];
                        ItemStack catalystStack = inv[this.getCatalystPosition()];

                        if (flaskStack == null || ingredientStack == null || catalystStack == null)
                        {
                            progress = 0;

                            if (worldObj != null)
                            {
                                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            }

                            return;
                        }

                        int potionID = AlchemicalPotionCreationHandler.getPotionIDForStack(ingredientStack);
                        int catalystLevel = ((ICatalyst) catalystStack.getItem()).getCatalystLevel();
                        boolean isConcentration = ((ICatalyst) catalystStack.getItem()).isConcentration();

                        if (potionID == -1 || catalystLevel < 0)
                        {
                            progress = 0;

                            if (worldObj != null)
                            {
                                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            }

                            return;
                        }

                        if (isConcentration)
                        {
                            ((AlchemyFlask) flaskStack.getItem()).setConcentrationOfPotion(flaskStack, potionID, catalystLevel);
                        } else
                        {
                            ((AlchemyFlask) flaskStack.getItem()).setDurationFactorOfPotion(flaskStack, potionID, catalystLevel);
                        }

                        //((AlchemyFlask)flaskStack.getItem()).setConcentrationOfPotion(flaskStack, Potion.regeneration.id, 2);
                        this.setInventorySlotContents(6, flaskStack);
                        this.decrStackSize(this.getPotionFlaskPosition(), 1);
                        this.decrStackSize(this.getCatalystPosition(), 1);
                        this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                    }
                }
            } else if (containsBindingAgent())
            {
                if (getStackInSlot(6) == null)
                {
                    progress++;

                    if (worldTime % 4 == 0)
                    {
                        PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                    }

                    if (progress >= progressNeeded)
                    {
                        ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                        ItemStack ingredientStack = inv[this.getRegisteredPotionIngredientPosition()];
                        ItemStack agentStack = inv[this.getBindingAgentPosition()];

                        if (flaskStack == null || ingredientStack == null || agentStack == null)
                        {
                            progress = 0;

                            if (worldObj != null)
                            {
                                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            }

                            return;
                        }

                        int potionEffectNumber = ((AlchemyFlask) flaskStack.getItem()).getNumberOfPotionEffects(flaskStack);
                        int potionID = AlchemicalPotionCreationHandler.getPotionIDForStack(ingredientStack);
                        int tickDuration = AlchemicalPotionCreationHandler.getPotionTickDurationForStack(ingredientStack);
                        float successChance = ((IBindingAgent) agentStack.getItem()).getSuccessRateForPotionNumber(potionEffectNumber);
                        //boolean isConcentration = ((ICatalyst)catalystStack.getItem()).isConcentration();

                        if (potionID == -1)
                        {
                            progress = 0;

                            if (worldObj != null)
                            {
                                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            }

                            return;
                        }

                        ((AlchemyFlask) flaskStack.getItem()).addPotionEffect(flaskStack, potionID, tickDuration);

                        //((AlchemyFlask)flaskStack.getItem()).addPotionEffect(flaskStack, Potion.regeneration.id, 1000);

                        if (successChance > worldObj.rand.nextFloat())
                        {
                            this.setInventorySlotContents(6, flaskStack);
                        } else
                        {
                            worldObj.createExplosion(null, xCoord + 0.5, yCoord + 1, zCoord + 0.5, 2, false);
                        }

                        this.decrStackSize(this.getPotionFlaskPosition(), 1);
                        this.decrStackSize(this.getBindingAgentPosition(), 1);
                        this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                    }
                }
            }
        } else if (this.containsBlankSlate() && this.containsPotionFlask())
        {
            if (getStackInSlot(6) == null)
            {
                progress++;

                if (worldTime % 4 == 0)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                }

                if (progress >= progressNeeded)
                {
                    ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                    //ItemStack ingredientStack = inv[this.getRegisteredPotionIngredientPosition()];
                    ItemStack blankSlate = inv[this.getBlankSlatePosition()];

                    if (flaskStack == null || blankSlate == null)
                    {
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }

                        return;
                    }

                    //boolean isConcentration = ((ICatalyst)catalystStack.getItem()).isConcentration();
                    ((AlchemyFlask) flaskStack.getItem()).setIsPotionThrowable(true, flaskStack);
                    //((AlchemyFlask)flaskStack.getItem()).addPotionEffect(flaskStack, Potion.regeneration.id, 1000);
                    //if(successChance>worldObj.rand.nextFloat())
                    this.setInventorySlotContents(6, flaskStack);
                    this.decrStackSize(this.getPotionFlaskPosition(), 1);
                    this.decrStackSize(this.getBlankSlatePosition(), 1);
                    //this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                    progress = 0;

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        } else if (this.containsFillingAgent() && this.containsPotionFlask())
        {
            //TODO
            if (getStackInSlot(6) == null)
            {
                progress++;

                if (worldTime % 4 == 0)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                }

                if (progress >= progressNeeded)
                {
                    ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                    //ItemStack ingredientStack = inv[this.getRegisteredPotionIngredientPosition()];
                    ItemStack fillingAgent = inv[this.getFillingAgentPosition()];

                    if (flaskStack == null || fillingAgent == null)
                    {
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }

                        return;
                    }

                    //boolean isConcentration = ((ICatalyst)catalystStack.getItem()).isConcentration();
                    int potionEffects = ((AlchemyFlask) flaskStack.getItem()).getNumberOfPotionEffects(flaskStack);
                    int potionFillAmount = ((IFillingAgent) fillingAgent.getItem()).getFilledAmountForPotionNumber(potionEffects);
                    flaskStack.setItemDamage(Math.max(0, flaskStack.getItemDamage() - potionFillAmount));
                    //((AlchemyFlask)flaskStack.getItem()).addPotionEffect(flaskStack, Potion.regeneration.id, 1000);
                    //if(successChance>worldObj.rand.nextFloat())
                    this.setInventorySlotContents(6, flaskStack);
                    this.decrStackSize(this.getPotionFlaskPosition(), 1);
                    this.decrStackSize(this.getFillingAgentPosition(), 1);
                    //this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                    progress = 0;

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        } else
        {
            if (!isRecipeValid())
            {
                progress = 0;
                return;
            }

            if (progress <= 0)
            {
                progress = 0;
                amountUsed = this.getAmountNeeded(getStackInSlot(0));

                if (worldObj != null)
                {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }

            if (getStackInSlot(6) == null)
            {
                if (worldTime % 4 == 0)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                }

                if (!EnergyItems.syphonWhileInContainer(getStackInSlot(0), amountUsed))
                {
                    return;
                }

                progress++;

                if (progress >= progressNeeded)
                {
                    progress = 0;
                    this.setInventorySlotContents(6, getResultingItemStack());

                    for (int i = 0; i < 5; i++)
                    {
                        this.decrStackSize(i + 1, 1);
                    }

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            } else if (getStackInSlot(6).getItem().itemID == getResultingItemStack().itemID && getResultingItemStack().stackSize <= (getStackInSlot(6).getMaxStackSize() - getStackInSlot(6).stackSize))
            {
                if (worldTime % 4 == 0)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                }

                if (!EnergyItems.syphonWhileInContainer(getStackInSlot(0), amountUsed))
                {
                    return;
                }

                progress++;

                if (progress >= progressNeeded)
                {
                    progress = 0;
                    ItemStack result = getResultingItemStack().copy();
                    result.stackSize += getStackInSlot(6).stackSize;
                    this.setInventorySlotContents(6, result);

                    for (int i = 0; i < 5; i++)
                    {
                        this.decrStackSize(i + 1, 1);
                    }

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        }

        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}