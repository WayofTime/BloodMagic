package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.common.data.recipe.builder.BloodAltarRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class BloodAltarRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "altar/";

		// ONE
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()), AltarTier.ONE.ordinal(), 2000, 2, 1).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "weakbloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STONE), new ItemStack(BloodMagicItems.SLATE.get()), AltarTier.ONE.ordinal(), 1000, 5, 5).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "slate"));

		// TWO
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(BloodMagicItems.SLATE.get()), new ItemStack(BloodMagicItems.REINFORCED_SLATE.get()), AltarTier.THREE.ordinal(), 2000, 5, 5).build(consumer, BloodMagic.rl(basePath + "reinforcedslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_REDSTONE), new ItemStack(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()), AltarTier.TWO.ordinal(), 5000, 5, 5).build(consumer, BloodMagic.rl(basePath + "apprenticebloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(Items.IRON_SWORD), new ItemStack(BloodMagicItems.DAGGER_OF_SACRIFICE.get()), AltarTier.TWO.ordinal(), 3000, 5, 5).build(consumer, BloodMagic.rl(basePath + "daggerofsacrifice"));

		// THREE
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get()), new ItemStack(BloodMagicItems.IMBUED_SLATE.get()), AltarTier.THREE.ordinal(), 5000, 15, 10).build(consumer, BloodMagic.rl(basePath + "imbuedslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_GOLD), new ItemStack(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()), AltarTier.THREE.ordinal(), 25000, 20, 20).build(consumer, BloodMagic.rl(basePath + "magicianbloodorb"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.OBSIDIAN), new ItemStack(BloodMagicItems.EARTH_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "earth_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_LAPIS), new ItemStack(BloodMagicItems.WATER_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "water_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(Items.MAGMA_CREAM), new ItemStack(BloodMagicItems.FIRE_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "fire_tool"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(Items.GHAST_TEAR), new ItemStack(BloodMagicItems.AIR_INSCRIPTION_TOOL.get()), AltarTier.THREE.ordinal(), 1000, 5, 5).build(consumer, BloodMagic.rl(basePath + "air_tool"));

		// FOUR
		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(BloodMagicItems.IMBUED_SLATE.get()), new ItemStack(BloodMagicItems.DEMONIC_SLATE.get()), AltarTier.FOUR.ordinal(), 15000, 20, 20).build(consumer, BloodMagic.rl(basePath + "demonicslate"));
		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL), new ItemStack(BloodMagicItems.DUSK_INSCRIPTION_TOOL.get()), AltarTier.FOUR.ordinal(), 2000, 20, 10).build(consumer, BloodMagic.rl(basePath + "dusk_tool"));

//		BloodAltarRecipeBuilder.altar(input, output, minimumTier, syphon, consumeRate, drainRate).build(consumer, BloodMagic.rl(basePath + ""));

//		registrar.addBloodAltar(new OreIngredient("stone"), ItemSlate.SlateType.BLANK.getStack(), AltarTier.ONE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.BUCKET), FluidUtil.getFilledBucket(new FluidStack(BlockLifeEssence.getLifeEssence(), Fluid.BUCKET_VOLUME)), AltarTier.ONE.ordinal(), 1000, 5, 0);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.BOOK), new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK), AltarTier.ONE.ordinal(), 1000, 20, 0);
//
//		// TWO
//		registrar.addBloodAltar(new OreIngredient("blockRedstone"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE), AltarTier.TWO.ordinal(), 5000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.BLANK.getStack()), ItemSlate.SlateType.REINFORCED.getStack(), AltarTier.TWO.ordinal(), 2000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.IRON_SWORD), new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE), AltarTier.TWO.ordinal(), 3000, 5, 5);
//
//		// THREE
//		registrar.addBloodAltar(new OreIngredient("blockGold"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN), AltarTier.THREE.ordinal(), 25000, 20, 20);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.REINFORCED.getStack()), ItemSlate.SlateType.IMBUED.getStack(), AltarTier.THREE.ordinal(), 5000, 15, 10);
//		registrar.addBloodAltar(new OreIngredient("obsidian"), EnumRuneType.EARTH.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(new OreIngredient("blockLapis"), EnumRuneType.WATER.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.MAGMA_CREAM), EnumRuneType.FIRE.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.GHAST_TEAR), EnumRuneType.AIR.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(RegistrarBloodMagicItems.LAVA_CRYSTAL), new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL), AltarTier.THREE.ordinal(), 10000, 20, 10);
//
//		// FOUR
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER), AltarTier.FOUR.ordinal(), 40000, 30, 50);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.IMBUED.getStack()), ItemSlate.SlateType.DEMONIC.getStack(), AltarTier.FOUR.ordinal(), 15000, 20, 20);
//		registrar.addBloodAltar(new OreIngredient("blockCoal"), EnumRuneType.DUSK.getStack(), AltarTier.FOUR.ordinal(), 2000, 20, 10);
//		registrar.addBloodAltar(new OreIngredient("enderpearl"), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS), AltarTier.FOUR.ordinal(), 2000, 10, 10);
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, 1, 1), AltarTier.FOUR.ordinal(), 10000, 20, 10);
//
//		// FIVE
//		registrar.addBloodAltar(new OreIngredient("netherStar"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_ARCHMAGE), AltarTier.FIVE.ordinal(), 80000, 50, 100);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.DEMONIC.getStack()), ItemSlate.SlateType.ETHEREAL.getStack(), AltarTier.FIVE.ordinal(), 30000, 40, 100);
	}

}
