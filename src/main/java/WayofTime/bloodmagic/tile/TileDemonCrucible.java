package WayofTime.bloodmagic.tile;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;

public class TileDemonCrucible extends TileInventory implements ITickable, IDemonWillConduit, ISidedInventory
{
    public HashMap<EnumDemonWillType, Double> willMap = new HashMap<EnumDemonWillType, Double>(); //TODO: Change to DemonWillHolder
    public final int maxWill = 100;
    public final double gemDrainRate = 10;

    public int internalCounter = 0;

    public TileDemonCrucible()
    {
        super(1, "demonCrucible");
    }

    @Override
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        internalCounter++;

        if (worldObj.isBlockPowered(getPos()))
        {
            //TODO: Fill the contained gem if it is there.
            ItemStack stack = this.getStackInSlot(0);
            if (stack != null)
            {
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    IDemonWillGem gemItem = (IDemonWillGem) stack.getItem();
                    for (EnumDemonWillType type : EnumDemonWillType.values())
                    {
                        if (willMap.containsKey(type))
                        {
                            double current = willMap.get(type);
                            double fillAmount = Math.min(gemDrainRate, current);
                            if (fillAmount > 0)
                            {
                                fillAmount = gemItem.fillWill(type, stack, fillAmount);
                                if (willMap.get(type) - fillAmount <= 0)
                                {
                                    willMap.remove(type);
                                } else
                                {
                                    willMap.put(type, willMap.get(type) - fillAmount);
                                }
                            }
                        }
                    }
                }
            }
        } else
        {
            ItemStack stack = this.getStackInSlot(0);
            if (stack != null)
            {
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    IDemonWillGem gemItem = (IDemonWillGem) stack.getItem();
                    for (EnumDemonWillType type : EnumDemonWillType.values())
                    {
                        double currentAmount = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);
                        double drainAmount = Math.min(maxWill - currentAmount, gemDrainRate);
                        double filled = WorldDemonWillHandler.fillWillToMaximum(worldObj, pos, type, drainAmount, maxWill, false);
                        filled = gemItem.drainWill(type, stack, filled);
                        if (filled > 0)
                        {
                            WorldDemonWillHandler.fillWillToMaximum(worldObj, pos, type, filled, maxWill, true);
                        }
                    }
                } else if (stack.getItem() instanceof IDiscreteDemonWill) //TODO: Limit the speed of this process
                {
                    IDiscreteDemonWill willItem = (IDiscreteDemonWill) stack.getItem();
                    EnumDemonWillType type = willItem.getType(stack);
                    double currentAmount = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);
                    double needed = maxWill - currentAmount;
                    double discreteAmount = willItem.getDiscretization(stack);
                    if (needed >= discreteAmount)
                    {
                        double filled = willItem.drainWill(stack, discreteAmount);
                        if (filled > 0)
                        {
                            WorldDemonWillHandler.fillWillToMaximum(worldObj, pos, type, filled, maxWill, true);
                            if (stack.stackSize <= 0)
                            {
                                this.setInventorySlotContents(0, null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        willMap.clear();

        for (EnumDemonWillType type : EnumDemonWillType.values())
        {
            double amount = tag.getDouble("EnumWill" + type.getName());
            if (amount > 0)
            {
                willMap.put(type, amount);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        for (Entry<EnumDemonWillType, Double> entry : willMap.entrySet())
        {
            tag.setDouble("EnumWill" + entry.getKey().getName(), entry.getValue());
        }
    }

    // IDemonWillConduit

    @Override
    public int getWeight()
    {
        return 10;
    }

    @Override
    public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill)
    {
        if (amount <= 0)
        {
            return 0;
        }

        if (!canFill(type))
        {
            return 0;
        }

        if (!doFill)
        {
            if (!willMap.containsKey(type))
            {
                return Math.min(maxWill, amount);
            }

            return Math.min(maxWill - willMap.get(type), amount);
        }

        if (!willMap.containsKey(type))
        {
            double max = Math.min(maxWill, amount);

            willMap.put(type, max);

            return max;
        }

        double current = willMap.get(type);
        double filled = maxWill - current;

        if (amount < filled)
        {
            willMap.put(type, current + amount);
            filled = amount;
        } else
        {
            willMap.put(type, (double) maxWill);
        }

        return filled;
    }

    @Override
    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
    {
        if (!willMap.containsKey(type))
        {
            return 0;
        }

        double drained = amount;
        double current = willMap.get(type);
        if (current < drained)
        {
            drained = current;
        }

        if (doDrain)
        {
            current -= drained;
            if (current <= 0)
            {
                willMap.remove(type);
            } else
            {
                willMap.put(type, current);
            }
        }

        return drained;
    }

    @Override
    public boolean canFill(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public boolean canDrain(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public double getCurrentWill(EnumDemonWillType type)
    {
        return willMap.containsKey(type) ? willMap.get(type) : 0;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
    {
        return stack != null ? stack.getItem() instanceof IDemonWillGem || stack.getItem() instanceof IDiscreteDemonWill : false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }
}