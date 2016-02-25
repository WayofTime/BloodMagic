package WayofTime.bloodmagic.compat.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import WayofTime.bloodmagic.compat.ICompatibility;

public class CompatibilityWaila implements ICompatibility
{
    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.INIT)
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
