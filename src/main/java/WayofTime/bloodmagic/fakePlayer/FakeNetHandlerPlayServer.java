package WayofTime.bloodmagic.fakePlayer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Set;

/**
 * All credits for this go to CrazyPants, from EIO
 */
public class FakeNetHandlerPlayServer extends NetHandlerPlayServer {
    public FakeNetHandlerPlayServer(EntityPlayerMP p_i1530_3_) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), new net.minecraft.network.NetworkManager(EnumPacketDirection.CLIENTBOUND), p_i1530_3_);
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
    public void processVehicleMove(CPacketVehicleMove packetIn) {
    }

    @Override
    public void processConfirmTeleport(CPacketConfirmTeleport packetIn) {
    }

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet) {
    }

    @Override
    public void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock packetIn) {
    }

    @Override
    public void processTryUseItem(CPacketPlayerTryUseItem packetIn) {
    }

    @Override
    public void processSteerBoat(CPacketSteerBoat packetIn) {
    }

    @Override
    public void processCustomPayload(CPacketCustomPayload packetIn) {
    }

    @Override
    public void processInput(CPacketInput p_147358_1_) {
    }

    @Override
    public void processPlayer(CPacketPlayer p_147347_1_) {
    }

    @Override
    public void setPlayerLocation(double p_147364_1_, double p_147364_3_, double p_147364_5_, float p_147364_7_, float p_147364_8_) {
    }

    @Override
    public void processPlayerDigging(CPacketPlayerDigging p_147345_1_) {
    }

    @Override
    public void onDisconnect(ITextComponent p_147231_1_) {
    }

    @Override
    public void sendPacket(Packet<?> p_147359_1_) {
    }

    @Override
    public void processHeldItemChange(CPacketHeldItemChange p_147355_1_) {
    }

    @Override
    public void processChatMessage(CPacketChatMessage p_147354_1_) {
    }

    @Override
    public void handleAnimation(CPacketAnimation packetIn) {

    }

    @Override
    public void processEntityAction(CPacketEntityAction p_147357_1_) {
    }

    @Override
    public void processUseEntity(CPacketUseEntity p_147340_1_) {
    }

    @Override
    public void processClientStatus(CPacketClientStatus p_147342_1_) {
    }

    @Override
    public void processCloseWindow(CPacketCloseWindow p_147356_1_) {
    }

    @Override
    public void processClickWindow(CPacketClickWindow p_147351_1_) {
    }

    @Override
    public void processEnchantItem(CPacketEnchantItem p_147338_1_) {
    }

    @Override
    public void processCreativeInventoryAction(CPacketCreativeInventoryAction p_147344_1_) {
    }

    @Override
    public void processConfirmTransaction(CPacketConfirmTransaction p_147339_1_) {
    }

    @Override
    public void processUpdateSign(CPacketUpdateSign p_147343_1_) {
    }

    @Override
    public void processKeepAlive(CPacketKeepAlive p_147353_1_) {
    }

    @Override
    public void processPlayerAbilities(CPacketPlayerAbilities p_147348_1_) {
    }

    @Override
    public void processTabComplete(CPacketTabComplete p_147341_1_) {
    }

    @Override
    public void processClientSettings(CPacketClientSettings p_147352_1_) {
    }

    @Override
    public void handleSpectate(CPacketSpectate packetIn) {
    }

    @Override
    public void handleResourcePackStatus(CPacketResourcePackStatus packetIn) {
    }
}