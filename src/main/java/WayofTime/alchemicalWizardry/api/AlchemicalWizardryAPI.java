package WayofTime.alchemicalWizardry.api;

import WayofTime.alchemicalWizardry.api.util.helper.LogHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;

public class AlchemicalWizardryAPI {

    @Getter @Setter
    private static boolean loggingEnabled;

    @Getter
    private static LogHelper logger = new LogHelper("AlchemicalWizardry|API");

    @Getter
    private static DamageSource damageSource = new DamageSourceBloodMagic();

    @Getter @Setter
    private static Item orbItem;

    @Getter @Setter
    private static Fluid lifeEssence;
}
