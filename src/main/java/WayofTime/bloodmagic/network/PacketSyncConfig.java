package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncConfig implements IMessage, IMessageHandler<PacketSyncConfig, IMessage>
{

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ConfigHandler.wailaAltarDisplayMode = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(ConfigHandler.wailaAltarDisplayMode);
    }

    @Override
    public IMessage onMessage(PacketSyncConfig message, MessageContext ctx)
    {
        return null;
    }
}
