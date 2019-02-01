package WayofTime.bloodmagic.teleport;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.event.TeleposeEvent;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
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
                BlockPos targetTeleposer = new BlockPos(x, y, z);
                if (entity.timeUntilPortal <= 0) {
                    entity.timeUntilPortal = 10;
                    if (entity instanceof EntityPlayer) {

                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < getTeleportCost())
                            return;

                        if (teleposer && MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), targetTeleposer)))
                            return;

                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        network.syphon(ticket(entity.world, player, getTeleportCost()));

                        player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                        player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                        player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));

                        player.getEntityWorld().playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), targetTeleposer));
                    } else {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                            return;

                        if (teleposer && MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), targetTeleposer)))
                            return;

                        WorldServer world = (WorldServer) entity.getEntityWorld();

                        network.syphon(ticket(world, entity, getTeleportCost() / 10));

                        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
                        world.resetUpdateEntityTick();

                        entity.getEntityWorld().playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), entity.getEntityWorld(), targetTeleposer));
                    }
                } else {
                    entity.timeUntilPortal = 10;
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
                    entity.timeUntilPortal = 10;
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    WorldServer oldWorldServer = server.getWorld(entity.dimension);
                    WorldServer newWorldServer = server.getWorld(newWorldID);
                    BlockPos targetTeleposer = new BlockPos(x, y, z);
                    ChunkPos teleposerChunk = new ChunkPos(targetTeleposer);
                    ForgeChunkManager.Ticket chunkTicket = ForgeChunkManager.requestTicket("bloodmagic", newWorldServer, ForgeChunkManager.Type.NORMAL);
                    ForgeChunkManager.forceChunk(chunkTicket, teleposerChunk);

                    if (entity instanceof EntityPlayer) {
                        EntityPlayerMP player = (EntityPlayerMP) entity;


                        if (!player.getEntityWorld().isRemote) {
                            SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                            if (network.getCurrentEssence() < getTeleportCost()) {
                                ForgeChunkManager.releaseTicket(chunkTicket);
                                return;
                            }

                            if (teleposer && MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, targetTeleposer))) {
                                ForgeChunkManager.releaseTicket(chunkTicket);
                                return;
                            }

                            network.syphon(ticket(oldWorld, player, getTeleportCost()));

                            /* begin brandon3055 "BrandonsCore" interdimensional teleportation code */

                            PlayerList playerList = server.getPlayerList();

                            player.dimension = newWorldID;
                            player.connection.sendPacket(new SPacketRespawn(player.dimension, newWorldServer.getDifficulty(), newWorldServer.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
                            playerList.updatePermissionLevel(player);
                            oldWorldServer.removeEntityDangerously(player);
                            player.isDead = false;

                            //region Transfer to world

                            player.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, player.rotationYaw, player.rotationPitch);
                            player.connection.setPlayerLocation(x + 0.5, y + 0.5, z + 0.5, player.rotationYaw, player.rotationPitch);
                            newWorldServer.spawnEntity(player);
                            newWorldServer.updateEntityWithOptionalForce(player, false);
                            player.setWorld(newWorldServer);

                            //endregion

                            playerList.preparePlayer(player, oldWorldServer);
                            player.connection.setPlayerLocation(x + 0.5, y + 0.5, z + 0.5, player.rotationYaw, player.rotationPitch);
                            player.interactionManager.setWorld(newWorldServer);
                            player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));

                            playerList.updateTimeAndWeatherForPlayer(player, newWorldServer);
                            playerList.syncPlayerInventory(player);

                            for (PotionEffect potioneffect : player.getActivePotionEffects()) {
                                player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
                            }
                            FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, entity.dimension, newWorldID);
                            player.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, player.rotationYaw, player.rotationPitch);

                            /* end brandon3055 teleportation code */

                            if (teleposer)
                                MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, targetTeleposer));
                        }

                    } else if (!entity.getEntityWorld().isRemote) {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkOwner);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                            return;

                        if (teleposer && MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, targetTeleposer)))
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
                        }

                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();
                        if (teleposer)
                            MinecraftForge.EVENT_BUS.post(new TeleposeEvent.Ent.Post(entity, entity.getEntityWorld(), entity.getPosition(), newWorldServer, targetTeleposer));
                    }
                    newWorldServer.playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

                    ForgeChunkManager.releaseTicket(chunkTicket);
                } else {
                    entity.timeUntilPortal = 10;
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
