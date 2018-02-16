package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DemonAuraPacketProcessor implements IMessage, IMessageHandler<DemonAuraPacketProcessor, IMessage> {
    public DemonWillHolder currentWill = new DemonWillHolder();

    public DemonAuraPacketProcessor() {

    }

    public DemonAuraPacketProcessor(DemonWillHolder holder) {
        this.currentWill = holder;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            currentWill.willMap.put(type, buff.readDouble());
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            if (currentWill.willMap.containsKey(type)) {
                buff.writeDouble(currentWill.willMap.get(type));
            } else {
                buff.writeDouble(0);
            }
        }
    }

    @Override
    public IMessage onMessage(DemonAuraPacketProcessor message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            message.onMessageFromServer();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void onMessageFromServer() {
        ClientProxy.currentAura = currentWill;
    }
}
