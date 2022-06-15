package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
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

	public static void encode(KeyProcessorPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeInt(pkt.keyId);
		buf.writeBoolean(pkt.showInChat);
	}

	public static KeyProcessorPacket decode(FriendlyByteBuf buf)
	{
		KeyProcessorPacket pkt = new KeyProcessorPacket(buf.readInt(), buf.readBoolean());

		return pkt;
	}

	public static void handle(KeyProcessorPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> sendKeyToServer(message, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public static void sendKeyToServer(KeyProcessorPacket msg, Player playerEntity)
	{
		if (playerEntity != null)
		{
			ItemStack heldStack = playerEntity.getMainHandItem();
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
}
