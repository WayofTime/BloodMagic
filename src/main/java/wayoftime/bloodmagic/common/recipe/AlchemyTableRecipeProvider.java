package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
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
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.FLINT, 2), 50, 20, 0).addIngredient(Ingredient.fromItems(Items.GRAVEL)).addIngredient(Ingredient.fromItems(Items.FLINT)).build(consumer, BloodMagic.rl(basePath + "flint_from_gravel"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.LEATHER, 4), 100, 200, 1).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.FLINT)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "leather_from_flesh"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.EXPLOSIVE_POWDER.get()), 500, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).build(consumer, BloodMagic.rl(basePath + "explosive_powder"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.BREAD), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "bread"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Blocks.GRASS_BLOCK), 200, 200, 1).addIngredient(Ingredient.fromItems(Items.DIRT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).addIngredient(Ingredient.fromItems(Items.WHEAT_SEEDS)).build(consumer, BloodMagic.rl(basePath + "grass_block"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.CLAY_BALL, 2), 50, 100, 2).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "clay_from_sand"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.COBWEB), 50, 50, 1).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).build(consumer, BloodMagic.rl(basePath + "cobweb"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.NETHER_WART), 50, 40, 1).addIngredient(Ingredient.fromItems(Items.NETHER_WART_BLOCK)).build(consumer, BloodMagic.rl(basePath + "nether_wart_from_block"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.GOLD_NUGGET, 9), 200, 100, 2).addIngredient(Ingredient.fromItems(Items.GILDED_BLACKSTONE)).build(consumer, BloodMagic.rl(basePath + "gold_ore_from_gilded"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.GUNPOWDER, 3), 0, 100, 0).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_SULFUR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_SALTPETER)).addIngredient(Ingredient.fromTag(ItemTags.COALS)).build(consumer, BloodMagic.rl(basePath + "gunpowder"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_CARROT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_carrots"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_POTATO)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_POTATO)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_taters"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_WHEAT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_wheat"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.PLANT_OIL.get()), 100, 100, 1).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_BEETROOT)).addIngredient(Ingredient.fromItems(Items.BONE_MEAL)).build(consumer, BloodMagic.rl(basePath + "plantoil_from_beets"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.IRON_SAND.get(), 2), 400, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.ORES_IRON)).addIngredient(Ingredient.fromTag(BloodMagicTags.ARC_TOOL_CUTTINGFLUID)).build(consumer, BloodMagic.rl(basePath + "sand_iron"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.GOLD_SAND.get(), 2), 400, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.ORES_GOLD)).addIngredient(Ingredient.fromTag(BloodMagicTags.ARC_TOOL_CUTTINGFLUID)).build(consumer, BloodMagic.rl(basePath + "sand_gold"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.COAL_SAND.get(), 4), 400, 200, 1).addIngredient(Ingredient.fromItems(Items.COAL)).addIngredient(Ingredient.fromItems(Items.COAL)).addIngredient(Ingredient.fromItems(Items.FLINT)).build(consumer, BloodMagic.rl(basePath + "sand_coal"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.BASIC_CUTTING_FLUID.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.PLANT_OIL.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).addIngredient(Ingredient.fromStacks(new ItemStack(Items.POTION))).build(consumer, BloodMagic.rl(basePath + "basic_cutting_fluid"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.SLATE_VIAL.get(), 8), 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE.get())).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).build(consumer, BloodMagic.rl(basePath + "slate_vial"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.FORTUNE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).build(consumer, BloodMagic.rl(basePath + "fortune_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.SILK_TOUCH_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.COBWEB)).addIngredient(Ingredient.fromTag(Tags.Items.NUGGETS_GOLD)).build(consumer, BloodMagic.rl(basePath + "silk_touch_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_QUARTZ)).build(consumer, BloodMagic.rl(basePath + "melee_damage_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.HOLY_WATER_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.GLISTERING_MELON_SLICE)).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_QUARTZ)).build(consumer, BloodMagic.rl(basePath + "holy_water_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.GLASS_BOTTLE)).addIngredient(Ingredient.fromItems(Items.ENCHANTED_BOOK)).build(consumer, BloodMagic.rl(basePath + "hidden_knowledge_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.QUICK_DRAW_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromItems(Items.SPECTRAL_ARROW)).build(consumer, BloodMagic.rl(basePath + "quick_draw_anointment"));
	}
}
