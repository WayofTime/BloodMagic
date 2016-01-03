package WayofTime.bloodmagic.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import WayofTime.bloodmagic.util.handler.EventHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class CommonProxy
{
    public InventoryRenderHelper getRenderHelper()
    {
        return null;
    }

    public void preInit()
    {
        Object obj = new EventHandler();
        MinecraftForge.EVENT_BUS.register(obj);
        FMLCommonHandler.instance().bus().register(obj);

    }

    public void init()
    {

    }

    public void postInit()
    {

    }
}
