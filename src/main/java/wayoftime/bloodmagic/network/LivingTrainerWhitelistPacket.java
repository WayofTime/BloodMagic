package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;

public class LivingTrainerWhitelistPacket
{
	private int slot;
	private boolean isWhitelist;

	public LivingTrainerWhitelistPacket()
	{
	}

	public LivingTrainerWhitelistPacket(int slot, boolean isWhitelist)
	{
		this.slot = slot;
		this.isWhitelist = isWhitelist;
	}

	public static void encode(LivingTrainerWhitelistPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slot);
		buf.writeBoolean(pkt.isWhitelist);
	}

	public static LivingTrainerWhitelistPacket decode(PacketBuffer buf)
	{
		LivingTrainerWhitelistPacket pkt = new LivingTrainerWhitelistPacket(buf.readInt(), buf.readBoolean());

		return pkt;
	}

	public static void handle(LivingTrainerWhitelistPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(LivingTrainerWhitelistPacket msg, PlayerEntity playerEntity)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		if (msg.slot > -1 && msg.slot < 9)
		{
			itemStack = playerEntity.inventory.getStackInSlot(msg.slot);
		}

		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemLivingTrainer)
		{
//			((IItemFilterProvider) itemStack.getItem()).setGhostItemAmount(itemStack, msg.ghostSlot, msg.level);
			((ItemLivingTrainer) itemStack.getItem()).setIsWhitelist(itemStack, msg.isWhitelist);
		}
	}
}