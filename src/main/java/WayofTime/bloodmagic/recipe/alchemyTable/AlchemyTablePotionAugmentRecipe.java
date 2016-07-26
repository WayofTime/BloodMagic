package WayofTime.bloodmagic.recipe.alchemyTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import WayofTime.bloodmagic.registry.ModItems;

public class AlchemyTablePotionAugmentRecipe extends AlchemyTablePotionRecipe
{
    protected double lengthAugment = 0;
    protected int powerAugment = 0;
    protected Potion wantedPotion;

    public AlchemyTablePotionAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, double lengthAugment, int powerAugment)
    {
        super(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect);

        ArrayList<Object> recipe = new ArrayList<Object>();
        for (ItemStack stack : inputItems)
        {
            recipe.add(stack);
        }
        recipe.add(getAugmentedPotionFlask(baseEffect));

        this.input = recipe;

        this.wantedPotion = baseEffect.getPotion();
        this.lengthAugment = lengthAugment;
    }

    public AlchemyTablePotionAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, double lengthAugment, int powerAugment)
    {
        this(lpDrained, ticksRequired, tierRequired, Arrays.asList(inputItem), baseEffect, lengthAugment, powerAugment);
    }

    @Override
    public boolean isPotionFlaskValidInput(ItemStack stack)
    {
        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(stack);
        for (PotionEffect eff : effectList)
        {
            if (eff.getPotion() == wantedPotion)
            {
                double currentAugment = BMPotionUtils.getLengthAugment(stack, wantedPotion);

                return currentAugment < lengthAugment || eff.getAmplifier() < powerAugment;
            }
        }

        return false;
    }

    @Override
    public ItemStack getModifiedFlaskForInput(ItemStack inputStack)
    {
        if (inputStack == null)
        {
            ItemStack outputStack = new ItemStack(ModItems.potionFlask);

            List<PotionEffect> effectList = new ArrayList<PotionEffect>();
            int potionLength = wantedPotion.isInstant() ? 1 : BMPotionUtils.getAugmentedLength(baseEffect.getDuration(), lengthAugment, powerAugment - baseEffect.getAmplifier());
            effectList.add(new PotionEffect(wantedPotion, potionLength, baseEffect.getAmplifier()));

            BMPotionUtils.setEffects(outputStack, effectList);

            return outputStack;
        }

        ItemStack outputStack = inputStack.copy();

        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(outputStack);
        List<PotionEffect> newEffectList = new ArrayList<PotionEffect>();

        Iterator<PotionEffect> effectIterator = effectList.iterator();
        while (effectIterator.hasNext())
        {
            PotionEffect effect = effectIterator.next();
            if (effect.getPotion() == wantedPotion)
            {
                double currentLengthAugment = Math.max(lengthAugment, BMPotionUtils.getLengthAugment(outputStack, wantedPotion));
                int currentPowerAugment = Math.max(powerAugment, effect.getAmplifier());
                int potionLength = wantedPotion.isInstant() ? 1 : BMPotionUtils.getAugmentedLength(baseEffect.getDuration(), currentLengthAugment, currentPowerAugment);
                newEffectList.add(new PotionEffect(wantedPotion, potionLength, currentPowerAugment));
                BMPotionUtils.setLengthAugment(outputStack, wantedPotion, currentLengthAugment);
            } else
            {
                newEffectList.add(effect);
            }
        }

        BMPotionUtils.setEffects(outputStack, newEffectList);

        return outputStack;
    }

    public static ItemStack getAugmentedPotionFlask(PotionEffect baseEffect)
    {
        ItemStack outputStack = new ItemStack(ModItems.potionFlask);

        List<PotionEffect> effectList = new ArrayList<PotionEffect>();
        effectList.add(baseEffect);

        BMPotionUtils.setEffects(outputStack, effectList);

        return outputStack;
    }
}
