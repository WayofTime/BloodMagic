package wayoftime.bloodmagic.common.recipe;

import net.minecraft.world.item.crafting.RecipeType;
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
	public static final RecipeType<RecipeBloodAltar> ALTAR = RecipeType.register(BloodMagic.MODID + ":altar");
	public static final RecipeType<RecipeAlchemyArray> ARRAY = RecipeType.register(BloodMagic.MODID + ":array");
	public static final RecipeType<RecipeTartaricForge> TARTARICFORGE = RecipeType.register(BloodMagic.MODID + ":soulforge");
	public static final RecipeType<RecipeARC> ARC = RecipeType.register(BloodMagic.MODID + ":arc");
	public static final RecipeType<RecipeAlchemyTable> ALCHEMYTABLE = RecipeType.register(BloodMagic.MODID + ":alchemytable");
	public static final RecipeType<RecipeLivingDowngrade> LIVINGDOWNGRADE = RecipeType.register(BloodMagic.MODID + ":livingdowngrade");
	public static final RecipeType<RecipePotionFlaskBase> POTIONFLASK = RecipeType.register(BloodMagic.MODID + ":potionflask");
	public static final RecipeType<RecipeMeteor> METEOR = RecipeType.register(BloodMagic.MODID + ":meteor");
}
