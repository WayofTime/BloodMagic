package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingCategory;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BloodMagicPlugin implements IModPlugin
{
    public static IJeiHelpers jeiHelper;

    @Override
    public boolean isModLoaded()
    {
        return true;
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeCategories(new AltarRecipeCategory(), new BindingRecipeCategory(), new AlchemyArrayCraftingCategory());

        registry.addRecipeHandlers(new AltarRecipeHandler(), new BindingRecipeHandler(), new AlchemyArrayCraftingRecipeHandler());

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
        registry.addRecipes(AlchemyArrayCraftingRecipeMaker.getRecipes());

        registry.addDescription(new ItemStack(ModItems.altarMaker), "jei.BloodMagic.desc.altarBuilder");
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {
        jeiHelper = jeiHelpers;

        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.bloodLight));
        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.spectralBlock));
        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.phantomBlock));

        jeiHelpers.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.OWNER_UUID);
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
