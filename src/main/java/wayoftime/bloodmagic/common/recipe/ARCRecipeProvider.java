package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCPotionRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

public class ARCRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "arc/";
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.BONES), null, new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), null).addRandomOutput(new ItemStack(Items.DIAMOND, 2), 0.5).build(consumer, BloodMagic.rl(basePath + "test1"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.BONES), null, new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), null).addRandomOutput(new ItemStack(Items.DIAMOND, 5), 0.5).build(consumer, BloodMagic.rl(basePath + "test3"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromItems(Items.ACACIA_BOAT), FluidStackIngredient.from(Fluids.LAVA, 1000), new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), new FluidStack(Fluids.WATER, 100)).build(consumer, BloodMagic.rl(basePath + "test2"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.NETHERRACK), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), FluidStackIngredient.from(Fluids.LAVA, 1000), new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), new FluidStack(Fluids.WATER, 100)).addRandomOutput(new ItemStack(BloodMagicItems.SLATE.get()), 0.2).addRandomOutput(new ItemStack(BloodMagicItems.REINFORCED_SLATE.get()), 0.1).addRandomOutput(new ItemStack(BloodMagicItems.IMBUED_SLATE.get()), 0.001).build(consumer, BloodMagic.rl(basePath + "test4"));

		ARCRecipeBuilder.arc(Ingredient.of(Items.IRON_ORE), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "ore/dustiron"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.GOLD_ORE), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "ore/dustgold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.NETHERRACK), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.SULFUR.get()), new FluidStack(Fluids.LAVA, 50)).build(consumer, BloodMagic.rl(basePath + "netherrack_to_sulfer"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.TERRACOTTA), Ingredient.of(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Blocks.CLAY), null).build(consumer, BloodMagic.rl(basePath + "clay_from_terracotta"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.SAND), Ingredient.of(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Items.CLAY_BALL), null).addRandomOutput(new ItemStack(Items.CLAY_BALL), 0.5).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));

		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicItems.STRONG_TAU_ITEM.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard_tau"));
//		ARCRecipeBuilder.arc(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard"));

//		ConditionalRecipe.builder().addCondition(new TagEmptyCondition(Tags.Items.ORES_IRON.getName()));

		ARCPotionRecipeBuilder.arc(Ingredient.of(BloodMagicItems.THROWING_DAGGER_COPPER.get()), Ingredient.of(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()), null, new ItemStack(BloodMagicItems.THROWING_DAGGER_COPPER_POTION.get(), 8), null).setRequiredInputCount(8).build(consumer, BloodMagic.rl(basePath + "tipped_copper"));

		addReversionRecipes(consumer);
		addSandRecipes(consumer);
		addFragmentRecipes(consumer);
		addGravelRecipes(consumer);
	}

	private ICondition getTagCondition(TagKey<Item> tag)
	{
		return new NotCondition(new TagEmptyCondition(tag.location()));
	}

	private void addSandRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "arc/dusts";

		// raw to dust: 1.5x
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.IRON_SAND.get()), 0.33, 0.17).build(consumer, BloodMagic.rl(basePath + "from_raw_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.GOLD_SAND.get()), 0.33, 0.17).build(consumer, BloodMagic.rl(basePath + "from_raw_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.COPPER_SAND.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.COPPER_SAND.get()), 0.33, 0.17).build(consumer, BloodMagic.rl(basePath + "from_raw_copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.RAW_HELLFORGED), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.HELLFORGED_SAND.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_SAND.get()), 0.33, 0.17).build(consumer, BloodMagic.rl(basePath + "from_raw_hellforged"));

		// Ore to dust
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "from_ore_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "from_ore_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.COPPER_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "from_ore_copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.ORE_HELLFORGED), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.HELLFORGED_SAND.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "from_ore_hellforged"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "from_ore_netherite_scrap"));

		// Ingot to dust
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.COPPER_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.INGOT_HELLFORGED), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.HELLFORGED_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_hellforged"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_netherite_scrap"));

		// Gravel to dust
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.COPPER_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_DEMONITE), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.HELLFORGED_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_hellforged"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_netherite_scrap"));
	}

	private void addFragmentRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "arc/fragments";

		double rawToFragmentExtra = 0.25;
		double oreToFragmentExtra = 0.5;

		// raw to fragment: 2.25x
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_FRAGMENT.get(), 2), null).addRandomOutput(new ItemStack(BloodMagicItems.IRON_FRAGMENT.get()), rawToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_FRAGMENT.get(), 2), null).addRandomOutput(new ItemStack(BloodMagicItems.GOLD_FRAGMENT.get()), rawToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.COPPER_FRAGMENT.get(), 2), null).addRandomOutput(new ItemStack(BloodMagicItems.COPPER_FRAGMENT.get()), rawToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.RAW_HELLFORGED), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.DEMONITE_FRAGMENT.get(), 2), null).addRandomOutput(new ItemStack(BloodMagicItems.DEMONITE_FRAGMENT.get()), rawToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "hellforged"));

		// ore to fragment: 4.5x
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_FRAGMENT.get(), 4), null).addRandomOutput(new ItemStack(BloodMagicItems.IRON_FRAGMENT.get()), oreToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "from_ore_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_FRAGMENT.get(), 4), null).addRandomOutput(new ItemStack(BloodMagicItems.GOLD_FRAGMENT.get()), oreToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "from_ore_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.COPPER_FRAGMENT.get(), 4), null).addRandomOutput(new ItemStack(BloodMagicItems.COPPER_FRAGMENT.get()), oreToFragmentExtra).build(consumer, BloodMagic.rl(basePath + "from_ore_copper"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "netherite_scrap"));
	}

	private void addGravelRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "arc/gravels";
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.IRON_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.5).build(consumer, BloodMagic.rl(basePath + "iron"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.GOLD_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.5).build(consumer, BloodMagic.rl(basePath + "gold"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_COPPER), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.COPPER_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.25).build(consumer, BloodMagic.rl(basePath + "copper"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_DEMONITE), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.DEMONITE_GRAVEL.get()), null).build(consumer, BloodMagic.rl(basePath + "hellforged"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.5).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.5).build(consumer, BloodMagic.rl(basePath + "netherite_scrap"));
	}

	private void addReversionRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "arc/reversion/";
		// ONE
		registerReversionRecipe(Ingredient.of(BloodMagicItems.WEAK_BLOOD_ORB.get()), new ItemStack(Items.DIAMOND), consumer, basePath + "weak_blood_orb");

		// TWO
		registerReversionRecipe(Ingredient.of(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()), new ItemStack(Blocks.REDSTONE_BLOCK), consumer, basePath + "apprentice_blood_orb");

		// THREE
		registerReversionRecipe(Ingredient.of(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()), new ItemStack(Blocks.GOLD_BLOCK), consumer, basePath + "magician_blood_orb");

		// FOUR
		registerReversionRecipe(Ingredient.of(BloodMagicItems.MASTER_BLOOD_ORB.get()), new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), consumer, basePath + "master_blood_orb");

		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "self_sac"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.ACCELERATION_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.ACCELERATION_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "acceleration"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "aug_capacity"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.CAPACITY_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.CAPACITY_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "capacity"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.CHARGING_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.CHARGING_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "charging"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.DISPLACEMENT_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.DISPLACEMENT_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "displacement"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.ORB_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.ORB_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "orb_rune"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.SPEED_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.SPEED_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "speed"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicBlocks.SACRIFICE_RUNE_2.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicBlocks.SACRIFICE_RUNE.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.HELLFORGED_PARTS.get()), 1, 0).addRandomOutput(new ItemStack(Items.NETHERITE_SCRAP, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "sac"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(Items.NETHERITE_SCRAP, 4), null).addRandomOutput(new ItemStack(Items.GOLD_INGOT, 4), 1, 0).build(consumer, BloodMagic.rl(basePath + "netherite_ingot"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicItems.BLEEDING_EDGE_MUSIC.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.DEMONITE_RAW.get(), 9), null).build(consumer, BloodMagic.rl(basePath + "bleeding_edge"));

	}

	private void registerReversionRecipe(Ingredient input, ItemStack outputStack, Consumer<FinishedRecipe> consumer, String path)
	{
		ARCRecipeBuilder.arcConsume(input, Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, outputStack, null).build(consumer, BloodMagic.rl(path));
	}
}
