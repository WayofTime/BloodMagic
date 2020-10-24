package wayoftime.bloodmagic.network;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
	public static String readString(PacketBuffer buffer)
	{
		return buffer.readString(Short.MAX_VALUE);
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
	public static PlayerEntity getPlayer(Supplier<Context> context)
	{
		return context.get().getSender();
	}

	private int index = 0;

	protected abstract SimpleChannel getChannel();

	public abstract void initialize();

	protected <MSG> void registerClientToServer(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder,
			Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> consumer)
	{
		getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}

	protected <MSG> void registerServerToClient(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder,
			Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> consumer)
	{
		getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

	/**
	 * Send this message to the specified player.
	 *
	 * @param message - the message to send
	 * @param player  - the player to send it to
	 */
	public <MSG> void sendTo(MSG message, ServerPlayerEntity player)
	{
		getChannel().sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
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
	public <MSG> void sendToDimension(MSG message, RegistryKey<World> dimension)
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

	public <MSG> void sendToAllTracking(MSG message, TileEntity tile)
	{
		sendToAllTracking(message, tile.getWorld(), tile.getPos());
	}

	public <MSG> void sendToAllTracking(MSG message, World world, BlockPos pos)
	{
		if (world instanceof ServerWorld)
		{
			// If we have a ServerWorld just directly figure out the ChunkPos so as to not
			// require looking up the chunk
			// This provides a decent performance boost over using the packet distributor
			((ServerWorld) world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(message, p));
		} else
		{
			// Otherwise fallback to entities tracking the chunk if some mod did something
			// odd and our world is not a ServerWorld
			getChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), message);
		}
	}

}