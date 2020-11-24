package wayoftime.bloodmagic.common.registries;

import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.common.recipe.serializer.ARCRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.AlchemyArrayRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.AlchemyTableRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.BloodAltarRecipeSerializer;
import wayoftime.bloodmagic.common.recipe.serializer.TartaricForgeRecipeSerializer;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.IRecipeSerializerRegistryObject;

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
	public static final IRecipeSerializerRegistryObject<RecipeAlchemyTable> ALCHEMYTABLE = RECIPE_SERIALIZERS.register("alchemytable", () -> new AlchemyTableRecipeSerializer<>(RecipeAlchemyTable::new));

//	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BloodMagic.MODID);

//	public static final DeferredObject<RecipeBloodAltar> REC = RECIPE_SERIALIZERS.register("test", () -> new BloodAltarRecipeSerializer<>(IRecipeBloodAltar::new));
//	public static final IRecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new IRecipeSerializerDeferredRegister(BloodMagic.MODID);
}
