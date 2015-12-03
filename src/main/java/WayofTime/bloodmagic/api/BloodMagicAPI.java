package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.api.util.helper.LogHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BloodMagicAPI {

    public static final String ORB = "ItemBloodOrb";
    public static final String SCRIBE = "ItemInscriptionTool";

    @Getter @Setter
    private static boolean loggingEnabled;

    @Getter
    private static LogHelper logger = new LogHelper("BloodMagic|API");

    @Getter
    private static DamageSource damageSource = new DamageSourceBloodMagic();

    /**
     * Used to obtain Items from BloodMagic. Use the constants above for common items in case internal names
     * change.
     *
     * @param name - The registered name of the item. Usually the same as the class name.
     * @return - The requested Item
     */
    public static Item getItem(String name) {
        return GameRegistry.findItem(Constants.Mod.MODID, name);
    }

    @Getter @Setter
    private static Fluid lifeEssence;
}
