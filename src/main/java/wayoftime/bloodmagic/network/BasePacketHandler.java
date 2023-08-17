package wayoftime.bloodmagic.network;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Copied liberally from Mekanism. Many thanks to pupnewfster!
 *
 */
public abstract class BasePacketHandler
{

	protected static SimpleChannel createChannel(ResourceLocation name)
	{
		return NetworkRegistry.ChannelBuilder.named(name).clientAcceptedVersions(getProtocolVersion()::equals).serverAcceptedVersions(getProtocolVersion()::equals).networkProtocolVersion(BasePacketHandler::getProtocolVersion).simpleChannel();
	}

	private static String getProtocolVersion()
	{
		return "1";
	}

	/**
	 * Helper for reading strings to make sure we don't accidentally call
	 * PacketBuffer#readString on the server
	 */
	public static String readString(FriendlyByteBuf buffer)
	{
		return buffer.readUtf(Short.MAX_VALUE);
	}

//	public static void log(String log)
//	{
//		// TODO: Add more logging for packets using this
//		if (MekanismConfig.general.logPackets.get())
//		{
//			Mekanism.logger.info(log);
//		}
//	}
//
	public static Player getPlayer(Supplier<Context> context)
	{
		return context.get().getSender();
	}

	private int index = 0;

	protected abstract SimpleChannel getChannel();

	public abstract void initialize();

	protected <MSG> void registerClientToServer(Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<Context>> consumer)
	{
		getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}

	protected <MSG> void registerServerToClient(Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<Context>> consumer)
	{
		getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

	/**
	 * Send this message to the specified player.
	 *
	 * @param message - the message to send
	 * @param player  - the player to send it to
	 */
	public <MSG> void sendTo(MSG message, ServerPlayer player)
	{
		getChannel().sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	/**
	 * Send this message to everyone connected to the server.
	 *
	 * @param message - message to send
	 */
	public <MSG> void sendToAll(MSG message)
	{
		getChannel().send(PacketDistributor.ALL.noArg(), message);
	}

	/**
	 * Send this message to everyone within the supplied dimension.
	 *
	 * @param message   - the message to send
	 * @param dimension - the dimension to target
	 */
	public <MSG> void sendToDimension(MSG message, ResourceKey<Level> dimension)
	{
		getChannel().send(PacketDistributor.DIMENSION.with(() -> dimension), message);
	}

	/**
	 * Send this message to the server.
	 *
	 * @param message - the message to send
	 */
	public <MSG> void sendToServer(MSG message)
	{
		getChannel().sendToServer(message);
	}

	public <MSG> void sendToAllTracking(MSG message, Entity entity)
	{
		getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
	}

	public <MSG> void sendToAllTracking(MSG message, BlockEntity tile)
	{
		sendToAllTracking(message, tile.getLevel(), tile.getBlockPos());
	}

	public <MSG> void sendToAllTracking(MSG message, Level world, BlockPos pos)
	{
		if (world instanceof ServerLevel)
		{
			// If we have a ServerWorld just directly figure out the ChunkPos so as to not
			// require looking up the chunk
			// This provides a decent performance boost over using the packet distributor
			((ServerLevel) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(message, p));
		} else
		{
			// Otherwise fallback to entities tracking the chunk if some mod did something
			// odd and our world is not a ServerWorld
			getChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), message);
		}
	}

}