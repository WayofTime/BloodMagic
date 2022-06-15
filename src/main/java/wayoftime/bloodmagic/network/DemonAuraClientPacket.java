package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class DemonAuraClientPacket
{
	public DemonWillHolder currentWill = new DemonWillHolder();

	public DemonAuraClientPacket()
	{

	}

	public DemonAuraClientPacket(DemonWillHolder holder)
	{
		this.currentWill = holder;
	}

	public static void encode(DemonAuraClientPacket pkt, FriendlyByteBuf buf)
	{
		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			if (pkt.currentWill.willMap.containsKey(type))
			{
				buf.writeDouble(pkt.currentWill.willMap.get(type));
			} else
			{
				buf.writeDouble(0);
			}
		}

	}

	public static DemonAuraClientPacket decode(FriendlyByteBuf buf)
	{
		DemonAuraClientPacket pkt = new DemonAuraClientPacket();
		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			pkt.currentWill.willMap.put(type, buf.readDouble());
		}

		return pkt;
	}

	public static void handle(DemonAuraClientPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateClientHolder(message.currentWill));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateClientHolder(DemonWillHolder holder)
	{
		ClientHandler.currentAura = holder;
	}
}
