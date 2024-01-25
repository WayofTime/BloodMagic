package wayoftime.bloodmagic.common.item.routing;

import net.minecraftforge.fluids.FluidStack;

public class FluidFilterKey implements IFilterKey<FluidStack>
{

    private FluidStack keyStack;
    private int amount;

    public FluidFilterKey(FluidStack keyStack, int amount)
    {
        this.amount = amount;
        this.keyStack = keyStack;
    }

    @Override
    public FluidStack getType()
    {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean doesStackMatch(FluidStack testStack)
    {
        return keyStack.isFluidEqual(testStack) && !testStack.isEmpty() && !keyStack.isEmpty();
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
        return this.amount == 0 || keyStack.isEmpty();
    }

    @Override
    public void shrink(int changeAmount)
    {
        this.amount -= changeAmount;
    }

}
