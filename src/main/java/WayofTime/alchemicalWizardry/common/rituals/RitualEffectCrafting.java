package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectCrafting extends RitualEffect
{
    public static final boolean limitToSingleStack = true;
    public static final int potentiaDrain = 2;
    public static final int virtusDrain = 2;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
//    	long startTime = System.nanoTime();
    	
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);
        
        if(world.getWorldTime() % (hasPotentia ? 1 : 4) != 0)
        {
        	return;
        }
        
        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
        	NBTTagCompound tag = ritualStone.getCustomRitualTag();

        	if(tag == null)
        	{
        		ritualStone.setCustomRitualTag(new NBTTagCompound());
        		tag = ritualStone.getCustomRitualTag();
        	}
        	
        	boolean lastFailed = tag.getBoolean("didLastCraftFail");
        	
            int slotDesignation = tag.getInteger("slotDesignation");
            if(lastFailed)
            {
            	slotDesignation++;
            	tag.setInteger("slotDesignation", slotDesignation);
            	tag.setBoolean("didLastCraftFail", false);
            }
            int direction = ritualStone.getDirection();
            
            boolean canContinue = false;
            
            ItemStack[] recipe = new ItemStack[9];
            InventoryCrafting inventory = new InventoryCrafting(new Container()
    	    {
    	        public boolean canInteractWith(EntityPlayer player)
    	        {
    	            return false;
    	        }
    	    }, 3, 3);
            
            for(int i=-1; i<=1; i++)
            {
            	for(int j=-1; j<=1; j++)
            	{
        			int gridSpace = (i+1)*3 + (j+1);

            		Int3 pos = this.getSlotPositionForDirection(gridSpace, direction);
            		TileEntity inv = world.getTileEntity(x + pos.xCoord, y + pos.yCoord, z + pos.zCoord);
            		if(inv instanceof IInventory)
            		{
            			if(((IInventory) inv).getSizeInventory() <= slotDesignation || !((IInventory) inv).isItemValidForSlot(slotDesignation, ((IInventory) inv).getStackInSlot(slotDesignation)))
            			{
            				continue;
            			}else
            			{
            				ItemStack invStack = ((IInventory) inv).getStackInSlot(slotDesignation);
            				if(invStack != null)
            				{
            					inventory.setInventorySlotContents(gridSpace, invStack);
            					recipe[gridSpace] = invStack;
            					canContinue = true;
            				}
            			}
            		}
            	}
            }
            
            if(!canContinue)
            {
            	tag.setInteger("slotDesignation", 0);
            	return;
            }
            
            ItemStack returnStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
            
            if (returnStack == null)
            {
            	tag.setBoolean("didLastCraftFail", true);
            }else
            {
            	boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
            	boolean addOutputToInputs = hasVirtus;

                IInventory outputInv = null;
                
                List<IInventory> invList = new ArrayList();

                TileEntity northEntity = world.getTileEntity(x, y-1, z - 2);
                TileEntity southEntity = world.getTileEntity(x, y-1, z + 2);
                TileEntity eastEntity = world.getTileEntity(x + 2, y-1, z);
                TileEntity westEntity = world.getTileEntity(x - 2, y-1, z);
                
                switch(direction)
                {
                case 1:
                	if(southEntity instanceof IInventory)
                    {
                    	outputInv = (IInventory)southEntity;
                    }else
                    {
                    	return;
                    }

            		if(northEntity instanceof IInventory)
            		{
            			invList.add((IInventory)northEntity);
            		}
            		if(eastEntity instanceof IInventory)
            		{
            			invList.add((IInventory)eastEntity);
            		}
            		if(westEntity instanceof IInventory)
            		{
            			invList.add((IInventory)westEntity);
            		}
                	
                	break;
                	
                case 2:
                	if(westEntity instanceof IInventory)
                    {
                    	outputInv = (IInventory)westEntity;
                    }else
                    {
                    	return;
                    }

            		if(northEntity instanceof IInventory)
            		{
            			invList.add((IInventory)northEntity);
            		}
            		if(eastEntity instanceof IInventory)
            		{
            			invList.add((IInventory)eastEntity);
            		}
            		if(southEntity instanceof IInventory)
            		{
            			invList.add((IInventory)southEntity);
            		}
            		
                	break;
                	
                case 3:
                	if(northEntity instanceof IInventory)
                    {
                    	outputInv = (IInventory)northEntity;
                    }else
                    {
                    	return;
                    }

            		if(eastEntity instanceof IInventory)
            		{
            			invList.add((IInventory)eastEntity);
            		}
            		if(southEntity instanceof IInventory)
            		{
            			invList.add((IInventory)southEntity);
            		}
            		if(westEntity instanceof IInventory)
            		{
            			invList.add((IInventory)westEntity);
            		}
            		
                	break;
                	
                case 4:
                	if(eastEntity instanceof IInventory)
                    {
                    	outputInv = (IInventory)eastEntity;
                    }else
                    {
                    	return;
                    }

            		if(northEntity instanceof IInventory)
            		{
            			invList.add((IInventory)northEntity);
            		}
            		if(southEntity instanceof IInventory)
            		{
            			invList.add((IInventory)southEntity);
            		}
            		if(westEntity instanceof IInventory)
            		{
            			invList.add((IInventory)westEntity);
            		}
            		
                	break;
                }

                if (outputInv != null)
                {
                	if(!(!limitToSingleStack ? SpellHelper.canInsertStackFullyIntoInventory(returnStack, outputInv, ForgeDirection.DOWN) : SpellHelper.canInsertStackFullyIntoInventory(returnStack, outputInv, ForgeDirection.DOWN, true, returnStack.getMaxStackSize())))
                	{
                		tag.setBoolean("didLastCraftFail", true);
                		return;
                	}
                	
                	if(addOutputToInputs)
                	{
                		invList.add(outputInv);
                	}
                	
                	Map<Integer, Map<Integer, Integer>> syphonMap = new HashMap(); //Inventory, Slot, how much claimed
                	
                	for(int n = 0; n < recipe.length; n++) //Look for the correct items
                	{
                		ItemStack recipeStack = recipe[n];
                		if(recipeStack == null)
                		{
                			continue;
                		}
                		
                		boolean isItemTaken = false;
                		
                		for(int i = 0; i < invList.size(); i++)
                		{
                			if(isItemTaken)
                			{
                				break;
                			}
                			IInventory inputInv = invList.get(i);
                			if(inputInv == null)
                			{
                				continue;
                			}
                			
                			for(int j = 0; j < inputInv.getSizeInventory(); j++)
                			{
                				if(!inputInv.isItemValidForSlot(j, recipeStack))
                				{
                					continue;
                				}
                				
                				ItemStack invItem = inputInv.getStackInSlot(j);
                				if(invItem == null)
                				{
                					continue;
                				}

                				if(this.areItemsEqualForCrafting(recipeStack, invItem))
                				{
                					//TODO
                					inventory.setInventorySlotContents(n, invItem);
//                					ItemStack returnedStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
//                					if(returnedStack == null || returnedStack.getItem() == null || returnedStack.getItem() != returnStack.getItem())
//                					{
//                						continue;
//                					}
                					Map<Integer, Integer> slotMap = syphonMap.get(i);
                					if(slotMap == null)
                					{
                						slotMap = new HashMap();
                						syphonMap.put(i, slotMap);
                					}
                					
                					if(slotMap.containsKey(j))
                					{
                						int syphoned = slotMap.get(j);
                						if(invItem.stackSize - syphoned > 0)
                						{
                							slotMap.put(j, syphoned + 1);
                							isItemTaken = true;
                							break;
                						}
                					}else
                					{
                						slotMap.put(j, 1);
            							isItemTaken = true;
            							break;
                					}
                				}
                			}
                		}
                		
                		if(!isItemTaken)
                		{
                			tag.setBoolean("didLastCraftFail", true);
                			return;
                		}
                	}
                	
                	/* The recipe is valid and the items have been found */
                	                	
                	SpellHelper.insertStackIntoInventory(CraftingManager.getInstance().findMatchingRecipe(inventory, world), outputInv, ForgeDirection.DOWN);
                	
                	for(Entry<Integer, Map<Integer, Integer>> entry1 : syphonMap.entrySet())
                	{
                		IInventory inputInv = invList.get(entry1.getKey());
                		for(Entry<Integer, Integer> entry2 : entry1.getValue().entrySet())
                		{
                			ItemStack drainedStack = inputInv.getStackInSlot(entry2.getKey());
                			Item item = drainedStack.getItem();
                			if(item.hasContainerItem(drainedStack))
                			{
                				inputInv.setInventorySlotContents(entry2.getKey(), item.getContainerItem(drainedStack));
                			}else
                			{
                				drainedStack.stackSize -= entry2.getValue();
                    			if(drainedStack.stackSize <= 0)
                    			{
                    				inputInv.setInventorySlotContents(entry2.getKey(), null);
                    			}
                			}
                		}
                	}
                	
                	if(addOutputToInputs && syphonMap.containsKey(invList.size()))
                	{
                		this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                	}
                	                	
                    SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
                    
                    if(hasPotentia)
                    {
                    	this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                    }
                    
                    world.markBlockForUpdate(x, y-1, z + 2);
                    world.markBlockForUpdate(x, y-1, z - 2);
                    world.markBlockForUpdate(x + 2, y-1, z);
                    world.markBlockForUpdate(x - 2, y-1, z);
                    
//                    long endTime = System.nanoTime();
//
//                	long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
//                	System.out.println("(Total) method time in ms: " + (float)(duration)/1000000.0);
                }
            }  
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 10;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> autoCraftingRitual = new ArrayList();

        this.addCornerRunes(autoCraftingRitual, 1, 1, RitualComponent.EARTH);
        this.addParallelRunes(autoCraftingRitual, 1, 1, RitualComponent.EARTH);
        autoCraftingRitual.add(new RitualComponent(0, 1, 0, RitualComponent.AIR));
        this.addOffsetRunes(autoCraftingRitual, 1, 2, -1, RitualComponent.FIRE);
        this.addCornerRunes(autoCraftingRitual, 1, -1, RitualComponent.FIRE);
        autoCraftingRitual.add(new RitualComponent(-1, -1, 0, RitualComponent.EARTH));
        autoCraftingRitual.add(new RitualComponent(1, -1, 0, RitualComponent.EARTH));
        autoCraftingRitual.add(new RitualComponent(0, -1, -1, RitualComponent.EARTH));
        autoCraftingRitual.add(new RitualComponent(0, -1, 0, RitualComponent.FIRE));
        autoCraftingRitual.add(new RitualComponent(0, -1, 1, RitualComponent.WATER));
        
        return autoCraftingRitual;
    }
    
    public boolean areItemsEqualForCrafting(ItemStack stack1, ItemStack stack2)
    {
    	if (stack1 == null || stack2 == null)
        {
            return false;
        }
//
//        if (stack1.isItemStackDamageable() ^ stack2.isItemStackDamageable())
//        {
//            return false;
//        }

        return stack1.getItem() == stack2.getItem() && !stack1.getItem().getHasSubtypes() || stack1.getItemDamage() == stack2.getItemDamage();
    }

    public boolean areItemStacksEqualWithWildcard(ItemStack recipeStack, ItemStack comparedStack)
    {
        return recipeStack.isItemEqual(comparedStack) || (recipeStack.getItemDamage() == OreDictionary.WILDCARD_VALUE && recipeStack.getItem() == comparedStack.getItem());
    }
    
    public Int3 getSlotPositionForDirection(int slot, int direction)
    {
    	int x = slot % 3 - 1;
    	int z = slot / 3 - 1;
    	switch(direction)
    	{
    	case 1: //NORTH-facing
    		return new Int3(x, 2, z);
    	case 2: //EAST-facing
    		return new Int3(z, 2, -x);
    	case 3: //SOUTH-facing
    		return new Int3(-x, 2, -z);
    	case 4: //WEST-facing
    		return new Int3(-z, 2, x);
    	}
    	return new Int3(0,0,0);
    }
}
