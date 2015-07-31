package WayofTime.alchemicalWizardry.common;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ClientToServerPacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("alchemicalwizardry");

    public static void init()
    {
        INSTANCE.registerMessage(MessageKeyPressed.class, MessageKeyPressed.class, 0, Side.SERVER);
    }
}