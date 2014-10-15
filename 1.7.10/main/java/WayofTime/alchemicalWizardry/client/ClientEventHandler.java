package WayofTime.alchemicalWizardry.client;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.client.renderer.RenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler
{
    private Minecraft mcClient = FMLClientHandler.instance().getClient();

    @SubscribeEvent
    public void onPlayerSoundEvent(SoundEvent event)
    {
        if (Minecraft.getMinecraft() != null)
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player != null && player.isPotionActive(AlchemicalWizardry.customPotionDeaf))
            {
                event.setResult(Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        if (!RenderHelper.onTickInGame(mcClient))
        {

        }
    }

    @SubscribeEvent
    public void onRenderLivingPlayerPre(RenderPlayerEvent.Pre event)
    {
        GL11.glDisable(2929);
    }

    @SubscribeEvent
    public void onRenderLivingPlayerPost(RenderPlayerEvent.Post event)
    {
        GL11.glEnable(2929);
    }
}
