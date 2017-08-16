package WayofTime.bloodmagic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerVelocityPacketProcessor implements IMessage, IMessageHandler<PlayerVelocityPacketProcessor, IMessage> {
    private double motionX;
    private double motionY;
    private double motionZ;

    public PlayerVelocityPacketProcessor() {

    }

    public PlayerVelocityPacketProcessor(double motionX, double motionY, double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        motionX = buff.readDouble();
        motionY = buff.readDouble();
        motionZ = buff.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        buff.writeDouble(motionX);
        buff.writeDouble(motionY);
        buff.writeDouble(motionZ);
    }

    @Override
    public IMessage onMessage(PlayerVelocityPacketProcessor message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            message.onMessageFromServer();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void onMessageFromServer() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        player.motionX = motionX;
        player.motionY = motionY;
        player.motionZ = motionZ;
    }
}
