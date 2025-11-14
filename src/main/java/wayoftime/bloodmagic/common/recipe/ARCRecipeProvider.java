package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCPotionRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCRecipeBuilder;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

public class ARCRecipeProvider implements ISubRecipeProvider
{

    private static final Ingredient hydration = Ingredient.of(BloodMagicTags.ARC_TOOL_HYDRATE);
    private static final FluidStackIngredient water = FluidStackIngredient.from(Fluids.WATER, 200);
    private static final FluidStackIngredient cauldron_use = FluidStackIngredient.from(Fluids.WATER, 333);

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

		ARCRecipeBuilder.arc(Ingredient.of(Items.TERRACOTTA), hydration, water, new ItemStack(Blocks.CLAY), null).build(consumer, BloodMagic.rl(basePath + "clay_from_terracotta"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.SAND), hydration, water, new ItemStack(Items.CLAY_BALL), null).addRandomOutput(new ItemStack(Items.CLAY_BALL), 0.5).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.DIRT), hydration, water, new ItemStack(Blocks.MUD), null).build(consumer, BloodMagic.rl(basePath + "mud_from_dirt"));

        ARCRecipeBuilder.arc(Ingredient.of(Blocks.COBBLESTONE), hydration, water, new ItemStack(Blocks.MOSSY_COBBLESTONE), null).build(consumer, BloodMagic.rl(basePath + "mossify_cobblestone"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.COBBLESTONE_SLAB), hydration, water, new ItemStack(Blocks.MOSSY_COBBLESTONE_SLAB), null).build(consumer, BloodMagic.rl(basePath + "mossify_cobblestone_slab"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.COBBLESTONE_STAIRS), hydration, water, new ItemStack(Blocks.MOSSY_COBBLESTONE_STAIRS), null).build(consumer, BloodMagic.rl(basePath + "mossify_cobblestone_stairs"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.COBBLESTONE_WALL), hydration, water, new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL), null).build(consumer, BloodMagic.rl(basePath + "mossify_cobblestone_wall"));

        ARCRecipeBuilder.arc(Ingredient.of(Blocks.STONE_BRICKS), hydration, water, new ItemStack(Blocks.MOSSY_STONE_BRICKS), null).build(consumer, BloodMagic.rl(basePath + "mossify_stone_bricks"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.STONE_BRICK_SLAB), hydration, water, new ItemStack(Blocks.MOSSY_STONE_BRICK_SLAB), null).build(consumer, BloodMagic.rl(basePath + "mossify_stone_brick_slab"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.STONE_BRICK_STAIRS), hydration, water, new ItemStack(Blocks.MOSSY_STONE_BRICK_STAIRS), null).build(consumer, BloodMagic.rl(basePath + "mossify_stone_brick_stairs"));
        ARCRecipeBuilder.arc(Ingredient.of(Blocks.STONE_BRICK_WALL), hydration, water, new ItemStack(Blocks.MOSSY_STONE_BRICK_WALL), null).build(consumer, BloodMagic.rl(basePath + "mossify_stone_brick_wall"));

        oxidize(consumer, Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER);
        oxidize(consumer, Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER);
        oxidize(consumer, Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS);
        oxidize(consumer, Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB);

        solidifyConcrete(consumer, Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE);
        solidifyConcrete(consumer, Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE);
        solidifyConcrete(consumer, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE);
        solidifyConcrete(consumer, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE);
        solidifyConcrete(consumer, Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE);
        solidifyConcrete(consumer, Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE);
        solidifyConcrete(consumer, Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE);
        solidifyConcrete(consumer, Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE);
        solidifyConcrete(consumer, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE);
        solidifyConcrete(consumer, Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE);
        solidifyConcrete(consumer, Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE);
        solidifyConcrete(consumer, Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE);
        solidifyConcrete(consumer, Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE);
        solidifyConcrete(consumer, Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE);
        solidifyConcrete(consumer, Blocks.RED_CONCRETE_POWDER, Blocks.RED_CONCRETE);
        solidifyConcrete(consumer, Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE);

        ARCRecipeBuilder.arc(Ingredient.of(ItemTags.BEDS), hydration, cauldron_use, new ItemStack(Items.WHITE_BED), null).build(consumer, BloodMagic.rl(basePath + "wash_bed"));
        ARCRecipeBuilder.arc(Ingredient.of(ItemTags.WOOL), hydration, cauldron_use, new ItemStack(Items.WHITE_WOOL), null).build(consumer, BloodMagic.rl(basePath + "wash_wool"));
        ARCRecipeBuilder.arc(Ingredient.of(ItemTags.WOOL_CARPETS), hydration, cauldron_use, new ItemStack(Items.WHITE_CARPET), null).build(consumer, BloodMagic.rl(basePath + "wash_carpet"));
        ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.GLASS), hydration, cauldron_use, new ItemStack(Items.GLASS), null).build(consumer, BloodMagic.rl(basePath + "wash_glass"));
        ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.GLASS_PANES), hydration, cauldron_use, new ItemStack(Items.GLASS_PANE), null).build(consumer, BloodMagic.rl(basePath + "wash_glass_pane"));

        ARCRecipeBuilder.arc(Ingredient.of(BloodMagicItems.WEAK_TAU_ITEM.get()), hydration, FluidStackIngredient.from(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1600), new ItemStack(BloodMagicItems.STRONG_TAU_ITEM.get()), null).build(consumer, BloodMagic.rl(basePath + "strengthen_tau"));

		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicItems.STRONG_TAU_ITEM.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard_tau"));
//		ARCRecipeBuilder.arc(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard"));

//		ConditionalRecipe.builder().addCondition(new TagEmptyCondition(Tags.Items.ORES_IRON.getName()));

		ARCPotionRecipeBuilder.arc(Ingredient.of(BloodMagicItems.THROWING_DAGGER_COPPER.get()), Ingredient.of(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()), null, new ItemStack(BloodMagicItems.THROWING_DAGGER_COPPER_POTION.get(), 8), null).setRequiredInputCount(8).build(consumer, BloodMagic.rl(basePath + "tipped_copper"));

		addReversionRecipes(consumer);
		addSandRecipes(consumer);
		addFragmentRecipes(consumer);
		addGravelRecipes(consumer);
	}

    private void solidifyConcrete(Consumer<FinishedRecipe> consumer, Block powder, Block concrete) {
        ARCRecipeBuilder.arc(Ingredient.of(powder), hydration, water, new ItemStack(concrete), null).build(consumer, BloodMagic.rl("arc/solidify_" + ForgeRegistries.BLOCKS.getKey(concrete).getPath()));
    }

    private void oxidize(Consumer<FinishedRecipe> consumer, Block start, Block exposed, Block weathered, Block oxidized) {
        ARCRecipeBuilder.arc(Ingredient.of(start), hydration, water, new ItemStack(exposed), null).build(consumer, BloodMagic.rl("arc/oxidize/" + ForgeRegistries.BLOCKS.getKey(start).getPath() + "_to_" + ForgeRegistries.BLOCKS.getKey(exposed).getPath()));
        ARCRecipeBuilder.arc(Ingredient.of(exposed), hydration, water, new ItemStack(weathered), null).build(consumer, BloodMagic.rl("arc/oxidize/" + ForgeRegistries.BLOCKS.getKey(exposed).getPath() + "_to_" + ForgeRegistries.BLOCKS.getKey(weathered).getPath()));
        ARCRecipeBuilder.arc(Ingredient.of(weathered), hydration, water, new ItemStack(oxidized), null).build(consumer, BloodMagic.rl("arc/oxidize/" + ForgeRegistries.BLOCKS.getKey(weathered).getPath() + "_to_" + ForgeRegistries.BLOCKS.getKey(oxidized).getPath()));
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
