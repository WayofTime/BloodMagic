package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;

public class ARCTanksPacket
{
	private BlockPos pos;
	private CompoundNBT inputNBT;
	private CompoundNBT outputNBT;

	public ARCTanksPacket()
	{
		pos = BlockPos.ZERO;
		inputNBT = new CompoundNBT();
		outputNBT = new CompoundNBT();
	}

	public ARCTanksPacket(TileAlchemicalReactionChamber tile)
	{
		this(tile.getPos(), tile.inputTank.writeToNBT(new CompoundNBT()), tile.outputTank.writeToNBT(new CompoundNBT()));
	}

	public ARCTanksPacket(BlockPos pos, CompoundNBT inputNBT, CompoundNBT outputNBT)
	{
		this.pos = pos;
		this.inputNBT = inputNBT;
		this.outputNBT = outputNBT;
	}

	public static void encode(ARCTanksPacket pkt, PacketBuffer buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeCompoundTag(pkt.inputNBT);
		buf.writeCompoundTag(pkt.outputNBT);
	}

	public static ARCTanksPacket decode(PacketBuffer buf)
	{
		ARCTanksPacket pkt = new ARCTanksPacket(buf.readBlockPos(), buf.readCompoundTag(), buf.readCompoundTag());

		return pkt;
	}

	public static void handle(ARCTanksPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateTanks(message.pos, message.inputNBT, message.outputNBT));
		context.get().setPacketHandled(true);
	}

	public static void updateTanks(BlockPos pos, CompoundNBT inputNBT, CompoundNBT outputNBT)
	{
		World world = Minecraft.getInstance().world;
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileAlchemicalReactionChamber)
		{
			((TileAlchemicalReactionChamber) tile).inputTank.readFromNBT(inputNBT);
			((TileAlchemicalReactionChamber) tile).outputTank.readFromNBT(outputNBT);
		}
	}
}
