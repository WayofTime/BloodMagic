package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;

public class FilterButtonPacket
{
	private int slot;
	private int ghostSlot;
	private String buttonKey;
	private int currentButtonState;

	public FilterButtonPacket()
	{
	}

	public FilterButtonPacket(int slot, int ghostSlot, String buttonKey, int currentButtonState)
	{
		this.slot = slot;
		this.ghostSlot = ghostSlot;
		this.buttonKey = buttonKey;
		this.currentButtonState = currentButtonState;
	}

	public static void encode(FilterButtonPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.ghostSlot);
		buf.writeUtf(pkt.buttonKey);
		buf.writeInt(pkt.currentButtonState);
	}

	public static FilterButtonPacket decode(FriendlyByteBuf buf)
	{
		FilterButtonPacket pkt = new FilterButtonPacket(buf.readInt(), buf.readInt(), buf.readUtf(32767), buf.readInt());

		return pkt;
	}

	public static void handle(FilterButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(FilterButtonPacket msg, Player playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.getInventory().getItem(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof IRoutingFilterProvider)
		{
			((IRoutingFilterProvider) itemStack.getItem()).receiveButtonPress(itemStack, msg.buttonKey, msg.ghostSlot, msg.currentButtonState);
		}
	}
}
