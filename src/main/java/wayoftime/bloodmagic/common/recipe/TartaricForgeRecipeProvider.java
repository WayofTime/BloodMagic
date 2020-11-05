package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.TartaricForgeRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class TartaricForgeRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "soulforge/";
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.PETTY_GEM.get()), 1, 1, Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.INGOTS_GOLD), Ingredient.fromTag(Tags.Items.GLASS), Ingredient.fromTag(Tags.Items.GEMS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "pettytartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.LESSER_GEM.get()), 60, 20, Ingredient.fromItems(BloodMagicItems.PETTY_GEM.get()), Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_REDSTONE), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "lessertartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.COMMON_GEM.get()), 240, 50, Ingredient.fromItems(BloodMagicItems.LESSER_GEM.get()), Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_GOLD), Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "commontartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.GREATER_GEM.get()), 1000, 100, Ingredient.fromItems(BloodMagicItems.COMMON_GEM.get()), Ingredient.fromItems(BloodMagicItems.DEMONIC_SLATE.get()), Ingredient.fromItems(BloodMagicItems.WEAK_BLOOD_SHARD.get()), Ingredient.fromTag(BloodMagicTags.CRYSTAL_DEMON)).build(consumer, BloodMagic.rl(basePath + "greatertartaricgem"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_SWORD.get()), 0, 0, Ingredient.fromItems(BloodMagicItems.PETTY_GEM.get()), Ingredient.fromItems(Items.IRON_SWORD)).build(consumer, BloodMagic.rl(basePath + "sentientsword"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_AIR.get()), 128, 20, Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromTag(Tags.Items.FEATHERS), Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(basePath + "reagent_air"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()), 0, 0, Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.DYES_WHITE), Ingredient.fromTag(Tags.Items.GUNPOWDER), Ingredient.fromTag(ItemTags.COALS)).build(consumer, BloodMagic.rl(basePath + "arcaneashes"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_WATER.get()), 10, 3, Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.WATER_BUCKET), Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "reagent_water"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_LAVA.get()), 32, 10, Ingredient.fromItems(Items.LAVA_BUCKET), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.COBBLESTONE), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL)).build(consumer, BloodMagic.rl(basePath + "reagent_lava"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_VOID.get()), 64, 10, Ingredient.fromItems(Items.BUCKET), Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_void"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_GROWTH.get()), 128, 20, Ingredient.fromTag(ItemTags.SAPLINGS), Ingredient.fromTag(ItemTags.SAPLINGS), Ingredient.fromItems(Items.SUGAR_CANE), Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "reagent_growth"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_MAGNETISM.get()), 600, 10, Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.INGOTS_GOLD), Ingredient.fromTag(Tags.Items.INGOTS_GOLD), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer, BloodMagic.rl(basePath + "reagent_magnetism"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_FAST_MINER.get()), 128, 20, Ingredient.fromItems(Items.IRON_PICKAXE), Ingredient.fromItems(Items.IRON_AXE), Ingredient.fromItems(Items.IRON_SHOVEL), Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_fastminer"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEMON_CRUCIBLE.get()), 400, 100, Ingredient.fromItems(Items.CAULDRON), Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromTag(Tags.Items.GEMS_LAPIS), Ingredient.fromTag(Tags.Items.GEMS_DIAMOND)).build(consumer, BloodMagic.rl(basePath + "demon_crucible"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEMON_CRYSTALLIZER.get()), 500, 100, Ingredient.fromItems(BloodMagicBlocks.SOUL_FORGE.get()), Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromTag(Tags.Items.GEMS_LAPIS), Ingredient.fromTag(Tags.Items.GLASS)).build(consumer, BloodMagic.rl(basePath + "demon_crystallizer"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SANGUINE_REVERTER.get()), 350, 30, Ingredient.fromItems(Items.SHEARS), Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.fromTag(Tags.Items.INGOTS_IRON)).build(consumer, BloodMagic.rl(basePath + "sanguine_reverter"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get()), 1200, 200, Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromTag(Tags.Items.INGOTS), Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "primitive_crystalline_resonator"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.RAW_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "raw_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.fromItems(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.CORROSIVE_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "corrosive_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.fromItems(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "destructive_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.fromItems(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.VENGEFUL_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "vengeful_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.fromItems(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.fromItems(BloodMagicItems.STEADFAST_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "steadfast_crystal_block"));
	}

}
