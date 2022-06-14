package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

public class ARCRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/";
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.BONES), null, new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), null).addRandomOutput(new ItemStack(Items.DIAMOND, 2), 0.5).build(consumer, BloodMagic.rl(basePath + "test1"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.BONES), null, new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), null).addRandomOutput(new ItemStack(Items.DIAMOND, 5), 0.5).build(consumer, BloodMagic.rl(basePath + "test3"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromItems(Items.ACACIA_BOAT), FluidStackIngredient.from(Fluids.LAVA, 1000), new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), new FluidStack(Fluids.WATER, 100)).build(consumer, BloodMagic.rl(basePath + "test2"));
//		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.NETHERRACK), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), FluidStackIngredient.from(Fluids.LAVA, 1000), new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), new FluidStack(Fluids.WATER, 100)).addRandomOutput(new ItemStack(BloodMagicItems.SLATE.get()), 0.2).addRandomOutput(new ItemStack(BloodMagicItems.REINFORCED_SLATE.get()), 0.1).addRandomOutput(new ItemStack(BloodMagicItems.IMBUED_SLATE.get()), 0.001).build(consumer, BloodMagic.rl(basePath + "test4"));

		ARCRecipeBuilder.arc(Ingredient.of(Items.IRON_ORE), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "ore/dustiron"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.GOLD_ORE), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "ore/dustgold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.NETHERRACK), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.SULFUR.get()), new FluidStack(Fluids.LAVA, 50)).build(consumer, BloodMagic.rl(basePath + "netherrack_to_sulfer"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.TERRACOTTA), Ingredient.of(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Blocks.CLAY), null).build(consumer, BloodMagic.rl(basePath + "clay_from_terracotta"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.SAND), Ingredient.of(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Items.CLAY_BALL), null).addRandomOutput(new ItemStack(Items.CLAY_BALL), 0.5).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));

		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicItems.STRONG_TAU_ITEM.get()), Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard_tau"));
//		ARCRecipeBuilder.arc(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard"));

//		ConditionalRecipe.builder().addCondition(new TagEmptyCondition(Tags.Items.ORES_IRON.getName()));

		addReversionRecipes(consumer);
		addSandRecipes(consumer);
		addFragmentRecipes(consumer);
		addGravelRecipes(consumer);
	}

	private ICondition getTagCondition(ITag.INamedTag<Item> tag)
	{
		return new NotCondition(new TagEmptyCondition(tag.getName()));
	}

	private void addSandRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/dusts";

		// Ore to dust
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "from_ore_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "from_ore_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "from_ore_netherite_scrap"));

		// Ingot to dust
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Items.NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_ingot_netherite_scrap"));

		// Gravel to dust
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.IRON_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_iron"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.GOLD_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_gold"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.GRAVEL_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_CUTTINGFLUID), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_SAND.get()), null).build(consumer, BloodMagic.rl(basePath + "from_gravel_netherite_scrap"));
	}

	private void addFragmentRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/fragments";
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_FRAGMENT.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "iron"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_FRAGMENT.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "gold"));
		ARCRecipeBuilder.arc(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT.get(), 3), null).build(consumer, BloodMagic.rl(basePath + "netherite_scrap"));
	}

	private void addGravelRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/gravels";
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_IRON), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.IRON_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.05).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.01).build(consumer, BloodMagic.rl(basePath + "iron"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_GOLD), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.GOLD_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.05).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.01).build(consumer, BloodMagic.rl(basePath + "gold"));
		ARCRecipeBuilder.arc(Ingredient.of(BloodMagicTags.FRAGMENT_NETHERITE_SCRAP), Ingredient.of(BloodMagicTags.ARC_TOOL_RESONATOR), null, new ItemStack(BloodMagicItems.NETHERITE_SCRAP_GRAVEL.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.05).addRandomOutput(new ItemStack(BloodMagicItems.CORRUPTED_DUST_TINY.get()), 0.01).build(consumer, BloodMagic.rl(basePath + "netherite_scrap"));
	}

	private void addReversionRecipes(Consumer<IFinishedRecipe> consumer)
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
	}

	private void registerReversionRecipe(Ingredient input, ItemStack outputStack, Consumer<IFinishedRecipe> consumer, String path)
	{
		ARCRecipeBuilder.arcConsume(input, Ingredient.of(BloodMagicTags.ARC_TOOL_REVERTER), null, outputStack, null).build(consumer, BloodMagic.rl(path));
	}
}
