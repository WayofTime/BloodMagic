package WayofTime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;

public class TileDemonCrucible extends TileInventory implements ITickable, IDemonWillConduit
{
    public AreaDescriptor checkArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public List<BlockPos> conduitList = new ArrayList<BlockPos>(); //Offset list

    public HashMap<EnumDemonWillType, Double> willMap = new HashMap<EnumDemonWillType, Double>();
    public final int maxWill = 100;
    public final double maxTransferPerTick = 1;
    public final double thresholdFill = 0.01;
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

        if (internalCounter % 100 == 0)
        {
            conduitList.clear();

            List<BlockPos> posList = checkArea.getContainedPositions(pos);
            for (BlockPos newPos : posList)
            {
                if (newPos.equals(pos))
                {
                    continue;
                }

                TileEntity tile = worldObj.getTileEntity(newPos);
                if (tile instanceof IDemonWillConduit)
                {
                    conduitList.add(newPos.subtract(getPos()));
                }
            }

            System.out.println("List size: " + conduitList.size());
            System.out.println("Current amount: " + getCurrentWill(EnumDemonWillType.DEFAULT));
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
                    if (willMap.containsKey(EnumDemonWillType.DEFAULT))
                    {
                        double current = willMap.get(EnumDemonWillType.DEFAULT);
                        double fillAmount = Math.min(gemDrainRate, Math.min(current, gemItem.getMaxWill(stack) - gemItem.getWill(stack)));
                        if (fillAmount > 0)
                        {
                            gemItem.setWill(stack, fillAmount + gemItem.getWill(stack));
                            if (willMap.get(EnumDemonWillType.DEFAULT) - fillAmount <= 0)
                            {
                                willMap.remove(EnumDemonWillType.DEFAULT);
                            } else
                            {
                                willMap.put(EnumDemonWillType.DEFAULT, willMap.get(EnumDemonWillType.DEFAULT) - fillAmount);
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
                    if (!willMap.containsKey(EnumDemonWillType.DEFAULT))
                    {
                        willMap.put(EnumDemonWillType.DEFAULT, 0d);
                    }

                    if (willMap.get(EnumDemonWillType.DEFAULT) < maxWill)
                    {
                        double drainAmount = Math.min(maxWill - willMap.get(EnumDemonWillType.DEFAULT), gemDrainRate);
                        double drained = gemItem.drainWill(stack, drainAmount);
                        willMap.put(EnumDemonWillType.DEFAULT, willMap.get(EnumDemonWillType.DEFAULT) + drained);
                    }
                }
            }

            double maxWeight = 0;
            List<IDemonWillConduit> tileList = new ArrayList<IDemonWillConduit>();

            Iterator<BlockPos> iterator = conduitList.iterator();
            while (iterator.hasNext())
            {
                BlockPos newPos = pos.add(iterator.next());
                TileEntity tile = worldObj.getTileEntity(newPos);
                if (tile instanceof IDemonWillConduit)
                {
                    maxWeight += ((IDemonWillConduit) tile).getWeight();
                    tileList.add((IDemonWillConduit) tile);
                } else
                {
                    iterator.remove();
                }
            }

            if (maxWeight > 0)
            {
                for (EnumDemonWillType type : EnumDemonWillType.values())
                {
                    double currentAmount = this.getCurrentWill(type);
                    if (currentAmount <= 0)
                    {
                        continue;
                    }

                    for (IDemonWillConduit conduit : tileList)
                    {
                        if (!conduit.canFill(type))
                        {
                            continue;
                        }

                        double transfer = Math.min(currentAmount, conduit.getWeight() * maxTransferPerTick / maxWeight);
                        if (transfer <= 0)
                        {
                            break;
                        }

                        double conduitAmount = conduit.getCurrentWill(type);

                        if (currentAmount - conduitAmount <= thresholdFill) // Will only fill if this conduit's amount is greater than the conduit it is filling.
                        {
                            continue;
                        }

                        transfer = conduit.fillDemonWill(type, transfer, false);
                        if (transfer > 0)
                        {
                            conduit.fillDemonWill(type, transfer, true);
                            currentAmount -= transfer;
                        }
                    }

                    if (currentAmount <= 0)
                    {
                        willMap.remove(type);
                    } else
                    {
                        willMap.put(type, currentAmount);
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

        NBTTagList tags = tag.getTagList(Constants.NBT.BLOCKPOS_CONNECTION, 10);
        for (int i = 0; i < tags.tagCount(); i++)
        {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            conduitList.add(newPos);
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

        NBTTagList tags = new NBTTagList();
        for (BlockPos pos : conduitList)
        {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }

        tag.setTag(Constants.NBT.BLOCKPOS_CONNECTION, tags);
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
}