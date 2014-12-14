package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IAlchemyGoggles;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.items.ILPGauge;
import WayofTime.alchemicalWizardry.common.items.sigil.DivinationSigil;
import cpw.mods.fml.common.FMLCommonHandler;

public class SpellHelper
{
    public static Random rand = new Random();
    public static final double root2 = Math.sqrt(2);

    public static boolean canEntityBeSeen(Entity entity, Entity entity2)
    {
        return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), Vec3.createVectorHelper(entity2.posX, entity2.posY, entity2.posZ), false) == null;
    }

    public static void smeltBlockInWorld(World world, int posX, int posY, int posZ)
    {
        FurnaceRecipes recipes = FurnaceRecipes.smelting();

        Block block = world.getBlock(posX, posY, posZ);
        if (block == null)
        {
            return;
        }

        int meta = world.getBlockMetadata(posX, posY, posZ);

        ItemStack smeltedStack = recipes.getSmeltingResult(new ItemStack(block, 1, meta));
        if (smeltedStack != null && smeltedStack.getItem() instanceof ItemBlock)
        {
            world.setBlock(posX, posY, posZ, ((ItemBlock) (smeltedStack.getItem())).field_150939_a, smeltedStack.getItemDamage(), 3);
        }
    }

    public static boolean canPlayerSeeAlchemy(EntityPlayer player)
    {
        if (player != null)
        {
            ItemStack stack = player.getCurrentArmor(3);
            if (stack != null)
            {
                Item item = stack.getItem();
                if (item instanceof IAlchemyGoggles && ((IAlchemyGoggles) item).showIngameHUD(player.worldObj, stack, player))
                {
                    return true;
                }
            }

            ItemStack heldStack = player.getHeldItem();
            if (heldStack != null && heldStack.getItem() instanceof IReagentManipulator)
            {
                return true;
            }
        }

        return false;
    }
    
    public static boolean canPlayerSeeLPBar(EntityPlayer player)
    {
    	if (player != null)
        {
    		for(int i=0; i<4; i++)
    		{
    			ItemStack stack = player.getCurrentArmor(i);
                if (stack != null)
                {
                    Item item = stack.getItem();
                    if (item instanceof ILPGauge && ((ILPGauge) item).canSeeLPBar(stack))
                    {
                        return true;
                    }
                }

    		}
            
            ItemStack heldStack = player.getHeldItem();
    		
            if (heldStack != null && heldStack.getItem() instanceof DivinationSigil)
            {
                return true;
            }
        }

        return false;
    }

    public static List<Entity> getEntitiesInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
    {
        return world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
    }

    public static List<EntityLivingBase> getLivingEntitiesInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
    {
        return world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
    }

    public static List<EntityItem> getItemsInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
    {
        return world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
    }

    public static List<EntityPlayer> getPlayersInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
    {
        return world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
    }

    public static double gaussian(double d)
    {
        return d * ((rand.nextFloat() - 0.5D));
    }

    public static Vec3 getEntityBlockVector(Entity entity)
    {
        int posX = (int) Math.round(entity.posX - 0.5f);
        int posY = (int) entity.posY;
        int posZ = (int) Math.round(entity.posZ - 0.5f);

        return entity.getLookVec().createVectorHelper(posX, posY, posZ);
    }

    public static ForgeDirection getDirectionForLookVector(Vec3 lookVec)
    {
        double distance = lookVec.lengthVector();

        if (lookVec.yCoord > distance * 0.9)
        {
            return ForgeDirection.UP;
        }
        if (lookVec.yCoord < distance * -0.9)
        {
            return ForgeDirection.DOWN;
        }

        return getCompassDirectionForLookVector(lookVec);
    }

    public static ForgeDirection getCompassDirectionForLookVector(Vec3 lookVec)
    {
        double radius = Math.sqrt(Math.pow(lookVec.xCoord, 2) + Math.pow(lookVec.zCoord, 2));

        if (lookVec.zCoord > radius * 1 / root2)
        {
            return ForgeDirection.SOUTH;
        }
        if (lookVec.zCoord < -radius * 1 / root2)
        {
            return ForgeDirection.NORTH;
        }
        if (lookVec.xCoord > radius * 1 / root2)
        {
            return ForgeDirection.EAST;
        }
        if (lookVec.xCoord < -radius * 1 / root2)
        {
            return ForgeDirection.WEST;
        }

        return ForgeDirection.EAST;
    }

    public static boolean freezeWaterBlock(World world, int posX, int posY, int posZ)
    {
        Block block = world.getBlock(posX, posY, posZ);

        if (block == Blocks.water || block == Blocks.flowing_water)
        {
            world.setBlock(posX, posY, posZ, Blocks.ice);
            return true;
        }

        return false;
    }

    public static String getUsername(EntityPlayer player)
    {
        return SoulNetworkHandler.getUsername(player);
    }

    public static EntityPlayer getPlayerForUsername(String str)
    {
        return SoulNetworkHandler.getPlayerForUsername(str);
    }

    public static void sendParticleToPlayer(EntityPlayer player, String str, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
    {
        if (player instanceof EntityPlayerMP)
        {
            NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getParticlePacket(str, xCoord, yCoord, zCoord, xVel, yVel, zVel), (EntityPlayerMP) player);
        }
    }

    public static void sendIndexedParticleToPlayer(EntityPlayer player, int index, double xCoord, double yCoord, double zCoord)
    {
        switch (index)
        {
            case 1:
                SpellHelper.sendParticleToPlayer(player, "mobSpell", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 0.5117D, 0.0117D, 0.0117D);
                break;
            case 2:
                SpellHelper.sendParticleToPlayer(player, "reddust", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 0.82D, 0.941D, 0.91D);
                break;
            case 3:
                SpellHelper.sendParticleToPlayer(player, "mobSpell", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 1.0D, 0.371D, 0.371D);
                break;
            case 4:
                float f = (float) 1.0F;
                float f1 = f * 0.6F + 0.4F;
                float f2 = f * f * 0.7F - 0.5F;
                float f3 = f * f * 0.6F - 0.7F;

                for (int l = 0; l < 8; ++l)
                {
                    SpellHelper.sendParticleToPlayer(player, "reddust", xCoord + Math.random() - Math.random(), yCoord + Math.random() - Math.random(), zCoord + Math.random() - Math.random(), f1, f2, f3);
                }
                break;
        }
    }

    public static void sendParticleToAllAround(World world, double xPos, double yPos, double zPos, int radius, int dimension, String str, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
    {
        List<EntityPlayer> entities = SpellHelper.getPlayersInRange(world, xPos, yPos, zPos, radius, radius);

        if (entities == null)
        {
            return;
        }

        for (EntityPlayer player : entities)
        {
            SpellHelper.sendParticleToPlayer(player, str, xCoord, yCoord, zCoord, xVel, yVel, zVel);
        }
    }

    public static void sendIndexedParticleToAllAround(World world, double xPos, double yPos, double zPos, int radius, int dimension, int index, double xCoord, double yCoord, double zCoord)
    {
        List<EntityPlayer> entities = SpellHelper.getPlayersInRange(world, xPos, yPos, zPos, radius, radius);

        if (entities == null)
        {
            return;
        }

        for (EntityPlayer player : entities)
        {
            SpellHelper.sendIndexedParticleToPlayer(player, index, xCoord, yCoord, zCoord);
        }
    }

    public static void setPlayerSpeedFromServer(EntityPlayer player, double motionX, double motionY, double motionZ)
    {
        if (player instanceof EntityPlayerMP)
        {
            NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getVelSettingPacket(motionX, motionY, motionZ), (EntityPlayerMP) player);
        }
    }

    public static boolean isFakePlayer(World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return false;
        }

        if (player instanceof FakePlayer || SpellHelper.getUsername(player).contains("[CoFH]") || SpellHelper.getUsername(player).contains("[ThaumcraftTablet]"))
        {
            return true;
        }

        String str = player.getClass().getSimpleName();
        if (str.contains("GC"))
        {
            return false;
        }

        if (player.getClass().equals(EntityPlayerMP.class))
        {
            return false;
        }

        return false;
    }

    public static void smashBlock(World world, int posX, int posY, int posZ)
    {
        Block block = world.getBlock(posX, posY, posZ);

        if (block == Blocks.stone)
        {
            world.setBlock(posX, posY, posZ, Blocks.cobblestone);
            return;
        } else if (block == Blocks.cobblestone)
        {
            world.setBlock(posX, posY, posZ, Blocks.gravel);
            return;
        } else if (block == Blocks.gravel)
        {
            world.setBlock(posX, posY, posZ, Blocks.sand);
            return;
        }
    }

    public static boolean isBlockFluid(Block block)
    {
        return block instanceof BlockLiquid;
    }

    public static void evaporateWaterBlock(World world, int posX, int posY, int posZ)
    {
        Block block = world.getBlock(posX, posY, posZ);

        if (block == Blocks.water || block == Blocks.flowing_water)
        {
            world.setBlockToAir(posX, posY, posZ);
        }
    }

    public static ItemStack getDustForOre(ItemStack item)
    {
        String oreName = OreDictionary.getOreName(OreDictionary.getOreID(item));

        if (oreName.contains("ore"))
        {
            String lowercaseOre = oreName.toLowerCase();
            boolean isAllowed = false;

            for (String str : AlchemicalWizardry.allowedCrushedOresArray)
            {
                String testStr = str.toLowerCase();

                if (lowercaseOre.contains(testStr))
                {
                    isAllowed = true;
                    break;
                }
            }

            if (!isAllowed)
            {
                return null;
            }

            String dustName = oreName.replace("ore", "dust");

            ArrayList<ItemStack> items = OreDictionary.getOres(dustName);

            if (items != null && items.size() >= 1)
            {
                return (items.get(0).copy());
            }
        }

        return null;
    }

    public static List<ItemStack> getItemsFromBlock(World world, Block block, int x, int y, int z, int meta, boolean silkTouch, int fortune)
    {
        return APISpellHelper.getItemsFromBlock(world, block, x, y, z, meta, silkTouch, fortune);
    }

    public static void spawnItemListInWorld(List<ItemStack> items, World world, float x, float y, float z)
    {
        APISpellHelper.spawnItemListInWorld(items, world, x, y, z);
    }

    public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range)
    {
        return APISpellHelper.raytraceFromEntity(world, player, par3, range);
    }

    public static String getNumeralForInt(int num)
    {
        switch (num)
        {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "";
        }
    }

    /**
     * Used to determine if stack1 can be placed into stack2. If stack2 is null and stack1 isn't null, returns true. Ignores stack size
     *
     * @param stack1 Stack that is placed into a slot
     * @param stack2 Slot content that stack1 is placed into
     * @return True if they can be combined
     */
    public static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        if (stack1 == null)
        {
            return false;
        }

        if (stack2 == null)
        {
            return true;
        }

        if (stack1.isItemStackDamageable() ^ stack2.isItemStackDamageable())
        {
            return false;
        }

        boolean tagsEqual = ItemStack.areItemStackTagsEqual(stack1, stack2);

        return stack1.getItem() == stack2.getItem() && tagsEqual && stack1.getItemDamage() == stack2.getItemDamage() && Math.min(stack2.getMaxStackSize() - stack2.stackSize, stack1.stackSize) > 0;
    }

    /**
     * @param stack1 Stack that is placed into a slot
     * @param stack2 Slot content that stack1 is placed into
     * @return Stacks after stacking
     */
    public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2)
    {
        ItemStack[] returned = new ItemStack[2];

        if (canCombine(stack1, stack2))
        {
            int transferedAmount = stack2 == null ? stack1.stackSize : Math.min(stack2.getMaxStackSize() - stack2.stackSize, stack1.stackSize);
            if (transferedAmount > 0)
            {
                ItemStack copyStack = stack1.splitStack(transferedAmount);
                if (stack2 == null)
                {
                    stack2 = copyStack;
                } else
                {
                    stack2.stackSize += transferedAmount;
                }
            }
        }

        returned[0] = stack1;
        returned[1] = stack2;

        return returned;
    }

    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory)
    {
        if (stack == null)
        {
            return stack;
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i));
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]);

            if (stack.stackSize <= 0)
            {
                return stack;
            }
        }

        return stack;
    }

    public static boolean hydrateSoil(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block == Blocks.dirt || block == Blocks.grass || (block == Blocks.farmland && world.getBlockMetadata(x, y, z) == 0))
        {
            world.setBlock(x, y, z, Blocks.farmland, 15, 2);

            return true;
        }

        return false;
    }

    public static Entity teleportEntitySameDim(double x, double y, double z, Entity entity)
    {
        if (entity != null)
        {
            if (entity.timeUntilPortal <= 0)
            {
                if (entity instanceof EntityPlayer)
                {
                    EntityPlayerMP player = (EntityPlayerMP) entity;
                    player.setPositionAndUpdate(x, y, z);
                    player.worldObj.updateEntityWithOptionalForce(player, false);
                    player.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                    player.timeUntilPortal = 150;
                    player.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    return player;
                } else
                {
                    WorldServer world = (WorldServer) entity.worldObj;
                    if (entity != null)
                    {
                        entity.setPosition(x, y, z);
                        entity.timeUntilPortal = 150;
                    }
                    world.resetUpdateEntityTick();
                    entity.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    return entity;
                }
            }
        }
        return null;
    }

    //Adapated from Enhanced Portals 3 code
    public static Entity teleportEntityToDim(World oldWorld, int newWorldID, int x, int y, int z, Entity entity)
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
                        oldWorld.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                        player.closeScreen();
                        player.dimension = newWorldServer.provider.dimensionId;
                        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, newWorldServer.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
                        oldWorldServer.removeEntity(player);
                        player.isDead = false;
                        player.setLocationAndAngles(x, y, z, player.rotationYaw, player.rotationPitch);
                        newWorldServer.spawnEntityInWorld(player);
                        player.setWorld(newWorldServer);
                        config.func_72375_a(player, oldWorldServer);
                        player.playerNetServerHandler.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
                        player.theItemInWorldManager.setWorld(newWorldServer);
                        config.updateTimeAndWeatherForPlayer(player, newWorldServer);
                        config.syncPlayerInventory(player);
                        player.worldObj.theProfiler.endSection();
                        oldWorldServer.resetUpdateEntityTick();
                        newWorldServer.resetUpdateEntityTick();
                        player.worldObj.theProfiler.endSection();
                        for (Iterator<PotionEffect> potion = player.getActivePotionEffects().iterator(); potion.hasNext(); )
                        {
                            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potion.next()));
                        }
                        player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
                        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldWorldServer.provider.dimensionId, player.dimension);
                        player.timeUntilPortal = 150;
                    }
                    player.worldObj.theProfiler.endSection();
                    newWorldServer.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    return player;
                } else
                {
                    NBTTagCompound tag = new NBTTagCompound();
                    entity.writeToNBTOptional(tag);
                    entity.setDead();
                    oldWorld.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                    Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorldServer);
                    if (teleportedEntity != null)
                    {
                        teleportedEntity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
                        teleportedEntity.forceSpawn = true;
                        newWorldServer.spawnEntityInWorld(teleportedEntity);
                        teleportedEntity.setWorld(newWorldServer);
                        teleportedEntity.timeUntilPortal = 150;
                    }
                    oldWorldServer.resetUpdateEntityTick();
                    newWorldServer.resetUpdateEntityTick();
                    newWorldServer.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
                    return teleportedEntity;
                }
            }
        }
        return null;
    }
    
    public static boolean areItemStacksEqual(ItemStack stack, ItemStack compressedStack)
    {
    	return stack.isItemEqual(compressedStack) && (stack.getTagCompound() == null ? compressedStack.getTagCompound() == null : stack.getTagCompound().equals(compressedStack.getTagCompound()));
    }
}
