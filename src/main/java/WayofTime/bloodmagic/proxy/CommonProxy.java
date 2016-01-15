package WayofTime.bloodmagic.proxy;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.register(new EventHandler());
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
