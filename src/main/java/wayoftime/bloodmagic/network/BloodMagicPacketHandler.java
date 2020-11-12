package wayoftime.bloodmagic.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.ChatUtil;

public class BloodMagicPacketHandler extends BasePacketHandler
{
//	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(BloodMagic.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	public static final SimpleChannel INSTANCE = createChannel(new ResourceLocation(BloodMagic.MODID, "main"));

	@Override
	public void initialize()
	{
		registerServerToClient(ChatUtil.PacketNoSpamChat.class, ChatUtil.PacketNoSpamChat::encode, ChatUtil.PacketNoSpamChat::decode, ChatUtil.PacketNoSpamChat::handle);
		registerServerToClient(ARCTanksPacket.class, ARCTanksPacket::encode, ARCTanksPacket::decode, ARCTanksPacket::handle);
		registerServerToClient(DemonAuraClientPacket.class, DemonAuraClientPacket::encode, DemonAuraClientPacket::decode, DemonAuraClientPacket::handle);
//		INSTANCE.registerMessage(id, messageType, encoder, decoder, messageConsumer);
//		INSTANCE.registerMessage(ChatUtil.PacketNoSpamChat.Handler.class, ChatUtil.PacketNoSpamChat.class, 0, Side.CLIENT);
//		INSTANCE.registerMessage(ItemRouterButtonPacketProcessor.class, ItemRouterButtonPacketProcessor.class, 1, Side.SERVER);
//		INSTANCE.registerMessage(PlayerVelocityPacketProcessor.class, PlayerVelocityPacketProcessor.class, 2, Side.CLIENT);
//		INSTANCE.registerMessage(PlayerFallDistancePacketProcessor.class, PlayerFallDistancePacketProcessor.class, 3, Side.SERVER);
//		INSTANCE.registerMessage(SigilHoldingPacketProcessor.class, SigilHoldingPacketProcessor.class, 4, Side.SERVER);
//		INSTANCE.registerMessage(KeyProcessor.class, KeyProcessor.class, 5, Side.SERVER);
//		INSTANCE.registerMessage(DemonAuraPacketProcessor.class, DemonAuraPacketProcessor.class, 6, Side.CLIENT);
//		INSTANCE.registerMessage(ItemRouterAmountPacketProcessor.class, ItemRouterAmountPacketProcessor.class, 7, Side.SERVER);
	}

	protected SimpleChannel getChannel()
	{
		return INSTANCE;
	}

//	public static void sendToAllAround(IMessage message, TileEntity te, int range)
//	{
//		INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
//	}
//
//	public static void sendToAllAround(IMessage message, TileEntity te)
//	{
//		sendToAllAround(message, te, 64);
//	}
//
//	public static void sendTo(IMessage message, EntityPlayerMP player)
//	{
//		INSTANCE.sendTo(message, player);
//	}
}