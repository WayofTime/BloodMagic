package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;

public class LivingTrainerPacket
{
	private int slot;
	private int ghostSlot;
	private int level;

	public LivingTrainerPacket()
	{
	}

	public LivingTrainerPacket(int slot, int ghostSlot, int level)
	{
		this.slot = slot;
		this.ghostSlot = ghostSlot;
		this.level = level;
	}

	public static void encode(LivingTrainerPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.ghostSlot);
		buf.writeInt(pkt.level);
	}

	public static LivingTrainerPacket decode(PacketBuffer buf)
	{
		LivingTrainerPacket pkt = new LivingTrainerPacket(buf.readInt(), buf.readInt(), buf.readInt());

		return pkt;
	}

	public static void handle(LivingTrainerPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(LivingTrainerPacket msg, PlayerEntity playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.inventory.getStackInSlot(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemLivingTrainer)
		{
//			((IItemFilterProvider) itemStack.getItem()).setGhostItemAmount(itemStack, msg.ghostSlot, msg.level);
			((ItemLivingTrainer) itemStack.getItem()).setTomeLevel(itemStack, msg.slot, msg.level);
		}
	}
}