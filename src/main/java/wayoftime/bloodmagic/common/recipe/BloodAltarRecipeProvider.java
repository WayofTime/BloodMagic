package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.BloodAltarRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class BloodAltarRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "altar/";

		// ONE
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.GEMS_DIAMOND), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()), AltarTier.ONE.ordinal(), 2000, 5, 1).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "weakbloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STONE), new ItemStack(BloodMagicItems.SLATE.get()), AltarTier.ONE.ordinal(), 1000, 5, 5).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "slate"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Items.BUCKET), new ItemStack(BloodMagicItems.LIFE_ESSENCE_BUCKET.get()), AltarTier.ONE.ordinal(), 1000, 5, 0).build(consumer, BloodMagic.rl(basePath + "bucket_life"));

		// TWO
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.REINFORCED_SLATE.get()), AltarTier.TWO.ordinal(), 2000, 5, 5).build(consumer, BloodMagic.rl(basePath + "reinforcedslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE), new ItemStack(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()), AltarTier.TWO.ordinal(), 5000, 5, 5).build(consumer, BloodMagic.rl(basePath + "apprenticebloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Items.IRON_SWORD), new ItemStack(BloodMagicItems.DAGGER_OF_SACRIFICE.get()), AltarTier.TWO.ordinal(), 3000, 5, 5).build(consumer, BloodMagic.rl(basePath + "daggerofsacrifice"));

		BloodAltarRecipeBuilder.altar(Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(BloodMagicItems.ALCHEMY_FLASK.get()), AltarTier.TWO.ordinal(), 4000, 5, 5).build(consumer, BloodMagic.rl(basePath + "alchemy_flask"));

		// THREE
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.IMBUED_SLATE.get()), AltarTier.THREE.ordinal(), 5000, 15, 10).build(consumer, BloodMagic.rl(basePath + "imbuedslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), new ItemStack(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()), AltarTier.THREE.ordinal(), 25000, 20, 20).build(consumer, BloodMagic.rl(basePath + "magicianbloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.OBSIDIAN), new ItemStack(BloodMagicItems.EARTH_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "earth_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS), new ItemStack(BloodMagicItems.WATER_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "water_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Items.MAGMA_CREAM), new ItemStack(BloodMagicItems.FIRE_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "fire_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Items.GHAST_TEAR), new ItemStack(BloodMagicItems.AIR_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "air_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.LAVA_CRYSTAL.get()), new ItemStack(BloodMagicItems.WEAK_ACTIVATION_CRYSTAL.get()), AltarTier.THREE.ordinal(), 10000, 20, 10).build(consumer, BloodMagic.rl("weak_activation_crystal"));

		// FOUR
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.DEMONIC_SLATE.get()), AltarTier.FOUR.ordinal(), 15000, 20, 20).build(consumer, BloodMagic.rl(basePath + "demonicslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL), new ItemStack(BloodMagicItems.DUSK_INSCRIPTION_TOOL.get()), AltarTier.FOUR.ordinal(), 2000, 20, 10).build(consumer, BloodMagic.rl(basePath + "dusk_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.WEAK_BLOOD_SHARD.get()), new ItemStack(BloodMagicItems.MASTER_BLOOD_ORB.get()), AltarTier.FOUR.ordinal(), 40000, 30, 50).build(consumer, BloodMagic.rl(basePath + "masterbloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.ENDER_PEARLS), new ItemStack(BloodMagicItems.TELEPOSER_FOCUS.get()), AltarTier.FOUR.ordinal(), 2000, 10, 10).build(consumer, BloodMagic.rl(basePath + "teleposer_focus"));
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.TELEPOSER_FOCUS.get()), new ItemStack(BloodMagicItems.ENHANCED_TELEPOSER_FOCUS.get()), AltarTier.FOUR.ordinal(), 10000, 20, 10).build(consumer, BloodMagic.rl(basePath + "enhanced_teleposer_focus"));

		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get()), new ItemStack(BloodMagicItems.BLEEDING_EDGE_MUSIC.get()), AltarTier.FOUR.ordinal(), 10000, 20, 10).build(consumer, BloodMagic.rl(basePath + "bleeding_edge_music"));

		// Changed Recipes
		{

		}
		{
			BloodAltarRecipeBuilder.altar(Ingredient.of(Tags.Items.STRING), new ItemStack(BloodMagicItems.SOUL_SNARE.get()), AltarTier.ONE.ordinal(), 500, 5, 1).build(consumer, BloodMagic.rl(basePath + "soul_snare"));
		}

//		// FOUR
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER), AltarTier.FOUR.ordinal(), 40000, 30, 50);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.IMBUED.getStack()), ItemSlate.SlateType.DEMONIC.getStack(), AltarTier.FOUR.ordinal(), 15000, 20, 20);
//		registrar.addBloodAltar(new OreIngredient("blockCoal"), EnumRuneType.DUSK.getStack(), AltarTier.FOUR.ordinal(), 2000, 20, 10);
//		registrar.addBloodAltar(new OreIngredient("enderpearl"), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS), AltarTier.FOUR.ordinal(), 2000, 10, 10);
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, 1, 1), AltarTier.FOUR.ordinal(), 10000, 20, 10);
//
//		// FIVE

		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicBlocks.HELLFORGED_BLOCK.get()), new ItemStack(BloodMagicItems.ARCHMAGE_BLOOD_ORB.get()), AltarTier.FIVE.ordinal(), 80000, 50, 100).build(consumer, BloodMagic.rl(basePath + "archmagebloodorb"));
		;
		BloodAltarRecipeBuilder.altar(Ingredient.of(BloodMagicItems.DEMONIC_SLATE.get()), new ItemStack(BloodMagicItems.ETHEREAL_SLATE.get()), AltarTier.FIVE.ordinal(), 30000, 40, 100).build(consumer, BloodMagic.rl(basePath + "etherealslate"));
		;
//		registrar.addBloodAltar(new OreIngredient("netherStar"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_ARCHMAGE), AltarTier.FIVE.ordinal(), 80000, 50, 100);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.DEMONIC.getStack()), ItemSlate.SlateType.ETHEREAL.getStack(), AltarTier.FIVE.ordinal(), 30000, 40, 100);
	}

}
