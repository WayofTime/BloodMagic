package wayoftime.bloodmagic.common.registries;

import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.common.recipe.serializer.ARCRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.AlchemyArrayRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.BloodAltarRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.TartaricForgeRecipeSerializer;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerRegistryObject;
import wayoftime.bloodmagic.recipe.IRecipeARC;
import wayoftime.bloodmagic.recipe.IRecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.IRecipeBloodAltar;
import wayoftime.bloodmagic.recipe.IRecipeTartaricForge;

public class BloodMagicRecipeSerializers
{
	private BloodMagicRecipeSerializers()
	{

	}

	public static final IRecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new IRecipeSerializerDeferredRegister(BloodMagic.MODID);

	public static final IRecipeSerializerRegistryObject<RecipeBloodAltar> ALTAR = RECIPE_SERIALIZERS.register("altar", () -> new BloodAltarRecipeSerializer<>(IRecipeBloodAltar::new));
	public static final IRecipeSerializerRegistryObject<RecipeAlchemyArray> ARRAY = RECIPE_SERIALIZERS.register("array", () -> new AlchemyArrayRecipeSerializer<>(IRecipeAlchemyArray::new));
	public static final IRecipeSerializerRegistryObject<RecipeTartaricForge> TARTARIC = RECIPE_SERIALIZERS.register("soulforge", () -> new TartaricForgeRecipeSerializer<>(IRecipeTartaricForge::new));
	public static final IRecipeSerializerRegistryObject<RecipeARC> ARC = RECIPE_SERIALIZERS.register("arc", () -> new ARCRecipeSerializer<>(IRecipeARC::new));

//	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BloodMagic.MODID);

//	public static final DeferredObject<RecipeBloodAltar> REC = RECIPE_SERIALIZERS.register("test", () -> new BloodAltarRecipeSerializer<>(IRecipeBloodAltar::new));
//	public static final IRecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new IRecipeSerializerDeferredRegister(BloodMagic.MODID);
}
