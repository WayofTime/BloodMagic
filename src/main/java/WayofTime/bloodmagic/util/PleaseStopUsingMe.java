package WayofTime.bloodmagic.util;

import net.minecraft.util.DamageSource;

/**
 * The primary API class. Includes helper methods and blacklists.
 * <p>
 * Some API methods can be used via IMC instead. The supported methods are:
 */
// TODO - Nuke this class
@Deprecated
public class PleaseStopUsingMe {
    public static boolean loggingEnabled;

    public static DamageSource damageSource = new DamageSourceBloodMagic();
}
