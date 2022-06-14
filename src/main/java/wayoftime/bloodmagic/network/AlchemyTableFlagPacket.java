package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.tile.TileAlchemyTable;

public class AlchemyTableFlagPacket
{
	private BlockPos pos;
	private boolean orbFlag;
	private boolean lpFlag;

	public AlchemyTableFlagPacket()
	{
		pos = BlockPos.ZERO;
		orbFlag = false;
		lpFlag = false;
	}

	public AlchemyTableFlagPacket(TileAlchemyTable tile)
	{
		this(tile.getBlockPos(), tile.getOrbFlagForGui(), tile.getLPFlagforGui());
	}

	public AlchemyTableFlagPacket(BlockPos pos, boolean orbFlag, boolean lpFlag)
	{
		this.pos = pos;
		this.orbFlag = orbFlag;
		this.lpFlag = lpFlag;
	}

	public static void encode(AlchemyTableFlagPacket pkt, PacketBuffer buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeBoolean(pkt.orbFlag);
		buf.writeBoolean(pkt.lpFlag);
	}

	public static AlchemyTableFlagPacket decode(PacketBuffer buf)
	{
		AlchemyTableFlagPacket pkt = new AlchemyTableFlagPacket(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean());

		return pkt;
	}

	public static void handle(AlchemyTableFlagPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateTanks(message.pos, message.orbFlag, message.lpFlag));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateTanks(BlockPos pos, boolean orbFlag, boolean lpFlag)
	{
		World world = Minecraft.getInstance().level;
		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemyTable)
		{
			((TileAlchemyTable) tile).setOrbFlagForGui(orbFlag);
			((TileAlchemyTable) tile).setLPFlagForGui(lpFlag);
			if (!orbFlag && !lpFlag)
			{
				((TileAlchemyTable) tile).burnTime = 0;
			}
		}
	}
}
