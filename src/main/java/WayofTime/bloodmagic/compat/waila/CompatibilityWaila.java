package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderBloodAltar;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderRitualController;
import WayofTime.bloodmagic.compat.waila.provider.DataProviderTeleposer;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CompatibilityWaila implements ICompatibility
{
    @Override
    public void loadCompatibility()
    {
        FMLInterModComms.sendMessage(getModId(), "register", getClass().getName() + ".callbackRegister");
    }

    @Override
    public String getModId()
    {
        return "Waila";
    }

    @Override
    public boolean enableCompat()
    {
        return true;
    }

    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerBodyProvider(new DataProviderTeleposer(), BlockTeleposer.class);
        registrar.registerBodyProvider(new DataProviderRitualController(), BlockRitualController.class);

        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK, false);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_ALTAR, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_TELEPOSER, true);
        registrar.addConfig(Constants.Mod.MODID, Constants.Compat.WAILA_CONFIG_RITUAL, true);
    }
}
