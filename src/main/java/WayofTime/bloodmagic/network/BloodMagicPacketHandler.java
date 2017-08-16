package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BloodMagicPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(BloodMagic.MODID);

    public static void init() {
        INSTANCE.registerMessage(ChatUtil.PacketNoSpamChat.Handler.class, ChatUtil.PacketNoSpamChat.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(ItemRouterButtonPacketProcessor.class, ItemRouterButtonPacketProcessor.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PlayerVelocityPacketProcessor.class, PlayerVelocityPacketProcessor.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(PlayerFallDistancePacketProcessor.class, PlayerFallDistancePacketProcessor.class, 3, Side.SERVER);
        INSTANCE.registerMessage(SigilHoldingPacketProcessor.class, SigilHoldingPacketProcessor.class, 4, Side.SERVER);
        INSTANCE.registerMessage(KeyProcessor.class, KeyProcessor.class, 5, Side.SERVER);
        INSTANCE.registerMessage(DemonAuraPacketProcessor.class, DemonAuraPacketProcessor.class, 6, Side.CLIENT);
        INSTANCE.registerMessage(ItemRouterAmountPacketProcessor.class, ItemRouterAmountPacketProcessor.class, 7, Side.SERVER);
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range) {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te) {
        sendToAllAround(message, te, 64);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }
}
