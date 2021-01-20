package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.client.key.IKeybindable;
import wayoftime.bloodmagic.client.key.KeyBindings;

public class KeyProcessorPacket
{
	public int keyId;
	public boolean showInChat;

	public KeyProcessorPacket(int keyId, boolean showInChat)
	{
		this.keyId = keyId;
		this.showInChat = showInChat;
	}

	public KeyProcessorPacket(KeyBindings key, boolean showInChat)
	{
		this(key.ordinal(), showInChat);
	}

	public static void encode(KeyProcessorPacket pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.keyId);
		buf.writeBoolean(pkt.showInChat);
	}

	public static KeyProcessorPacket decode(PacketBuffer buf)
	{
		KeyProcessorPacket pkt = new KeyProcessorPacket(buf.readInt(), buf.readBoolean());

		return pkt;
	}

	public static void handle(KeyProcessorPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(KeyProcessorPacket msg, PlayerEntity playerEntity)
	{
		System.out.println("Hoiiiii");
		if (playerEntity != null)
		{
			ItemStack heldStack = playerEntity.getHeldItemMainhand();
			if (heldStack.getItem() instanceof IKeybindable)
			{
				if (msg.keyId < 0 || msg.keyId >= KeyBindings.values().length)
				{
					return;
				}
				KeyBindings key = KeyBindings.values()[msg.keyId];

				((IKeybindable) heldStack.getItem()).onKeyPressed(heldStack, playerEntity, key, msg.showInChat);
			}
		}
	}

//	@OnlyIn(Dist.CLIENT)
//	public static void updateClientHolder(DemonWillHolder holder)
//	{
//		ClientHandler.currentAura = holder;
//	}
}
