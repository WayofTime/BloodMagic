package WayofTime.bloodmagic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerFallDistancePacketProcessor implements IMessage, IMessageHandler<PlayerFallDistancePacketProcessor, IMessage> {
    private float fallDistance;

    public PlayerFallDistancePacketProcessor() {

    }

    public PlayerFallDistancePacketProcessor(float fallDistance) {
        this.fallDistance = fallDistance;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        fallDistance = buff.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        buff.writeFloat(fallDistance);
    }

    @Override
    public IMessage onMessage(PlayerFallDistancePacketProcessor message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            message.onMessageFromClient(ctx.getServerHandler().player);
        }
        return null;
    }

    public void onMessageFromClient(EntityPlayer player) {
        player.fallDistance = fallDistance;
    }
}
