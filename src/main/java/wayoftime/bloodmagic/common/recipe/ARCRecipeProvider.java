package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.event.recipes.FluidStackIngredient;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

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

		ARCRecipeBuilder.arc(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), null, new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), null).addRandomOutput(new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), 0.2).build(consumer, BloodMagic.rl(basePath + "weakbloodshard"));
		ARCRecipeBuilder.arc(Ingredient.fromItems(Items.IRON_ORE), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.IRON_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "ore/dustiron"));
		ARCRecipeBuilder.arc(Ingredient.fromItems(Items.GOLD_ORE), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.GOLD_SAND.get(), 2), null).build(consumer, BloodMagic.rl(basePath + "ore/dustgold"));
		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.NETHERRACK), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE), null, new ItemStack(BloodMagicItems.SULFUR.get()), new FluidStack(Fluids.LAVA, 5)).build(consumer, BloodMagic.rl(basePath + "netherrack_to_sulfer"));
		ARCRecipeBuilder.arc(Ingredient.fromItems(Items.TERRACOTTA), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Blocks.CLAY), null).build(consumer, BloodMagic.rl(basePath + "clay_from_terracotta"));
		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.SAND), Ingredient.fromTag(BloodMagicTags.ARC_TOOL_HYDRATE), FluidStackIngredient.from(Fluids.WATER, 200), new ItemStack(Items.CLAY_BALL), null).addRandomOutput(new ItemStack(Items.CLAY_BALL), 0.5).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));

		addReversionRecipes(consumer);
	}

	private void addReversionRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/reversion/";
		// ONE
		registerReversionRecipe(Ingredient.fromItems(BloodMagicItems.WEAK_BLOOD_ORB.get()), new ItemStack(Items.DIAMOND), consumer, basePath + "weak_blood_orb");

		// TWO
		registerReversionRecipe(Ingredient.fromItems(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()), new ItemStack(Blocks.REDSTONE_BLOCK), consumer, basePath + "apprentice_blood_orb");

		// THREE
		registerReversionRecipe(Ingredient.fromItems(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()), new ItemStack(Blocks.GOLD_BLOCK), consumer, basePath + "magician_blood_orb");

		// FOUR
		registerReversionRecipe(Ingredient.fromItems(BloodMagicItems.MASTER_BLOOD_ORB.get()), new ItemStack(BloodMagicItems.WEAK_BLOOD_SHARD.get()), consumer, basePath + "master_blood_orb");
	}

	private void registerReversionRecipe(Ingredient input, ItemStack outputStack, Consumer<IFinishedRecipe> consumer, String path)
	{
		ARCRecipeBuilder.arc(input, Ingredient.fromTag(BloodMagicTags.ARC_TOOL_REVERTER), null, outputStack, null).build(consumer, BloodMagic.rl(path));
	}
}
