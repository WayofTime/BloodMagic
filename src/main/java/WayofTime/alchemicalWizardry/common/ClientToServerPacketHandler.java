package WayofTime.alchemicalWizardry.common;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientToServerPacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("alchemicalwizardry");

    public static void init()
    {
        INSTANCE.registerMessage(MessageKeyPressed.class, MessageKeyPressed.class, 0, Side.SERVER);
    }
}