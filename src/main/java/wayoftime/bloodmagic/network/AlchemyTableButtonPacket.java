package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.tile.TileAlchemyTable;

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

	public static void encode(AlchemyTableButtonPacket pkt, PacketBuffer buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeInt(pkt.slot);
		buf.writeInt(pkt.dir.ordinal());
		buf.writeBoolean(pkt.enable);
	}

	public static AlchemyTableButtonPacket decode(PacketBuffer buf)
	{
		AlchemyTableButtonPacket pkt = new AlchemyTableButtonPacket(buf.readBlockPos(), buf.readInt(), Direction.from3DDataValue(buf.readInt()), buf.readBoolean());

		return pkt;
	}

	public static void handle(AlchemyTableButtonPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			if (player == null)
			{
				return;
			}
			TileEntity tile = player.getCommandSenderWorld().getBlockEntity(message.pos);
			if (tile instanceof TileAlchemyTable)
			{
				((TileAlchemyTable) tile).setSlotEnabled(message.enable, message.slot, message.dir);
			}
		});
		context.get().setPacketHandled(true);
	}
}