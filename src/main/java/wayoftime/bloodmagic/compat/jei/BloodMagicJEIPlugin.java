package wayoftime.bloodmagic.compat.jei;

import java.util.Objects;

import com.google.common.collect.ImmutableSet;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.screens.ScreenFilter;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.compat.jei.alchemytable.AlchemyTableRecipeCategory;
import wayoftime.bloodmagic.compat.jei.alchemytable.PotionRecipeCategory;
import wayoftime.bloodmagic.compat.jei.altar.BloodAltarRecipeCategory;
import wayoftime.bloodmagic.compat.jei.arc.ARCFurnaceRecipeCategory;
import wayoftime.bloodmagic.compat.jei.arc.ARCRecipeCategory;
import wayoftime.bloodmagic.compat.jei.array.AlchemyArrayCraftingCategory;
import wayoftime.bloodmagic.compat.jei.forge.TartaricForgeRecipeCategory;
import wayoftime.bloodmagic.compat.jei.ghostingredienthandlers.GhostFilter;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

@JeiPlugin
public class BloodMagicJEIPlugin implements IModPlugin
{
	public static IJeiHelpers jeiHelper;

	private static final ResourceLocation ID = BloodMagic.rl("jei_plugin");

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.SOUL_FORGE.get()), TartaricForgeRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), BloodAltarRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()), AlchemyArrayCraftingCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()), ARCRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()), ARCFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.ALCHEMY_TABLE.get()), AlchemyTableRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.ALCHEMY_TABLE.get()), PotionRecipeCategory.RECIPE_TYPE);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		jeiHelper = registration.getJeiHelpers();
		registration.addRecipeCategories(new TartaricForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BloodAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new AlchemyArrayCraftingCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new ARCRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new ARCFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new AlchemyTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new PotionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
		registration.addRecipes(TartaricForgeRecipeCategory.RECIPE_TYPE, BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes(world));
		registration.addRecipes(BloodAltarRecipeCategory.RECIPE_TYPE, BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes(world));
		registration.addRecipes(AlchemyArrayCraftingCategory.RECIPE_TYPE, BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArrayRecipes(world));
		registration.addRecipes(ARCRecipeCategory.RECIPE_TYPE, BloodMagicAPI.INSTANCE.getRecipeRegistrar().getARCRecipes(world));
		registration.addRecipes(ARCFurnaceRecipeCategory.RECIPE_TYPE, world.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING));
		registration.addRecipes(AlchemyTableRecipeCategory.RECIPE_TYPE, BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTableRecipes(world));
		registration.addRecipes(PotionRecipeCategory.RECIPE_TYPE , BloodMagicAPI.INSTANCE.getRecipeRegistrar().getPotionFlaskRecipes(world));
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGhostIngredientHandler(ScreenFilter.class, new GhostFilter());
	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}

}
