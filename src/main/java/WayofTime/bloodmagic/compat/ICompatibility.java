package WayofTime.bloodmagic.compat;

/**
 * Implement on all primary compatibility classes.
 */
public interface ICompatibility {

    /**
     * Called after the given {@code modid} has been verified as loaded.
     */
    void loadCompatibility();

    /**
     * The {@code modid} of the mod we are adding compatibility for.
     */
    String getModId();

    /**
     * Whether or not compatibility should be loaded even if the mod were to be found.
     *
     * Generally a determined by a config option.
     */
    boolean enableCompat();
}
