package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EnergyItems extends Item implements IBindable
{
    private int energyUsed;

    public EnergyItems(int id)
    {
        super(id);
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
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

    /**
     * Master method to syphon from the Soul Network.
     */
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
        SoulNetworkHandler.syphonFromNetwork(ist, damageToBeDone);

        return true;
    }

    public static boolean canSyphonInContainer(ItemStack ist, int damageToBeDone)
    {
        return SoulNetworkHandler.canSyphonFromOnlyNetwork(ist, damageToBeDone);
    }

    public static boolean syphonAndDamageWhileInContainer(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
        return SoulNetworkHandler.syphonAndDamageFromNetwork(ist, player, damageToBeDone);
    }

    public static void checkAndSetItemOwner(ItemStack item, EntityPlayer player)
    {
    	SoulNetworkHandler.checkAndSetItemOwner(item, player);
    }

    public static void checkAndSetItemOwner(ItemStack item, String ownerName)
    {
        SoulNetworkHandler.checkAndSetItemOwner(item, ownerName);
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
