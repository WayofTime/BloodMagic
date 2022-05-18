package wayoftime.bloodmagic.common.recipe;

import net.minecraft.item.crafting.IRecipeType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;

public class BloodMagicRecipeType
{
	public static final IRecipeType<RecipeBloodAltar> ALTAR = IRecipeType.register(BloodMagic.MODID + ":altar");
	public static final IRecipeType<RecipeAlchemyArray> ARRAY = IRecipeType.register(BloodMagic.MODID + ":array");
	public static final IRecipeType<RecipeTartaricForge> TARTARICFORGE = IRecipeType.register(BloodMagic.MODID + ":soulforge");
	public static final IRecipeType<RecipeARC> ARC = IRecipeType.register(BloodMagic.MODID + ":arc");
	public static final IRecipeType<RecipeAlchemyTable> ALCHEMYTABLE = IRecipeType.register(BloodMagic.MODID + ":alchemytable");
	public static final IRecipeType<RecipeLivingDowngrade> LIVINGDOWNGRADE = IRecipeType.register(BloodMagic.MODID + ":livingdowngrade");
	public static final IRecipeType<RecipePotionFlaskBase> POTIONFLASK = IRecipeType.register(BloodMagic.MODID + ":potionflask");
	public static final IRecipeType<RecipeMeteor> METEOR = IRecipeType.register(BloodMagic.MODID + ":meteor");
}
