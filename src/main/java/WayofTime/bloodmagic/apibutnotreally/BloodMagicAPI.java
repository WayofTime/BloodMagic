package WayofTime.bloodmagic.apibutnotreally;

import WayofTime.bloodmagic.apibutnotreally.util.helper.LogHelper;
import net.minecraft.util.DamageSource;

/**
 * The primary API class. Includes helper methods and blacklists.
 * <p>
 * Some API methods can be used via IMC instead. The supported methods are:
 */
// TODO - Nuke this class
public class BloodMagicAPI {
    public static boolean loggingEnabled;

    public static LogHelper logger = new LogHelper("BloodMagic|API");

    public static DamageSource damageSource = new DamageSourceBloodMagic();
}
