package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.compat.waila.provider.*;
import WayofTime.bloodmagic.tile.*;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class BloodMagicHwylaPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(DataProviderBloodAltar.INSTANCE, TileAltar.class);
        registrar.registerNBTProvider(DataProviderBloodAltar.INSTANCE, TileAltar.class);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_ALTAR, true);

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
