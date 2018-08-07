package WayofTime.bloodmagic.ritual.portal;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.event.TeleposeEvent;
import WayofTime.bloodmagic.teleport.Teleport;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class Teleports {

    public static class TeleportSameDim extends Teleport {
        private final boolean teleposer;

        public TeleportSameDim(int x, int y, int z, Entity entity, UUID networkOwner, boolean teleposer) {
            this(new BlockPos(x, y, z), entity, networkOwner, teleposer);
        }

        public TeleportSameDim(BlockPos blockPos, Entity entity, UUID networkOwner, boolean teleposer) {
            super(blockPos, entity, networkOwner);
            this.teleposer = teleposer;
        }

        @Override
        public void teleport() {
            if (entity != null) {
                if (entity.timeUntilPortal <= 0) {
                    if (entity instanceof EntityPlayer) {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < getTeleportCost())
                            return;

                        if (teleposer)
                            if (MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), new BlockPos(x, y, z))))
                                return;

                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        network.syphon(ticket(entity.world, player, getTeleportCost()));

                        player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                        player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                        player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                        player.timeUntilPortal = 150;

                        player.getEntityWorld().playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), new BlockPos(x, y, z)));
                    } else {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                            return;

                        if (teleposer)
                            if (MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), new BlockPos(x, y, z))))
                                return;

                        WorldServer world = (WorldServer) entity.getEntityWorld();

                        network.syphon(ticket(world, entity, getTeleportCost() / 10));

                        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
                        entity.timeUntilPortal = 150;
                        world.resetUpdateEntityTick();

                        entity.getEntityWorld().playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), new BlockPos(x, y, z)));
                    }
                }
            }
        }

        @Override
        public int getTeleportCost() {
            return 1000;
        }
    }

    public static class TeleportToDim extends Teleport {
        private World oldWorld;
        private int newWorldID;
        private boolean teleposer;

        public TeleportToDim(int x, int y, int z, Entity entity, UUID networkOwner, World oldWorld, int newWorld, boolean teleposer) {
            this(new BlockPos(x, y, z), entity, networkOwner, oldWorld, newWorld, teleposer);
        }

        public TeleportToDim(BlockPos blockPos, Entity entity, UUID networkOwner, World oldWorld, int newWorldID, boolean teleposer) {
            super(blockPos, entity, networkOwner);
            this.oldWorld = oldWorld;
            this.newWorldID = newWorldID;
            this.teleposer = teleposer;
        }

        @Override
        public void teleport() {
            if (entity != null) {
                if (entity.timeUntilPortal <= 0) {
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    WorldServer oldWorldServer = server.getWorld(entity.dimension);
                    WorldServer newWorldServer = server.getWorld(newWorldID);

                    if (entity instanceof EntityPlayer) {
                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        if (!player.getEntityWorld().isRemote) {
                            SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                            if (network.getCurrentEssence() < getTeleportCost())
                                return;

                            if (teleposer)
                                if (MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, new BlockPos(x, y, z))))
                                    return;

                            network.syphon(ticket(oldWorld, player, getTeleportCost()));

                            player.changeDimension(newWorldID); //TODO: UNTESTED
//                            server.getConfigurationManager().transferPlayerToDimension(player, newWorldID, new TeleporterBloodMagic(newWorldServer));
                            player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                            player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                            player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                            if (teleposer)
                                MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, new BlockPos(x, y, z)));
                        }

                    } else if (!entity.getEntityWorld().isRemote) {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                            return;

                        if (teleposer)
                            if (MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, new BlockPos(x, y, z))))
                                return;

                        network.syphon(ticket(oldWorld, entity, getTeleportCost() / 10));

                        NBTTagCompound tag = new NBTTagCompound();

                        entity.writeToNBTOptional(tag);
                        entity.setDead();
                        oldWorld.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

                        Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorldServer);
                        if (teleportedEntity != null) {
                            teleportedEntity.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, entity.rotationYaw, entity.rotationPitch);
                            teleportedEntity.forceSpawn = true;
                            newWorldServer.spawnEntity(teleportedEntity);
                            teleportedEntity.setWorld(newWorldServer);
                            teleportedEntity.timeUntilPortal = teleportedEntity instanceof EntityPlayer ? 150 : 20;
                        }

                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, new BlockPos(x, y, z)));
                    }
                    entity.timeUntilPortal = entity instanceof EntityLiving ? 150 : 20;
                    newWorldServer.playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                }
            }
        }

        @Override
        public int getTeleportCost() {
            return 10000;
        }

        public World getOldWorld() {
            return oldWorld;
        }

        public int getNewWorldID() {
            return newWorldID;
        }

        public boolean isTeleposer() {
            return teleposer;
        }
    }

    public static SoulTicket ticket(World world, Entity entity, int amount) {
        return new SoulTicket(new TextComponentString("teleport|" + world.provider.getDimension() + "|" + entity.getName() + "|" + entity.getPosition().toLong()), amount);
    }
}
