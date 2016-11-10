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
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.client.gui.GuiSoulForge;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingCategory;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeCategory;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeHandler;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeMaker;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeCategory;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeHandler;
import WayofTime.bloodmagic.compat.jei.armourDowngrade.ArmourDowngradeRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeCategory;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeHandler;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeMaker;
import WayofTime.bloodmagic.compat.jei.orb.ShapedOrbRecipeHandler;
import WayofTime.bloodmagic.compat.jei.orb.ShapelessOrbRecipeHandler;
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

        registry.addRecipeCategories(new AltarRecipeCategory(), new BindingRecipeCategory(), new AlchemyArrayCraftingCategory(), new TartaricForgeRecipeCategory(), new AlchemyTableRecipeCategory(), new ArmourDowngradeRecipeCategory());

        registry.addRecipeHandlers(new AltarRecipeHandler(), new BindingRecipeHandler(), new AlchemyArrayCraftingRecipeHandler(), new TartaricForgeRecipeHandler(), new AlchemyTableRecipeHandler(), new ArmourDowngradeRecipeHandler(), new ShapedOrbRecipeHandler(), new ShapelessOrbRecipeHandler());

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
        registry.addRecipes(AlchemyArrayCraftingRecipeMaker.getRecipes());
        registry.addRecipes(TartaricForgeRecipeMaker.getRecipes());
        registry.addRecipes(AlchemyTableRecipeMaker.getRecipes());
        registry.addRecipes(ArmourDowngradeRecipeMaker.getRecipes());

        registry.addDescription(new ItemStack(ModItems.ALTAR_MAKER), "jei.BloodMagic.desc.altarBuilder");
        registry.addDescription(new ItemStack(ModItems.MONSTER_SOUL), "jei.BloodMagic.desc.demonicWill");

        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.BLOOD_LIGHT));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.SPECTRAL_BLOCK));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.PHANTOM_BLOCK));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.ALCHEMY_ARRAY));
        jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.DIMENSIONAL_PORTAL, 1, OreDictionary.WILDCARD_VALUE));

        for (Map.Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet())
        {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel - 1; i++)
            {
                ItemStack stack = new ItemStack(ModItems.UPGRADE_TOME);
                LivingUpgrades.setKey(stack, key);
                LivingUpgrades.setLevel(stack, i);
                jeiHelper.getItemBlacklist().addItemToBlacklist(stack);
            }
        }

        registry.addRecipeClickArea(GuiSoulForge.class, 115, 15, 16, 88, Constants.Compat.JEI_CATEGORY_SOULFORGE);

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.ALTAR), Constants.Compat.JEI_CATEGORY_ALTAR);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.SOUL_FORGE), Constants.Compat.JEI_CATEGORY_SOULFORGE);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.ARCANE_ASHES), Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.ARCANE_ASHES), Constants.Compat.JEI_CATEGORY_BINDING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.ALCHEMY_TABLE), Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.RITUAL_CONTROLLER), Constants.Compat.JEI_CATEGORY_ARMOURDOWNGRADE);

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
