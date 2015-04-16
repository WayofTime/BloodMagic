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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectCrafting extends RitualEffect
{
    public static final boolean isTesting = false;
    public static final boolean limitToSingleStack = false;
    public static final int potentiaDrain = 2;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if(world.getWorldTime() % 1 != 0)
        {
        	return;
        }
        
        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            int slotDesignation = 0;
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
            		TileEntity inv = world.getTileEntity(x + j, y + 2, z + i);
            		if(inv instanceof IInventory)
            		{
            			int gridSpace = (i+1)*3 + (j+1);
            			if(((IInventory) inv).getSizeInventory() < slotDesignation || !((IInventory) inv).isItemValidForSlot(slotDesignation, ((IInventory) inv).getStackInSlot(slotDesignation)))
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
            	return;
            }
            
            ItemStack returnStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
            
            if (returnStack != null)
            {
                IInventory outputInv = null;
                
                List<IInventory> invList = new ArrayList();

                TileEntity northEntity = world.getTileEntity(x, y-1, z - 2);
                TileEntity southEntity = world.getTileEntity(x, y-1, z + 2);
                TileEntity eastEntity = world.getTileEntity(x + 2, y-1, z);
                TileEntity westEntity = world.getTileEntity(x - 2, y-1, z);

                if(southEntity instanceof IInventory)
                {
                	outputInv = (IInventory)southEntity;
                }
                
                if(northEntity instanceof IInventory)
                {
                	invList.add((IInventory)northEntity);
                }

                if (outputInv != null && (!limitToSingleStack ? SpellHelper.canInsertStackFullyIntoInventory(returnStack, outputInv, ForgeDirection.DOWN) : SpellHelper.canInsertStackFullyIntoInventory(returnStack, outputInv, ForgeDirection.DOWN, true, returnStack.getMaxStackSize())))
                {
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
                					System.out.println("Item is equal and valid");
                					//TODO
                					inventory.setInventorySlotContents(n, invItem);
                					ItemStack returnedStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
                					if(returnedStack == null || returnedStack.getItem() == null || returnedStack.getItem() != returnStack.getItem())
                					{
                						continue;
                					}
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
//                			System.out.println("Item is not available!");
                			return;
                		}
                	}
                	
                	/* The recipe is valid and the items have been found */
                	
                	System.out.println("Valid!");
                	
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
                	                	
                    SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
                    
                    world.markBlockForUpdate(x, y-1, z + 2);
                    world.markBlockForUpdate(x, y-1, z - 2);
                    world.markBlockForUpdate(x + 2, y-1, z);
                    world.markBlockForUpdate(x - 2, y-1, z);
                }
            }  
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
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
    	if (stack1 == null)
        {
            return false;
        }

        if (stack2 == null)
        {
            return true;
        }

        if (stack1.isItemStackDamageable() ^ stack2.isItemStackDamageable())
        {
            return false;
        }

        return stack1.getItem() == stack2.getItem() && (stack1.getItem().getHasSubtypes() ? stack1.getItemDamage() == stack2.getItemDamage() : true);
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
    		return new Int3(-z, 2, x);
    	case 3: //SOUTH-facing
    		return new Int3(-x, 2, -z);
    	case 4: //WEST-facing
    		return new Int3(z, 2, -x);
    	}
    	return new Int3(0,0,0);
    }
}
