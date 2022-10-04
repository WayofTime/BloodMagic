package wayoftime.bloodmagic.common.data.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.data.DataGenerator;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.*;

public class BloodMagicRecipeProvider extends BaseRecipeProvider
{
	public BloodMagicRecipeProvider(DataGenerator gen)
	{
		super(gen, BloodMagic.MODID);
	}

	@Override
	protected List<ISubRecipeProvider> getSubRecipeProviders()
	{
//		return Arrays.asList(new BloodAltarRecipeProvider(), new AlchemyArrayRecipeProvider(), new TartaricForgeRecipeProvider(), new ARCRecipeProvider(), new AlchemyTableRecipeProvider(), new LivingDowngradeRecipeProvider(), new PotionRecipeProvider(), new MeteorRecipeProvider());
		return Arrays.asList(new BloodAltarRecipeProvider(), new AlchemyArrayRecipeProvider(), new TartaricForgeRecipeProvider(), new ARCRecipeProvider(), new AlchemyTableRecipeProvider(), new LivingDowngradeRecipeProvider(), new PotionRecipeProvider(), new MeteorRecipeProvider(), new ArmorDyeRecipeProvider());
	}
}
