package wayoftime.bloodmagic;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import wayoftime.bloodmagic.common.fluid.BMFluids;

@Mod(BloodMagic.MODID)
public class BloodMagic {
    public static final String MODID = "bloodmagic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BloodMagic(IEventBus modBus) {
        BMFluids.register(modBus);
    }
}
