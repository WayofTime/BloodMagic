package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
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

	public static void encode(CycleRitualDivinerPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeInt(pkt.slot);
	}

	public static CycleRitualDivinerPacket decode(FriendlyByteBuf buf)
	{
		CycleRitualDivinerPacket pkt = new CycleRitualDivinerPacket(buf.readInt());

		return pkt;
	}

	public static void handle(CycleRitualDivinerPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(CycleRitualDivinerPacket msg, Player playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.getInventory().getItem(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemRitualDiviner)
		{
			((ItemRitualDiviner) itemStack.getItem()).cycleRitual(itemStack, playerEntity, true);
		}
	}
}
