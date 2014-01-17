package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.IBindable;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EnergyItems extends Item implements IBindable
{
    private int energyUsed;

    public EnergyItems(int id)
    {
        super(id);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
    }
    //Heals the player using the item. If the player is at full health, or if the durability cannot be used any more,
    //the item is not used.

    protected void damagePlayer(World world, EntityPlayer player, int damage)
    {
        if (world != null)
        {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            float f = (float) 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            for (int l = 0; l < 8; ++l)
            {
                world.spawnParticle("reddust", posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
            }
        }

        for (int i = 0; i < damage; i++)
        {
            //player.setEntityHealth((player.getHealth()-1));
            player.setHealth((player.getHealth() - 1));

            if (player.getHealth() <= 0)
            {
                player.inventory.dropAllItems();
            }
        }
    }

    public static boolean syphonBatteriesWithoutParticles(ItemStack ist, EntityPlayer player, int damageToBeDone, boolean particles)
    {
        if (!player.capabilities.isCreativeMode)
        {
            NBTTagCompound itemTag = ist.stackTagCompound;

            if (itemTag == null || itemTag.getString("ownerName").equals(""))
            {
                return false;
            }

            World world = player.worldObj;

            if (world != null)
            {
                double posX = player.posX;
                double posY = player.posY;
                double posZ = player.posZ;

                if (particles)
                {
                    PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 20, world.provider.dimensionId, TEAltar.getParticlePacket(posX, posY, posZ, (short) 4));
                    world.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                }
            }

            if (!player.worldObj.isRemote)
            {
                return false;
            }

            PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(itemTag.getString("ownerName"), -damageToBeDone, 0));
            return true;
        } else
        {
            return true;
        }
    }

    public static boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
        if (!player.worldObj.isRemote)
        {
            return syphonAndDamageWhileInContainer(ist, player, damageToBeDone);
        } else
        {
            World world = player.worldObj;

            if (world != null)
            {
                double posX = player.posX;
                double posY = player.posY;
                double posZ = player.posZ;
                //if(particles)
                {
                    PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 20, world.provider.dimensionId, TEAltar.getParticlePacket(posX, posY, posZ, (short) 4));
                    world.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                }
            }
        }

        return true;
        //return syphonBatteriesWithoutParticles(ist, player, damageToBeDone, true);
    }

    public static boolean syphonWhileInContainer(ItemStack ist, int damageToBeDone)
    {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = ist.getTagCompound().getString("ownerName");

            if (MinecraftServer.getServer() == null)
            {
                return false;
            }

            World world = MinecraftServer.getServer().worldServers[0];
            LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

            if (data == null)
            {
                data = new LifeEssenceNetwork(ownerName);
                world.setItemData(ownerName, data);
            }

            if (data.currentEssence >= damageToBeDone)
            {
                data.currentEssence -= damageToBeDone;
                data.markDirty();
                return true;
            }

//        	EntityPlayer ownerEntity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(ist.getTagCompound().getString("ownerName"));
//            if(ownerEntity==null){return false;}
//            NBTTagCompound tag = ownerEntity.getEntityData();
//            int currentEssence = tag.getInteger("currentEssence");
//            if(currentEssence>=damageToBeDone)
//            {
//            	tag.setInteger("currentEssence", currentEssence-damageToBeDone);
//            	return true;
//            }
        }

        return false;
    }

    public static boolean canSyphonInContainer(ItemStack ist, int damageToBeDone)
    {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = ist.getTagCompound().getString("ownerName");

            if (MinecraftServer.getServer() == null)
            {
                return false;
            }

            World world = MinecraftServer.getServer().worldServers[0];
            LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

            if (data == null)
            {
                data = new LifeEssenceNetwork(ownerName);
                world.setItemData(ownerName, data);
            }

            return data.currentEssence >= damageToBeDone;
//        	EntityPlayer ownerEntity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(ist.getTagCompound().getString("ownerName"));
//            if(ownerEntity==null){return false;}
//            NBTTagCompound tag = ownerEntity.getEntityData();
//            int currentEssence = tag.getInteger("currentEssence");
//            if(currentEssence>=damageToBeDone)
//            {
//            	tag.setInteger("currentEssence", currentEssence-damageToBeDone);
//            	return true;
//            }
        }

        return false;
    }

    public static void hurtPlayer(EntityPlayer user, int energySyphoned)
    {
        if (energySyphoned < 100 && energySyphoned > 0)
        {
            if (!user.capabilities.isCreativeMode)
            {
                //player.setEntityHealth((player.getHealth()-1));
                user.setHealth((user.getHealth() - 1));

                if (user.getHealth() <= 0.1f)
                {
                    user.onDeath(DamageSource.generic);
                }
            }
        } else if (energySyphoned >= 100)
        {
            if (!user.capabilities.isCreativeMode)
            {
                for (int i = 0; i < ((energySyphoned + 99) / 100); i++)
                {
                    //player.setEntityHealth((player.getHealth()-1));
                    user.setHealth((user.getHealth() - 1));

                    if (user.getHealth() <= 0.1f)
                    {
                        user.onDeath(DamageSource.generic);
                    }
                }
            }
        }
    }

    public static boolean syphonAndDamageWhileInContainer(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
        if (!syphonWhileInContainer(ist, damageToBeDone))
        {
            hurtPlayer(player, damageToBeDone);
        }

        return true;
    }

    //Global static methods
    public static void checkAndSetItemOwner(ItemStack item, EntityPlayer player)
    {
        if (item.stackTagCompound == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        if (item.stackTagCompound.getString("ownerName").equals(""))
        {
            item.stackTagCompound.setString("ownerName", player.getEntityName());
        }

        initializePlayer(player);
    }

    public static void checkAndSetItemOwner(ItemStack item, String ownerName)
    {
        if (item.stackTagCompound == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        if (item.stackTagCompound.getString("ownerName").equals(""))
        {
            item.stackTagCompound.setString("ownerName", ownerName);
        }
    }

    public static void initializePlayer(EntityPlayer player)
    {
        NBTTagCompound tag = player.getEntityData();

        if (tag.getInteger("currentEssence") == 0)
        {
            tag.setInteger("currentEssence", 0);
        }
    }

    public String getOwnerName(ItemStack item)
    {
        if (item.stackTagCompound == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        return item.stackTagCompound.getString("ownerName");
    }
}
