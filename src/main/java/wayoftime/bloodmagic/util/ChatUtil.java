package wayoftime.bloodmagic.util;

import java.text.DecimalFormat;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ChatUtil
{
	private static final int DELETION_ID = 2525277;
	private static int lastAdded;
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");

	private static void sendNoSpamMessages(ITextComponent[] messages)
	{
		NewChatGui chat = Minecraft.getInstance().gui.getChat();
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
	public static ITextComponent wrap(String s)
	{
		return new StringTextComponent(s);
	}

	/**
	 * @see #wrap(String)
	 */
	public static ITextComponent[] wrap(String... s)
	{
		ITextComponent[] ret = new ITextComponent[s.length];
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
	public static ITextComponent wrapFormatted(String s, Object... args)
	{
		return new TranslationTextComponent(s, args);
	}

	/**
	 * Simply sends the passed lines to the player in a chat message.
	 *
	 * @param player The player to send the chat to
	 * @param lines  The lines to send
	 */
	public static void sendChat(PlayerEntity player, String... lines)
	{
		sendChat(player, wrap(lines));
	}

	/**
	 * Localizes the lines before sending them.
	 *
	 * @see #sendChat(EntityPlayer, String...)
	 */
	public static void sendChatUnloc(PlayerEntity player, String... unlocLines)
	{
		sendChat(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * Sends all passed chat components to the player.
	 *
	 * @param player The player to send the chat lines to.
	 * @param lines  The {@link ITextComponent chat components} to send.yes
	 */
	public static void sendChat(PlayerEntity player, ITextComponent... lines)
	{
		for (ITextComponent c : lines)
		{
//			BloodMagic.packetHandler.send
			player.sendMessage(c, Util.NIL_UUID);
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
	public static void sendNoSpamClient(ITextComponent... lines)
	{
		sendNoSpamMessages(lines);
	}

	/**
	 * Localizes the strings before sending them.
	 *
	 * @see #sendNoSpam(EntityPlayer, String...)
	 */
	public static void sendNoSpamUnloc(PlayerEntity player, String... unlocLines)
	{
		sendNoSpam(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayer, ITextComponent...)
	 */
	public static void sendNoSpam(PlayerEntity player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}

	/**
	 * First checks if the player is instanceof {@link ServerPlayerEntity} before
	 * casting.
	 *
	 * @see #sendNoSpam(ServerPlayerEntity, ITextComponent...)
	 */
	public static void sendNoSpam(PlayerEntity player, ITextComponent... lines)
	{
		if (player instanceof ServerPlayerEntity)
		{
			sendNoSpam((ServerPlayerEntity) player, lines);
		}
	}

	/**
	 * Localizes the strings before sending them.
	 *
	 * @see #sendNoSpam(ServerPlayerEntity, String...)
	 */
	public static void sendNoSpamUnloc(ServerPlayerEntity player, String... unlocLines)
	{
		sendNoSpam(player, TextHelper.localizeAll(unlocLines));
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(ServerPlayerEntity, ITextComponent...)
	 */
	public static void sendNoSpam(ServerPlayerEntity player, String... lines)
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
	public static void sendNoSpam(ServerPlayerEntity player, ITextComponent... lines)
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
		private ITextComponent[] chatLines;

		public PacketNoSpamChat()
		{
			chatLines = new ITextComponent[0];
		}

		private PacketNoSpamChat(ITextComponent... lines)
		{
			// this is guaranteed to be >1 length by accessing methods
			this.chatLines = lines;
		}

		public static void encode(PacketNoSpamChat pkt, PacketBuffer buf)
		{
			buf.writeInt(pkt.chatLines.length);
			for (ITextComponent c : pkt.chatLines)
			{
//				ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(c));
				buf.writeUtf(ITextComponent.Serializer.toJson(c));
			}
		}

		public static PacketNoSpamChat decode(PacketBuffer buf)
		{
			PacketNoSpamChat pkt = new PacketNoSpamChat(new ITextComponent[buf.readInt()]);
			for (int i = 0; i < pkt.chatLines.length; i++)
			{
//				pkt.chatLines[i] = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
				pkt.chatLines[i] = ITextComponent.Serializer.fromJsonLenient(buf.readUtf());
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