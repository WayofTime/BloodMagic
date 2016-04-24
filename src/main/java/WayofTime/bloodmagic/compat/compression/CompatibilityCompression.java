package WayofTime.bloodmagic.compat.compression;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compress.StorageBlockCraftingRecipeAssimilator;

public class CompatibilityCompression implements ICompatibility
{

    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.INIT)
        {
            try
            {
                Class compressionRecipe = Class.forName("temportalist.compression.main.common.recipe.RecipeClassicCompress");
                Class decompressionRecipe = Class.forName("temportalist.compression.main.common.recipe.RecipeClassicDecompress");
                StorageBlockCraftingRecipeAssimilator.ignore.add(compressionRecipe);
                StorageBlockCraftingRecipeAssimilator.ignore.add(decompressionRecipe);
            } catch (ClassNotFoundException e)
            {
                BloodMagic.instance.getLogger().error("Found mod Compression but did not find the IRecipe classes. Did they get moved?");
                BloodMagic.instance.getLogger().error(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public String getModId()
    {
        return "compression";
    }

    @Override
    public boolean enableCompat()
    {
        return ConfigHandler.ignoreCompressionSpamAddedByCompression;
    }
}
