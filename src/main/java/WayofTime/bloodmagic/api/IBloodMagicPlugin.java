package WayofTime.bloodmagic.api;

/**
 * The main class to implement to create a Blood Magic plugin. Everything communicated between a mod and Blood Magic is through this class.
 * IBloodMagicPlugins must have the {@link BloodMagicPlugin} annotation to get loaded by Blood Magic.
 */
public interface IBloodMagicPlugin {

    /**
     * Register mod content with the API
     *
     * @param api The active instance of the {@link IBloodMagicAPI}
     */
    void register(IBloodMagicAPI api);
}
