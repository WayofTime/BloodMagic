package wayoftime.bloodmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;

public class SetClientVelocityPacket
{
	public double motionX;
	public double motionY;
	public double motionZ;

	public SetClientVelocityPacket()
	{

	}

	public SetClientVelocityPacket(double motionX, double motionY, double motionZ)
	{
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public static void encode(SetClientVelocityPacket pkt, FriendlyByteBuf buf)
	{
		buf.writeDouble(pkt.motionX);
		buf.writeDouble(pkt.motionY);
		buf.writeDouble(pkt.motionZ);
	}

	public static SetClientVelocityPacket decode(FriendlyByteBuf buf)
	{
		SetClientVelocityPacket pkt = new SetClientVelocityPacket(buf.readDouble(), buf.readDouble(), buf.readDouble());

		return pkt;
	}

	public static void handle(SetClientVelocityPacket message, Supplier<Context> context)
	{
		context.get().enqueueWork(() -> updateClientVelocity(message.motionX, message.motionY, message.motionZ));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateClientVelocity(double motionX, double motionY, double motionZ)
	{
		Minecraft.getInstance().player.setDeltaMovement(motionX, motionY, motionZ);
	}
}
