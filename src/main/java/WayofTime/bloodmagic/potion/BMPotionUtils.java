package WayofTime.bloodmagic.potion;

import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTablePotionAugmentRecipe;

import com.google.common.base.Objects;

public class BMPotionUtils
{
    public static double getLengthAugment(ItemStack flaskStack, Potion potion)
    {
        NBTHelper.checkNBT(flaskStack);
        NBTTagCompound tag = flaskStack.getTagCompound();

        return tag.getDouble(Constants.NBT.POTION_AUGMENT_LENGHT + potion.getName());
    }

    public static void setLengthAugment(ItemStack flaskStack, Potion potion, double value)
    {
        if (value < 0)
        {
            value = 0;
        }

        NBTHelper.checkNBT(flaskStack);
        NBTTagCompound tag = flaskStack.getTagCompound();

        tag.setDouble(Constants.NBT.POTION_AUGMENT_LENGHT + potion.getName(), value);
    }

    public static int getAugmentedLength(int originalLength, double lengthAugment, double powerAugment)
    {
        return Math.max((int) (originalLength * (Math.pow(8f / 3f, lengthAugment) * Math.pow(0.5, powerAugment))), 1);
    }

    /**
     * Copied from PotionUtils
     * 
     * @param stack
     * @param effects
     * @return
     */
    public static ItemStack setEffects(ItemStack stack, Collection<PotionEffect> effects)
    {
        if (effects.isEmpty())
        {
            return stack;
        } else
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound) Objects.firstNonNull(stack.getTagCompound(), new NBTTagCompound());
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : effects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
            stack.setTagCompound(nbttagcompound);
            return stack;
        }
    }

    public static AlchemyTablePotionAugmentRecipe getLengthAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, double lengthAugment)
    {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect, lengthAugment, 0);
    }

    public static AlchemyTablePotionAugmentRecipe getPowerAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, int powerAugment)
    {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect, 0, powerAugment);
    }

    public static AlchemyTablePotionAugmentRecipe getLengthAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, double lengthAugment)
    {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItem, baseEffect, lengthAugment, 0);
    }

    public static AlchemyTablePotionAugmentRecipe getPowerAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, int powerAugment)
    {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItem, baseEffect, 0, powerAugment);
    }
}
