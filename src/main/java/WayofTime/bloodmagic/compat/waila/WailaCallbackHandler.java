package WayofTime.bloodmagic.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockAlchemyArray;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.block.base.BlockEnumPillar;
import WayofTime.bloodmagic.block.base.BlockEnumPillarCap;
import WayofTime.bloodmagic.block.base.BlockEnumStairs;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderAlchemyArray;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderBloodAltar;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderMimic;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderPillar;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderRitualController;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderTeleposer;

public class WailaCallbackHandler
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerNBTProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerBodyProvider(new DataProviderTeleposer(), BlockTeleposer.class);
        registrar.registerBodyProvider(new DataProviderRitualController(), BlockRitualController.class);
        registrar.registerBodyProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerStackProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerStackProvider(new DataProviderMimic(), BlockMimic.class);
        registrar.registerNBTProvider(new DataProviderMimic(), BlockMimic.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumPillarCap.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumPillar.class);
        registrar.registerStackProvider(DataProviderPillar.INSTANCE, BlockEnumStairs.class);

        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK, false);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_ALTAR, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_TELEPOSER, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_RITUAL, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_ARRAY, true);
    }
}
