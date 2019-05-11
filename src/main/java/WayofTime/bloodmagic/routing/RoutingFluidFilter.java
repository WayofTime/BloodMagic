package WayofTime.bloodmagic.routing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class RoutingFluidFilter implements IFluidFilter {
    protected List<FluidStack> requestList;
    protected TileEntity accessedTile;
    protected IFluidHandler fluidHandler;

    @Override
    public void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IFluidHandler fluidHandler, boolean isFilterOutput) {
        this.accessedTile = tile;
        this.fluidHandler = fluidHandler;
        if (isFilterOutput) {
            //The requestList contains a list of how much can be extracted.
            requestList = new ArrayList<>();
            for (ItemStack filterStack : filteredList) {
                FluidStack fluidFilterStack = getFluidStackFromItemStack(filterStack);
                if (fluidFilterStack != null) {
                    requestList.add(fluidFilterStack);
                }
            }

            IFluidTankProperties[] properties = fluidHandler.getTankProperties();

            for (IFluidTankProperties property : properties) {
                FluidStack containedStack = property.getContents();
                if (containedStack != null) {
                    for (FluidStack fluidFilterStack : requestList) {
                        if (doStacksMatch(fluidFilterStack, containedStack)) {
                            fluidFilterStack.amount = Math.max(fluidFilterStack.amount - containedStack.amount, 0);
                        }
                    }
                }
            }
        } else {
            requestList = new ArrayList<>();
            for (ItemStack filterStack : filteredList) {
                FluidStack fluidFilterStack = getFluidStackFromItemStack(filterStack);
                if (fluidFilterStack != null) {
                    fluidFilterStack.amount *= -1;
                    requestList.add(fluidFilterStack);
                }
            }

            IFluidTankProperties[] properties = fluidHandler.getTankProperties();

            for (IFluidTankProperties property : properties) {
                FluidStack containedStack = property.getContents();
                if (containedStack != null) {
                    for (FluidStack fluidFilterStack : requestList) {
                        if (doStacksMatch(fluidFilterStack, containedStack)) {
                            fluidFilterStack.amount += containedStack.amount;
                        }
                    }
                }
            }
        }
    }

    /**
     * Gives the remainder~
     */
    @Override
    public FluidStack transferStackThroughOutputFilter(FluidStack fluidStack) {
        int allowedAmount = 0;
        for (FluidStack filterStack : requestList) {
            if (doStacksMatch(filterStack, fluidStack)) {
                allowedAmount = Math.min(filterStack.amount, fluidStack.amount);
                break;
            }
        }

        if (allowedAmount <= 0) {
            return fluidStack;
        }

        FluidStack copyStack = fluidStack.copy();
        int filledAmount = fluidHandler.fill(fluidStack, true);
        copyStack.amount = fluidStack.amount - filledAmount;

        Iterator<FluidStack> itr = requestList.iterator();
        while (itr.hasNext()) {
            FluidStack filterStack = itr.next();
            if (doStacksMatch(filterStack, copyStack)) {
                filterStack.amount -= filledAmount;
                if (filterStack.amount <= 0) {
                    itr.remove();
                }
            }
        }

        World world = accessedTile.getWorld();
        BlockPos pos = accessedTile.getPos();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        return copyStack.amount <= 0 ? null : copyStack;
    }

    @Override
    public int transferThroughInputFilter(IFluidFilter outputFilter, int maxTransfer) {
        for (FluidStack filterFluidStack : requestList) {
            int allowedAmount = Math.min(filterFluidStack.amount, maxTransfer);
            if (allowedAmount <= 0) {
                continue;
            }

            FluidStack copyStack = filterFluidStack.copy();
            copyStack.amount = allowedAmount;
            FluidStack drainStack = fluidHandler.drain(copyStack, false);
            if (drainStack != null) //Can't pull this liquid out for some reason if it fails this check
            {
                FluidStack remainderStack = outputFilter.transferStackThroughOutputFilter(drainStack);
                int drained = remainderStack == null ? copyStack.amount : (copyStack.amount - remainderStack.amount);

                if (drained > 0) {
                    drainStack.amount = drained;

                    fluidHandler.drain(drainStack, true);
                    maxTransfer -= drained;
                }

                Iterator<FluidStack> itr = requestList.iterator();
                while (itr.hasNext()) {
                    FluidStack filterStack = itr.next();
                    if (doStacksMatch(filterStack, copyStack)) {
                        filterStack.amount -= drained;
                        if (filterStack.amount <= 0) {
                            itr.remove();
                        }
                    }
                }

                World world = accessedTile.getWorld();
                BlockPos pos = accessedTile.getPos();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

                return maxTransfer;
            }
        }

        return 0;
    }

    @Override
    public boolean doesStackMatchFilter(FluidStack testStack) {
        for (FluidStack filterStack : requestList) {
            if (doStacksMatch(filterStack, testStack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean doStacksMatch(FluidStack filterStack, FluidStack testStack) {
        return testStack != null && filterStack.getFluid() == testStack.getFluid();
    }

    @Nullable
    public static FluidStack getFluidStackFromItemStack(ItemStack inputStack) {
        boolean isEmpty = false;
        if (inputStack.getCount() == 0) {
            isEmpty = true;
            inputStack.setCount(1);
        }

        FluidStack fluidStack = FluidUtil.getFluidContained(inputStack);
        if (fluidStack == null)
            return null;

        fluidStack.amount = isEmpty ? 0 : inputStack.getCount();
        return fluidStack;
    }
}
