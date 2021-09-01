package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;

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

	public static void encode(FilterButtonPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.ghostSlot);
		buf.writeString(pkt.buttonKey);
		buf.writeInt(pkt.currentButtonState);
	}

	public static FilterButtonPacket decode(PacketBuffer buf)
	{
		FilterButtonPacket pkt = new FilterButtonPacket(buf.readInt(), buf.readInt(), buf.readString(), buf.readInt());

		return pkt;
	}

	public static void handle(FilterButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(FilterButtonPacket msg, PlayerEntity playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.inventory.getStackInSlot(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof IItemFilterProvider)
		{
			((IItemFilterProvider) itemStack.getItem()).receiveButtonPress(itemStack, msg.buttonKey, msg.ghostSlot, msg.currentButtonState);
		}
	}
}
