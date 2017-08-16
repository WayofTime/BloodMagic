package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ItemRouterButtonPacketProcessor implements IMessage, IMessageHandler<ItemRouterButtonPacketProcessor, IMessage> {
    private int buttonPress;
    private int dimension;
    private BlockPos pos;

    public ItemRouterButtonPacketProcessor() {

    }

    public ItemRouterButtonPacketProcessor(int buttonPress, BlockPos pos, World world) {
        this.buttonPress = buttonPress;
        this.pos = pos;
        this.dimension = world.provider.getDimension();
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        dimension = buff.readInt();
        pos = buff.readBlockPos();
        buttonPress = buff.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        buff.writeInt(dimension);
        buff.writeBlockPos(pos);
        buff.writeInt(buttonPress);
    }

    @Override
    public IMessage onMessage(ItemRouterButtonPacketProcessor message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            message.onMessageFromClient();
        }
        return null;
    }

    public void onMessageFromClient() {
        World world = DimensionManager.getWorld(dimension);
        if (world != null) {
            if (!world.isBlockLoaded(pos))
                return;

            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFilteredRoutingNode) {
                if (buttonPress >= 6) {
                    if (buttonPress == 6) {
                        ((TileFilteredRoutingNode) tile).incrementCurrentPriotiryToMaximum(9);
                    } else if (buttonPress == 7) {
                        ((TileFilteredRoutingNode) tile).decrementCurrentPriority();
                    }
                } else {
                    ((TileFilteredRoutingNode) tile).swapFilters(buttonPress);
                }
            }
        }
    }
}
