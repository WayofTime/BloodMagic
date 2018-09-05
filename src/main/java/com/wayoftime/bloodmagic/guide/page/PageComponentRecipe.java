package com.wayoftime.bloodmagic.guide.page;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Supplier;

public class PageComponentRecipe extends PageComponent {

    private final Supplier<IRecipe> recipeGetter;
    private IRecipe recipe;

    public PageComponentRecipe(ResourceLocation recipe) {
        super(60);

        this.recipeGetter = () -> ForgeRegistries.RECIPES.getValue(recipe);
    }
}
