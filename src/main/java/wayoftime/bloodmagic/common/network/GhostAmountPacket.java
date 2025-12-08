package wayoftime.bloodmagic.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.menu.FilterMenu;

public record GhostAmountPacket(int slot, int amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GhostAmountPacket> TYPE = new CustomPacketPayload.Type<>(BloodMagic.rl("ghost_amount"));

    public static final StreamCodec<FriendlyByteBuf, GhostAmountPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, GhostAmountPacket::slot,
            ByteBufCodecs.VAR_INT, GhostAmountPacket::amount,
            GhostAmountPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GhostAmountPacket packet, IPayloadContext context) {
        if (!(context.player().containerMenu instanceof FilterMenu filterMenu)) {
            BloodMagic.LOGGER.warn("ghost amount packet recieved but no open filter menu");
            return;
        }
        ItemStack contentStack = filterMenu.handler.getStackInSlot(packet.slot);
        contentStack.set(BMDataComponents.GHOST_AMOUNT, packet.amount);
        filterMenu.handler.setChanged(packet.slot);
    }
}
