package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.block.enums.EnumDecorative;
import WayofTime.bloodmagic.client.gui.GuiSoulForge;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingCategory;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeJEI;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeCategory;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeJEI;
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
import WayofTime.bloodmagic.core.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.ItemHelper.LivingUpgrades;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

        registry.addRecipes(getAnvilRecipes(), VanillaRecipeCategoryUid.ANVIL);

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

        if (!ConfigHandler.general.enableTierSixEvenThoughThereIsNoContent) {
            jeiHelper.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(RegistrarBloodMagicBlocks.DECORATIVE_BRICK, 1, EnumDecorative.CRYSTAL_TILE.ordinal()));
            jeiHelper.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(RegistrarBloodMagicBlocks.DECORATIVE_BRICK, 1, EnumDecorative.CRYSTAL_BRICK.ordinal()));
            jeiHelper.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(RegistrarBloodMagicItems.INSCRIPTION_TOOL, 1, EnumRuneType.DAWN.ordinal()));
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(RegistrarBloodMagicItems.UPGRADE_TOME);
        subtypeRegistry.useNbtForSubtypes(RegistrarBloodMagicItems.POTION_FLASK);
        subtypeRegistry.registerSubtypeInterpreter(RegistrarBloodMagicItems.BLOOD_ORB, stack -> {
            if (!(stack.getItem() instanceof IBloodOrb))
                return ISubtypeRegistry.ISubtypeInterpreter.NONE;

            BloodOrb orb = ((IBloodOrb) stack.getItem()).getOrb(stack);
            if (orb == null)
                return ISubtypeRegistry.ISubtypeInterpreter.NONE;

            return orb.getRegistryName().toString();
        });
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

    public Collection<IRecipeWrapper> getAnvilRecipes() {
        IVanillaRecipeFactory vanillaRecipeFactory = jeiHelper.getVanillaRecipeFactory();


        /* Sentient Tool repair recipes */

        List<ItemStack> outputSwords = new LinkedList<>();
        List<ItemStack> outputPickaxes = new LinkedList<>();
        List<ItemStack> outputAxes = new LinkedList<>();
        List<ItemStack> outputBows = new LinkedList<>();
        List<ItemStack> outputShovels = new LinkedList<>();

        List<ItemStack> inputRightSentient = new LinkedList<>();

        List<List<ItemStack>> sentientOutputs = new LinkedList<>();

        List<ItemStack> sentientTools = new LinkedList<>();
        sentientTools.add(new ItemStack(RegistrarBloodMagicItems.SENTIENT_AXE));
        sentientTools.add(new ItemStack(RegistrarBloodMagicItems.SENTIENT_PICKAXE));
        sentientTools.add(new ItemStack(RegistrarBloodMagicItems.SENTIENT_BOW));
        sentientTools.add(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SHOVEL));
        sentientTools.add(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SWORD));

        for (int i = 4; i > 0; i--) {
            for (ItemStack j : sentientTools) {
                int maxDmg = j.getMaxDamage();
                j.setItemDamage(maxDmg - (maxDmg / 4) * i);
            }
            outputAxes.add(sentientTools.get(0).copy());
            outputPickaxes.add(sentientTools.get(1).copy());
            outputBows.add(sentientTools.get(2).copy());
            outputShovels.add(sentientTools.get(3).copy());
            outputSwords.add(sentientTools.get(4).copy());

            inputRightSentient.add(new ItemStack(RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL, i));
        }
        sentientOutputs.add(outputAxes);
        sentientOutputs.add(outputPickaxes);
        sentientOutputs.add(outputBows);
        sentientOutputs.add(outputShovels);
        sentientOutputs.add(outputSwords);


        Collection<IRecipeWrapper> collection = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            ItemStack inputLeft = sentientTools.get(i);
            inputLeft.setItemDamage(inputLeft.getMaxDamage());
            collection.add(vanillaRecipeFactory.createAnvilRecipe(inputLeft, inputRightSentient, sentientOutputs.get(i)));
        }

        /* Living Armor repair recipes */

        List<ItemStack> outputHelmets = new LinkedList<>();
        List<ItemStack> outputChestplates = new LinkedList<>();
        List<ItemStack> outputLeggings = new LinkedList<>();
        List<ItemStack> outputBoots = new LinkedList<>();

        List<ItemStack> inputRightLiving = new LinkedList<>();

        List<List<ItemStack>> livingOutputs = new LinkedList<>();

        List<ItemStack> livingTools = new LinkedList<>();
        livingTools.add(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET));
        livingTools.add(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST));
        livingTools.add(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS));
        livingTools.add(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS));

        for (int i = 4; i > 0; i--) {
            for (ItemStack j : livingTools) {
                int maxDmg = j.getMaxDamage();
                j.setItemDamage(maxDmg - (maxDmg / 4) * i);
            }
            outputHelmets.add(livingTools.get(0).copy());
            outputChestplates.add(livingTools.get(1).copy());
            outputLeggings.add(livingTools.get(2).copy());
            outputBoots.add(livingTools.get(3).copy());

            inputRightLiving.add(new ItemStack(RegistrarBloodMagicItems.COMPONENT, i, 8));
        }
        livingOutputs.add(outputHelmets);
        livingOutputs.add(outputChestplates);
        livingOutputs.add(outputLeggings);
        livingOutputs.add(outputBoots);

        for (int i = 0; i < 4; i++) {
            ItemStack inputLeft = livingTools.get(i);
            inputLeft.setItemDamage(inputLeft.getMaxDamage());
            collection.add(vanillaRecipeFactory.createAnvilRecipe(inputLeft, inputRightLiving, livingOutputs.get(i)));
        }

        return collection;
    }
}
