package com.wayoftime.bloodmagic.core;

import com.wayoftime.bloodmagic.api.impl.BloodMagicRecipeRegistrar;
import com.wayoftime.bloodmagic.core.altar.AltarTier;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreIngredient;

import static com.wayoftime.bloodmagic.core.RegistrarBloodMagicItems.*;

public class RegistrarBloodMagicRecipes {

    public static void registerAltarRecipes(BloodMagicRecipeRegistrar registrar) {
        // Tier 1
        registrar.addBloodAltar(new OreIngredient("gemDiamond"), new ItemStack(BLOOD_ORB_WEAK), AltarTier.ONE, 2000, 2, 1);
        registrar.addBloodAltar(new OreIngredient("stone"), new ItemStack(SLATE_BLANK), AltarTier.ONE, 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.BUCKET), FluidUtil.getFilledBucket(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, 1000)), AltarTier.ONE, 1000, 5, 5);

        // Tier 2
        registrar.addBloodAltar(new OreIngredient("blockRedstone"), new ItemStack(BLOOD_ORB_APPRENTICE), AltarTier.TWO, 5000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(SLATE_BLANK), new ItemStack(SLATE_REINFORCED), AltarTier.TWO, 2000, 5, 5);

        // Tier 3
        registrar.addBloodAltar(new OreIngredient("blockGold"), new ItemStack(BLOOD_ORB_MAGICIAN), AltarTier.THREE, 25000, 20, 20);
        registrar.addBloodAltar(Ingredient.fromItem(SLATE_REINFORCED), new ItemStack(SLATE_IMBUED), AltarTier.THREE, 5000, 15, 10);

        // Tier 4
        registrar.addBloodAltar(new OreIngredient("ingotIron"), new ItemStack(BLOOD_ORB_MASTER), AltarTier.FOUR, 40000, 30, 50); // TODO - Blood Shard input
        registrar.addBloodAltar(Ingredient.fromItem(SLATE_IMBUED), new ItemStack(SLATE_DEMONIC), AltarTier.FOUR, 15000, 20, 20);

        // Tier 5
        registrar.addBloodAltar(new OreIngredient("netherStar"), new ItemStack(BLOOD_ORB_ARCHMAGE), AltarTier.FIVE, 80000, 50, 100);
        registrar.addBloodAltar(Ingredient.fromItem(SLATE_DEMONIC), new ItemStack(SLATE_ETHEREAL), AltarTier.FIVE, 30000, 40, 100);

        // Tier 6
        registrar.addBloodAltar(new OreIngredient("gemDiamond"), new ItemStack(BLOOD_ORB_TRANSCENDENT), AltarTier.SIX, 200000, 100, 200); // TODO - Whatever this input is supposed to be
    }
}
