package WayofTime.bloodmagic.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;

public class ModRecipes {

    public static void init() {
        addAltarRecipes();
        addAlchemyArrayRecipes();
    }

    public static void addAltarRecipes() {
        // ONE
    	AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(OrbRegistry.getOrbStack(ModItems.orbWeak), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 5000, 2, 1, true));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.stone), new ItemStack(ModItems.slate), EnumAltarTier.ONE, 1000, 5, 5, false));

        // TWO
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.emerald), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 2, 1, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate), new ItemStack(ModItems.slate, 1, 1), EnumAltarTier.TWO, 2000, 5, 5, false));

        // THREE
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.gold_block), OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, 25000, 2, 1, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.slate, 1, 2), EnumAltarTier.THREE, 5000, 15, 10, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.obsidian), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.lapis_block), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.magma_cream), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ghast_tear), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5, false));

        // FOUR
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.slate, 1, 3), EnumAltarTier.FOUR, 15000, 20, 20, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.coal_block), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR, 2000, 20, 10, false));

        // FIVE
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.slate, 1, 4), EnumAltarTier.FIVE, 30000, 40, 100, false));

        // SIX
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModBlocks.crystal), OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, 200000, 100, 200, false));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.glowstone), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX, 200000, 100, 200, false));
    }
    
    public static void addAlchemyArrayRecipes() {
    	AlchemyArrayRecipeRegistry.registerCraftingRecipe(new ItemStack(Items.redstone), new ItemStack(ModItems.slate), new ItemStack(Items.diamond), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
    	AlchemyArrayRecipeRegistry.registerCraftingRecipe(new ItemStack(Items.diamond), new ItemStack(ModItems.slate), new ItemStack(Blocks.diamond_block), new BindingAlchemyCircleRenderer());
    }
}
