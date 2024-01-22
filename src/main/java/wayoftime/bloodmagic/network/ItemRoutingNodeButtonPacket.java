package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.tile.routing.TileFilteredRoutingNode;

public class ItemRoutingNodeButtonPacket
{
	private BlockPos pos;
	private int buttonPress;

	public ItemRoutingNodeButtonPacket()
	{
		this(BlockPos.ZERO, 0);
	}

	public ItemRoutingNodeButtonPacket(BlockPos pos, int buttonPress)
	{
		this.pos = pos;
		this.buttonPress = buttonPress;
	}

	public static void encode(ItemRoutingNodeButtonPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeInt(pkt.buttonPress);
	}

	public static ItemRoutingNodeButtonPacket decode(FriendlyByteBuf buf)
	{
		ItemRoutingNodeButtonPacket pkt = new ItemRoutingNodeButtonPacket(buf.readBlockPos(), buf.readInt());

		return pkt;
	}

	public static void handle(ItemRoutingNodeButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			Player player = context.get().getSender();
			if (player == null || player.level().isClientSide)
			{
				return;
			}
			BlockEntity tile = player.getCommandSenderWorld().getBlockEntity(message.pos);
			if (tile instanceof TileFilteredRoutingNode)
			{
				int buttonPress = message.buttonPress;
				if (buttonPress >= 6)
				{
					if (buttonPress == 6)
					{
						((TileFilteredRoutingNode) tile).incrementCurrentPriotiryToMaximum(9);
					} else if (buttonPress == 7)
					{
						((TileFilteredRoutingNode) tile).decrementCurrentPriority();
					}
				} else
				{
					((TileFilteredRoutingNode) tile).swapFilters(buttonPress);
				}
			}
		});
		context.get().setPacketHandled(true);
	}
}
