package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.client.KeyBindingBloodMagic;
import WayofTime.bloodmagic.client.IKeybindable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyProcessor implements IMessage, IMessageHandler<KeyProcessor, IMessage>
{
    public int keyId;
    public boolean showInChat;

    public KeyProcessor()
    {
    }

    public KeyProcessor(KeyBindingBloodMagic.KeyBindings key, boolean showInChat)
    {
        this.keyId = key.ordinal();
        this.showInChat = showInChat;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.keyId = buf.readInt();
        this.showInChat = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.keyId);
        buf.writeBoolean(this.showInChat);
    }

    @Override
    public IMessage onMessage(KeyProcessor msg, MessageContext ctx)
    {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;

        if (entityPlayer != null)
        {
            ItemStack heldStack = entityPlayer.getHeldItemMainhand();
            if (heldStack.getItem() instanceof IKeybindable)
            {
                if (msg.keyId < 0 || msg.keyId >= KeyBindingBloodMagic.KeyBindings.values().length)
                {
                    return null;
                }
                KeyBindingBloodMagic.KeyBindings key = KeyBindingBloodMagic.KeyBindings.values()[msg.keyId];
                ((IKeybindable) heldStack.getItem()).onKeyPressed(heldStack, entityPlayer, key, msg.showInChat);
            }
        }
        return null;
    }
}
