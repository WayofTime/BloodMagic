package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.AlchemyArrayRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;

public class AlchemyArrayRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "array/";
//		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/airsigil.png"), Ingredient.fromItems(Items.STONE), Ingredient.fromItems(Items.STONE), new ItemStack(Items.DIAMOND)).build(consumer, BloodMagic.rl(basePath
//				+ "airsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/divinationsigil.png"), Ingredient.of(Items.REDSTONE), Ingredient.of(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.DIVINATION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "divinationsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/watersigil.png"), Ingredient.of(BloodMagicItems.REAGENT_WATER.get()), Ingredient.of(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.WATER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "watersigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/lavasigil.png"), Ingredient.of(BloodMagicItems.REAGENT_LAVA.get()), Ingredient.of(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.LAVA_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "lavasigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/voidsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_VOID.get()), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.VOID_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "voidsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/growthsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_GROWTH.get()), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.GREEN_GROVE_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "growthsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/fastminersigil.png"), Ingredient.of(BloodMagicItems.REAGENT_FAST_MINER.get()), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.FAST_MINER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "fastminersigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/magnetismsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_MAGNETISM.get()), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.MAGNETISM_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "magnetismsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/lightsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_BLOOD_LIGHT.get()), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.BLOOD_LIGHT_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "bloodlightsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/airsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_AIR.get()), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.AIR_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "airsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/sightsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_SIGHT.get()), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.SEER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "seersigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/sightsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_HOLDING.get()), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.HOLDING_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "holdingsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/suppressionsigil.png"), Ingredient.of(BloodMagicItems.REAGENT_SUPPRESSION.get()), Ingredient.of(BloodMagicItems.DEMONIC_SLATE.get()), new ItemStack(BloodMagicItems.SUPPRESSION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "suppressionsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/teleportation.png"), Ingredient.of(BloodMagicItems.REAGENT_TELEPOSITION.get()), Ingredient.of(BloodMagicItems.DEMONIC_SLATE.get()), new ItemStack(BloodMagicItems.TELEPOSITION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "telepositionsigil"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.of(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.of(Items.IRON_HELMET), new ItemStack(BloodMagicItems.LIVING_HELMET.get())).build(consumer, BloodMagic.rl(basePath + "living_helmet"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.of(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.of(Items.IRON_CHESTPLATE), new ItemStack(BloodMagicItems.LIVING_PLATE.get())).build(consumer, BloodMagic.rl(basePath + "living_plate"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.of(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.of(Items.IRON_LEGGINGS), new ItemStack(BloodMagicItems.LIVING_LEGGINGS.get())).build(consumer, BloodMagic.rl(basePath + "living_leggings"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.of(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.of(Items.IRON_BOOTS), new ItemStack(BloodMagicItems.LIVING_BOOTS.get())).build(consumer, BloodMagic.rl(basePath + "living_boots"));

		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.of(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.of(Items.DIAMOND), new ItemStack(BloodMagicItems.LIVING_TRAINER.get())).build(consumer, BloodMagic.rl(basePath + "living_trainer"));

		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/movementarray.png"), Ingredient.of(Items.FEATHER), Ingredient.of(Tags.Items.DUSTS_REDSTONE), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "movement"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/updraftarray.png"), Ingredient.of(Items.FEATHER), Ingredient.of(Tags.Items.DUSTS_GLOWSTONE), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "updraft"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/spikearray.png"), Ingredient.of(Items.COBBLESTONE), Ingredient.of(Tags.Items.INGOTS_IRON), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "spike"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/sunarray.png"), Ingredient.of(Items.COAL), Ingredient.of(Items.COAL), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "day"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/moonarray.png"), Ingredient.of(Items.LAPIS_LAZULI), Ingredient.of(Items.LAPIS_LAZULI), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "night"));
//		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/growthsigil.png"), Ingredient.of(Tags.Items.BONES), Ingredient.of(Tags.Items.BONES), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "grove"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/bouncearray.png"), Ingredient.of(Tags.Items.SLIMEBALLS), Ingredient.of(Tags.Items.DUSTS_REDSTONE), new ItemStack(Items.BEDROCK)).build(consumer, BloodMagic.rl(basePath + "bounce"));
		// AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/fastminersigil.png"),
		// Ingredient.fromItems(BloodMagicItems.REAGENT_FAST_MINER.get()),
		// Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new
		// ItemStack(BloodMagicItems.FAST_MINER_SIGIL.get())).build(consumer,
		// BloodMagic.rl(basePath + "frostsigil"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()), AltarTier.ONE.ordinal(), 2000, 2, 1).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath
//				+ "weakbloodorb"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STONE), new ItemStack(BloodMagicItems.SLATE.get()), AltarTier.ONE.ordinal(), 1000, 5, 5).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath
//				+ "slate"));

	}
}