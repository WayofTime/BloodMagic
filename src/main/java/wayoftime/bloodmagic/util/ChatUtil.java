package wayoftime.bloodmagic.util;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.helper.TextHelper;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class ChatUtil
{
	private static final int DELETION_ID = 2525277;
	private static int lastAdded;
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");

	private static void sendNoSpamMessages(Component[] messages)
	{
		ChatComponent chat = Minecraft.getInstance().gui.getChat();
//		 Minecraft.getMinecraft().ingameGUI.getChatGUI();
//		for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
//		{
//			chat.
////			chat.deleteChatLine(i);
//		}
		for (int i = 0; i < messages.length; i++)
		{
			chat.addMessage(messages[i]);
//			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		}
		lastAdded = DELETION_ID + messages.length - 1;
	}

	/**
	 * Returns a standard {@link TextComponentString} for the given {@link String} .
	 *
	 * @param s The string to wrap.
	 * @return An {@link ITextComponent} containing the string.
	 */
	public static Component wrap(String s)
	{
		return Component.literal(s);
	}

	/**
	 * @see #wrap(String)
	 */
	public static Component[] wrap(String... s)
	{
		Component[] ret = new Component[s.length];
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = wrap(s[i]);
		}
		return ret;
	}

	/**
	 * Returns a translatable chat component for the given string and format args.
	 *
	 * @param s    The string to format
	 * @param args The args to apply to the format
	 */
	public static Component wrapFormatted(String s, Object... args)
	{
		return Component.translatable(s, args);
	}

	/**
	 * Simply sends the passed lines to the player in a chat message.
	 *
	 * @param player The player to send the chat to
	 * @param lines  The lines to send
	 */
	public static void sendChat(Player player, String... lines)
	{
		sendChat(player, wrap(lines));
	}

	/**
	 * Localizes the lines before sending them.
	 *
	 * @see #sendChat(EntityPlayer, String...)
	 */
	public static void sendChatUnloc(Player player, String... unlocLines)
	{
		sendChat(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * Sends all passed chat components to the player.
	 *
	 * @param player The player to send the chat lines to.
	 * @param lines  The {@link ITextComponent chat components} to send.yes
	 */
	public static void sendChat(Player player, Component... lines)
	{
		for (Component c : lines)
		{
//			BloodMagic.packetHandler.send
			player.sendSystemMessage(c);
//			player.sendMessage(c);
		}
	}

	/**
	 * Localizes the strings before sending them.
	 *
	 * @see #sendNoSpamClient(String...)
	 */
	public static void sendNoSpamClientUnloc(String... unlocLines)
	{
		sendNoSpamClient(TextHelper.localizeAll(unlocLines));
	}

	/**
	 * Same as {@link #sendNoSpamClient(ITextComponent...)}, but wraps the Strings
	 * automatically.
	 *
	 * @param lines The chat lines to send
	 * @see #wrap(String)
	 */
	public static void sendNoSpamClient(String... lines)
	{
		sendNoSpamClient(wrap(lines));
	}

	/**
	 * Skips the packet sending, unsafe to call on servers.
	 *
	 * @see #sendNoSpam(ServerPlayerEntity, ITextComponent...)
	 */
	public static void sendNoSpamClient(Component... lines)
	{
		sendNoSpamMessages(lines);
	}

	/**
	 * Localizes the strings before sending them.
	 *
	 * @see #sendNoSpam(EntityPlayer, String...)
	 */
	public static void sendNoSpamUnloc(Player player, String... unlocLines)
	{
		sendNoSpam(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayer, ITextComponent...)
	 */
	public static void sendNoSpam(Player player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}

	/**
	 * First checks if the player is instanceof {@link ServerPlayerEntity} before
	 * casting.
	 *
	 * @see #sendNoSpam(ServerPlayerEntity, ITextComponent...)
	 */
	public static void sendNoSpam(Player player, Component... lines)
	{
		if (player instanceof ServerPlayer)
		{
			sendNoSpam((ServerPlayer) player, lines);
		}
	}

	/**
	 * Localizes the strings before sending them.
	 *
	 * @see #sendNoSpam(ServerPlayerEntity, String...)
	 */
	public static void sendNoSpamUnloc(ServerPlayer player, String... unlocLines)
	{
		sendNoSpam(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(ServerPlayerEntity, ITextComponent...)
	 */
	public static void sendNoSpam(ServerPlayer player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}

	/**
	 * Sends a chat message to the client, deleting past messages also sent via this
	 * method.
	 * <p>
	 * Credit to RWTema for the idea
	 *
	 * @param player The player to send the chat message to
	 * @param lines  The chat lines to send.
	 */
	public static void sendNoSpam(ServerPlayer player, Component... lines)
	{
		if (lines.length > 0)
			BloodMagic.packetHandler.sendTo(new PacketNoSpamChat(lines), player);
	}

	/**
	 * @author tterrag1098
	 *         <p>
	 *         Ripped from EnderCore (and slightly altered)
	 */
	public static class PacketNoSpamChat
	{
		private Component[] chatLines;

		public PacketNoSpamChat()
		{
			chatLines = new Component[0];
		}

		private PacketNoSpamChat(Component... lines)
		{
			// this is guaranteed to be >1 length by accessing methods
			this.chatLines = lines;
		}

		public static void encode(PacketNoSpamChat pkt, FriendlyByteBuf buf)
		{
			buf.writeInt(pkt.chatLines.length);
			for (Component c : pkt.chatLines)
			{
//				ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(c));
				buf.writeUtf(Component.Serializer.toJson(c));
			}
		}

		public static PacketNoSpamChat decode(FriendlyByteBuf buf)
		{
			PacketNoSpamChat pkt = new PacketNoSpamChat(new Component[buf.readInt()]);
			for (int i = 0; i < pkt.chatLines.length; i++)
			{
//				pkt.chatLines[i] = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
				pkt.chatLines[i] = Component.Serializer.fromJsonLenient(buf.readUtf());
			}
			return pkt;
		}

		public static void handle(PacketNoSpamChat message, Supplier<Context> context)
		{
			context.get().enqueueWork(() -> sendNoSpamMessages(message.chatLines));
			context.get().setPacketHandled(true);
		}

//		public static class Handler implements IMessageHandler<PacketNoSpamChat, IMessage>
//		{
//			@Override
//			public IMessage onMessage(final PacketNoSpamChat message, MessageContext ctx)
//			{
//				Minecraft.getMinecraft().addScheduledTask(() -> sendNoSpamMessages(message.chatLines));
//				return null;
//			}
//		}
	}
}