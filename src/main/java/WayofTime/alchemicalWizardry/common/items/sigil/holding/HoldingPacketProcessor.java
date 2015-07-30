package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HoldingPacketProcessor implements IMessage, IMessageHandler<HoldingPacketProcessor, IMessage>
{
    private int slot;
    private int mode;

    public HoldingPacketProcessor()
    {
    }

    public HoldingPacketProcessor(int slot, int mode)
    {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(slot);
        buffer.writeInt(mode);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        slot = buffer.readInt();
        mode = buffer.readInt();
    }

    @Override
    public IMessage onMessage(HoldingPacketProcessor message, MessageContext ctx)
    {
        ItemStack itemStack = null;

        if (message.slot > -1 && message.slot < 9)
        {
            itemStack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
        }

        if (itemStack != null)
        {
            SigilOfHolding.cycleSigil(itemStack, message.mode);
        }

        return null;
    }
}
