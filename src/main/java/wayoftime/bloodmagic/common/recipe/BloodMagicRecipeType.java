package wayoftime.bloodmagic.common.recipe;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registration.impl.RecipeTypeDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.RecipeTypeRegistryObject;
import wayoftime.bloodmagic.recipe.*;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;

public class BloodMagicRecipeType<RECIPE extends BloodMagicRecipe> implements RecipeType<RECIPE>
{
	public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(BloodMagic.MODID);
	public static final DeferredRegister<RecipeType<?>> CUSTOM_RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, BloodMagic.MODID);

	public static final RecipeTypeRegistryObject<RecipeBloodAltar> ALTAR = register("altar");
	public static final RecipeTypeRegistryObject<RecipeAlchemyArray> ARRAY = register("array");
	public static final RecipeTypeRegistryObject<RecipeTartaricForge> TARTARICFORGE = register("soulforge");
	public static final RecipeTypeRegistryObject<RecipeARC> ARC = register("arc");
	public static final RecipeTypeRegistryObject<RecipeAlchemyTable> ALCHEMYTABLE = register("alchemytable");
	public static final RecipeTypeRegistryObject<RecipeLivingDowngrade> LIVINGDOWNGRADE = register("livingdowngrade");
	public static final RecipeTypeRegistryObject<RecipePotionFlaskBase> POTIONFLASK = register("potionflask");
	public static final RecipeTypeRegistryObject<RecipeMeteor> METEOR = register("meteor");
	public static final RegistryObject<RecipeType<DyeRecipe>> DYE = CUSTOM_RECIPE_TYPES.register("armordye", ModRecipeType::new);

//	public static final RecipeType<RecipeBloodAltar> ALTAR = RecipeType.register(BloodMagic.MODID + ":altar");
//	public static final RecipeType<RecipeAlchemyArray> ARRAY = RecipeType.register(BloodMagic.MODID + ":array");
//	public static final RecipeType<RecipeTartaricForge> TARTARICFORGE = RecipeType.register(BloodMagic.MODID + ":soulforge");
//	public static final RecipeType<RecipeARC> ARC = RecipeType.register(BloodMagic.MODID + ":arc");
//	public static final RecipeType<RecipeAlchemyTable> ALCHEMYTABLE = RecipeType.register(BloodMagic.MODID + ":alchemytable");
//	public static final RecipeType<RecipeLivingDowngrade> LIVINGDOWNGRADE = RecipeType.register(BloodMagic.MODID + ":livingdowngrade");
//	public static final RecipeType<RecipePotionFlaskBase> POTIONFLASK = RecipeType.register(BloodMagic.MODID + ":potionflask");
//	public static final RecipeType<RecipeMeteor> METEOR = RecipeType.register(BloodMagic.MODID + ":meteor");

	private static <RECIPE extends BloodMagicRecipe> RecipeTypeRegistryObject<RECIPE> register(String name)
	{
		return RECIPE_TYPES.register(name, () -> new BloodMagicRecipeType<RECIPE>(name));
	}

	private final ResourceLocation registryName;

	private BloodMagicRecipeType(String name)
	{
		this.registryName = BloodMagic.rl(name);
	}


	private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
