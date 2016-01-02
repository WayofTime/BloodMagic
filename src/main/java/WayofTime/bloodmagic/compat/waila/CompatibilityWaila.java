package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.compat.ICompatibility;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CompatibilityWaila implements ICompatibility
{
    @Override
    public void loadCompatibility()
    {
        FMLInterModComms.sendMessage(getModId(), "register", "WayofTime.bloodmagic.compat.waila.WailaCallbackHandler.callbackRegister");
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
}
