package WayofTime.alchemicalWizardry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;


public class ClientEventHandler
{
    private Minecraft mcClient = FMLClientHandler.instance().getClient();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) 
    {
//        if(KeyBindings.omegaTest.isPressed())
//        {
//            System.out.println("ping");
////            NewPacketHandler.INSTANCE.sendToServer(NewPacketHandler.getKeyboardPressPacket((byte)2));
//            ClientToServerPacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(MessageKeyPressed.Key.OMEGA_ACTIVE));
//        }
    }
    
    @SubscribeEvent
    public void onPlayerSoundEvent(SoundEvent event)
    {
        if (Minecraft.getMinecraft() != null)
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player != null && player.isPotionActive(AlchemicalWizardry.customPotionDeaf))
            {
                event.setResult(Result.DENY);
                if(event.isCancelable())
                	event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onOverlayEvent(RenderBlockOverlayEvent event)
    {
    	if(event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER && event.player.isPotionActive(AlchemicalWizardry.customPotionAmphibian.id))//TODO Placeholder for new potion effect
			if(event.isCancelable())
			{
				event.setCanceled(true);
			}
    	
    	if(event.blockForOverlay == ModBlocks.blockMimic && event.isCancelable())
    	{
    		event.setCanceled(true);
    	}
    }

    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

//        if (!RenderHelper.onTickInGame(mcClient))
        {

        }
    }

//    @SubscribeEvent
//    public void onRenderLivingPlayerPre(RenderPlayerEvent.Pre event)
//    {
//        GL11.glDisable(2929);
//    }
//
//    @SubscribeEvent
//    public void onRenderLivingPlayerPost(RenderPlayerEvent.Post event)
//    {
//        GL11.glEnable(2929);
//    }
}
