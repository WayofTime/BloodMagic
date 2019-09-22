package WayofTime.bloodmagic.fakePlayer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Set;

/**
 * All credits for this go to CrazyPants, from EIO
 */
public class FakeNetHandlerPlayServer extends ServerPlayNetHandler {
    public FakeNetHandlerPlayServer(ServerPlayerEntity p_i1530_3_) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), new net.minecraft.network.NetworkManager(PacketDirection.CLIENTBOUND), p_i1530_3_);
    }

    @Override
    public NetworkManager getNetworkManager() {
        return null;
    }

    @Override
    public void update() {
    }

    @Override
    public void disconnect(final ITextComponent textComponent) {
    }

    @Override
    public void processVehicleMove(CMoveVehiclePacket packetIn) {
    }

    @Override
    public void processConfirmTeleport(CConfirmTeleportPacket packetIn) {
    }

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPlayerPositionLookPacket.EnumFlags> relativeSet) {
    }

    @Override
    public void processTryUseItemOnBlock(CPlayerTryUseItemOnBlockPacket packetIn) {
    }

    @Override
    public void processTryUseItem(CPlayerTryUseItemPacket packetIn) {
    }

    @Override
    public void processSteerBoat(CSteerBoatPacket packetIn) {
    }

    @Override
    public void processCustomPayload(CCustomPayloadPacket packetIn) {
    }

    @Override
    public void processInput(CInputPacket p_147358_1_) {
    }

    @Override
    public void processPlayer(CPlayerPacket p_147347_1_) {
    }

    @Override
    public void setPlayerLocation(double p_147364_1_, double p_147364_3_, double p_147364_5_, float p_147364_7_, float p_147364_8_) {
    }

    @Override
    public void processPlayerDigging(CPlayerDiggingPacket p_147345_1_) {
    }

    @Override
    public void onDisconnect(ITextComponent p_147231_1_) {
    }

    @Override
    public void sendPacket(IPacket<?> p_147359_1_) {
    }

    @Override
    public void processHeldItemChange(CHeldItemChangePacket p_147355_1_) {
    }

    @Override
    public void processChatMessage(CChatMessagePacket p_147354_1_) {
    }

    @Override
    public void handleAnimation(CAnimateHandPacket packetIn) {

    }

    @Override
    public void processEntityAction(CEntityActionPacket p_147357_1_) {
    }

    @Override
    public void processUseEntity(CUseEntityPacket p_147340_1_) {
    }

    @Override
    public void processClientStatus(CClientStatusPacket p_147342_1_) {
    }

    @Override
    public void processCloseWindow(CCloseWindowPacket p_147356_1_) {
    }

    @Override
    public void processClickWindow(CClickWindowPacket p_147351_1_) {
    }

    @Override
    public void processEnchantItem(CEnchantItemPacket p_147338_1_) {
    }

    @Override
    public void processCreativeInventoryAction(CCreativeInventoryActionPacket p_147344_1_) {
    }

    @Override
    public void processConfirmTransaction(CConfirmTransactionPacket p_147339_1_) {
    }

    @Override
    public void processUpdateSign(CUpdateSignPacket p_147343_1_) {
    }

    @Override
    public void processKeepAlive(CKeepAlivePacket p_147353_1_) {
    }

    @Override
    public void processPlayerAbilities(CPlayerAbilitiesPacket p_147348_1_) {
    }

    @Override
    public void processTabComplete(CTabCompletePacket p_147341_1_) {
    }

    @Override
    public void processClientSettings(CClientSettingsPacket p_147352_1_) {
    }

    @Override
    public void handleSpectate(CSpectatePacket packetIn) {
    }

    @Override
    public void handleResourcePackStatus(CResourcePackStatusPacket packetIn) {
    }
}