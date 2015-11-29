package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes {
    public static void init() {
        addAltarRecipes();
    }

    public static void addAltarRecipes() {
        AltarRecipeRegistry.registerRecipe(new AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipe(new ItemStack(Items.emerald), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 2, 1, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipe(new ItemStack(Blocks.gold_block), OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, 25000, 2, 1, false));
    }
}
