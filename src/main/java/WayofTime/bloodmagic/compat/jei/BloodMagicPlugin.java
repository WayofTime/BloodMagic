package WayofTime.bloodmagic.compat.jei;

import java.util.Map;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
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
import WayofTime.bloodmagic.compat.jei.orb.ShapedOrbRecipeHandler;
import WayofTime.bloodmagic.compat.jei.orb.ShapelessOrbRecipeHandler;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;

@JEIPlugin
public class BloodMagicPlugin extends BlankModPlugin
{
    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        jeiHelper = registry.getJeiHelpers();

        registry.addRecipeCategories(new AltarRecipeCategory(), new BindingRecipeCategory(), new AlchemyArrayCraftingCategory(), new TartaricForgeRecipeCategory());

        registry.addRecipeHandlers(new AltarRecipeHandler(), new BindingRecipeHandler(), new AlchemyArrayCraftingRecipeHandler(), new TartaricForgeRecipeHandler(), new ShapedOrbRecipeHandler(), new ShapelessOrbRecipeHandler());

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
        registry.addRecipes(AlchemyArrayCraftingRecipeMaker.getRecipes());
        registry.addRecipes(TartaricForgeRecipeMaker.getRecipes());

        registry.addDescription(new ItemStack(ModItems.altarMaker), "jei.BloodMagic.desc.altarBuilder");
        registry.addDescription(new ItemStack(ModItems.monsterSoul), "jei.BloodMagic.desc.demonicWill");

        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.bloodLight));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.spectralBlock));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.phantomBlock));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.alchemyArray));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.dimensionalPortal, 1, OreDictionary.WILDCARD_VALUE));

        for (Map.Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet())
        {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel - 1; i++)
            {
                ItemStack stack = new ItemStack(ModItems.upgradeTome);
                ItemUpgradeTome.setKey(stack, key);
                ItemUpgradeTome.setLevel(stack, i);
                jeiHelper.getItemBlacklist().addItemToBlacklist(stack);
            }
        }

        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.OWNER_UUID);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.OWNER_NAME);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.USES);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.SOULS);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.X_COORD);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.Y_COORD);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.Z_COORD);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.DIMENSION_ID);
        jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(Constants.NBT.ITEM_INVENTORY);
    }
}
