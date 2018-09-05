package com.wayoftime.bloodmagic.compat.waila;

import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class BloodMagicHwylaPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(DataProviderBloodAltar.INSTANCE, TileBloodAltar.class);
        registrar.registerNBTProvider(DataProviderBloodAltar.INSTANCE, TileBloodAltar.class);
    }
}
