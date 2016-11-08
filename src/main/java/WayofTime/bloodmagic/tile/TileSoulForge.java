package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;

public class TileSoulForge extends TileInventory implements ITickable, IDemonWillConduit
{
    public static final int ticksRequired = 100;
    public static final double worldWillTransferRate = 1;

    public static final int soulSlot = 4;
    public static final int outputSlot = 5;

    //Input slots are from 0 to 3.

    public int burnTime = 0;

    public TileSoulForge()
    {
        super(6, "soulForge");
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        super.deserialize(tag);

        burnTime = tag.getInteger(Constants.NBT.SOUL_FORGE_BURN);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setInteger(Constants.NBT.SOUL_FORGE_BURN, burnTime);
        return tag;
    }

    @Override
    public void update()
    {
        if (!worldObj.isRemote)
        {
            for (EnumDemonWillType type : EnumDemonWillType.values())
            {
                double willInWorld = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);
                double filled = Math.min(willInWorld, worldWillTransferRate);

                if (filled > 0)
                {
                    filled = this.fillDemonWill(type, filled, false);
                    filled = WorldDemonWillHandler.drainWill(worldObj, pos, type, filled, false);

                    if (filled > 0)
                    {
                        this.fillDemonWill(type, filled, true);
                        WorldDemonWillHandler.drainWill(worldObj, pos, type, filled, true);
                    }
                }
            }
        }

        if (!hasSoulGemOrSoul())
        {
            burnTime = 0;
            return;
        }

        double soulsInGem = getWill(EnumDemonWillType.DEFAULT);

        List<ItemStack> inputList = new ArrayList<ItemStack>();

        for (int i = 0; i < 4; i++)
        {
            if (getStackInSlot(i) != null)
            {
                inputList.add(getStackInSlot(i));
            }
        }

        TartaricForgeRecipe recipe = TartaricForgeRecipeRegistry.getMatchingRecipe(inputList, getWorld(), getPos());
        if (recipe != null && (soulsInGem >= recipe.getMinimumSouls() || burnTime > 0))
        {
            if (canCraft(recipe))
            {
                burnTime++;

                if (burnTime == ticksRequired)
                {
                    if (!worldObj.isRemote)
                    {
                        double requiredSouls = recipe.getSoulsDrained();
                        if (requiredSouls > 0)
                        {
                            if (!worldObj.isRemote && soulsInGem >= recipe.getMinimumSouls())
                            {
                                consumeSouls(EnumDemonWillType.DEFAULT, requiredSouls);
                            }
                        }

                        if (!worldObj.isRemote && soulsInGem >= recipe.getMinimumSouls())
                            craftItem(recipe);
                    }

                    burnTime = 0;
                } else if (burnTime > ticksRequired + 10)
                {
                    burnTime = 0;
                }
            } else
            {
                burnTime = 0;
            }
        } else
        {
            burnTime = 0;
        }
    }

    public double getProgressForGui()
    {
        return ((double) burnTime) / ticksRequired;
    }

    private boolean canCraft(TartaricForgeRecipe recipe)
    {
        if (recipe == null)
        {
            return false;
        }

        ItemStack outputStack = recipe.getRecipeOutput();
        ItemStack currentOutputStack = getStackInSlot(outputSlot);
        if (outputStack == null)
            return false;
        if (currentOutputStack == null)
            return true;
        if (!currentOutputStack.isItemEqual(outputStack))
            return false;
        int result = currentOutputStack.stackSize + outputStack.stackSize;
        return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();

    }

    public void craftItem(TartaricForgeRecipe recipe)
    {
        if (this.canCraft(recipe))
        {
            ItemStack outputStack = recipe.getRecipeOutput();
            ItemStack currentOutputStack = getStackInSlot(outputSlot);

            if (currentOutputStack == null)
            {
                setInventorySlotContents(outputSlot, outputStack);
            } else if (currentOutputStack.getItem() == currentOutputStack.getItem())
            {
                currentOutputStack.stackSize += outputStack.stackSize;
            }

            consumeInventory();
        }
    }

    public boolean hasSoulGemOrSoul()
    {
        ItemStack soulStack = getStackInSlot(soulSlot);

        if (soulStack != null)
        {
            if (soulStack.getItem() instanceof IDemonWill || soulStack.getItem() instanceof IDemonWillGem)
            {
                return true;
            }
        }

        return false;
    }

    public double getWill(EnumDemonWillType type)
    {
        ItemStack soulStack = getStackInSlot(soulSlot);

        if (soulStack != null)
        {
            if (soulStack.getItem() instanceof IDemonWill && ((IDemonWill) soulStack.getItem()).getType(soulStack) == type)
            {
                IDemonWill soul = (IDemonWill) soulStack.getItem();
                return soul.getWill(type, soulStack);
            }

            if (soulStack.getItem() instanceof IDemonWillGem)
            {
                IDemonWillGem soul = (IDemonWillGem) soulStack.getItem();
                return soul.getWill(type, soulStack);
            }
        }

        return 0;
    }

    public double consumeSouls(EnumDemonWillType type, double requested)
    {
        ItemStack soulStack = getStackInSlot(soulSlot);

        if (soulStack != null)
        {
            if (soulStack.getItem() instanceof IDemonWill && ((IDemonWill) soulStack.getItem()).getType(soulStack) == type)
            {
                IDemonWill soul = (IDemonWill) soulStack.getItem();
                double souls = soul.drainWill(type, soulStack, requested);
                if (soul.getWill(type, soulStack) <= 0)
                {
                    setInventorySlotContents(soulSlot, null);
                }
                return souls;
            }

            if (soulStack.getItem() instanceof IDemonWillGem)
            {
                IDemonWillGem soul = (IDemonWillGem) soulStack.getItem();
                return soul.drainWill(type, soulStack, requested, true);
            }
        }

        return 0;
    }

    public void consumeInventory()
    {
        for (int i = 0; i < 4; i++)
        {
            ItemStack inputStack = getStackInSlot(i);
            if (inputStack != null)
            {
                if (inputStack.getItem().hasContainerItem(inputStack))
                {
                    setInventorySlotContents(i, inputStack.getItem().getContainerItem(inputStack));
                    continue;
                }

                inputStack.stackSize--;
                if (inputStack.stackSize <= 0)
                {
                    setInventorySlotContents(i, null);
                    continue;
                }
            }
        }
    }

    @Override
    public int getWeight()
    {
        return 50;
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

        ItemStack stack = this.getStackInSlot(soulSlot);
        if (stack == null || !(stack.getItem() instanceof IDemonWillGem))
        {
            return 0;
        }

        IDemonWillGem willGem = (IDemonWillGem) stack.getItem();

        double filled = willGem.fillWill(type, stack, amount, doFill);

        return filled;
    }

    @Override
    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
    {
        ItemStack stack = this.getStackInSlot(soulSlot);
        if (stack == null || !(stack.getItem() instanceof IDemonWillGem))
        {
            return 0;
        }

        IDemonWillGem willGem = (IDemonWillGem) stack.getItem();

        double drained = amount;
        double current = willGem.getWill(type, stack);
        if (current < drained)
        {
            drained = current;
        }

        if (doDrain)
        {
            drained = willGem.drainWill(type, stack, drained, true);
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
        return 0;
    }
}
