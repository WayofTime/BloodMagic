package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.RoutingFocusParadigm;
import WayofTime.alchemicalWizardry.api.RoutingFocusPosAndFacing;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.routing.InputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.routing.OutputRoutingFocus;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectItemRouting extends RitualEffect
{
	Random rand = new Random();
	
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }
                
        int xBufOffset = 0;
        int yBufOffset = 1;
        int zBufOffset = 0;
        
        TileEntity bufferTile = world.getTileEntity(x + xBufOffset, y + yBufOffset, z + zBufOffset);
        
        if(!(bufferTile instanceof IInventory))
        {
        	return;
        }
        
        Map<Int3, IInventory> tileMap = new HashMap();
        
        IInventory bufferInventory = (IInventory)bufferTile;
        
        List<IInventory> outputList = new ArrayList();
        for(int i=0; i<4; i++) //Check output foci chests, return if none available
        {
        	Int3 outputFocusChest = this.getOutputBufferChestLocation(i);
        	TileEntity outputFocusInv = world.getTileEntity(x + outputFocusChest.xCoord, y + outputFocusChest.yCoord, z + outputFocusChest.zCoord);
        	if(outputFocusInv instanceof IInventory)
        	{
        		outputList.add((IInventory)outputFocusInv);
        	}
        }
        
        if(outputList.isEmpty())
        {
        	return;
        }
        
        for(int i=0; i<4; i++)
        {
        	Int3 inputFocusChest = this.getInputBufferChestLocation(i);
        	TileEntity inputFocusInv = world.getTileEntity(x + inputFocusChest.xCoord, y + inputFocusChest.yCoord, z + inputFocusChest.zCoord);
        	if(inputFocusInv instanceof IInventory)
        	{
        		for(int ji=0; ji<((IInventory) inputFocusInv).getSizeInventory(); ji++) //Iterate through foci inventory
        		{
        			ItemStack inputFocusStack = ((IInventory) inputFocusInv).getStackInSlot(ji);
        			if(inputFocusStack != null && inputFocusStack.getItem() instanceof InputRoutingFocus)
        			{
        				InputRoutingFocus inputFocus = (InputRoutingFocus)inputFocusStack.getItem();
        				TileEntity inputChest = world.getTileEntity(inputFocus.xCoord(inputFocusStack), inputFocus.yCoord(inputFocusStack), inputFocus.zCoord(inputFocusStack));
        				if(inputChest instanceof IInventory)
        				{
        					IInventory inputChestInventory = (IInventory)inputChest;
        					ForgeDirection syphonDirection = inputFocus.getSetDirection(inputFocusStack);
        					boolean[] canSyphonList = new boolean[inputChestInventory.getSizeInventory()];
        					if(inputChest instanceof ISidedInventory)
        					{
        						int[] validSlots = ((ISidedInventory) inputChest).getAccessibleSlotsFromSide(syphonDirection.ordinal());
        						for(int in : validSlots)
        						{
        							canSyphonList[in] = true;
        						}
        					}else
        					{
        						for(int ni=0; ni<inputChestInventory.getSizeInventory(); ni++)
        						{
        							canSyphonList[ni] = true;
        						}
        					}
        					
        					for(int ni=0; ni<inputChestInventory.getSizeInventory(); ni++)
        					{
        						if(canSyphonList[ni])
        						{
        							ItemStack syphonedStack = inputChestInventory.getStackInSlot(ni); //Has a syphoned item linked, next need to find a destination
        							if(syphonedStack == null)
        							{
        								continue;
        							}
        							        							
        							int size = syphonedStack.stackSize;
        							
        							for(IInventory outputFocusInventory : outputList)
        							{
        								ItemStack stack = outputFocusInventory.getStackInSlot(0);
        				    			if(stack != null && stack.getItem() instanceof OutputRoutingFocus) //TODO change to output routing focus
        				    			{
        				    				boolean transferEverything = true;
        				    				for(int j=1; j<outputFocusInventory.getSizeInventory(); j++)
        				    				{
        				    					if(outputFocusInventory.getStackInSlot(j) != null)
        				    					{
        				    						transferEverything = false;
        				    						break;
        				    					}
        				    				}
        				    				
        				    				OutputRoutingFocus outputFocus = (OutputRoutingFocus)stack.getItem();
        				    				
        				    				RoutingFocusParadigm parad = new RoutingFocusParadigm();
        				    				parad.addRoutingFocusPosAndFacing(outputFocus.getPosAndFacing(stack));
        				    				parad.addLogic(outputFocus.getLogic(stack.getItemDamage()));
        				    				
        				    				Int3 outputChestPos = new Int3(outputFocus.xCoord(stack), outputFocus.yCoord(stack), outputFocus.zCoord(stack));
        				    				TileEntity outputChest; //Destination
        				    				if(tileMap.containsKey(outputChestPos))
        				    				{
        				    					outputChest = (TileEntity) tileMap.get(outputChestPos);
        				    				}else
        				    				{
        				    					outputChest = world.getTileEntity(outputFocus.xCoord(stack), outputFocus.yCoord(stack), outputFocus.zCoord(stack));
        				    					if(outputChest instanceof IInventory)
        				    					{
        				    						tileMap.put(outputChestPos, (IInventory)outputChest);
        				    					}
        				    				}
        									ForgeDirection inputDirection = outputFocus.getSetDirection(stack);
        				    				
//        				    				if(transferEverything)
//        				    				{
//        				        				if(outputChest instanceof IInventory)
//        				        				{
//        				        					IInventory outputChestInventory = (IInventory)outputChest;
//        				        					
//        				        					for(int n=0; n<bufferInventory.getSizeInventory(); n++)
//        				        					{
//        				    							ItemStack syphonedStack = bufferInventory.getStackInSlot(n);
//        				    							if(syphonedStack == null)
//        				    							{
//        				    								continue;
//        				    							}
//        				    							int size = syphonedStack.stackSize;
//        				    							ItemStack newStack = SpellHelper.insertStackIntoInventory(syphonedStack, outputChestInventory, inputDirection);
//        				    							if(size == newStack.stackSize)
//        												{
//        													continue;
//        												}
//        				    							if(newStack != null && newStack.stackSize <= 0)
//        				    							{
//        				    								newStack = null;
//        				    							}
//        				    							bufferInventory.setInventorySlotContents(n, newStack);
////        				        						break;
//        				        					}
//        				        				}
//        				    				}else
        				    				{
        				    					if(!(outputChest instanceof IInventory))
        				    					{
        				    						continue;
        				    					}
        				    					
        				    					IInventory outputChestInventory = (IInventory)outputChest;
        				    					
        				    					boolean lastItemWasFocus = true;
        				    					
        				    					for(int j=1; j<outputFocusInventory.getSizeInventory(); j++)
        				    					{
        				    						ItemStack keyStack = outputFocusInventory.getStackInSlot(j);
        				    						if(keyStack == null)
        				    						{
        				    							continue;	
        				    						}
        				    						
        				    						if(keyStack.getItem() instanceof OutputRoutingFocus)
        				    						{
        				    							if(!lastItemWasFocus)
        				    							{    							
        				    								parad.clear();
        				    							}
        				    							
        				    							outputFocus = (OutputRoutingFocus)keyStack.getItem();
        				    							
        				    							parad.addRoutingFocusPosAndFacing(outputFocus.getPosAndFacing(keyStack));
        				    							parad.addLogic(outputFocus.getLogic(keyStack.getItemDamage()));
        				    							lastItemWasFocus = true;
        				    							continue;
        				    						}else
        				    						{
        				    							lastItemWasFocus = false;
        				    						}
        				    						
        				    						for(RoutingFocusPosAndFacing posAndFacing : parad.locationList)
        				    						{
        				    							if(posAndFacing == null)
        				    							{
        				    								continue;
        				    							}
        				    							inputDirection = posAndFacing.facing;
        				    							if(outputChest == null || !posAndFacing.location.equals(new Int3(outputChest.xCoord, outputChest.yCoord, outputChest.zCoord)))
        				    							{
        				    								outputChestPos = new Int3(outputChest.xCoord, outputChest.yCoord, outputChest.zCoord);
        				    								if(tileMap.containsKey(outputChestPos))
        				    								{
        				    									outputChest = (TileEntity) tileMap.get(outputChestPos);
        				    								}else
        				    								{
            				    								outputChest = world.getTileEntity(posAndFacing.location.xCoord, posAndFacing.location.yCoord, posAndFacing.location.zCoord);
            				    								if(outputChest instanceof IInventory)
            				    								{
            				    									tileMap.put(outputChestPos, (IInventory)outputChest);
            				    								}
        				    								}
        				    								if(outputChest instanceof IInventory)
        				    								{
        				    									outputChestInventory = (IInventory)outputChest;
        				    								}else
        				    								{
        				    									continue;
        				    								}
        				    							}
        				    								
    				    								if(parad.doesItemMatch(keyStack, syphonedStack))
    				    								{
    				    									ItemStack newStack = SpellHelper.insertStackIntoInventory(syphonedStack, outputChestInventory, inputDirection);
    				    									if(size == newStack.stackSize)
    				    									{
    				    										continue;
    				    									}
    				    									
    				            							if(newStack != null && newStack.stackSize <= 0)
    				            							{
        				            							size = newStack.stackSize;
    				            								newStack = null;
    				            							}
    				            							inputChestInventory.setInventorySlotContents(ni, newStack);
//        				                					break;
    				    								}
        				    						}
        				    					}
        				    				}
        				    			}
        				        	
        							}
        							
//        							ItemStack newStack = SpellHelper.insertStackIntoInventory(syphonedStack, bufferInventory, ForgeDirection.DOWN);
//        							if(size == newStack.stackSize)
//									{
//										continue;
//									}
//        							if(newStack != null && newStack.stackSize <= 0)
//        							{
//        								newStack = null;
//        							}
//        							inputChestInventory.setInventorySlotContents(n, newStack);
//        							break;
        						}
        					}
        				}
        			}
        		}
        	}
        }
        
//        for(int i=0; i<4; i++)
//        {
//        	Int3 outputFocusChest = this.getOutputBufferChestLocation(i);
//        	TileEntity outputFocusInv = world.getTileEntity(x + outputFocusChest.xCoord, y + outputFocusChest.yCoord, z + outputFocusChest.zCoord);
//        	if(outputFocusInv instanceof IInventory)
//        	{
//        		IInventory outputFocusInventory = (IInventory)outputFocusInv;
//    			ItemStack stack = outputFocusInventory.getStackInSlot(0);
//    			if(stack != null && stack.getItem() instanceof OutputRoutingFocus) //TODO change to output routing focus
//    			{
//    				boolean transferEverything = true;
//    				for(int j=1; j<outputFocusInventory.getSizeInventory(); j++)
//    				{
//    					if(outputFocusInventory.getStackInSlot(j) != null)
//    					{
//    						transferEverything = false;
//    						break;
//    					}
//    				}
//    				
//    				OutputRoutingFocus outputFocus = (OutputRoutingFocus)stack.getItem();
//    				
//    				RoutingFocusParadigm parad = new RoutingFocusParadigm();
//    				parad.addRoutingFocusPosAndFacing(outputFocus.getPosAndFacing(stack));
//    				parad.addLogic(outputFocus.getLogic(stack.getItemDamage()));
//    				
//    				TileEntity outputChest = world.getTileEntity(outputFocus.xCoord(stack), outputFocus.yCoord(stack), outputFocus.zCoord(stack)); //Destination
//					ForgeDirection inputDirection = outputFocus.getSetDirection(stack);
//    				
//    				if(transferEverything)
//    				{
//        				if(outputChest instanceof IInventory)
//        				{
//        					IInventory outputChestInventory = (IInventory)outputChest;
//        					
//        					for(int n=0; n<bufferInventory.getSizeInventory(); n++)
//        					{
//    							ItemStack syphonedStack = bufferInventory.getStackInSlot(n);
//    							if(syphonedStack == null)
//    							{
//    								continue;
//    							}
//    							int size = syphonedStack.stackSize;
//    							ItemStack newStack = SpellHelper.insertStackIntoInventory(syphonedStack, outputChestInventory, inputDirection);
//    							if(size == newStack.stackSize)
//								{
//									continue;
//								}
//    							if(newStack != null && newStack.stackSize <= 0)
//    							{
//    								newStack = null;
//    							}
//    							bufferInventory.setInventorySlotContents(n, newStack);
////        						break;
//        					}
//        				}
//    				}else
//    				{
//    					if(!(outputChest instanceof IInventory))
//    					{
//    						continue;
//    					}
//    					
//    					IInventory outputChestInventory = (IInventory)outputChest;
//    					
//    					boolean lastItemWasFocus = true;
//    					
//    					for(int j=1; j<outputFocusInventory.getSizeInventory(); j++)
//    					{
//    						ItemStack keyStack = outputFocusInventory.getStackInSlot(j);
//    						if(keyStack == null)
//    						{
//    							continue;	
//    						}
//    						
//    						if(keyStack.getItem() instanceof OutputRoutingFocus)
//    						{
//    							if(!lastItemWasFocus)
//    							{    							
//    								parad.clear();
//    							}
//    							
//    							outputFocus = (OutputRoutingFocus)keyStack.getItem();
//    							
//    							parad.addRoutingFocusPosAndFacing(outputFocus.getPosAndFacing(keyStack));
//    							parad.addLogic(outputFocus.getLogic(keyStack.getItemDamage()));
//    							lastItemWasFocus = true;
//    							continue;
//    						}else
//    						{
//    							lastItemWasFocus = false;
//    						}
//    						
//    						for(RoutingFocusPosAndFacing posAndFacing : parad.locationList)
//    						{
//    							if(posAndFacing == null)
//    							{
//    								continue;
//    							}
//    							inputDirection = posAndFacing.facing;
//    							if(outputChest == null || !posAndFacing.location.equals(new Int3(outputChest.xCoord, outputChest.yCoord, outputChest.zCoord)))
//    							{
//    								outputChest = world.getTileEntity(posAndFacing.location.xCoord, posAndFacing.location.yCoord, posAndFacing.location.zCoord);
//    								if(outputChest instanceof IInventory)
//    								{
//    									outputChestInventory = (IInventory)outputChest;
//    								}else
//    								{
//    									continue;
//    								}
//    							}
//    							
//    							for(int n=0; n<bufferInventory.getSizeInventory(); n++)
//    							{
//    								ItemStack checkStack = bufferInventory.getStackInSlot(n);
//    								if(checkStack == null)
//    								{
//    									continue;
//    								}
//    								
//    								if(parad.doesItemMatch(keyStack, checkStack))
//    								{
//    									int size = checkStack.stackSize;
//    									ItemStack newStack = SpellHelper.insertStackIntoInventory(checkStack, outputChestInventory, inputDirection);
//    									if(size == newStack.stackSize)
//    									{
//    										continue;
//    									}
//            							if(newStack != null && newStack.stackSize <= 0)
//            							{
//            								newStack = null;
//            							}
//            							bufferInventory.setInventorySlotContents(n, newStack);
////                						break;
//    								}
//    							}
//    						}
//    					}
//    				}
//    			}
//        	}
//        }
    }
    
    public Int3 getInputBufferChestLocation(int number)
    {
    	switch(number)
    	{
    	case 0:
    		return new Int3(1, 0, 0);
    	case 1:
    		return new Int3(-1, 0, 0);
    	case 2:
    		return new Int3(0, 0, 1);
    	case 3:
    		return new Int3(0, 0, -1);
    	}
    	return new Int3(0, 0, 0);
    }
    
    public Int3 getOutputBufferChestLocation(int number)
    {
    	switch(number)
    	{
    	case 0:
    		return new Int3(2, 0, 0);
    	case 1:
    		return new Int3(-2, 0, 0);
    	case 2:
    		return new Int3(0, 0, 2);
    	case 3:
    		return new Int3(0, 0, -2);
    	}
    	return new Int3(0, 0, 0);
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> omegaRitual = new ArrayList();
        
        this.addCornerRunes(omegaRitual, 1, 0, RitualComponent.BLANK);
        this.addOffsetRunes(omegaRitual, 2, 1, 0, RitualComponent.FIRE);
        this.addParallelRunes(omegaRitual, 4, 0, RitualComponent.WATER);
        this.addParallelRunes(omegaRitual, 5, 0, RitualComponent.EARTH);
        this.addCornerRunes(omegaRitual, 4, 0, RitualComponent.WATER);

        return omegaRitual;
    }
}
