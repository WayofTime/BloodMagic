package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingCategory;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeCategory;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeHandler;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeMaker;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;

import java.util.Map;

@JEIPlugin
public class BloodMagicPlugin implements IModPlugin
{
    public static IJeiHelpers jeiHelper;

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeCategories(new AltarRecipeCategory(), new BindingRecipeCategory(), new AlchemyArrayCraftingCategory(), new TartaricForgeRecipeCategory());

        registry.addRecipeHandlers(new AltarRecipeHandler(), new BindingRecipeHandler(), new AlchemyArrayCraftingRecipeHandler(), new TartaricForgeRecipeHandler());

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
        registry.addRecipes(AlchemyArrayCraftingRecipeMaker.getRecipes());
        registry.addRecipes(TartaricForgeRecipeMaker.getRecipes());

        registry.addDescription(new ItemStack(ModItems.altarMaker), "jei.BloodMagic.desc.altarBuilder");

        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.bloodLight));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.spectralBlock));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.phantomBlock));

        for (Map.Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet())
        {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel - 1; i++)
            {
                ItemStack stack = new ItemStack(ModItems.upgradeTome);
                ((ItemUpgradeTome)ModItems.upgradeTome).setKey(stack, key);
                ((ItemUpgradeTome)ModItems.upgradeTome).setLevel(stack, i);
                jeiHelper.getItemBlacklist().addItemToBlacklist(stack);
            }
        }

        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.OWNER_UUID);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.SOULS);
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {
        jeiHelper = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {

    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }
}
