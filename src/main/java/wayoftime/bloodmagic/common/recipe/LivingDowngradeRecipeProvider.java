package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.LivingDowngradeRecipeBuilder;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;

public class LivingDowngradeRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{

		String basePath = "downgrade/";

		// ONE
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()), AltarTier.ONE.ordinal(), 2000, 5, 1).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "weakbloodorb"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STONE), new ItemStack(BloodMagicItems.SLATE.get()), AltarTier.ONE.ordinal(), 1000, 5, 5).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "slate"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(Items.BUCKET), new ItemStack(BloodMagicBlocks.LIFE_ESSENCE_BUCKET.get()), AltarTier.ONE.ordinal(), 1000, 5, 0).build(consumer, BloodMagic.rl(basePath + "bucket_life"));

		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.ROTTEN_FLESH), LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getKey()).build(consumer, BloodMagic.rl(basePath + "battle_hungry"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.STONE_SWORD), LivingArmorRegistrar.DOWNGRADE_MELEE_DECREASE.get().getKey()).build(consumer, BloodMagic.rl(basePath + "melee_decrease"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.GLASS_BOTTLE), LivingArmorRegistrar.DOWNGRADE_QUENCHED.get().getKey()).build(consumer, BloodMagic.rl(basePath + "quenched"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.ARROW), LivingArmorRegistrar.DOWNGRADE_STORM_TROOPER.get().getKey()).build(consumer, BloodMagic.rl(basePath + "storm_trooper"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.STONE_PICKAXE), LivingArmorRegistrar.DOWNGRADE_DIG_SLOWDOWN.get().getKey()).build(consumer, BloodMagic.rl(basePath + "dig_slowdown"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.GHAST_TEAR), LivingArmorRegistrar.DOWNGRADE_SLOW_HEAL.get().getKey()).build(consumer, BloodMagic.rl(basePath + "slow_heal"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.WATER_BUCKET), LivingArmorRegistrar.DOWNGRADE_SWIM_DECREASE.get().getKey()).build(consumer, BloodMagic.rl(basePath + "swim_decrease"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.SOUL_SAND), LivingArmorRegistrar.DOWNGRADE_SPEED_DECREASE.get().getKey()).build(consumer, BloodMagic.rl(basePath + "speed_decrease"));
		LivingDowngradeRecipeBuilder.downgrade(Ingredient.of(Items.SHIELD), LivingArmorRegistrar.DOWNGRADE_CRIPPLED_ARM.get().getKey()).build(consumer, BloodMagic.rl(basePath + "crippled_arm"));
	}
}
