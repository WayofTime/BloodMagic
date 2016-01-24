package WayofTime.bloodmagic.tile;

import java.util.Iterator;
import java.util.List;

import WayofTime.bloodmagic.api.BlockStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.ItemBindable;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;

import com.google.common.base.Strings;

public class TileTeleposer extends TileInventory implements ITickable
{
    //TODO FUTURE: Make AreaDescriptor for Teleposer perhaps?
    public static final String TELEPOSER_RANGE = "teleposerRange";

    private int previousInput;

    public TileTeleposer()
    {
        super(1, "teleposer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        previousInput = tagCompound.getInteger(Constants.NBT.PREVIOUS_INPUT);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.PREVIOUS_INPUT, previousInput);
    }

    @Override
    public void update()
    {
        if (!worldObj.isRemote)
        {
            int currentInput = worldObj.getStrongPower(pos);

            if (previousInput == 0 && currentInput != 0)
            {
                initiateTeleport();
            }

            previousInput = currentInput;
        }
    }

    public void initiateTeleport()
    {
        if (!worldObj.isRemote && worldObj.getTileEntity(pos) != null && worldObj.getTileEntity(pos) instanceof TileTeleposer && canInitiateTeleport((TileTeleposer) worldObj.getTileEntity(pos)) && worldObj.getBlockState(pos).getBlock() instanceof BlockTeleposer)
        {
            TileTeleposer teleposer = (TileTeleposer) worldObj.getTileEntity(pos);
            ItemTelepositionFocus focus = (ItemTelepositionFocus) teleposer.getStackInSlot(0).getItem();
            BlockPos focusPos = focus.getBlockPos(teleposer.getStackInSlot(0));
            World focusWorld = focus.getWorld(teleposer.getStackInSlot(0));

            if (focusWorld != null && focusWorld.getTileEntity(focusPos) instanceof TileTeleposer && !focusWorld.getTileEntity(focusPos).equals(this))
            {
                final int focusLevel = (teleposer.getStackInSlot(0).getItemDamage() + 1);
                final int lpToBeDrained = (int) (0.5F * Math.sqrt((pos.getX() - focusPos.getX()) * (pos.getX() - focusPos.getX()) + (pos.getY() - focusPos.getY() + 1) * (pos.getY() - focusPos.getY() + 1) + (pos.getZ() - focusPos.getZ()) * (pos.getZ() - focusPos.getZ())));
                int entityCount = 0;

                //TODO MAKE THIS SYPHON LP BETTER
                if (ItemBindable.syphonNetwork(teleposer.getStackInSlot(0), lpToBeDrained * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) + lpToBeDrained * entityCount))
                {
                    List<EntityLivingBase> entityList1 = null;
                    List<EntityLivingBase> entityList2 = null;

                    if (focusWorld.equals(worldObj))
                    {
                        {
                            AxisAlignedBB area51 = AxisAlignedBB.fromBounds(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, Math.min(focusWorld.getHeight(), pos.getY() + 2 * focusLevel), pos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                            entityList1 = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, area51);
                            for (EntityLivingBase entity : entityList1)
                                entityCount++;
                        }

                        {
                            AxisAlignedBB area52 = AxisAlignedBB.fromBounds(focusPos.getX(), focusPos.getY() + 1, focusPos.getZ(), focusPos.getX() + 1, Math.min(focusWorld.getHeight(), focusPos.getY() + 2 * focusLevel), focusPos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                            entityList2 = focusWorld.getEntitiesWithinAABB(EntityLivingBase.class, area52);
                            for (EntityLivingBase entity : entityList2)
                                entityCount++;
                        }
                    }

                    int blocksTransported = 0;

                    for (int i = -(focusLevel - 1); i <= (focusLevel - 1); i++)
                    {
                        for (int j = 0; j <= (focusLevel * 2 - 2); j++)
                        {
                            for (int k = -(focusLevel - 1); k <= (focusLevel - 1); k++)
                            {
                                if (teleportBlocks(this, worldObj, pos.add(i, 1 + j, k), focusWorld, focusPos.add(i, 1 + j, k)))
                                {
                                    blocksTransported++;
                                }
                            }
                        }
                    }

                    NetworkHelper.syphonFromContainer(teleposer.getStackInSlot(0), lpToBeDrained * blocksTransported + lpToBeDrained * entityCount);

                    if (focusWorld.equals(worldObj))
                    {
                        if (entityList1 != null && !entityList1.isEmpty())
                        {
                            for (EntityLivingBase entity : entityList1)
                            {
                                entity.worldObj = focusWorld;
                                entity.setPositionAndUpdate(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ());
                            }
                        }

                        if (entityList2 != null && !entityList2.isEmpty())
                        {
                            for (EntityLivingBase entity : entityList2)
                            {
                                entity.worldObj = focusWorld;
                                entity.setPositionAndUpdate(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ());
                            }
                        }
                    } else
                    {
                        if (entityList1 != null && !entityList1.isEmpty())
                        {
                            for (EntityLivingBase entity : entityList1)
                            {
                                entity.worldObj = focusWorld;
                                teleportEntityToDim(worldObj, focusWorld.provider.getDimensionId(), entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ(), entity);
                            }
                        }

                        if (entityList2 != null && !entityList2.isEmpty())
                        {
                            for (EntityLivingBase entity : entityList2)
                            {
                                entity.worldObj = focusWorld;
                                teleportEntityToDim(focusWorld, worldObj.provider.getDimensionId(), entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ(), entity);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canInitiateTeleport(TileTeleposer teleposer)
    {
        return teleposer.getStackInSlot(0) != null && teleposer.getStackInSlot(0).getItem() instanceof ItemTelepositionFocus && !Strings.isNullOrEmpty(((ItemTelepositionFocus) teleposer.getStackInSlot(0).getItem()).getBindableOwner(teleposer.getStackInSlot(0)));
    }

    public static boolean teleportBlocks(Object caller, World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos)
    {
        TileEntity initialTile = initialWorld.getTileEntity(initialPos);
        TileEntity finalTile = finalWorld.getTileEntity(finalPos);
        NBTTagCompound initialTag = new NBTTagCompound();
        NBTTagCompound finalTag = new NBTTagCompound();
        if (initialTile != null)
            initialTile.writeToNBT(initialTag);
        if (finalTile != null)
            finalTile.writeToNBT(finalTag);

        BlockStack initialStack = BlockStack.getStackFromPos(initialWorld, initialPos);
        BlockStack finalStack = BlockStack.getStackFromPos(finalWorld, finalPos);

        if ((initialStack.getBlock().equals(Blocks.air) && finalStack.getBlock().equals(Blocks.air)) || (initialStack.getBlock() instanceof BlockMobSpawner || finalStack.getBlock() instanceof BlockMobSpawner ||
//                caller instanceof TEDemonPortal ? false :
                initialStack.getBlock() instanceof BlockPortal || finalStack.getBlock() instanceof BlockPortal))
        {
            return false;
        }

        TeleposeEvent event = new TeleposeEvent(initialWorld, initialPos, finalWorld, finalPos);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;

        initialWorld.playSoundEffect(initialPos.getX(), initialPos.getY(), initialPos.getZ(), "mob.endermen.portal", 1.0F, 1.0F);
        finalWorld.playSoundEffect(finalPos.getX(), finalPos.getY(), finalPos.getZ(), "mob.endermen.portal", 1.0F, 1.0F);

        //Finally, we get to do something! (CLEARING TILES)
        if (finalStack.getBlock() != null)
            finalWorld.removeTileEntity(finalPos);
        if (initialStack.getBlock() != null)
            initialWorld.removeTileEntity(initialPos);

        //TILES CLEARED
        IBlockState initialBlockState = initialWorld.getBlockState(initialPos);
        IBlockState finalBlockState = finalWorld.getBlockState(finalPos);
        finalWorld.setBlockState(finalPos, initialBlockState, 3);

        if (initialTile != null)
        {
            TileEntity newTileInitial = TileEntity.createAndLoadEntity(initialTag);

            //TODO FUTURE FMP STUFF HERE

            finalWorld.setTileEntity(finalPos, newTileInitial);
            newTileInitial.setPos(finalPos);
        }

        initialWorld.setBlockState(initialPos, finalBlockState, 3);

        if (finalTile != null)
        {
            TileEntity newTileFinal = TileEntity.createAndLoadEntity(finalTag);

            //TODO FUTURE FMP STUFF HERE

            initialWorld.setTileEntity(initialPos, newTileFinal);
            newTileFinal.setPos(initialPos);
        }

        initialWorld.notifyNeighborsOfStateChange(initialPos, finalStack.getBlock());
        finalWorld.notifyNeighborsOfStateChange(finalPos, initialStack.getBlock());

        return true;
    }

    public static Entity teleportEntityToDim(World prevWorld, int newWorldID, double d, double e, double f, Entity entity)
    {
        if (entity != null)
        {
            if (entity.timeUntilPortal <= 0)
            {
                WorldServer oldWorldServer = MinecraftServer.getServer().worldServerForDimension(entity.dimension);
                WorldServer newWorldServer = MinecraftServer.getServer().worldServerForDimension(newWorldID);
                if (entity instanceof EntityPlayer)
                {
                    EntityPlayerMP player = (EntityPlayerMP) entity;
                    if (!player.worldObj.isRemote)
                    {
                        player.worldObj.theProfiler.startSection("portal");
                        player.worldObj.theProfiler.startSection("changeDimension");
                        ServerConfigurationManager config = player.mcServer.getConfigurationManager();
                        prevWorld.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                        player.closeScreen();
                        player.dimension = newWorldServer.provider.getDimensionId();
                        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), newWorldServer.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
                        oldWorldServer.removeEntity(player);
                        player.isDead = false;
                        player.setLocationAndAngles(d, e, f, player.rotationYaw, player.rotationPitch);
                        newWorldServer.spawnEntityInWorld(player);
                        player.setWorld(newWorldServer);
                        config.preparePlayer(player, oldWorldServer);
                        player.playerNetServerHandler.setPlayerLocation(d, e, f, entity.rotationYaw, entity.rotationPitch);
                        player.theItemInWorldManager.setWorld(newWorldServer);
                        config.updateTimeAndWeatherForPlayer(player, newWorldServer);
                        config.syncPlayerInventory(player);
                        player.worldObj.theProfiler.endSection();
                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();
                        player.worldObj.theProfiler.endSection();
                        for (Iterator<PotionEffect> potion = player.getActivePotionEffects().iterator(); potion.hasNext();)
                        {
                            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potion.next()));
                        }
                        player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
                        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldWorldServer.provider.getDimensionId(), player.dimension);
                        player.timeUntilPortal = 150;
                    }
                    player.worldObj.theProfiler.endSection();
                    newWorldServer.playSoundEffect(d, e, f, "mob.endermen.portal", 1.0F, 1.0F);
                    return player;
                } else
                {
                    NBTTagCompound tag = new NBTTagCompound();
                    entity.writeToNBTOptional(tag);
                    entity.setDead();
                    prevWorld.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                    Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorldServer);
                    if (teleportedEntity != null)
                    {
                        teleportedEntity.setLocationAndAngles(d, e, f, entity.rotationYaw, entity.rotationPitch);
                        teleportedEntity.forceSpawn = true;
                        newWorldServer.spawnEntityInWorld(teleportedEntity);
                        teleportedEntity.setWorld(newWorldServer);
                        teleportedEntity.timeUntilPortal = 150;
                    }
                    oldWorldServer.resetUpdateEntityTick();
                    newWorldServer.resetUpdateEntityTick();
                    newWorldServer.playSoundEffect(d, e, f, "mob.endermen.portal", 1.0F, 1.0F);
                    return teleportedEntity;
                }
            }
        }
        return null;
    }
}
