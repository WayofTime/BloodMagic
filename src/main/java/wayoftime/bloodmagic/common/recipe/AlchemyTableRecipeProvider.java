package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.AlchemyTableRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class AlchemyTableRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "alchemytable/";
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.STRING, 4), 100, 100, 0).addIngredient(Ingredient.fromTag(ItemTags.WOOL)).addIngredient(Ingredient.fromItems(Items.FLINT)).build(consumer, BloodMagic.rl(basePath + "string"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.FLINT), 50, 20, 0).addIngredient(Ingredient.fromItems(Items.GRAVEL)).addIngredient(Ingredient.fromItems(Items.FLINT)).build(consumer, BloodMagic.rl(basePath + "flint_from_gravel"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.LEATHER, 4), 100, 200, 1).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.FLINT)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "leather_from_flesh"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.EXPLOSIVE_POWDER.get()), 500, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).build(consumer, BloodMagic.rl(basePath + "explosive_powder"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.BREAD), 100, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "bread"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Blocks.GRASS_BLOCK), 200, 200, 1).addIngredient(Ingredient.fromItems(Items.DIRT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).addIngredient(Ingredient.fromItems(Items.WHEAT_SEEDS)).build(consumer, BloodMagic.rl(basePath + "grass_block"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.CLAY_BALL, 2), 50, 100, 2).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.GUNPOWDER, 3), 0, 100, 0).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_SULFUR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_SALTPETER)).addIngredient(Ingredient.fromTag(ItemTags.COALS)).build(consumer, BloodMagic.rl(basePath + "gunpowder"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_carrots"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_POTATO)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_POTATO)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_taters"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_wheat"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_beets"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.BASIC_CUTTING_FLUID.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.PLANT_OIL.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).addIngredient(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER))).build(consumer, BloodMagic.rl(basePath + "basic_cutting_fluid"));
	}

}
