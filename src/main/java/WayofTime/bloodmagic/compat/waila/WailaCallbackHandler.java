package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.block.*;
import WayofTime.bloodmagic.compat.waila.provider.*;
import mcp.mobius.waila.api.IWailaRegistrar;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockEnumPillar;
import WayofTime.bloodmagic.block.base.BlockEnumPillarCap;
import WayofTime.bloodmagic.block.base.BlockEnumStairs;

public class WailaCallbackHandler
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerNBTProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerBodyProvider(new DataProviderTeleposer(), BlockTeleposer.class);
        registrar.registerBodyProvider(new DataProviderRitualController(), BlockRitualController.class);
        registrar.registerBodyProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerBodyProvider(new DataProviderBloodTank(), BlockBloodTank.class);
        registrar.registerNBTProvider(new DataProviderBloodTank(), BlockBloodTank.class);
        registrar.registerStackProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerStackProvider(new DataProviderMimic(), BlockMimic.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumPillarCap.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumPillar.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumStairs.class);

        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK, false);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_ALTAR, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_TELEPOSER, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_RITUAL, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_ARRAY, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_BLOOD_TANK, true);
    }
}
