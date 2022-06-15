package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.tile.TileAlchemyTable;

public class AlchemyTableButtonPacket
{
	private BlockPos pos;
	private int slot;
	private Direction dir;
	private boolean enable;

	public AlchemyTableButtonPacket()
	{
		this(BlockPos.ZERO, 0, Direction.DOWN, true);
	}

	public AlchemyTableButtonPacket(BlockPos pos, int slot, Direction dir, boolean enable)
	{
		this.pos = pos;
		this.slot = slot;
		this.dir = dir;
		this.enable = enable;
	}

	public static void encode(AlchemyTableButtonPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.dir.ordinal());
		buf.writeBoolean(pkt.enable);
	}

	public static AlchemyTableButtonPacket decode(FriendlyByteBuf buf)
	{
		AlchemyTableButtonPacket pkt = new AlchemyTableButtonPacket(buf.readBlockPos(), buf.readInt(), Direction.from3DDataValue(buf.readInt()), buf.readBoolean());

		return pkt;
	}

	public static void handle(AlchemyTableButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			Player player = context.get().getSender();
			if (player == null)
			{
				return;
			}
			BlockEntity tile = player.getCommandSenderWorld().getBlockEntity(message.pos);
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).setSlotEnabled(message.enable, message.slot, message.dir);
			}
		});
		context.get().setPacketHandled(true);
	}
}