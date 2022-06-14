package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;

public class CycleRitualDivinerPacket
{
	private int slot;

	public CycleRitualDivinerPacket()
	{
	}

	public CycleRitualDivinerPacket(int slot)
	{
		this.slot = slot;

	}

	public static void encode(CycleRitualDivinerPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slot);
	}

	public static CycleRitualDivinerPacket decode(PacketBuffer buf)
	{
		CycleRitualDivinerPacket pkt = new CycleRitualDivinerPacket(buf.readInt());

		return pkt;
	}

	public static void handle(CycleRitualDivinerPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(CycleRitualDivinerPacket msg, PlayerEntity playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.inventory.getItem(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemRitualDiviner)
		{
			((ItemRitualDiviner) itemStack.getItem()).cycleRitual(itemStack, playerEntity, true);
		}
	}
}
