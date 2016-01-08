package WayofTime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.SoulForgeRecipe;
import WayofTime.bloodmagic.api.registry.SoulForgeRecipeRegistry;
import WayofTime.bloodmagic.api.soul.ISoul;
import WayofTime.bloodmagic.api.soul.ISoulGem;

public class TileSoulForge extends TileInventory implements ITickable
{
    public static final int ticksRequired = 100;

    public static final int soulSlot = 4;
    public static final int outputSlot = 5;

    //Input slots are from 0 to 3.

    public int burnTime = 0;

    public TileSoulForge()
    {
        super(6, "soulForge");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        burnTime = tag.getInteger(Constants.NBT.SOUL_FORGE_BURN);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger(Constants.NBT.SOUL_FORGE_BURN, burnTime);
    }

    @Override
    public void update()
    {
        if (!hasSoulGemOrSoul())
        {
            burnTime = 0;
            return;
        }

        double soulsInGem = getSouls();

        List<ItemStack> inputList = new ArrayList<ItemStack>();

        for (int i = 0; i < 4; i++)
        {
            if (getStackInSlot(i) != null)
            {
                inputList.add(getStackInSlot(i));
            }
        }

        SoulForgeRecipe recipe = SoulForgeRecipeRegistry.getMatchingRecipe(inputList, getWorld(), getPos());
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
                            consumeSouls(requiredSouls);
                        }

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
        }
    }

    public double getProgressForGui()
    {
        return ((double) burnTime) / ticksRequired;
    }

    private boolean canCraft(SoulForgeRecipe recipe)
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

    public void craftItem(SoulForgeRecipe recipe)
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
            if (soulStack.getItem() instanceof ISoul || soulStack.getItem() instanceof ISoulGem)
            {
                return true;
            }
        }

        return false;
    }

    public double getSouls()
    {
        ItemStack soulStack = getStackInSlot(soulSlot);

        if (soulStack != null)
        {
            if (soulStack.getItem() instanceof ISoul)
            {
                ISoul soul = (ISoul) soulStack.getItem();
                return soul.getSouls(soulStack);
            }

            if (soulStack.getItem() instanceof ISoulGem)
            {
                ISoulGem soul = (ISoulGem) soulStack.getItem();
                return soul.getSouls(soulStack);
            }
        }

        return 0;
    }

    public double consumeSouls(double requested)
    {
        ItemStack soulStack = getStackInSlot(soulSlot);

        if (soulStack != null)
        {
            if (soulStack.getItem() instanceof ISoul)
            {
                ISoul soul = (ISoul) soulStack.getItem();
                double souls = soul.drainSouls(soulStack, requested);
                if (soul.getSouls(soulStack) <= 0)
                {
                    setInventorySlotContents(soulSlot, null);
                }
                return souls;
            }

            if (soulStack.getItem() instanceof ISoulGem)
            {
                ISoulGem soul = (ISoulGem) soulStack.getItem();
                return soul.drainSouls(soulStack, requested);
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
}
