package wayoftime.bloodmagic.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.alchemyarray.AlchemyArrayRecipe;
import wayoftime.bloodmagic.common.recipe.alchemytable.AlchemyTableRecipe;
import wayoftime.bloodmagic.common.recipe.bloodaltar.BloodAltarRecipe;
import wayoftime.bloodmagic.common.recipe.forge.ForgeRecipe;
import wayoftime.bloodmagic.compat.jei.alchemytable.AlchemyTableRecipeCategory;
import wayoftime.bloodmagic.compat.jei.altar.BloodAltarRecipeCategory;
import wayoftime.bloodmagic.compat.jei.array.AlchemyArrayCraftingCategory;
import wayoftime.bloodmagic.compat.jei.forge.SoulForgeRecipeCategory;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class BloodMagicJEIPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelper;
    private static final ResourceLocation ID = BloodMagic.rl("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new SoulForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BloodAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyArrayCraftingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BMBlocks.HELLFIRE_FORGE.block().get()), SoulForgeRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BMBlocks.BLOOD_ALTAR.block().get()), BloodAltarRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BMItems.ARCANE_ASHES.get()), AlchemyArrayCraftingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BMBlocks.ALCHEMY_TABLE.block().get()), AlchemyTableRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);

        // Soul Forge recipes
        List<ForgeRecipe> forgeRecipes = world.getRecipeManager()
                .getAllRecipesFor(BMRecipes.SOUL_FORGE_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(SoulForgeRecipeCategory.RECIPE_TYPE, forgeRecipes);

        // Blood Altar recipes
        List<BloodAltarRecipe> altarRecipes = world.getRecipeManager()
                .getAllRecipesFor(BMRecipes.BLOOD_ALTAR_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(BloodAltarRecipeCategory.RECIPE_TYPE, altarRecipes);

        // Alchemy Array recipes
        List<AlchemyArrayRecipe> arrayRecipes = world.getRecipeManager()
                .getAllRecipesFor(BMRecipes.ALCHEMY_ARRAY_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(AlchemyArrayCraftingCategory.RECIPE_TYPE, arrayRecipes);

        // Alchemy Table recipes
        List<AlchemyTableRecipe> tableRecipes = world.getRecipeManager()
                .getAllRecipesFor(BMRecipes.ALCHEMY_TABLE_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(AlchemyTableRecipeCategory.RECIPE_TYPE, tableRecipes);
    }
}
