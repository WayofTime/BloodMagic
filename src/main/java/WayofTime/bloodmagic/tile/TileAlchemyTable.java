package WayofTime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.api.event.BloodMagicCraftedEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import org.apache.commons.lang3.ArrayUtils;

public class TileAlchemyTable extends TileInventory implements ISidedInventory, ITickable
{
    public static final int orbSlot = 6;
    public static final int toolSlot = 7;
    public static final int outputSlot = 8;

    public EnumFacing direction = EnumFacing.NORTH;
    public boolean isSlave = false;
    public int burnTime = 0;
    public int ticksRequired = 1;

    public BlockPos connectedPos = BlockPos.ORIGIN;
    public boolean[] blockedSlots = new boolean[] { false, false, false, false, false, false };

    public TileAlchemyTable()
    {
        super(9, "alchemyTable");
    }

    public void setInitialTableParameters(EnumFacing direction, boolean isSlave, BlockPos connectedPos)
    {
        this.isSlave = isSlave;
        this.connectedPos = connectedPos;

        if (!isSlave)
        {
            this.direction = direction;
        }
    }

    public boolean isInvisible()
    {
        return isSlave();
    }

    public boolean isInputSlotAccessible(int slot)
    {
        return !(slot < 6 && slot >= 0) || !blockedSlots[slot];
    }

    public void toggleInputSlotAccessible(int slot)
    {
        if (slot < 6 && slot >= 0)
            blockedSlots[slot] = !blockedSlots[slot];
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        super.deserialize(tag);

        isSlave = tag.getBoolean("isSlave");
        direction = EnumFacing.getFront(tag.getInteger(Constants.NBT.DIRECTION));
        connectedPos = new BlockPos(tag.getInteger(Constants.NBT.X_COORD), tag.getInteger(Constants.NBT.Y_COORD), tag.getInteger(Constants.NBT.Z_COORD));

        burnTime = tag.getInteger("burnTime");
        ticksRequired = tag.getInteger("ticksRequired");

        byte[] array = tag.getByteArray("blockedSlots");
        for (int i = 0; i < array.length; i++)
            blockedSlots[i] = array[i] != 0;
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setBoolean("isSlave", isSlave);
        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
        tag.setInteger(Constants.NBT.X_COORD, connectedPos.getX());
        tag.setInteger(Constants.NBT.Y_COORD, connectedPos.getY());
        tag.setInteger(Constants.NBT.Z_COORD, connectedPos.getZ());

        tag.setInteger("burnTime", burnTime);
        tag.setInteger("ticksRequired", ticksRequired);

        byte[] blockedSlotArray = new byte[blockedSlots.length];
        for (int i = 0; i < blockedSlots.length; i++)
            blockedSlotArray[i] = (byte) (blockedSlots[i] ? 1 : 0);

        tag.setByteArray("blockedSlots", blockedSlotArray);
        return tag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (this.isSlave())
            {
                TileEntity tile = getWorld().getTileEntity(connectedPos);
                if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
                {
                    return (T) tile.getCapability(capability, facing);
                }
            } else
            {
                return super.getCapability(capability, facing);
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        switch (side)
        {
        case DOWN:
            return new int[] { outputSlot };
        case UP:
            return new int[] { orbSlot, toolSlot };
        default:
            return new int[] { 0, 1, 2, 3, 4, 5 };
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
    {
        switch (direction)
        {
        case DOWN:
            return index != outputSlot && index != orbSlot && index != toolSlot;
        case UP:
            if (index == orbSlot && !stack.isEmpty() && stack.getItem() instanceof IBloodOrb)
            {
                return true;
            } else if (index == toolSlot)
            {
                return false; //TODO:
            } else
            {
                return true;
            }
        default:
            if (this.isSlave)
            {
                TileEntity tile = getWorld().getTileEntity(connectedPos);
                if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
                {
                    return ((TileAlchemyTable) tile).canInsertItem(index, stack, direction);
                }
            }
            return getAccessibleInputSlots(direction).contains(index);
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        switch (direction)
        {
        case DOWN:
            return index == outputSlot;
        case UP:
            if (index == orbSlot && !stack.isEmpty() && stack.getItem() instanceof IBloodOrb)
            {
                return true;
            } else if (index == toolSlot)
            {
                return true; //TODO:
            } else
            {
                return true;
            }
        default:
            if (this.isSlave)
            {
                TileEntity tile = getWorld().getTileEntity(connectedPos);
                if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
                {
                    return ((TileAlchemyTable) tile).canExtractItem(index, stack, direction);
                }
            }
            return getAccessibleInputSlots(direction).contains(index);
        }
    }

    public List<Integer> getAccessibleInputSlots(EnumFacing direction)
    {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 6; i++)
        {
            if (isInputSlotAccessible(i))
            {
                list.add(i);
            }
        }

        return list;
    }

    @Override
    public void update()
    {
        if (isSlave())
        {
            return;
        }

        List<ItemStack> inputList = new ArrayList<>();

        for (int i = 0; i < 6; i++)
        {
            if (!getStackInSlot(i).isEmpty())
            {
                inputList.add(getStackInSlot(i));
            }
        }

        int tier = getTierOfOrb();

        // special recipes like dying
        AlchemyTableRecipe recipe = AlchemyTableRecipeRegistry.getMatchingRecipe(inputList, getWorld(), getPos());
        if (recipe != null && (burnTime > 0 || (!getWorld().isRemote && tier >= recipe.getTierRequired() && this.getContainedLp() >= recipe.getLpDrained())))
        {
            if (burnTime == 1)
                notifyUpdate();

            if (canCraft(recipe.getRecipeOutput(inputList)))
            {
                ticksRequired = recipe.getTicksRequired();
                burnTime++;

                if (burnTime == ticksRequired)
                {
                    if (!getWorld().isRemote)
                    {
                        int requiredLp = recipe.getLpDrained();
                        if (requiredLp > 0)
                        {
                            if (!getWorld().isRemote)
                            {
                                consumeLp(requiredLp);
                            }
                        }

                        if (!getWorld().isRemote)
                        {
                            craftItem(inputList, recipe);
                        }
                    }

                    burnTime = 0;

                    IBlockState state = getWorld().getBlockState(pos);
                    getWorld().notifyBlockUpdate(getPos(), state, state, 3);
                } else if (burnTime > ticksRequired + 10)
                {
                    burnTime = 0;
                }
            } else
            {
                burnTime = 0;
            }
        } else
        { // Simple recipes
            RecipeAlchemyTable recipeAlchemyTable = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(inputList);
            if (recipeAlchemyTable != null && (burnTime > 0 || (!getWorld().isRemote && tier >= recipeAlchemyTable.getMinimumTier() && getContainedLp() >= recipeAlchemyTable.getSyphon())))
            {
                if (burnTime == 1)
                    notifyUpdate();

                if (canCraft(recipeAlchemyTable.getOutput()))
                {
                    ticksRequired = recipeAlchemyTable.getTicks();
                    burnTime++;
                    if (burnTime >= ticksRequired)
                    {
                        if (!getWorld().isRemote)
                        {
                            if (recipeAlchemyTable.getSyphon() > 0)
                            {
                                if (consumeLp(recipeAlchemyTable.getSyphon()) < recipeAlchemyTable.getSyphon())
                                {
                                    //There was not enough LP to craft or there was no orb
                                    burnTime = 0;
                                    notifyUpdate();
                                    return;
                                }
                            }

                            ItemStack[] inputs = new ItemStack[0];
                            for (ItemStack stack : inputList)
                                ArrayUtils.add(inputs, stack.copy());

                            BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(recipeAlchemyTable.getOutput().copy(), inputs);
                            MinecraftForge.EVENT_BUS.post(event);

                            ItemStack outputSlotStack = getStackInSlot(outputSlot);
                            if (outputSlotStack.isEmpty())
                                setInventorySlotContents(outputSlot, event.getOutput());
                            else
                                outputSlotStack.grow(event.getOutput().getCount());

                            for (int i = 0; i < 6; i++)
                            {
                                ItemStack currentStack = getStackInSlot(i);
                                if (currentStack.getItem().hasContainerItem(currentStack))
                                    setInventorySlotContents(i, currentStack.getItem().getContainerItem(currentStack));
                                else
                                    currentStack.shrink(1);
                            }

                            burnTime = 0;
                            notifyUpdate();
                        }
                    }
                }
            } else
            {
                burnTime = 0;
            }
        }
    }

    public double getProgressForGui()
    {
        return ((double) burnTime) / ticksRequired;
    }

    private boolean canCraft(ItemStack output)
    {
        ItemStack currentOutputStack = getStackInSlot(outputSlot);
        if (output.isEmpty())
            return false;
        if (currentOutputStack.isEmpty())
            return true;
        if (!ItemHandlerHelper.canItemStacksStack(output, currentOutputStack))
            return false;
        int result = currentOutputStack.getCount() + output.getCount();
        return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();
    }

    public int getTierOfOrb()
    {
        ItemStack orbStack = getStackInSlot(orbSlot);
        if (!orbStack.isEmpty())
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                BloodOrb orb = ((IBloodOrb) orbStack.getItem()).getOrb(orbStack);
                return orb == null ? 0 : orb.getTier();
            }
        }

        return 0;
    }

    public int getContainedLp()
    {
        ItemStack orbStack = getStackInSlot(orbSlot);
        if (!orbStack.isEmpty())
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                Binding binding = ((IBindable) orbStack.getItem()).getBinding(orbStack);
                if (binding == null)
                {
                    return 0;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

                return network.getCurrentEssence();
            }
        }

        return 0;
    }

    public void craftItem(List<ItemStack> inputList, AlchemyTableRecipe recipe)
    {
        ItemStack outputStack = recipe.getRecipeOutput(inputList);
        if (this.canCraft(outputStack))
        {
            ItemStack currentOutputStack = getStackInSlot(outputSlot);

            ItemStack[] inputs = new ItemStack[0];
            for (ItemStack stack : inputList)
                ArrayUtils.add(inputs, stack.copy());

            BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(outputStack.copy(), inputs);
            MinecraftForge.EVENT_BUS.post(event);
            outputStack = event.getOutput();

            if (currentOutputStack.isEmpty())
            {
                setInventorySlotContents(outputSlot, outputStack);
            } else if (ItemHandlerHelper.canItemStacksStack(outputStack, currentOutputStack))
            {
                currentOutputStack.grow(outputStack.getCount());
            }

            consumeInventory(recipe);
        }
    }

    public int consumeLp(int requested)
    {
        ItemStack orbStack = getStackInSlot(orbSlot);

        if (!orbStack.isEmpty())
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                if (NetworkHelper.syphonFromContainer(orbStack, requested))
                {
                    return requested;
                }
            }
        }

        return 0;
    }

    public void consumeInventory(AlchemyTableRecipe recipe)
    {
        ItemStack[] input = new ItemStack[6];

        for (int i = 0; i < 6; i++)
        {
            input[i] = getStackInSlot(i);
        }

        ItemStack[] result = recipe.getRemainingItems(input);
        for (int i = 0; i < 6; i++)
        {
            setInventorySlotContents(i, result[i]);
        }
    }

    public EnumFacing getDirection()
    {
        return direction;
    }

    public boolean isSlave()
    {
        return isSlave;
    }

    public int getBurnTime()
    {
        return burnTime;
    }

    public int getTicksRequired()
    {
        return ticksRequired;
    }

    public BlockPos getConnectedPos()
    {
        return connectedPos;
    }

    public boolean[] getBlockedSlots()
    {
        return blockedSlots;
    }

    public static int getOrbSlot()
    {
        return orbSlot;
    }

    public static int getToolSlot()
    {
        return toolSlot;
    }

    public static int getOutputSlot()
    {
        return outputSlot;
    }
}
