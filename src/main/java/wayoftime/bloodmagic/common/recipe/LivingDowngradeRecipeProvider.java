package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.LivingDowngradeRecipeBuilder;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;

public class LivingDowngradeRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "downgrade/";

		// ONE
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()), AltarTier.ONE.ordinal(), 2000, 5, 1).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "weakbloodorb"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromTag(Tags.Items.STONE), new ItemStack(BloodMagicItems.SLATE.get()), AltarTier.ONE.ordinal(), 1000, 5, 5).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "slate"));
//		BloodAltarRecipeBuilder.altar(Ingredient.fromItems(Items.BUCKET), new ItemStack(BloodMagicBlocks.LIFE_ESSENCE_BUCKET.get()), AltarTier.ONE.ordinal(), 1000, 5, 0).build(consumer, BloodMagic.rl(basePath + "bucket_life"));

		LivingDowngradeRecipeBuilder.downgrade(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getKey()).build(consumer, BloodMagic.rl(basePath + "battle_hungry"));
	}
}
