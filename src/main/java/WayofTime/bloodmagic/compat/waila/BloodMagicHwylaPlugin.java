package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.compat.waila.provider.*;
import WayofTime.bloodmagic.tile.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.util.ResourceLocation;

@WailaPlugin
public class BloodMagicHwylaPlugin implements IWailaPlugin {

    public static final ResourceLocation CONFIG_SHOW_ALTAR_STATS = new ResourceLocation(BloodMagic.MODID, "show_altar_stats");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(DataProviderBloodAltar.INSTANCE, TooltipPosition.BODY, TileAltar.class);
        registrar.registerBlockDataProvider(DataProviderBloodAltar.INSTANCE, TileAltar.class);
        registrar.addConfig(CONFIG_SHOW_ALTAR_STATS, true);

        registrar.registerBodyProvider(DataProviderTeleposer.INSTANCE, TileTeleposer.class);
        registrar.registerNBTProvider(DataProviderTeleposer.INSTANCE, TileTeleposer.class);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_TELEPOSER, true);

        registrar.registerBodyProvider(DataProviderRitualController.INSTANCE, TileMasterRitualStone.class);
        registrar.registerNBTProvider(DataProviderRitualController.INSTANCE, TileMasterRitualStone.class);
        registrar.registerBodyProvider(DataProviderRitualController.INSTANCE, TileImperfectRitualStone.class);
        registrar.registerNBTProvider(DataProviderRitualController.INSTANCE, TileImperfectRitualStone.class);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_RITUAL, true);

        registrar.registerBodyProvider(DataProviderBloodTank.INSTANCE, TileBloodTank.class);
        registrar.registerNBTProvider(DataProviderBloodTank.INSTANCE, TileBloodTank.class);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_BLOOD_TANK, true);

        registrar.registerStackProvider(DataProviderAlchemyArray.INSTANCE, TileAlchemyArray.class);
        registrar.registerBodyProvider(DataProviderAlchemyArray.INSTANCE, TileAlchemyArray.class);
        registrar.registerNBTProvider(DataProviderAlchemyArray.INSTANCE, TileAlchemyArray.class);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_ARRAY, true);

        registrar.registerStackProvider(DataProviderMimic.INSTANCE, TileMimic.class);
        registrar.registerNBTProvider(DataProviderMimic.INSTANCE, TileMimic.class);
    }
}
