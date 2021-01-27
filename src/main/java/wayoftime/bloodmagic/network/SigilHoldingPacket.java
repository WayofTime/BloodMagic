package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class SigilHoldingPacket
{
	private int slot;
	private int mode;

	public SigilHoldingPacket()
	{
	}

	public SigilHoldingPacket(int slot, int mode)
	{
		this.slot = slot;
		this.mode = mode;
	}

	public static void encode(SigilHoldingPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.mode);
	}

	public static SigilHoldingPacket decode(PacketBuffer buf)
	{
		SigilHoldingPacket pkt = new SigilHoldingPacket(buf.readInt(), buf.readInt());

		return pkt;
	}

	public static void handle(SigilHoldingPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(SigilHoldingPacket msg, PlayerEntity playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.inventory.getStackInSlot(msg.slot);
		}

		if (!itemStack.isEmpty())
		{
			ItemSigilHolding.cycleToNextSigil(itemStack, msg.mode);
		}
	}
}
