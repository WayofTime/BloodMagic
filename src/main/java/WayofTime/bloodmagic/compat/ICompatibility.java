package WayofTime.bloodmagic.compat;

/**
 * Implement on all primary compatibility classes.
 */
public interface ICompatibility
{
    /**
     * Called during each initialization phase after the given
     * {@link #getModId()} has been verified as loaded.
     * 
     * @param phase
     *        - The load phase at which this method is being called.
     */
    void loadCompatibility(InitializationPhase phase);

    /**
     * @return The {@code modid} of the mod we are adding compatibility for.
     */
    String getModId();

    /**
     * Whether or not compatibility should be loaded even if the mod were to be
     * found.
     * 
     * Generally a determined by a config option.
     * 
     * @return If Compatibility should load.
     */
    boolean enableCompat();

    /**
     * Represents a given mod initialization state.
     */
    enum InitializationPhase
    {
        /**
         * Represents
         * {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
         */
        PRE_INIT,
        /**
         * Represents
         * {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}
         */
        INIT,
        /**
         * Represents
         * {@link net.minecraftforge.fml.common.event.FMLPostInitializationEvent}
         */
        POST_INIT
    }
}
