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
import wayoftime.bloodmagic.common.data.recipe.builder.FilterMergeAlchemyTableRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.routing.ItemCompositeFilter;
import wayoftime.bloodmagic.common.item.routing.ItemStandardFilter;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class AlchemyTableRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ItemStack waterbottleStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);

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

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.BASIC_CUTTING_FLUID.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.PLANT_OIL.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).addIngredient(Ingredient.fromStacks(waterbottleStack)).build(consumer, BloodMagic.rl(basePath + "basic_cutting_fluid"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.SLATE_VIAL.get(), 8), 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE.get())).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).build(consumer, BloodMagic.rl(basePath + "slate_vial"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.FORTUNE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).build(consumer, BloodMagic.rl(basePath + "fortune_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.SILK_TOUCH_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.COBWEB)).addIngredient(Ingredient.fromTag(Tags.Items.NUGGETS_GOLD)).build(consumer, BloodMagic.rl(basePath + "silk_touch_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_QUARTZ)).build(consumer, BloodMagic.rl(basePath + "melee_damage_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.HOLY_WATER_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.GLISTERING_MELON_SLICE)).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_QUARTZ)).build(consumer, BloodMagic.rl(basePath + "holy_water_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.GLASS_BOTTLE)).addIngredient(Ingredient.fromItems(Items.ENCHANTED_BOOK)).build(consumer, BloodMagic.rl(basePath + "hidden_knowledge_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.QUICK_DRAW_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromItems(Items.SPECTRAL_ARROW)).build(consumer, BloodMagic.rl(basePath + "quick_draw_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.LOOTING_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_LAPIS)).addIngredient(Ingredient.fromTag(Tags.Items.BONES)).build(consumer, BloodMagic.rl(basePath + "looting_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.BOW_POWER_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_IRON)).addIngredient(Ingredient.fromItems(Items.BOW)).build(consumer, BloodMagic.rl(basePath + "bow_power_anointment"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.SMELTING_ANOINTMENT.get()), 500, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE_VIAL.get())).addIngredient(Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART)).addIngredient(Ingredient.fromItems(Items.FURNACE)).addIngredient(Ingredient.fromItems(Items.CHARCOAL, Items.COAL)).build(consumer, BloodMagic.rl(basePath + "smelting_anointment"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.WATER_BUCKET), 300, 60, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.WATER_SIGIL.get())).addIngredient(Ingredient.fromItems(Items.BUCKET)).build(consumer, BloodMagic.rl(basePath + "sigil_water_bucket"));
		AlchemyTableRecipeBuilder.alchemyTable(waterbottleStack, 100, 60, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.WATER_SIGIL.get())).addIngredient(Ingredient.fromItems(Items.GLASS_BOTTLE)).build(consumer, BloodMagic.rl(basePath + "sigil_water_bottle"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.LAVA_BUCKET), 1000, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.LAVA_SIGIL.get())).addIngredient(Ingredient.fromItems(Items.BUCKET)).build(consumer, BloodMagic.rl(basePath + "sigil_lava_bucket"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.LEATHER, 4), 400, 200, 1).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH)).addIngredient(Ingredient.fromItems(Items.FLINT)).addIngredient(Ingredient.fromItems(BloodMagicItems.WATER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "leather_from_flesh_sigil"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.CLAY_BALL, 2), 350, 100, 2).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromTag(Tags.Items.SAND)).addIngredient(Ingredient.fromItems(BloodMagicItems.WATER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "clay_from_sand_sigil"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.BASIC_CUTTING_FLUID.get()), 1100, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.PLANT_OIL.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromTag(BloodMagicTags.DUST_COAL)).addIngredient(Ingredient.fromItems(BloodMagicItems.WATER_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "basic_cutting_fluid_sigil"));

		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.FRAME_PARTS.get()), 1000, 200, 3).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.STONE)).addIngredient(Ingredient.fromItems(BloodMagicItems.SLATE.get())).build(consumer, BloodMagic.rl(basePath + "component_frame_parts"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ITEM_ROUTER_FILTER.get()), 500, 100, 3).addIngredient(Ingredient.fromItems(BloodMagicItems.FRAME_PARTS.get())).addIngredient(Ingredient.fromTag(Tags.Items.LEATHER)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DYES_RED)).build(consumer, BloodMagic.rl(basePath + "router_filter"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ITEM_TAG_FILTER.get()), 500, 100, 3).addIngredient(Ingredient.fromItems(BloodMagicItems.FRAME_PARTS.get())).addIngredient(Ingredient.fromTag(Tags.Items.INGOTS)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromItems(Items.CLAY_BALL)).build(consumer, BloodMagic.rl(basePath + "tag_router_filter"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ITEM_MOD_FILTER.get()), 500, 100, 3).addIngredient(Ingredient.fromItems(BloodMagicItems.FRAME_PARTS.get())).addIngredient(Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DYES_YELLOW)).build(consumer, BloodMagic.rl(basePath + "mod_router_filter"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ITEM_ENCHANT_FILTER.get()), 500, 100, 3).addIngredient(Ingredient.fromItems(BloodMagicItems.FRAME_PARTS.get())).addIngredient(Ingredient.fromItems(Items.ENCHANTED_BOOK)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromItems(Items.PAPER)).build(consumer, BloodMagic.rl(basePath + "enchant_router_filter"));
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ITEM_COMPOSITE_FILTER.get()), 1000, 200, 3).addIngredient(Ingredient.fromItems(BloodMagicItems.FRAME_PARTS.get())).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "composite_router_filter"));
		// Changed Recipes
		{

		}
		{
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()), 500, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DYES_WHITE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(ItemTags.COALS)).build(consumer, BloodMagic.rl(basePath + "arcane_ash"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_AIR.get()), 2000, 200, 2).addIngredient(Ingredient.fromItems(Items.GHAST_TEAR)).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(basePath + "reagent_air"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_WATER.get()), 300, 200, 1).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).addIngredient(Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "reagent_water"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_LAVA.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(Items.LAVA_BUCKET)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.COBBLESTONE)).addIngredient(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL)).build(consumer, BloodMagic.rl(basePath + "reagent_lava"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_VOID.get()), 1000, 200, 2).addIngredient(Ingredient.fromItems(Items.BUCKET)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_void"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_GROWTH.get()), 2000, 200, 2).addIngredient(Ingredient.fromTag(ItemTags.SAPLINGS)).addIngredient(Ingredient.fromTag(ItemTags.SAPLINGS)).addIngredient(Ingredient.fromItems(Items.SUGAR_CANE)).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "reagent_growth"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_MAGNETISM.get()), 1000, 200, 3).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_GOLD)).addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_GOLD)).addIngredient(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer, BloodMagic.rl(basePath + "reagent_magnetism"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_FAST_MINER.get()), 2000, 200, 2).addIngredient(Ingredient.fromItems(Items.IRON_PICKAXE)).addIngredient(Ingredient.fromItems(Items.IRON_AXE)).addIngredient(Ingredient.fromItems(Items.IRON_SHOVEL)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_fastminer"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_BLOOD_LIGHT.get()), 1000, 200, 3).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE)).addIngredient(Ingredient.fromItems(Items.TORCH)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).build(consumer, BloodMagic.rl(basePath + "reagent_blood_light"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_SIGHT.get()), 500, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromTag(Tags.Items.GLASS)).addIngredient(Ingredient.fromItems(BloodMagicItems.DIVINATION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "reagent_sight"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_BINDING.get()), 1000, 200, 3).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).addIngredient(Ingredient.fromTag(Tags.Items.GUNPOWDER)).addIngredient(Ingredient.fromTag(Tags.Items.NUGGETS_GOLD)).build(consumer, BloodMagic.rl(basePath + "reagent_binding"));
			AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(BloodMagicItems.REAGENT_HOLDING.get()), 2000, 200, 2).addIngredient(Ingredient.fromTag(Tags.Items.CHESTS)).addIngredient(Ingredient.fromTag(Tags.Items.LEATHER)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).addIngredient(Ingredient.fromTag(Tags.Items.STRING)).build(consumer, BloodMagic.rl(basePath + "reagent_holding"));
		}

		String filterPath = "alchemytable/filter/";

		ItemStack enchantStack = new ItemStack(BloodMagicItems.ITEM_ENCHANT_FILTER.get());
		ItemStack tagStack = new ItemStack(BloodMagicItems.ITEM_TAG_FILTER.get());
		ItemStack modStack = new ItemStack(BloodMagicItems.ITEM_MOD_FILTER.get());
		// Filter combination recipes
		ItemStack itemPlusEnchantStack = new ItemStack(BloodMagicItems.ITEM_ROUTER_FILTER.get());
		((ItemStandardFilter) BloodMagicItems.ITEM_ROUTER_FILTER.get()).nestFilter(itemPlusEnchantStack, enchantStack);

		ItemStack compositePlusEnchant = new ItemStack(BloodMagicItems.ITEM_COMPOSITE_FILTER.get());
		ItemStack compositePlusTag = new ItemStack(BloodMagicItems.ITEM_COMPOSITE_FILTER.get());
		ItemStack compositePlusMod = new ItemStack(BloodMagicItems.ITEM_COMPOSITE_FILTER.get());
		((ItemCompositeFilter) BloodMagicItems.ITEM_COMPOSITE_FILTER.get()).nestFilter(compositePlusEnchant, enchantStack);
		((ItemCompositeFilter) BloodMagicItems.ITEM_COMPOSITE_FILTER.get()).nestFilter(compositePlusTag, tagStack);
		((ItemCompositeFilter) BloodMagicItems.ITEM_COMPOSITE_FILTER.get()).nestFilter(compositePlusMod, modStack);

		FilterMergeAlchemyTableRecipeBuilder.alchemyTable(Ingredient.fromItems(BloodMagicItems.ITEM_COMPOSITE_FILTER.get()), 500, 100, 0).addOptionalOutputStack(compositePlusEnchant).addIngredient(Ingredient.fromItems(BloodMagicItems.ITEM_ENCHANT_FILTER.get())).addIngredient(Ingredient.fromTag(Tags.Items.SLIMEBALLS)).build(consumer, BloodMagic.rl(filterPath + "composite_enchant_filter"));
		FilterMergeAlchemyTableRecipeBuilder.alchemyTable(Ingredient.fromItems(BloodMagicItems.ITEM_COMPOSITE_FILTER.get()), 500, 100, 0).addOptionalOutputStack(compositePlusTag).addIngredient(Ingredient.fromItems(BloodMagicItems.ITEM_TAG_FILTER.get())).addIngredient(Ingredient.fromTag(Tags.Items.SLIMEBALLS)).build(consumer, BloodMagic.rl(filterPath + "composite_tag_filter"));
		FilterMergeAlchemyTableRecipeBuilder.alchemyTable(Ingredient.fromItems(BloodMagicItems.ITEM_COMPOSITE_FILTER.get()), 500, 100, 0).addOptionalOutputStack(compositePlusMod).addIngredient(Ingredient.fromItems(BloodMagicItems.ITEM_MOD_FILTER.get())).addIngredient(Ingredient.fromTag(Tags.Items.SLIMEBALLS)).build(consumer, BloodMagic.rl(filterPath + "composite_mod_filter"));
	}
}
