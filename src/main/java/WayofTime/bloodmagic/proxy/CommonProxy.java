package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.util.handler.EventHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelperV2;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    @Deprecated
    public InventoryRenderHelper getRenderHelper()
    {
        return null;
    }

    public InventoryRenderHelperV2 getRenderHelperV2()
    {
        return null;
    }

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(TeleportQueue.getInstance());
        registerRenderers();
    }

    public void init()
    {

    }

    public void postInit()
    {

    }

    public void registerRenderers()
    {

    }

    public Object beamCont(World worldObj, double xi, double yi, double zi, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
