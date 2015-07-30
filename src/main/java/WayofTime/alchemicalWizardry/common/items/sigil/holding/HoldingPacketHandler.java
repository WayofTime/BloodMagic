package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class HoldingPacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SigilOfHolding");

    public static void init()
    {
        INSTANCE.registerMessage(HoldingPacketProcessor.class, HoldingPacketProcessor.class, 0, Side.SERVER);
    }
}
