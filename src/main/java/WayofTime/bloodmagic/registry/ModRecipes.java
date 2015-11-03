package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes {

    public static void init() {
        addAltarRecipes();
    }

    public static void addAltarRecipes() {
        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 0), new ItemStack(Items.diamond), 1, 2000, 2, 1, false);
        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 1), new ItemStack(Items.emerald), 2, 5000, 5, 5, false);
        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 2), new ItemStack(Blocks.gold_block), 3, 25000, 20, 20, false);
//        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 3), new ItemStack(ModItems.weakBloodShard), 4, 40000, 30, 50, false);
//        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 4), new ItemStack(ModItems.demonBloodShard), 5, 75000, 50, 100, false);
//        AltarRecipeRegistry.registerRecipe(new ItemStack(ModItems.bloodOrb, 5), new ItemStack(ModBlocks.blockCrystal), 6, 200000, 100, 200, false);
    }
}
