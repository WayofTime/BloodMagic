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
import net.minecraftforge.common.MinecraftForge;
import WayofTime.alchemicalWizardry.api.event.AddToNetworkEvent;
import WayofTime.alchemicalWizardry.api.event.ItemBindEvent;
import WayofTime.alchemicalWizardry.api.event.ItemDrainInContainerEvent;
import WayofTime.alchemicalWizardry.api.event.ItemDrainNetworkEvent;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;

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
        return null;
    }
    
    public static boolean syphonFromNetworkWhileInContainer(ItemStack ist, int damageToBeDone)
    {
    	String ownerName = "";
    	if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            ownerName = ist.getTagCompound().getString("ownerName");
        }
    	
    	ItemDrainInContainerEvent event = new ItemDrainInContainerEvent(ist, ownerName, damageToBeDone);
    	
    	if(MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY)
    	{
    		return false;
    	}
    	
    	return syphonFromNetwork(event.ownerNetwork, event.drainAmount) >= damageToBeDone;
    }
    
    public static int getCurrentMaxOrb(String ownerName)
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

        return data.maxOrb;
    }
    
    public static void setMaxOrbToMax(String ownerName, int maxOrb)
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
        
        data.maxOrb = Math.max(maxOrb, data.maxOrb);
        data.markDirty();
    }
    
    public static int getMaximumForOrbTier(int maxOrb)
    {
    	switch(maxOrb)
    	{
    	case 1:
    		return 5000;
    	case 2:
    		return 25000;
    	case 3:
    		return 150000;
    	case 4:
    		return 1000000;
    	case 5:
    		return 10000000;
    	case 6:
    		return 30000000;
    	default: 
    		return 1;
    	}
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
     * @param ist            Owned itemStack
     * @param player         Player using the item
     * @param damageToBeDone
     * @return True if the action should be executed and false if it should not. Always returns false if client-sided.
     */
    public static boolean syphonAndDamageFromNetwork(ItemStack ist, EntityPlayer player, int drain)
    {
        if (player.worldObj.isRemote) 
        {
            return false;
        }
        
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = ist.getTagCompound().getString("ownerName");
            
            ItemDrainNetworkEvent event = new ItemDrainNetworkEvent(player, ownerName, ist, drain);
            
            if(MinecraftForge.EVENT_BUS.post(event))
            {
            	return false;
            }
            
            int drainAmount = syphonFromNetwork(event.ownerNetwork, event.drainAmount);
            if(drainAmount == 0 || event.shouldDamage)
            {
            	hurtPlayer(player, event.damageAmount);
            }
            
            return (event.getResult() != Event.Result.DENY); //The event has been told to prevent the action but allow all repercussions of using the item.
        }

        int amount = SoulNetworkHandler.syphonFromNetwork(ist, drain);

        hurtPlayer(player, drain - amount);

        return true;
    }
    
    public static boolean syphonAndDamageFromNetwork(String ownerName, EntityPlayer player, int damageToBeDone)
    {
        if (player.worldObj.isRemote)
        {
            return false;
        }

        World world = player.worldObj;
        if (world != null)
        {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;

            world.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }
        
        int amount = SoulNetworkHandler.syphonFromNetwork(ownerName, damageToBeDone);

        hurtPlayer(player, damageToBeDone - amount);

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
    	AddToNetworkEvent event = new AddToNetworkEvent(ownerName, addedEssence, maximum);
    	
    	if(MinecraftForge.EVENT_BUS.post(event))
    	{
    		return 0;
    	}
    	
        if (MinecraftServer.getServer() == null)
        {
            return 0;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, event.ownerNetwork);

        if (data == null)
        {
            data = new LifeEssenceNetwork(event.ownerNetwork);
            world.setItemData(event.ownerNetwork, data);
        }

        int currEss = data.currentEssence;

        if (currEss >= event.maximum)
        {
            return 0;
        }

        int newEss = Math.min(event.maximum, currEss + event.addedAmount);
        if(event.getResult() != Event.Result.DENY)
        {
            data.currentEssence = newEss;
        }

        return newEss - currEss;
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
    
    public static void hurtPlayer(EntityPlayer user, float damage)
    {
        if (!user.capabilities.isCreativeMode)
        {
	        user.setHealth((user.getHealth() - damage));
	
	        if (user.getHealth() <= 0.0005f)
	        {
	            user.onDeath(DamageSource.generic);
	        }
        }
    }

    public static void checkAndSetItemOwner(ItemStack item, EntityPlayer player)
    {
        if (item.getTagCompound() == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        if (item.getTagCompound().getString("ownerName").equals(""))
        {
        	ItemBindEvent event = new ItemBindEvent(player, SoulNetworkHandler.getUsername(player), item);
        	
        	if(!MinecraftForge.EVENT_BUS.post(event))
        	{
                item.getTagCompound().setString("ownerName", event.key);
        	}
        }
    }

    public static void checkAndSetItemOwner(ItemStack item, String ownerName)
    {
        if (item.getTagCompound() == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        if (item.getTagCompound().getString("ownerName").equals(""))
        {
            item.getTagCompound().setString("ownerName", ownerName);
        }
    }

    public static String getUsername(EntityPlayer player)
    {
        return player.getDisplayName();
    }

    public static EntityPlayer getPlayerForUsername(String str)
    {
        if (MinecraftServer.getServer() == null)
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
    
    public static String getOwnerName(ItemStack item)
    {
        if (item.getTagCompound() == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        return item.getTagCompound().getString("ownerName");
    }
}
