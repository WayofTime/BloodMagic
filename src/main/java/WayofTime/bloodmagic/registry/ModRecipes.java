package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.item.ItemComponent;
import net.minecraftforge.oredict.RecipeSorter;

public class ModRecipes {

	public static void init() {
		RecipeSorter.register(Constants.Mod.DOMAIN + "shapedorb", ShapedBloodOrbRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shapeless");
		RecipeSorter.register(Constants.Mod.DOMAIN + ":shapelessorb", ShapelessBloodOrbRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

		addCraftingRecipes();
		addAltarRecipes();
		addAlchemyArrayRecipes();
	}

	public static void addCraftingRecipes() {
		GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), "xox", "oSo", "xox", 'S', OrbRegistry.getOrbStack(ModItems.orbMagician), 'o', new ItemStack(Items.redstone), 'x', new ItemStack(Items.glowstone_dust)));
		// Added for testing
		GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(Items.gold_ingot), new ItemStack(ModItems.slate, 1, 1), OrbRegistry.getOrbStack(ModItems.orbApprentice)));
		GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(Items.diamond), "   ", " s ", " o ", 's', new ItemStack(ModItems.slate), 'o', OrbRegistry.getOrbStack(ModItems.orbWeak)));

		GameRegistry.addRecipe(new ItemStack(ModItems.ritualDiviner), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', new ItemStack(Items.diamond), 's', new ItemStack(Items.stick));
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
		AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_sword), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundSword)), new BindingAlchemyCircleRenderer());
		AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_axe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundAxe)));
		AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_pickaxe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundPickaxe)));
		AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_shovel), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundShovel)));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilWater), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilLava), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilAir), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilFastMiner), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilVoid), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilGreenGrove), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilElementalAffinity), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
		AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilSeer), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
	}

	public static void addCompressionHandlers() {
		StorageBlockCraftingManager.getInstance().addStorageBlockRecipes();
		CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.glowstone_dust, 4, 0), new ItemStack(Blocks.glowstone), 64));
		CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.snowball, 4, 0), new ItemStack(Blocks.snow), 8));
		CompressionRegistry.registerHandler(new AdvancedCompressionHandler());

		CompressionRegistry.registerItemThreshold(new ItemStack(Blocks.cobblestone), 64);
	}
}
