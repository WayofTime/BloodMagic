package wayoftime.bloodmagic.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;
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
		registerServerToClient(SetClientHealthPacket.class, SetClientHealthPacket::encode, SetClientHealthPacket::decode, SetClientHealthPacket::handle);
		registerServerToClient(SetClientVelocityPacket.class, SetClientVelocityPacket::encode, SetClientVelocityPacket::decode, SetClientVelocityPacket::handle);
		registerServerToClient(AlchemyTableFlagPacket.class, AlchemyTableFlagPacket::encode, AlchemyTableFlagPacket::decode, AlchemyTableFlagPacket::handle);

		registerClientToServer(KeyProcessorPacket.class, KeyProcessorPacket::encode, KeyProcessorPacket::decode, KeyProcessorPacket::handle);
		registerClientToServer(SigilHoldingPacket.class, SigilHoldingPacket::encode, SigilHoldingPacket::decode, SigilHoldingPacket::handle);
		registerClientToServer(AlchemyTableButtonPacket.class, AlchemyTableButtonPacket::encode, AlchemyTableButtonPacket::decode, AlchemyTableButtonPacket::handle);
		registerClientToServer(RouterFilterPacket.class, RouterFilterPacket::encode, RouterFilterPacket::decode, RouterFilterPacket::handle);
		registerClientToServer(ItemRoutingNodeButtonPacket.class, ItemRoutingNodeButtonPacket::encode, ItemRoutingNodeButtonPacket::decode, ItemRoutingNodeButtonPacket::handle);
		registerClientToServer(FilterButtonPacket.class, FilterButtonPacket::encode, FilterButtonPacket::decode, FilterButtonPacket::handle);
		registerClientToServer(LivingTrainerPacket.class, LivingTrainerPacket::encode, LivingTrainerPacket::decode, LivingTrainerPacket::handle);
		registerClientToServer(LivingTrainerWhitelistPacket.class, LivingTrainerWhitelistPacket::encode, LivingTrainerWhitelistPacket::decode, LivingTrainerWhitelistPacket::handle);
		registerClientToServer(CycleRitualDivinerPacket.class, CycleRitualDivinerPacket::encode, CycleRitualDivinerPacket::decode, CycleRitualDivinerPacket::handle);
		registerClientToServer(FilterGhostSlotPacket.class, FilterGhostSlotPacket::encode, FilterGhostSlotPacket::decode, FilterGhostSlotPacket::handle);

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