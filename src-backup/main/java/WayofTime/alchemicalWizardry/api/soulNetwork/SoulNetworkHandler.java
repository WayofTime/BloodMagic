package WayofTime.alchemicalWizardry.api.soulNetwork;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

public class SoulNetworkHandler 
{
	public static UUID getUUIDFromPlayer(EntityPlayer player)
	{
		return player.getPersistentID();
	}
	
	public static EntityPlayer getPlayerFromUUID(UUID uuid)
	{
		MinecraftServer server = MinecraftServer.getServer();
		GameProfile gameProfile;
		gameProfile = server.func_152358_ax().func_152652_a(uuid);
//		LogHelper.info("player is " + gameProfile.getName() + " : " + gameProfile.getId());
		
		return null;
	}
	
	public static int syphonFromNetwork(ItemStack ist, int damageToBeDone)
	{
		if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = ist.getTagCompound().getString("ownerName");

            return syphonFromNetwork(ownerName, damageToBeDone);
        }
		return 0;
	}
	
	public static int syphonFromNetwork(String ownerName, int damageToBeDone)
	{
        if (MinecraftServer.getServer() == null)
        {
            return 0;
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
            return damageToBeDone;
        }
        
        return 0;
	}
	
	/**
	 * Master method used to syphon from the player's network, and will damage them accordingly if they do not have enough LP.
	 * Does not drain on the client side.
	 * 
	 * @param ist		Owned itemStack
	 * @param player	Player using the item
	 * @param damageToBeDone
	 * @return	True if server-sided, false if client-sided
	 */
	public static boolean syphonAndDamageFromNetwork(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
		if(player.worldObj.isRemote)
		{
			return false;
		}
		
       	int amount = SoulNetworkHandler.syphonFromNetwork(ist, damageToBeDone);
        
        hurtPlayer(player, damageToBeDone-amount);  

        return true;
    }
	
	public static boolean canSyphonFromOnlyNetwork(ItemStack ist, int damageToBeDone)
    {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = ist.getTagCompound().getString("ownerName");

            return canSyphonFromOnlyNetwork(ownerName, damageToBeDone);
        }

        return false;
    }
	
	public static boolean canSyphonFromOnlyNetwork(String ownerName, int damageToBeDone)
    {
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
    }
	
	public static int getCurrentEssence(String ownerName)
	{
		if (MinecraftServer.getServer() == null)
        {
            return 0;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null)
        {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }
        
        return data.currentEssence;
	}
	
	public static void setCurrentEssence(String ownerName, int essence)
	{
		if (MinecraftServer.getServer() == null)
        {
            return;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null)
        {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }
        
        data.currentEssence = essence;
        data.markDirty();
	}
	
	/**
	 * A method to add to an owner's network up to a maximum value.
	 * 
	 * @param ownerName
	 * @param addedEssence
	 * @param maximum
	 * @return amount added to the network
	 */
	public static int addCurrentEssenceToMaximum(String ownerName, int addedEssence, int maximum)
	{
		if (MinecraftServer.getServer() == null)
        {
            return 0;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null)
        {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }
        
        int currEss = data.currentEssence;
        
        if(currEss>=maximum)
        {
        	return 0;
        }
        
        int newEss = Math.min(maximum, currEss+addedEssence);
        data.currentEssence = newEss;
        
		return newEss-currEss;
	}
	
	public static void hurtPlayer(EntityPlayer user, int energySyphoned)
    {
        if (energySyphoned < 100 && energySyphoned > 0)
        {
            if (!user.capabilities.isCreativeMode)
            {
                user.setHealth((user.getHealth() - 1));

                if (user.getHealth() <= 0.0005f)
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
                    user.setHealth((user.getHealth() - 1));

                    if (user.getHealth() <= 0.0005f)
                    {
                        user.onDeath(DamageSource.generic);
                        break;
                    }
                }
            }
        }
    }
	
	public static void checkAndSetItemOwner(ItemStack item, EntityPlayer player)
    {
        if (item.stackTagCompound == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        if (item.stackTagCompound.getString("ownerName").equals(""))
        {
            item.stackTagCompound.setString("ownerName", SoulNetworkHandler.getUsername(player));
        }
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
    
    public static String getUsername(EntityPlayer player)
	{
		return player.getDisplayName();
	}
    
    public static EntityPlayer getPlayerForUsername(String str)
	{
		if(MinecraftServer.getServer() == null)
		{
			return null;
		}
		return MinecraftServer.getServer().getConfigurationManager().func_152612_a(str);
	}
    
    public static void causeNauseaToPlayer(ItemStack stack)
    {
    	if (stack.getTagCompound() != null && !(stack.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = stack.getTagCompound().getString("ownerName");
            
            SoulNetworkHandler.causeNauseaToPlayer(ownerName);
        }
    }
    
    public static void causeNauseaToPlayer(String ownerName)
    {
    	EntityPlayer entityOwner = SoulNetworkHandler.getPlayerForUsername(ownerName);

        if (entityOwner == null)
        {
            return;
        }

        entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
    }
}
