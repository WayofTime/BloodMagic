package WayofTime.bloodmagic.recipe.alchemyTable;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlchemyTablePotionAugmentRecipe extends AlchemyTablePotionRecipe {
    protected double lengthAugment = 0;
    protected int powerAugment = 0;
    protected Potion wantedPotion;

    public AlchemyTablePotionAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, double lengthAugment, int powerAugment) {
        super(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect);

        ArrayList<Object> recipe = new ArrayList<>();
        recipe.addAll(inputItems);
        recipe.add(getAugmentedPotionFlask(baseEffect));

        this.input = recipe;

        this.wantedPotion = baseEffect.getPotion();
        this.lengthAugment = lengthAugment;
        this.powerAugment = powerAugment;
    }

    public AlchemyTablePotionAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, double lengthAugment, int powerAugment) {
        this(lpDrained, ticksRequired, tierRequired, Collections.singletonList(inputItem), baseEffect, lengthAugment, powerAugment);
    }

    @Override
    public boolean isPotionFlaskValidInput(ItemStack stack) {
        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(stack);
        for (PotionEffect eff : effectList) {
            if (eff.getPotion() == wantedPotion) {
                double currentAugment = BMPotionUtils.getLengthAugment(stack, wantedPotion);

                return currentAugment < lengthAugment || eff.getAmplifier() < powerAugment;
            }
        }

        return false;
    }

    @Override
    public ItemStack getModifiedFlaskForInput(ItemStack inputStack) {
        if (inputStack.isEmpty()) {
            ItemStack outputStack = new ItemStack(RegistrarBloodMagicItems.POTION_FLASK);

            List<PotionEffect> effectList = new ArrayList<>();
            int potionLength = wantedPotion.isInstant() ? 1 : BMPotionUtils.getAugmentedLength(baseEffect.getDuration(), lengthAugment, powerAugment - baseEffect.getAmplifier());
            effectList.add(new PotionEffect(wantedPotion, potionLength, powerAugment - baseEffect.getAmplifier()));

            BMPotionUtils.setEffects(outputStack, effectList);

            return outputStack;
        }

        ItemStack outputStack = inputStack.copy();

        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(outputStack);
        List<PotionEffect> newEffectList = new ArrayList<>();

        for (PotionEffect effect : effectList) {
            if (effect.getPotion() == wantedPotion) {
                double currentLengthAugment = Math.max(lengthAugment, BMPotionUtils.getLengthAugment(outputStack, wantedPotion));
                int currentPowerAugment = Math.max(powerAugment, effect.getAmplifier());
                int potionLength = wantedPotion.isInstant() ? 1 : BMPotionUtils.getAugmentedLength(baseEffect.getDuration(), currentLengthAugment, currentPowerAugment);
                newEffectList.add(new PotionEffect(wantedPotion, potionLength, currentPowerAugment));
                BMPotionUtils.setLengthAugment(outputStack, wantedPotion, currentLengthAugment);
            } else {
                newEffectList.add(effect);
            }
        }

        BMPotionUtils.setEffects(outputStack, newEffectList);

        return outputStack;
    }

    public static ItemStack getAugmentedPotionFlask(PotionEffect baseEffect) {
        ItemStack outputStack = new ItemStack(RegistrarBloodMagicItems.POTION_FLASK);

        List<PotionEffect> effectList = new ArrayList<>();
        effectList.add(baseEffect);

        BMPotionUtils.setEffects(outputStack, effectList);

        return outputStack;
    }
}
