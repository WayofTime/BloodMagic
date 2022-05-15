package wayoftime.bloodmagic.common.data.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.data.DataGenerator;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.ARCRecipeProvider;
import wayoftime.bloodmagic.common.recipe.AlchemyArrayRecipeProvider;
import wayoftime.bloodmagic.common.recipe.AlchemyTableRecipeProvider;
import wayoftime.bloodmagic.common.recipe.BloodAltarRecipeProvider;
import wayoftime.bloodmagic.common.recipe.ISubRecipeProvider;
import wayoftime.bloodmagic.common.recipe.LivingDowngradeRecipeProvider;
import wayoftime.bloodmagic.common.recipe.PotionRecipeProvider;
import wayoftime.bloodmagic.common.recipe.TartaricForgeRecipeProvider;

public class BloodMagicRecipeProvider extends BaseRecipeProvider
{
	public BloodMagicRecipeProvider(DataGenerator gen)
	{
		super(gen, BloodMagic.MODID);
	}

	@Override
	protected List<ISubRecipeProvider> getSubRecipeProviders()
	{
		return Arrays.asList(new BloodAltarRecipeProvider(), new AlchemyArrayRecipeProvider(), new TartaricForgeRecipeProvider(), new ARCRecipeProvider(), new AlchemyTableRecipeProvider(), new LivingDowngradeRecipeProvider(), new PotionRecipeProvider());
	}
}
