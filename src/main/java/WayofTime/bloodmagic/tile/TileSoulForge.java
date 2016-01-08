package WayofTime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.recipe.SoulForgeRecipe;
import WayofTime.bloodmagic.api.registry.SoulForgeRecipeRegistry;
import WayofTime.bloodmagic.api.soul.ISoul;
import WayofTime.bloodmagic.api.soul.ISoulGem;

public class TileSoulForge extends TileInventory implements ITickable
{
    public static final int soulSlot = 4;
    public static final int outputSlot = 5;

    //Input slots are from 0 to 3.

    public TileSoulForge()
    {
        super(6, "soulForge");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
    }

    @Override
    public void update()
    {
        if (!hasSoulGemOrSoul())
        {
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
        if (recipe != null && recipe.getMinimumSouls() <= soulsInGem)
        {
            ItemStack outputStack = recipe.getRecipeOutput();
            if (getStackInSlot(outputSlot) == null)
            {
                setInventorySlotContents(outputSlot, outputStack);
                consumeInventory();
            } else
            {

            }
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
