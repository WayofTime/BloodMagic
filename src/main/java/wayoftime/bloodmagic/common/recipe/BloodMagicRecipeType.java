package wayoftime.bloodmagic.common.recipe;

import net.minecraft.item.crafting.IRecipeType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

public class BloodMagicRecipeType
{
	public static final IRecipeType<RecipeBloodAltar> ALTAR = IRecipeType.register(BloodMagic.MODID + ":altar");
	public static final IRecipeType<RecipeAlchemyArray> ARRAY = IRecipeType.register(BloodMagic.MODID + ":array");
	public static final IRecipeType<RecipeTartaricForge> TARTARICFORGE = IRecipeType.register(BloodMagic.MODID + ":soulforge");
	public static final IRecipeType<RecipeARC> ARC = IRecipeType.register(BloodMagic.MODID + ":arc");
	public static final IRecipeType<RecipeAlchemyTable> ALCHEMYTABLE = IRecipeType.register(BloodMagic.MODID + ":alchemytable");
}
