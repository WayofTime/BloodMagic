package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.Constants;
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
        BMLog.DEFAULT.info("decoded slot {} and stack {} with tag '{}'", pkt.ghostSlot, pkt.stack, pkt.stack.getTag());

		return pkt;
	}

	public static void handle(FilterGhostSlotPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender == null) {
                return;
            }

            if (!(sender.containerMenu instanceof ContainerFilter)) {
                return;
            }
            ItemStack filterStack = sender.getItemInHand(InteractionHand.MAIN_HAND);
            CompoundTag tag = filterStack.getOrCreateTag();
            InventoryFilter filterInv = new InventoryFilter(9);
            filterInv.deserializeNBT(tag.getCompound(Constants.NBT.ITEM_INVENTORY));
            filterInv.setStackInSlot(message.ghostSlot, message.stack);
            tag.put(Constants.NBT.ITEM_INVENTORY, filterInv.serializeNBT());
            filterStack.setTag(tag);
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
