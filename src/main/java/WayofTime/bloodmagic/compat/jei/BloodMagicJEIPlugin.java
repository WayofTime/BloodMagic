package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeJEI;
import WayofTime.bloodmagic.core.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.client.gui.GuiSoulForge;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingCategory;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeJEI;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeJEI;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeCategory;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeHandler;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeCategory;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeJEI;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

@JEIPlugin
public class BloodMagicJEIPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelper = registry.getJeiHelpers();

        registry.addRecipeHandlers(
                new BindingRecipeHandler(),
                new ArmourDowngradeRecipeHandler()
        );

        registry.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes(), Constants.Compat.JEI_CATEGORY_ALTAR);
        registry.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes(), Constants.Compat.JEI_CATEGORY_SOULFORGE);
        registry.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArrayRecipes(), Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY);
        registry.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyRecipes(), Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);
        registry.addRecipes(AlchemyTableRecipeRegistry.getRecipeList(), Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);

        registry.handleRecipes(RecipeBloodAltar.class, AltarRecipeJEI::new, Constants.Compat.JEI_CATEGORY_ALTAR);
        registry.handleRecipes(RecipeTartaricForge.class, TartaricForgeRecipeJEI::new, Constants.Compat.JEI_CATEGORY_SOULFORGE);
        registry.handleRecipes(RecipeAlchemyArray.class, AlchemyArrayCraftingRecipeJEI::new, Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY);
        registry.handleRecipes(RecipeAlchemyTable.class, AlchemyTableRecipeJEI::new, Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);
        registry.handleRecipes(AlchemyTableRecipe.class, AlchemyTableRecipeJEI::new, Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);

        registry.addRecipes(BindingRecipeMaker.getRecipes());
        registry.addRecipes(ArmourDowngradeRecipeMaker.getRecipes());

        registry.addIngredientInfo(new ItemStack(RegistrarBloodMagicItems.ALTAR_MAKER), ItemStack.class, "jei.bloodmagic.desc.altarBuilder");
        registry.addIngredientInfo(new ItemStack(RegistrarBloodMagicItems.MONSTER_SOUL), ItemStack.class, "jei.bloodmagic.desc.demonicWill");

        for (Map.Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet()) {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel - 1; i++) {
                ItemStack stack = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
                LivingUpgrades.setKey(stack, key);
                LivingUpgrades.setLevel(stack, i);
                jeiHelper.getIngredientBlacklist().addIngredientToBlacklist(stack);
            }
        }

        registry.addRecipeClickArea(GuiSoulForge.class, 115, 15, 16, 88, Constants.Compat.JEI_CATEGORY_SOULFORGE);

        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicBlocks.ALTAR), Constants.Compat.JEI_CATEGORY_ALTAR);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicBlocks.SOUL_FORGE), Constants.Compat.JEI_CATEGORY_SOULFORGE);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES), Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES), Constants.Compat.JEI_CATEGORY_BINDING);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicBlocks.ALCHEMY_TABLE), Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicBlocks.RITUAL_CONTROLLER), Constants.Compat.JEI_CATEGORY_ARMOURDOWNGRADE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(RegistrarBloodMagicItems.UPGRADE_TOME);
        subtypeRegistry.useNbtForSubtypes(RegistrarBloodMagicItems.BLOOD_ORB);
        subtypeRegistry.useNbtForSubtypes(RegistrarBloodMagicItems.POTION_FLASK);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (jeiHelper == null)
            jeiHelper = registry.getJeiHelpers();

        registry.addRecipeCategories(
                new AltarRecipeCategory(),
                new BindingRecipeCategory(),
                new AlchemyArrayCraftingCategory(),
                new TartaricForgeRecipeCategory(),
                new AlchemyTableRecipeCategory(),
                new ArmourDowngradeRecipeCategory()
        );
    }
}
