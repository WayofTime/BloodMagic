package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.AlchemyArrayRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;

public class AlchemyArrayRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "array/";
//		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/airsigil.png"), Ingredient.fromItems(Items.STONE), Ingredient.fromItems(Items.STONE), new ItemStack(Items.DIAMOND)).build(consumer, BloodMagic.rl(basePath
//				+ "airsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/divinationsigil.png"), Ingredient.fromItems(Items.REDSTONE), Ingredient.fromItems(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.DIVINATION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "divinationsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/watersigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_WATER.get()), Ingredient.fromItems(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.WATER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "watersigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/lavasigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_LAVA.get()), Ingredient.fromItems(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.LAVA_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "lavasigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/voidsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_VOID.get()), Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.VOID_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "voidsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/growthsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_GROWTH.get()), Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.GREEN_GROVE_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "growthsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/fastminersigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_FAST_MINER.get()), Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.FAST_MINER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "fastminersigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/magnetismsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_MAGNETISM.get()), Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.MAGNETISM_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "magnetismsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/lightsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_BLOOD_LIGHT.get()), Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.BLOOD_LIGHT_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "bloodlightsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/airsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_AIR.get()), Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.AIR_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "airsigil"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/sightsigil.png"), Ingredient.fromItems(BloodMagicItems.REAGENT_SIGHT.get()), Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.SEER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "seersigil"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.fromItems(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.fromItems(Items.IRON_HELMET), new ItemStack(BloodMagicItems.LIVING_HELMET.get())).build(consumer, BloodMagic.rl(basePath + "living_helmet"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.fromItems(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.fromItems(Items.IRON_CHESTPLATE), new ItemStack(BloodMagicItems.LIVING_PLATE.get())).build(consumer, BloodMagic.rl(basePath + "living_plate"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.fromItems(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.fromItems(Items.IRON_LEGGINGS), new ItemStack(BloodMagicItems.LIVING_LEGGINGS.get())).build(consumer, BloodMagic.rl(basePath + "living_leggings"));
		AlchemyArrayRecipeBuilder.array(AlchemyArrayRegistry.BINDING_ARRAY, Ingredient.fromItems(BloodMagicItems.REAGENT_BINDING.get()), Ingredient.fromItems(Items.IRON_BOOTS), new ItemStack(BloodMagicItems.LIVING_BOOTS.get())).build(consumer, BloodMagic.rl(basePath + "living_boots"));

		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/movementarray.png"), Ingredient.fromItems(Items.FEATHER), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "movement"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/updraftarray.png"), Ingredient.fromItems(Items.FEATHER), Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "updraft"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/spikearray.png"), Ingredient.fromItems(Items.COBBLESTONE), Ingredient.fromTag(Tags.Items.INGOTS_IRON), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "spike"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/sunarray.png"), Ingredient.fromItems(Items.COAL), Ingredient.fromItems(Items.COAL), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "day"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/moonarray.png"), Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.fromItems(Items.LAPIS_LAZULI), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "night"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/growthsigil.png"), Ingredient.fromTag(Tags.Items.BONES), Ingredient.fromTag(Tags.Items.BONES), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "grove"));
		AlchemyArrayRecipeBuilder.array(BloodMagic.rl("textures/models/alchemyarrays/bouncearray.png"), Ingredient.fromTag(Tags.Items.SLIMEBALLS), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), ItemStack.EMPTY).build(consumer, BloodMagic.rl(basePath + "bounce"));
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