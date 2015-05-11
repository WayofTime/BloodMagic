package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;

public class RitualEffectAutoAlchemy extends RitualEffect
{
    public static final boolean fillToOne = true;

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

        if (currentEssence < this.getCostPerRefresh() * 6)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

            int flag = 0;

            TileEntity topEntity = world.getTileEntity(x, y + 1, z);
            if (!(topEntity instanceof IBloodAltar) || !(topEntity instanceof IBloodAltar))
            {
                return;
            }

            IBloodAltar altar = (IBloodAltar) topEntity;
            ItemStack targetStack = ((IInventory)altar).getStackInSlot(0);
            if (targetStack == null)
            {
                return;
            }

            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(targetStack);
            if (recipe != null)
            {
                TEWritingTable alchemyEntity;
                IInventory outputInv = null;
                IInventory inputInv1 = null;
                IInventory inputInv2 = null;

                TileEntity northEntity = world.getTileEntity(x, y, z - 1);
                TileEntity southEntity = world.getTileEntity(x, y, z + 1);
                TileEntity eastEntity = world.getTileEntity(x + 1, y, z);
                TileEntity westEntity = world.getTileEntity(x - 1, y, z);

                if (northEntity instanceof TEWritingTable)
                {
                    alchemyEntity = (TEWritingTable) northEntity;
                    if (southEntity instanceof IInventory && !(southEntity instanceof TEWritingTable))
                    {
                        outputInv = (IInventory) southEntity;
                    }
                    if (eastEntity instanceof IInventory && !(eastEntity instanceof TEWritingTable))
                    {
                        inputInv1 = (IInventory) eastEntity;
                    }
                    if (westEntity instanceof IInventory && !(westEntity instanceof TEWritingTable))
                    {
                        inputInv2 = (IInventory) westEntity;
                    }
                } else if (southEntity instanceof TEWritingTable)
                {
                    alchemyEntity = (TEWritingTable) southEntity;
                    if (northEntity instanceof IInventory && !(northEntity instanceof TEWritingTable))
                    {
                        outputInv = (IInventory) northEntity;
                    }
                    if (eastEntity instanceof IInventory && !(eastEntity instanceof TEWritingTable))
                    {
                        inputInv1 = (IInventory) eastEntity;
                    }
                    if (westEntity instanceof IInventory && !(westEntity instanceof TEWritingTable))
                    {
                        inputInv2 = (IInventory) westEntity;
                    }
                } else if (eastEntity instanceof TEWritingTable)
                {
                    alchemyEntity = (TEWritingTable) eastEntity;
                    if (westEntity instanceof IInventory && !(westEntity instanceof TEWritingTable))
                    {
                        outputInv = (IInventory) westEntity;
                    }
                    if (northEntity instanceof IInventory && !(northEntity instanceof TEWritingTable))
                    {
                        inputInv1 = (IInventory) northEntity;
                    }
                    if (southEntity instanceof IInventory && !(southEntity instanceof TEWritingTable))
                    {
                        inputInv2 = (IInventory) southEntity;
                    }
                } else if (westEntity instanceof TEWritingTable)
                {
                    alchemyEntity = (TEWritingTable) westEntity;
                    if (eastEntity instanceof IInventory && !(eastEntity instanceof TEWritingTable))
                    {
                        outputInv = (IInventory) eastEntity;
                    }
                    if (northEntity instanceof IInventory && !(northEntity instanceof TEWritingTable))
                    {
                        inputInv1 = (IInventory) northEntity;
                    }
                    if (southEntity instanceof IInventory && !(southEntity instanceof TEWritingTable))
                    {
                        inputInv2 = (IInventory) southEntity;
                    }
                } else
                {
                    return;
                }

                if (alchemyEntity != null && hasPotentia)
                {
                    alchemyEntity.setAccelerationTime(5);
                    if (alchemyEntity.isWorking())
                    {
                        this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                    }
                }

                if (outputInv != null)
                {
                    ItemStack outputStack = alchemyEntity.getStackInSlot(6);
                    if (outputStack != null)
                    {
                        for (int i = 0; i < outputInv.getSizeInventory(); i++)
                        {
                            ItemStack curStack = outputInv.getStackInSlot(i);
                            if (curStack == null)
                            {
                                ItemStack copyStack = outputStack.copy();
                                copyStack.stackSize = 1;

                                outputStack.stackSize--;
                                if (outputStack.stackSize <= 0)
                                {
                                    alchemyEntity.setInventorySlotContents(6, null);
                                } else
                                {
                                    alchemyEntity.setInventorySlotContents(6, outputStack);
                                }

                                outputInv.setInventorySlotContents(i, copyStack);
                                flag++;
                                break;
                            } else if (curStack.isItemEqual(outputStack) && curStack.stackSize < curStack.getMaxStackSize() && ItemStack.areItemStackTagsEqual(outputStack, curStack))
                            {
                                outputStack.stackSize--;
                                if (outputStack.stackSize <= 0)
                                {
                                    alchemyEntity.setInventorySlotContents(6, null);
                                } else
                                {
                                    alchemyEntity.setInventorySlotContents(6, outputStack);
                                }

                                curStack.stackSize++;
                                outputInv.setInventorySlotContents(i, curStack);
                                flag++;
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < 5; i++)
                    {
                        ItemStack recItem;
                        if (recipe.length <= i)
                        {
                            recItem = null;
                        } else
                        {
                            recItem = recipe[i];
                        }

                        ItemStack alchStack = alchemyEntity.getStackInSlot(i + 1);
                        if ((recItem == null && alchStack != null) || (alchStack != null && !(areItemStacksEqualWithWildcard(recItem, alchStack))))
                        {
                            for (int j = 0; j < outputInv.getSizeInventory(); j++)
                            {
                                ItemStack curStack = outputInv.getStackInSlot(j);
                                if (curStack == null)
                                {
                                    ItemStack copyStack = alchStack.copy();
                                    copyStack.stackSize = 1;

                                    alchStack.stackSize--;
                                    if (alchStack.stackSize <= 0)
                                    {
                                        alchemyEntity.setInventorySlotContents(i + 1, null);
                                    } else
                                    {
                                        alchemyEntity.setInventorySlotContents(i + 1, alchStack);
                                    }

                                    outputInv.setInventorySlotContents(j, copyStack);
                                    flag++;
                                    break;
                                } else if (curStack.isItemEqual(alchStack) && curStack.stackSize < curStack.getMaxStackSize() && ItemStack.areItemStackTagsEqual(alchStack, curStack))
                                {
                                    alchStack.stackSize--;
                                    if (alchStack.stackSize <= 0)
                                    {
                                        alchemyEntity.setInventorySlotContents(i + 1, null);
                                    } else
                                    {
                                        alchemyEntity.setInventorySlotContents(i + 1, alchStack);
                                    }

                                    curStack.stackSize++;
                                    outputInv.setInventorySlotContents(j, curStack);
                                    flag++;
                                    break;
                                }
                            }
                            continue;
                        }
                    }

                }

                if (world.getWorldTime() % 10 == 0)
                {
                    if (flag == 0 && inputInv1 != null)
                    {
                        for (int i = 0; i < recipe.length; i++)
                        {
                            ItemStack recItem = recipe[i];
                            if (recItem == null)
                            {
                                continue;
                            }
                            ItemStack alchStack = alchemyEntity.getStackInSlot(i + 1);

                            if (alchStack != null && ((!areItemStacksEqualWithWildcard(recItem, alchStack)) || alchStack.stackSize >= (fillToOne ? 1 : alchStack.getMaxStackSize())))
                            {
                                continue;
                            }

                            for (int j = 0; j < inputInv1.getSizeInventory(); j++)
                            {
                                ItemStack curItem = inputInv1.getStackInSlot(j);
                                if (curItem == null)
                                {
                                    continue;
                                }
                                
                            	if(!inputInv1.isItemValidForSlot(j, curItem) || (inputInv1 instanceof ISidedInventory && !((ISidedInventory) inputInv1).canExtractItem(j, curItem, ForgeDirection.DOWN.ordinal())))
                            	{
                            		continue;
                            	}
                                
                                if (areItemStacksEqualWithWildcard(recItem, curItem))
                                {
                                    if (alchStack == null)
                                    {
                                        ItemStack copyStack = recItem.copy();
                                        copyStack.stackSize = 1;
                                        alchemyEntity.setInventorySlotContents(i + 1, copyStack);

                                        curItem.stackSize--;
                                        if (curItem.stackSize <= 0)
                                        {
                                            inputInv1.setInventorySlotContents(j, null);
                                        } else
                                        {
                                            inputInv1.setInventorySlotContents(j, curItem);
                                        }

                                        flag++;
                                        break;

                                    } else
                                    {
                                        alchStack.stackSize++;
                                        alchemyEntity.setInventorySlotContents(i + 1, alchStack);

                                        curItem.stackSize--;
                                        if (curItem.stackSize <= 0)
                                        {
                                            inputInv1.setInventorySlotContents(j, null);
                                        } else
                                        {
                                            inputInv1.setInventorySlotContents(j, curItem);
                                        }

                                        flag++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (flag == 0 && inputInv2 != null)
                    {
                        for (int i = 0; i < recipe.length; i++)
                        {
                            ItemStack recItem = recipe[i];
                            if (recItem == null)
                            {
                                continue;
                            }
                            ItemStack alchStack = alchemyEntity.getStackInSlot(i + 1);
                            if (alchStack != null && ((!areItemStacksEqualWithWildcard(recItem, alchStack)) || alchStack.stackSize >= (fillToOne ? 1 : alchStack.getMaxStackSize())))
                            {
                                continue;
                            }

                            for (int j = 0; j < inputInv2.getSizeInventory(); j++)
                            {
                                ItemStack curItem = inputInv2.getStackInSlot(j);
                                if (curItem == null)
                                {
                                    continue;
                                }
                                if (areItemStacksEqualWithWildcard(recItem, curItem))
                                {
                                    if (alchStack == null)
                                    {
                                        ItemStack copyStack = recItem.copy();
                                        copyStack.stackSize = 1;
                                        alchemyEntity.setInventorySlotContents(i + 1, copyStack);

                                        curItem.stackSize--;
                                        if (curItem.stackSize <= 0)
                                        {
                                            inputInv2.setInventorySlotContents(j, null);
                                        } else
                                        {
                                            inputInv2.setInventorySlotContents(j, curItem);
                                        }

                                        flag++;
                                        break;

                                    } else
                                    {
                                        alchStack.stackSize++;
                                        alchemyEntity.setInventorySlotContents(i + 1, alchStack);

                                        curItem.stackSize--;
                                        if (curItem.stackSize <= 0)
                                        {
                                            inputInv2.setInventorySlotContents(j, null);
                                        } else
                                        {
                                            inputInv2.setInventorySlotContents(j, curItem);
                                        }

                                        flag++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (flag > 0)
            {
                world.markBlockForUpdate(x, y, z + 1);
                world.markBlockForUpdate(x, y, z - 1);
                world.markBlockForUpdate(x + 1, y, z);
                world.markBlockForUpdate(x - 1, y, z);
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * flag);
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
        ArrayList<RitualComponent> autoAlchemyRitual = new ArrayList();
        autoAlchemyRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));
        autoAlchemyRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        autoAlchemyRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        autoAlchemyRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        autoAlchemyRitual.add(new RitualComponent(2, 0, 2, RitualComponent.WATER));
        autoAlchemyRitual.add(new RitualComponent(2, 0, -2, RitualComponent.WATER));
        autoAlchemyRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.WATER));
        autoAlchemyRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.WATER));
        autoAlchemyRitual.add(new RitualComponent(-3, 0, -2, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(-2, 0, -3, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(-3, 0, 2, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(-2, 0, 3, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(3, 0, -2, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(2, 0, -3, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(3, 0, 2, RitualComponent.FIRE));
        autoAlchemyRitual.add(new RitualComponent(2, 0, 3, RitualComponent.FIRE));
        return autoAlchemyRitual;
    }

    public boolean areItemStacksEqualWithWildcard(ItemStack recipeStack, ItemStack comparedStack)
    {
        return recipeStack.isItemEqual(comparedStack) || (recipeStack.getItemDamage() == OreDictionary.WILDCARD_VALUE && recipeStack.getItem() == comparedStack.getItem());
    }
}
