package WayofTime.bloodmagic.ritual.portal;

import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.teleport.Teleport;
import WayofTime.bloodmagic.api.teleport.TeleporterBloodMagic;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Teleports
{

    public static class TeleportSameDim extends Teleport
    {

        public TeleportSameDim(int x, int y, int z, Entity entity, String networkToDrain)
        {
            super(x, y, z, entity, networkToDrain);
        }

        public TeleportSameDim(BlockPos blockPos, Entity entity, String networkToDrain)
        {
            super(blockPos, entity, networkToDrain);
        }

        @Override
        public void teleport()
        {
            if (entity != null)
            {
                if (entity.timeUntilPortal <= 0)
                {
                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                        player.worldObj.updateEntityWithOptionalForce(player, false);
                        player.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                        player.timeUntilPortal = 150;

                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < getTeleportCost())
                        {
                            return;
                        }
                        network.syphon(getTeleportCost());

                        player.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    } else
                    {
                        WorldServer world = (WorldServer) entity.worldObj;

                        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
                        entity.timeUntilPortal = 150;
                        world.resetUpdateEntityTick();

                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                        {
                            return;
                        }
                        network.syphon(getTeleportCost() / 10);

                        entity.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    }
                }
            }
        }

        @Override
        public int getTeleportCost()
        {
            return 1000;
        }
    }

    public static class TeleportToDim extends Teleport
    {
        @Getter
        private World oldWorld;
        @Getter
        private int newWorldID;

        public TeleportToDim(int x, int y, int z, Entity entity, String networkToDrain, World oldWorld, int newWorld)
        {
            super(x, y, z, entity, networkToDrain);
            this.oldWorld = oldWorld;
            this.newWorldID = newWorld;
        }

        public TeleportToDim(BlockPos blockPos, Entity entity, String networkToDrain, World oldWorld, int newWorldID)
        {
            super(blockPos, entity, networkToDrain);
            this.oldWorld = oldWorld;
            this.newWorldID = newWorldID;
        }

        @Override
        public void teleport()
        {
            if (entity != null)
            {
                if (entity.timeUntilPortal <= 0)
                {
                    MinecraftServer server = MinecraftServer.getServer();
                    WorldServer oldWorldServer = server.worldServerForDimension(entity.dimension);
                    WorldServer newWorldServer = server.worldServerForDimension(newWorldID);

                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        if (!player.worldObj.isRemote)
                        {
                            server.getConfigurationManager().transferPlayerToDimension(player, newWorldID, new TeleporterBloodMagic(newWorldServer));
                            player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                            player.worldObj.updateEntityWithOptionalForce(player, false);
                            player.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                        }

                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < getTeleportCost())
                        {
                            return;
                        }
                        network.syphon(getTeleportCost());
                    } else if (!entity.worldObj.isRemote)
                    {
                        NBTTagCompound tag = new NBTTagCompound();

                        entity.writeToNBTOptional(tag);
                        entity.setDead();
                        oldWorld.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);

                        Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorldServer);
                        if (teleportedEntity != null)
                        {
                            teleportedEntity.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, entity.rotationYaw, entity.rotationPitch);
                            teleportedEntity.forceSpawn = true;
                            newWorldServer.spawnEntityInWorld(teleportedEntity);
                            teleportedEntity.setWorld(newWorldServer);
                            teleportedEntity.timeUntilPortal = 150;
                        }

                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();

                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                        {
                            return;
                        }
                        network.syphon(getTeleportCost() / 10);
                    }
                    entity.timeUntilPortal = 150;

                    newWorldServer.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                }
            }
        }

        @Override
        public int getTeleportCost()
        {
            return 10000;
        }
    }
}
