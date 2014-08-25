package WayofTime.alchemicalWizardry.client;

import net.minecraft.client.Minecraft;
import WayofTime.alchemicalWizardry.client.renderer.RenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ClientEventHandler 
{
    private Minecraft      mcClient = FMLClientHandler.instance().getClient();

    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        if (!RenderHelper.onTickInGame(mcClient))
        {

        }
    }
}
