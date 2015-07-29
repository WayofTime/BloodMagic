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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }
                
        Map<Int3, IInventory> tileMap = new HashMap();
                
        List<IInventory> outputList = new ArrayList();
        for(int i=0; i<4; i++) //Check output foci chests, return if none available
        {
        	Int3 outputFocusChest = this.getOutputBufferChestLocation(i);
        	BlockPos newPos = pos.add(outputFocusChest.xCoord, outputFocusChest.yCoord, outputFocusChest.zCoord);
        	TileEntity outputFocusInv = world.getTileEntity(newPos);
        	if(outputFocusInv instanceof IInventory)
        	{
        		outputList.add((IInventory)outputFocusInv);
        	}
        }
        
        if(outputList.isEmpty())
        {
        	return;
        }
        
        for(IInventory outputFocusInventory : outputList)
		{
			{        				    				
				OutputRoutingFocus outputFocus;;
				
				RoutingFocusParadigm parad = new RoutingFocusParadigm();
				
				TileEntity outputChest = null; //Destination
				EnumFacing inputDirection;
				
				{        				    					
					IInventory outputChestInventory = null;
					
					boolean lastItemWasFocus = true;
					
					for(int j=0; j<outputFocusInventory.getSizeInventory(); j++)
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
							parad.addLogic(outputFocus.getLogic(keyStack));
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
							if(outputChest == null || !posAndFacing.location.equals(new Int3(outputChest.getPos())))
							{
								outputChest = world.getTileEntity(new BlockPos(posAndFacing.location.xCoord, posAndFacing.location.yCoord, posAndFacing.location.zCoord));
								if(outputChest instanceof IInventory)
								{
									outputChestInventory = (IInventory)outputChest;
								}else
								{
									continue;
								}
							}
        
					        for(int i=0; i<4; i++)
					        {
					        	Int3 inputFocusChest = this.getInputBufferChestLocation(i);
					        	TileEntity inputFocusInv = world.getTileEntity(pos.add(inputFocusChest.xCoord, inputFocusChest.yCoord, inputFocusChest.zCoord));
					        	if(inputFocusInv instanceof IInventory)
					        	{
					        		for(int ji=0; ji<((IInventory) inputFocusInv).getSizeInventory(); ji++) //Iterate through foci inventory
					        		{
					        			ItemStack inputFocusStack = ((IInventory) inputFocusInv).getStackInSlot(ji);
					        			if(inputFocusStack != null && inputFocusStack.getItem() instanceof InputRoutingFocus)
					        			{
					        				InputRoutingFocus inputFocus = (InputRoutingFocus)inputFocusStack.getItem();
					        				TileEntity inputChest = world.getTileEntity(new BlockPos(inputFocus.xCoord(inputFocusStack), inputFocus.yCoord(inputFocusStack), inputFocus.zCoord(inputFocusStack)));
					        				if(inputChest instanceof IInventory)
					        				{
					        					IInventory inputChestInventory = (IInventory)inputChest;
					        					EnumFacing syphonDirection = inputFocus.getSetDirection(inputFocusStack);
					        					boolean[] canSyphonList = new boolean[inputChestInventory.getSizeInventory()];
					        					if(inputChest instanceof ISidedInventory)
					        					{
					        						int[] validSlots = ((ISidedInventory) inputChest).getSlotsForFace(syphonDirection);
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
					        							if(syphonedStack == null || (inputChestInventory instanceof ISidedInventory && !((ISidedInventory)inputChestInventory).canExtractItem(ni, syphonedStack, syphonDirection)))
					        							{
					        								continue;
					        							}
					        							        							
					        							int size = syphonedStack.stackSize;
        				    								
    				    								if(parad.doesItemMatch(keyStack, syphonedStack))
    				    								{
    				    									ItemStack newStack = null;
    				    									if(parad.maximumAmount <= 0)
    				    									{
        				    									newStack = SpellHelper.insertStackIntoInventory(syphonedStack, outputChestInventory, inputDirection);
    				    									}else
    				    									{
    				    										newStack = SpellHelper.insertStackIntoInventory(syphonedStack, outputChestInventory, inputDirection, parad.maximumAmount);
    				    									}
    				    									if(size == newStack.stackSize)
    				    									{
    				    										continue;
    				    									}
    				    									
    				    									int numberSyphoned = size - newStack.stackSize;
    				    									
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
        						}
        					}
        				}
        			}
        		}
        	}
        }
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
    		return new Int3(2, 0, 2);
    	case 1:
    		return new Int3(-2, 0, 2);
    	case 2:
    		return new Int3(2, 0, -2);
    	case 3:
    		return new Int3(-2, 0, -2);
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
