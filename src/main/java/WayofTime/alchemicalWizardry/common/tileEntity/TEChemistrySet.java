package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipe;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.IBindingAgent;
import WayofTime.alchemicalWizardry.common.ICatalyst;
import WayofTime.alchemicalWizardry.common.IFillingAgent;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.alchemy.CombinedPotionRegistry;
import WayofTime.alchemicalWizardry.common.alchemy.ICombinationalCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class TEChemistrySet extends TEInventory implements ISidedInventory, IUpdatePlayerListBox
{
	public static final int sizeInv = 7;
	
    private int progress;
    private int amountUsed;

    private int accelerationTime;

    public TEChemistrySet()
    {
        super(sizeInv);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        progress = tagCompound.getInteger("progress");
        amountUsed = tagCompound.getInteger("amountUsed");

        accelerationTime = tagCompound.getInteger("accelerationTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        tagCompound.setInteger("progress", progress);
        tagCompound.setInteger("amountUsed", amountUsed);

        tagCompound.setInteger("accelerationTime", accelerationTime);
    }

    @Override
    public String getName()
    {
        return "aw.TEWritingTable";
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
    	switch(i)
    	{
    	case 0:
    		if(itemstack != null)
    		{
    			return itemstack.getItem() instanceof IBloodOrb;
    		}
    	}
        return i != 6;
    }

    @Override
    public net.minecraft.network.Packet getDescriptionPacket()
    {
        return NewPacketHandler.getPacket(this);
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
                    ItemStack is = new ItemStack(Item.getItemById(intData[i * 3]), intData[i * 3 + 2], intData[i * 3 + 1]);
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
                sortList[pos++] = Item.getIdFromItem(is.getItem());
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
        return getPotionFlaskPosition() != -1;
    }

    public int getPotionFlaskPosition()
    {
        for (int i = 1; i <= 5; i++)
        {
            if (inv[i] != null && !(inv[i].getItem() instanceof ItemBlock) && inv[i].getItem() == ModItems.alchemyFlask)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean containsCombinationCatalyst()
    {
        return getCombinationCatalystPosition() != -1;
    }

    public int getCombinationCatalystPosition()
    {
        for (int i = 1; i <= 5; i++)
        {
            if (inv[i] != null && inv[i].getItem() instanceof ICombinationalCatalyst)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean containsRegisteredPotionIngredient()
    {
        return getRegisteredPotionIngredientPosition() != -1;
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
        return getCatalystPosition() != -1;
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
        return getBindingAgentPosition() != -1;
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
        return getFillingAgentPosition() != -1;
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
        return getBlankSlatePosition() != -1;
    }

    public int getBlankSlatePosition()
    {
        for (int i = 0; i < 5; i++)
        {
            if (inv[i + 1] != null && inv[i + 1].getItem() == ModItems.blankSlate)
            {
                return i + 1;
            }
        }

        return -1;
    }

    @Override
    public void update()
    {
        int progressNeeded = 100;
        long worldTime = worldObj.getWorldTime();

        if (worldObj.isRemote)
        {
            return;
        }
        if (accelerationTime > 0)
        {
            accelerationTime--;
        }

        if (containsPotionFlask() && containsRegisteredPotionIngredient())
        {
            if (containsCatalyst())
            {
                if (getStackInSlot(6) == null)
                {
                    progress++;

                    if (worldTime % 4 == 0)
                    {
                        SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
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
                                worldObj.markBlockForUpdate(pos);
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
                                worldObj.markBlockForUpdate(pos);
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
                        this.setInventorySlotContents(6, flaskStack);
                        this.decrStackSize(this.getPotionFlaskPosition(), 1);
                        this.decrStackSize(this.getCatalystPosition(), 1);
                        this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
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
                        SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
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
                                worldObj.markBlockForUpdate(pos);
                            }

                            return;
                        }

                        int potionEffectNumber = ((AlchemyFlask) flaskStack.getItem()).getNumberOfPotionEffects(flaskStack);
                        int potionID = AlchemicalPotionCreationHandler.getPotionIDForStack(ingredientStack);
                        int tickDuration = AlchemicalPotionCreationHandler.getPotionTickDurationForStack(ingredientStack);
                        float successChance = ((IBindingAgent) agentStack.getItem()).getSuccessRateForPotionNumber(potionEffectNumber);
                        if (potionID == -1)
                        {
                            progress = 0;

                            if (worldObj != null)
                            {
                                worldObj.markBlockForUpdate(pos);
                            }

                            return;
                        }

                        ((AlchemyFlask) flaskStack.getItem()).addPotionEffect(flaskStack, potionID, tickDuration);
                        if (successChance > worldObj.rand.nextFloat())
                        {
                            this.setInventorySlotContents(6, flaskStack);
                        } else
                        {
                            worldObj.createExplosion(null, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 2, false);
                        }

                        this.decrStackSize(this.getPotionFlaskPosition(), 1);
                        this.decrStackSize(this.getBindingAgentPosition(), 1);
                        this.decrStackSize(this.getRegisteredPotionIngredientPosition(), 1);
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
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
                    SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
                }

                if (progress >= progressNeeded)
                {
                    ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                    ItemStack blankSlate = inv[this.getBlankSlatePosition()];

                    if (flaskStack == null || blankSlate == null)
                    {
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
                        }

                        return;
                    }
                    ((AlchemyFlask) flaskStack.getItem()).setIsPotionThrowable(true, flaskStack);
                    this.setInventorySlotContents(6, flaskStack);
                    this.decrStackSize(this.getPotionFlaskPosition(), 1);
                    this.decrStackSize(this.getBlankSlatePosition(), 1);
                    progress = 0;

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(pos);
                    }
                }
            }
        } else if (this.containsFillingAgent() && this.containsPotionFlask())
        {
            if (getStackInSlot(6) == null)
            {
                progress++;

                if (worldTime % 4 == 0)
                {
                    SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
                }

                if (progress >= progressNeeded)
                {
                    ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                    ItemStack fillingAgent = inv[this.getFillingAgentPosition()];

                    if (flaskStack == null || fillingAgent == null)
                    {
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
                        }

                        return;
                    }
                    int potionEffects = ((AlchemyFlask) flaskStack.getItem()).getNumberOfPotionEffects(flaskStack);
                    int potionFillAmount = ((IFillingAgent) fillingAgent.getItem()).getFilledAmountForPotionNumber(potionEffects);
                    flaskStack.setItemDamage(Math.max(0, flaskStack.getItemDamage() - potionFillAmount));
                    this.setInventorySlotContents(6, flaskStack);
                    this.decrStackSize(this.getPotionFlaskPosition(), 1);
                    this.decrStackSize(this.getFillingAgentPosition(), 1);
                    progress = 0;

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(pos);
                    }
                }
            }
        } else if (this.containsPotionFlask() && this.containsCombinationCatalyst())
        {
            //TODO
            if (getStackInSlot(6) == null && CombinedPotionRegistry.hasCombinablePotionEffect(inv[this.getPotionFlaskPosition()]))
            {
                progress++;

                if (worldTime % 4 == 0)
                {
                    SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
                }

                if (progress >= progressNeeded)
                {
                    ItemStack flaskStack = inv[this.getPotionFlaskPosition()];
                    ItemStack combinationCatalyst = inv[this.getCombinationCatalystPosition()];

                    if (flaskStack == null || combinationCatalyst == null)
                    {
                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
                        }

                        return;
                    }

                    ItemStack newFlask = CombinedPotionRegistry.applyPotionEffect(flaskStack);
                    if (newFlask != null)
                    {
                        this.setInventorySlotContents(6, newFlask);
                        this.decrStackSize(this.getPotionFlaskPosition(), 1);
                        this.decrStackSize(this.getCombinationCatalystPosition(), 1);

                        progress = 0;

                        if (worldObj != null)
                        {
                            worldObj.markBlockForUpdate(pos);
                        }
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
                    worldObj.markBlockForUpdate(pos);
                }
            }

            int acceleration = this.getSpeedIncrease();

            if (getStackInSlot(6) == null)
            {
                if (!SoulNetworkHandler.syphonFromNetworkWhileInContainer(getStackInSlot(0), amountUsed * acceleration))
                {
                    return;
                }

                if (worldTime % 4 == 0)
                {
                    SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
                }

                progress += acceleration;

                if (progress >= progressNeeded)
                {
                    progress = 0;
                    this.setInventorySlotContents(6, getResultingItemStack());

                    ItemStack[] composedRecipe = new ItemStack[5];

                    for (int i = 0; i < 5; i++)
                    {
                        composedRecipe[i] = inv[i + 1];
                    }

                    this.decrementSlots(this.getRecipeForItems(composedRecipe, inv[0]));

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(pos);
                    }
                }
            } else if (getStackInSlot(6).getItem() == getResultingItemStack().getItem() && getResultingItemStack().stackSize <= (getStackInSlot(6).getMaxStackSize() - getStackInSlot(6).stackSize))
            {
                if (worldTime % 4 == 0)
                {
                    SpellHelper.sendIndexedParticleToAllAround(worldObj, pos, 20, worldObj.provider.getDimensionId(), 1, pos);
                }

                if (!SoulNetworkHandler.syphonFromNetworkWhileInContainer(getStackInSlot(0), amountUsed * acceleration))
                {
                    return;
                }

                progress += acceleration;

                if (progress >= progressNeeded)
                {
                    progress = 0;
                    ItemStack result = getResultingItemStack().copy();
                    result.stackSize += getStackInSlot(6).stackSize;
                    this.setInventorySlotContents(6, result);

                    ItemStack[] composedRecipe = new ItemStack[5];

                    for (int i = 0; i < 5; i++)
                    {
                        composedRecipe[i] = inv[i + 1];
                    }

                    this.decrementSlots(this.getRecipeForItems(composedRecipe, inv[0]));

                    if (worldObj != null)
                    {
                        worldObj.markBlockForUpdate(pos);
                    }
                }
            }
        }
    }

    public void decrementSlots(ItemStack[] recipe) //TODO Fix this. This doesn't work.
    {
        boolean[] decrementedList = new boolean[]{false, false, false, false, false};

        for (int i = 0; i < (Math.min(recipe.length, 5)); i++)
        {
            ItemStack decStack = recipe[i];

            if (decStack == null)
            {
                continue;
            }

            for (int j = 0; j < 5; j++)
            {
                ItemStack testStack = this.getStackInSlot(j + 1);

                if (testStack != null && (testStack.isItemEqual(decStack) || (testStack.getItem() == decStack.getItem() && decStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)) && !(decrementedList[j]))
                {
                    if (testStack.getItem().hasContainerItem(testStack))
                    {
                        this.inv[j + 1] = testStack.getItem().getContainerItem(testStack);
                    } else
                    {
                        this.decrStackSize(j + 1, 1);
                    }

                    decrementedList[j] = true;
                    break;
                }
            }
        }
    }

    public ItemStack[] getRecipeForItems(ItemStack[] recipe, ItemStack bloodOrb)
    {
        if (bloodOrb == null)
        {
            return null;
        }

        if (!(bloodOrb.getItem() instanceof IBloodOrb))
        {
            return null;
        }

        int bloodOrbLevel = ((IBloodOrb) bloodOrb.getItem()).getOrbLevel();

        for (AlchemyRecipe ar : AlchemyRecipeRegistry.recipes)
        {
            if (ar.doesRecipeMatch(recipe, bloodOrbLevel))
            {
                return ar.getRecipe();
            }
        }

        return null;
    }

    public int getSpeedIncrease()
    {
        return accelerationTime > 0 ? 5 : 1;
    }

    public boolean isWorking()
    {
        return this.progress > 0;
    }

    public void setAccelerationTime(int accelerationTime)
    {
        this.accelerationTime = accelerationTime;
    }

	@Override
	public int[] getSlotsForFace(EnumFacing facing)
	{
		switch(facing)
		{
		    case DOWN:
			    return new int[]{6};
		    default:
			    return new int[]{0, 1, 2, 3, 4, 5};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing facing) 
	{
		return slot != 6;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing facing) 
	{
		return slot == 6;
	}
}
