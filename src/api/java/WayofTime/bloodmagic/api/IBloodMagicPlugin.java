package WayofTime.bloodmagic.api;

/**
 * The main class to implement to create a Blood Magic plugin. Everything communicated between a mod and Blood Magic is through this class.
 * IBloodMagicPlugins must have the {@link BloodMagicPlugin} annotation to get loaded by Blood Magic.
 */
public interface IBloodMagicPlugin {

    /**
     * Register mod content with the API. Called during {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}.
     *
     * @param api The active instance of the {@link IBloodMagicAPI}
     */
    default void register(IBloodMagicAPI api) {
        // No-op
    }

    /**
     * Register recipes with the API. Called during {@link net.minecraftforge.event.RegistryEvent.Register<net.minecraft.item.crafting.IRecipe>}.
     *
     * @param recipeRegistrar The active instance of the {@link IBloodMagicRecipeRegistrar}
     */
    default void registerRecipes(IBloodMagicRecipeRegistrar recipeRegistrar) {
        // No-op
    }
}
