package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.tile.routing.TileFilteredRoutingNode;

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

	public static void encode(ItemRoutingNodeButtonPacket pkt, PacketBuffer buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeInt(pkt.buttonPress);
	}

	public static ItemRoutingNodeButtonPacket decode(PacketBuffer buf)
	{
		ItemRoutingNodeButtonPacket pkt = new ItemRoutingNodeButtonPacket(buf.readBlockPos(), buf.readInt());

		return pkt;
	}

	public static void handle(ItemRoutingNodeButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			if (player == null || player.world.isRemote)
			{
				return;
			}
			TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
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
