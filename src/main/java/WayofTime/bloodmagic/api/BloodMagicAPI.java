package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.api.util.helper.LogHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;

public class BloodMagicAPI {

    @Getter @Setter
    private static boolean loggingEnabled;

    @Getter
    private static LogHelper logger = new LogHelper("BloodMagic|API");

    @Getter
    private static DamageSource damageSource = new DamageSourceBloodMagic();

    @Getter @Setter
    private static Item orbItem;

    @Getter @Setter
    private static Fluid lifeEssence;
}
