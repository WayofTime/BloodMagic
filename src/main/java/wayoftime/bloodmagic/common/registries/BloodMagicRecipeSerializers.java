package wayoftime.bloodmagic.common.registries;

import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.serializer.ARCPotionRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.ARCRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.AlchemyArrayRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.AlchemyTableRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.BloodAltarRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.FilterMergeAlchemyTableRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.LivingDowngradeRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.MeteorRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionCycleRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionEffectRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionFillRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionFlaskTransformRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionIncreaseLengthRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionIncreasePotencyRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.PotionTransformRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.TartaricForgeRecipeSerializer;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerRegistryObject;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.RecipeARCPotion;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.recipe.RecipeFilterMergeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.recipe.flask.RecipePotionCycle;
import wayoftime.bloodmagic.recipe.flask.RecipePotionEffect;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFill;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskTransform;
import wayoftime.bloodmagic.recipe.flask.RecipePotionIncreaseLength;
import wayoftime.bloodmagic.recipe.flask.RecipePotionIncreasePotency;
import wayoftime.bloodmagic.recipe.flask.RecipePotionTransform;

public class BloodMagicRecipeSerializers
{
	private BloodMagicRecipeSerializers()
	{

	}

	public static final IRecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new IRecipeSerializerDeferredRegister(BloodMagic.MODID);

	public static final IRecipeSerializerRegistryObject<RecipeBloodAltar> ALTAR = RECIPE_SERIALIZERS.register("altar", () -> new BloodAltarRecipeSerializer<>(RecipeBloodAltar::new));
	public static final IRecipeSerializerRegistryObject<RecipeAlchemyArray> ARRAY = RECIPE_SERIALIZERS.register("array", () -> new AlchemyArrayRecipeSerializer<>(RecipeAlchemyArray::new));
	public static final IRecipeSerializerRegistryObject<RecipeTartaricForge> TARTARIC = RECIPE_SERIALIZERS.register("soulforge", () -> new TartaricForgeRecipeSerializer<>(RecipeTartaricForge::new));
	public static final IRecipeSerializerRegistryObject<RecipeARC> ARC = RECIPE_SERIALIZERS.register("arc", () -> new ARCRecipeSerializer<>(RecipeARC::new));
	public static final IRecipeSerializerRegistryObject<RecipeARCPotion> ARC_POTION = RECIPE_SERIALIZERS.register("arc_potion", () -> new ARCPotionRecipeSerializer<>(RecipeARCPotion::new));
	public static final IRecipeSerializerRegistryObject<RecipeAlchemyTable> ALCHEMYTABLE = RECIPE_SERIALIZERS.register("alchemytable", () -> new AlchemyTableRecipeSerializer<>(RecipeAlchemyTable::new));
	public static final IRecipeSerializerRegistryObject<RecipeFilterMergeAlchemyTable> FILTERALCHEMYTABLE = RECIPE_SERIALIZERS.register("filteralchemytable", () -> new FilterMergeAlchemyTableRecipeSerializer<>(RecipeFilterMergeAlchemyTable::new));
	public static final IRecipeSerializerRegistryObject<RecipeLivingDowngrade> LIVINGDOWNGRADE = RECIPE_SERIALIZERS.register("livingdowngrade", () -> new LivingDowngradeRecipeSerializer<>(RecipeLivingDowngrade::new));

	public static final IRecipeSerializerRegistryObject<RecipePotionEffect> POTIONEFFECT = RECIPE_SERIALIZERS.register("flask_potioneffect", () -> new PotionEffectRecipeSerializer<>(RecipePotionEffect::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionIncreasePotency> POTIONPOTENCY = RECIPE_SERIALIZERS.register("flask_potionpotency", () -> new PotionIncreasePotencyRecipeSerializer<>(RecipePotionIncreasePotency::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionIncreaseLength> POTIONLENGTH = RECIPE_SERIALIZERS.register("flask_potionlength", () -> new PotionIncreaseLengthRecipeSerializer<>(RecipePotionIncreaseLength::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionTransform> POTIONTRANSFORM = RECIPE_SERIALIZERS.register("flask_potiontransform", () -> new PotionTransformRecipeSerializer<>(RecipePotionTransform::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionFill> POTIONFILL = RECIPE_SERIALIZERS.register("flask_potionfill", () -> new PotionFillRecipeSerializer<>(RecipePotionFill::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionFlaskTransform> POTIONFLASKTRANSFORM = RECIPE_SERIALIZERS.register("flask_potionflasktransform", () -> new PotionFlaskTransformRecipeSerializer<>(RecipePotionFlaskTransform::new));
	public static final IRecipeSerializerRegistryObject<RecipePotionCycle> POTIONCYCLE = RECIPE_SERIALIZERS.register("flask_potioncycle", () -> new PotionCycleRecipeSerializer<>(RecipePotionCycle::new));

	public static final IRecipeSerializerRegistryObject<RecipeMeteor> METEOR = RECIPE_SERIALIZERS.register("meteor", () -> new MeteorRecipeSerializer<>(RecipeMeteor::new));

//	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BloodMagic.MODID);

//	public static final DeferredObject<RecipeBloodAltar> REC = RECIPE_SERIALIZERS.register("test", () -> new BloodAltarRecipeSerializer<>(IRecipeBloodAltar::new));
//	public static final IRecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new IRecipeSerializerDeferredRegister(BloodMagic.MODID);
}
