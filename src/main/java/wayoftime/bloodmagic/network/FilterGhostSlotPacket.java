package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.common.item.routing.ItemItemRouterFilter;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class FilterGhostSlotPacket
{
	private int ghostSlot;
	private ItemStack stack;

	public FilterGhostSlotPacket()
	{
	}

	public FilterGhostSlotPacket(int ghostSlot, ItemStack stack)
	{
		this.ghostSlot = ghostSlot;
		this.stack = stack;
	}

	public static void encode(FilterGhostSlotPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeInt(pkt.ghostSlot);
		buf.writeItem(pkt.stack);
	}

	public static FilterGhostSlotPacket decode(FriendlyByteBuf buf)
	{
		FilterGhostSlotPacket pkt = new FilterGhostSlotPacket(buf.readInt(), buf.readItem());

		return pkt;
	}

	public static void handle(FilterGhostSlotPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender == null)
				return;

			AbstractContainerMenu container = sender.containerMenu;
			if (container == null)
				return;

//			ItemStack filterStack = container.filterStack;
			Slot slot = container.slots.get(message.ghostSlot);
			if (container instanceof ContainerFilter)
			{
//				ItemStack copyStack = heldStack.copy();
				ItemStack filterStack = ((ContainerFilter) container).filterStack;
				GhostItemHelper.setItemGhostAmount(message.stack, 0);
				message.stack.setCount(1);
				slot.set(message.stack);

//				ItemStack filterStack = this.filterStack;
				if (filterStack.getItem() instanceof IRoutingFilterProvider)
				{
					ItemStack filterCopy = ((IRoutingFilterProvider) filterStack.getItem()).getContainedStackForType(filterStack, message.stack);
					slot.set(filterCopy);
				}
			}
		});
		context.get().setPacketHandled(true);
	}

//	public static void sendKeyToServer(FilterGhostSlotPacket msg, Player playerEntity)
//	{
//		ItemStack itemStack = ItemStack.EMPTY;
//
//		if (msg.slot > -1 && msg.slot < 9)
//		{
//			itemStack = playerEntity.getInventory().getItem(msg.slot);
//		}
//
//		if (!itemStack.isEmpty() && itemStack.getItem() instanceof IItemFilterProvider)
//		{
//			((IItemFilterProvider) itemStack.getItem()).receiveButtonPress(itemStack, msg.buttonKey, msg.ghostSlot, msg.currentButtonState);
//		}
//	}
}
