package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraft.nbt.NBTTagCompound;

public class ReagentStack
{
    public Reagent reagent;
    public int amount;

    public ReagentStack(Reagent reagent, int amount)
    {
        this.reagent = reagent;
        this.amount = amount;
    }

    public ReagentStack(ReagentStack reagentStack, int amount)
    {
        this(reagentStack.reagent, amount);
    }

    public static ReagentStack loadReagentStackFromNBT(NBTTagCompound tag)
    {
        Reagent reagent = ReagentRegistry.getReagentForKey(tag.getString("Reagent"));

        if (reagent == null)
        {
            return null;
        }

        int amount = tag.getInteger("amount");
        ReagentStack stack = new ReagentStack(reagent, amount);

        return stack;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setString("Reagent", ReagentRegistry.getKeyForReagent(this.reagent));
        tag.setInteger("amount", this.amount);

        return tag;
    }

    public ReagentStack splitStack(int amount)
    {
        ReagentStack copyStack = this.copy();
        int splitAmount = Math.min(amount, this.amount);
        copyStack.amount = splitAmount;
        this.amount -= splitAmount;

        return copyStack;
    }

    public ReagentStack copy()
    {
        return new ReagentStack(this.reagent, this.amount);
    }

    public boolean isReagentEqual(ReagentStack other)
    {
        return other != null && this.reagent == other.reagent;
    }
}
