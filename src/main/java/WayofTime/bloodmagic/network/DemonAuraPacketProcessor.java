package WayofTime.bloodmagic.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.proxy.ClientProxy;

public class DemonAuraPacketProcessor implements IMessage, IMessageHandler<DemonAuraPacketProcessor, IMessage>
{
    public DemonWillHolder currentWill = new DemonWillHolder();

    public DemonAuraPacketProcessor()
    {

    }

    public DemonAuraPacketProcessor(DemonWillHolder holder)
    {
        this.currentWill = holder;
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        PacketBuffer buff = new PacketBuffer(buffer);
        try
        {
            NBTTagCompound tag = buff.readNBTTagCompoundFromBuffer();
            currentWill.readFromNBT(tag, "Aura");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        PacketBuffer buff = new PacketBuffer(buffer);
        NBTTagCompound tag = new NBTTagCompound();
        currentWill.writeToNBT(tag, "Aura");
        buff.writeNBTTagCompoundToBuffer(tag);
    }

    @Override
    public IMessage onMessage(DemonAuraPacketProcessor message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            message.onMessageFromServer();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void onMessageFromServer()
    {
        ClientProxy.currentAura = currentWill;
    }
}
