package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraft.nbt.NBTTagCompound;

public class ReagentContainer implements IReagentContainer
{
    protected ReagentStack reagentStack;
    protected int capacity;

    public ReagentContainer(int capacity)
    {
        this(null, capacity);
    }

    public ReagentContainer(ReagentStack stack, int capacity)
    {
        this.reagentStack = stack;
        this.capacity = capacity;
    }

    public ReagentContainer(Reagent reagent, int amount, int capacity)
    {
        this(new ReagentStack(reagent, amount), capacity);
    }

    public static ReagentContainer readFromNBT(NBTTagCompound nbt)
    {
        ReagentStack reagent = ReagentStack.loadReagentStackFromNBT(nbt);
        int capacity = nbt.getInteger("capacity");

        if (reagent != null)
        {
            return new ReagentContainer(reagent, capacity);
        } else
        {
            return new ReagentContainer(null, capacity);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (reagentStack != null)
        {
            reagentStack.writeToNBT(nbt);
        }

        nbt.setInteger("capacity", capacity);

        return nbt;
    }

    @Override
    public ReagentStack getReagent()
    {
        return reagentStack;
    }

    @Override
    public int getReagentStackAmount()
    {
        if (reagentStack == null)
        {
            return 0;
        }
        return reagentStack.amount;
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    @Override
    public int fill(ReagentStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        if (!doFill)
        {
            if (reagentStack == null)
            {
                return Math.min(capacity, resource.amount);
            }

            if (!reagentStack.isReagentEqual(resource))
            {
                return 0;
            }

            return Math.min(capacity - reagentStack.amount, resource.amount);
        }

        if (reagentStack == null)
        {
            reagentStack = new ReagentStack(resource, Math.min(capacity, resource.amount));

            return reagentStack.amount;
        }

        if (!reagentStack.isReagentEqual(resource))
        {
            return 0;
        }
        int filled = capacity - reagentStack.amount;

        if (resource.amount < filled)
        {
            reagentStack.amount += resource.amount;
            filled = resource.amount;
        } else
        {
            reagentStack.amount = capacity;
        }

        return filled;
    }

    @Override
    public ReagentStack drain(int maxDrain, boolean doDrain)
    {
        if (reagentStack == null)
        {
            return null;
        }

        int drained = maxDrain;
        if (reagentStack.amount < drained)
        {
            drained = reagentStack.amount;
        }

        ReagentStack stack = new ReagentStack(reagentStack, drained);
        if (doDrain)
        {
            reagentStack.amount -= drained;
            if (reagentStack.amount <= 0)
            {
                reagentStack = null;
            }
        }
        return stack;
    }

    @Override
    public ReagentContainerInfo getInfo()
    {
        return new ReagentContainerInfo(this);
    }
}
