package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SigilHoldingPacketProcessor implements IMessage, IMessageHandler<SigilHoldingPacketProcessor, IMessage> {
    private int slot;
    private int mode;

    public SigilHoldingPacketProcessor() {
    }

    public SigilHoldingPacketProcessor(int slot, int mode) {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(slot);
        buffer.writeInt(mode);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        slot = buffer.readInt();
        mode = buffer.readInt();
    }

    @Override
    public IMessage onMessage(SigilHoldingPacketProcessor message, MessageContext ctx) {
        ItemStack itemStack = ItemStack.EMPTY;

        if (message.slot > -1 && message.slot < 9) {
            itemStack = ctx.getServerHandler().player.inventory.getStackInSlot(message.slot);
        }

        if (!itemStack.isEmpty()) {
            ItemSigilHolding.cycleToNextSigil(itemStack, message.mode);
        }

        return null;
    }
}