package WayofTime.bloodmagic.ritual.portal;

import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.teleport.Teleport;
import WayofTime.bloodmagic.api.teleport.TeleporterBloodMagic;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < getTeleportCost())
                        {
                            return;
                        }
                        network.syphon(getTeleportCost());

                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                        player.worldObj.updateEntityWithOptionalForce(player, false);
                        player.playerNetServerHandler.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                        player.timeUntilPortal = 150;

                        player.worldObj.playSound(x, y, z, SoundEvents.entity_endermen_teleport, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                    } else
                    {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                        {
                            return;
                        }
                        network.syphon(getTeleportCost() / 10);

                        WorldServer world = (WorldServer) entity.worldObj;

                        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
                        entity.timeUntilPortal = 150;
                        world.resetUpdateEntityTick();

                        entity.worldObj.playSound(x, y, z, SoundEvents.entity_endermen_teleport, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
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
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    WorldServer oldWorldServer = server.worldServerForDimension(entity.dimension);
                    WorldServer newWorldServer = server.worldServerForDimension(newWorldID);

                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayerMP player = (EntityPlayerMP) entity;

                        if (!player.worldObj.isRemote)
                        {
                            SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                            if (network.getCurrentEssence() < getTeleportCost())
                            {
                                return;
                            }
                            network.syphon(getTeleportCost());

                            player.changeDimension(newWorldID); //TODO: UNTESTED
//                            server.getConfigurationManager().transferPlayerToDimension(player, newWorldID, new TeleporterBloodMagic(newWorldServer));
                            player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                            player.worldObj.updateEntityWithOptionalForce(player, false);
                            player.playerNetServerHandler.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                        }

                    } else if (!entity.worldObj.isRemote)
                    {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(networkToDrain);
                        if (network.getCurrentEssence() < (getTeleportCost() / 10))
                        {
                            return;
                        }
                        network.syphon(getTeleportCost() / 10);

                        NBTTagCompound tag = new NBTTagCompound();

                        entity.writeToNBTOptional(tag);
                        entity.setDead();
                        oldWorld.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.entity_endermen_teleport, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

                        Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorldServer);
                        if (teleportedEntity != null)
                        {
                            teleportedEntity.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, entity.rotationYaw, entity.rotationPitch);
                            teleportedEntity.forceSpawn = true;
                            newWorldServer.spawnEntityInWorld(teleportedEntity);
                            teleportedEntity.setWorld(newWorldServer);
                            teleportedEntity.timeUntilPortal = teleportedEntity instanceof EntityPlayer ? 150 : 20;
                        }

                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();
                    }
                    entity.timeUntilPortal = entity instanceof EntityLiving ? 150 : 20;
                    newWorldServer.playSound(x, y, z, SoundEvents.entity_endermen_teleport, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
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
