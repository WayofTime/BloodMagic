package wayoftime.bloodmagic.common.recipe;

import net.minecraft.item.crafting.IRecipeType;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.api.impl.recipe.RecipeTartaricForge;

public class BloodMagicRecipeType
{
	public static final IRecipeType<RecipeBloodAltar> ALTAR = IRecipeType.register("altar");
	public static final IRecipeType<RecipeAlchemyArray> ARRAY = IRecipeType.register("array");
	public static final IRecipeType<RecipeTartaricForge> TARTARICFORGE = IRecipeType.register("soulforge");
	public static final IRecipeType<RecipeARC> ARC = IRecipeType.register("arc");
}
