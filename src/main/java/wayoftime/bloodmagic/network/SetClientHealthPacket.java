package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;

public class SetClientHealthPacket
{
	public float health;

	public SetClientHealthPacket()
	{

	}

	public SetClientHealthPacket(float health)
	{
		this.health = health;
	}

	public static void encode(SetClientHealthPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeFloat(pkt.health);
	}

	public static SetClientHealthPacket decode(FriendlyByteBuf buf)
	{
		SetClientHealthPacket pkt = new SetClientHealthPacket(buf.readFloat());

		return pkt;
	}

	public static void handle(SetClientHealthPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateClientHealth(message.health));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateClientHealth(float health)
	{
		Minecraft.getInstance().player.setHealth(health);
	}
}
