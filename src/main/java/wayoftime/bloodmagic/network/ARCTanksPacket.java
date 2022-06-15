package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;

public class ARCTanksPacket
{
	private BlockPos pos;
	private CompoundTag inputNBT;
	private CompoundTag outputNBT;

	public ARCTanksPacket()
	{
		pos = BlockPos.ZERO;
		inputNBT = new CompoundTag();
		outputNBT = new CompoundTag();
	}

	public ARCTanksPacket(TileAlchemicalReactionChamber tile)
	{
		this(tile.getBlockPos(), tile.inputTank.writeToNBT(new CompoundTag()), tile.outputTank.writeToNBT(new CompoundTag()));
	}

	public ARCTanksPacket(BlockPos pos, CompoundTag inputNBT, CompoundTag outputNBT)
	{
		this.pos = pos;
		this.inputNBT = inputNBT;
		this.outputNBT = outputNBT;
	}

	public static void encode(ARCTanksPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pkt.pos);
		buf.writeNbt(pkt.inputNBT);
		buf.writeNbt(pkt.outputNBT);
	}

	public static ARCTanksPacket decode(FriendlyByteBuf buf)
	{
		ARCTanksPacket pkt = new ARCTanksPacket(buf.readBlockPos(), buf.readNbt(), buf.readNbt());

		return pkt;
	}

	public static void handle(ARCTanksPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateTanks(message.pos, message.inputNBT, message.outputNBT));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateTanks(BlockPos pos, CompoundTag inputNBT, CompoundTag outputNBT)
	{
		Level world = Minecraft.getInstance().level;
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileAlchemicalReactionChamber)
		{
			((TileAlchemicalReactionChamber) tile).inputTank.readFromNBT(inputNBT);
			((TileAlchemicalReactionChamber) tile).outputTank.readFromNBT(outputNBT);
		}
	}
}
