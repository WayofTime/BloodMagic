package wayoftime.bloodmagic.common.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class BMPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1"); // no clue what the version is on about, but docs say 1 so we do 1

        registrar.playToServer(
                GhostAmountPacket.TYPE,
                GhostAmountPacket.STREAM_CODEC,
                GhostAmountPacket::handle
        );
    }
}
