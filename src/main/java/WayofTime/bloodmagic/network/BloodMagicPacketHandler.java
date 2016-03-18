package WayofTime.bloodmagic.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.ChatUtil;

public class BloodMagicPacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Constants.Mod.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(ChatUtil.PacketNoSpamChat.Handler.class, ChatUtil.PacketNoSpamChat.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(ItemRouterButtonPacketProcessor.class, ItemRouterButtonPacketProcessor.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncConfig.class, PacketSyncConfig.class, 2, Side.CLIENT);
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range)
    {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimensionId(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te)
    {
        sendToAllAround(message, te, 64);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        INSTANCE.sendTo(message, player);
    }
}
