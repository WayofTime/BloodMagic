package wayoftime.bloodmagic.common.item.routing;

import net.minecraftforge.fluids.FluidStack;

public class FluidModFilterKey implements IFilterKey<FluidStack>
{

    private String namespace;
    private int amount;

    public FluidModFilterKey(String namespace, int amount)
    {
        this.amount = amount;
        this.namespace = namespace;
    }

    @Override
    public FluidStack getType()
    {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean doesStackMatch(FluidStack testStack)
    {
        return !testStack.isEmpty() && testStack.getFluid().getRegistryName().getNamespace().equals(namespace);
    }

    @Override
    public int getCount()
    {
        return amount;
    }

    @Override
    public void setCount(int count)
    {
        this.amount = count;
    }

    @Override
    public void grow(int changeAmount)
    {
        this.amount += changeAmount;
    }

    @Override
    public boolean isEmpty()
    {
        return this.amount == 0;
    }

    @Override
    public void shrink(int changeAmount)
    {
        this.amount -= changeAmount;
    }

}
