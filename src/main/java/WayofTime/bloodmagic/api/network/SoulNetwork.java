package WayofTime.bloodmagic.api.network;

import javax.annotation.Nullable;

import WayofTime.bloodmagic.ConfigHandler;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.AddToNetworkEvent;
import WayofTime.bloodmagic.api.event.SoulNetworkEvent;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

import com.google.common.base.Strings;

@Getter
public class SoulNetwork extends WorldSavedData
{
    @Nullable
    private final EntityPlayer player;
    private int currentEssence;
    private int orbTier;

    public SoulNetwork(String name)
    {
        super(name);

        currentEssence = 0;
        orbTier = 0;
        player = PlayerHelper.getPlayerFromUUID(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        currentEssence = nbttagcompound.getInteger(Constants.NBT.CURRENT_ESSENCE);
        orbTier = nbttagcompound.getInteger(Constants.NBT.ORB_TIER);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger(Constants.NBT.CURRENT_ESSENCE, currentEssence);
        nbttagcompound.setInteger(Constants.NBT.ORB_TIER, orbTier);
    }

    public int addLifeEssence(int toAdd, int maximum)
    {
        AddToNetworkEvent event = new AddToNetworkEvent(mapName, toAdd, maximum);

        if (MinecraftForge.EVENT_BUS.post(event))
            return 0;

        if (MinecraftServer.getServer() == null)
            return 0;

        World world = MinecraftServer.getServer().worldServers[0];
        SoulNetwork data = (SoulNetwork) world.loadItemData(SoulNetwork.class, event.ownerNetwork);

        if (data == null)
        {
            data = new SoulNetwork(event.ownerNetwork);
            world.setItemData(event.ownerNetwork, data);
        }

        int currEss = data.getCurrentEssence();

        if (currEss >= event.maximum)
            return 0;

        int newEss = Math.min(event.maximum, currEss + event.addedAmount);
        if (event.getResult() != Event.Result.DENY)
            data.setCurrentEssence(newEss);

        markDirty();

        return newEss - currEss;
    }

    /**
     * Used to syphon LP from the network
     * 
     * @param syphon
     *        - The amount of LP to syphon
     * 
     * @return The amount of LP syphoned
     */
    public int syphon(int syphon)
    {
        if (getCurrentEssence() >= syphon)
        {
            setCurrentEssence(getCurrentEssence() - syphon);
            return syphon;
        }

        return 0;
    }

    /**
     * Syphons from the network of the owner. If not enough LP is found, it will
     * instead take away from the user's health.
     * 
     * Always returns false on the client side.
     * 
     * @param user
     *        - The Player to syphon from
     * @param toSyphon
     *        - The amount of LP to syphon
     * 
     * @return - Whether the action should be performed.
     */
    public boolean syphonAndDamage(EntityPlayer user, int toSyphon)
    {
        if (user != null)
        {
            if (user.worldObj.isRemote)
                return false;

            if (!Strings.isNullOrEmpty(mapName))
            {
                SoulNetworkEvent.ItemDrainNetworkEvent event = new SoulNetworkEvent.ItemDrainNetworkEvent(user, mapName, null, toSyphon);

                if (MinecraftForge.EVENT_BUS.post(event))
                    return false;

                int drainAmount = syphon(event.syphon);

                if (drainAmount <= 0 || event.shouldDamage)
                    hurtPlayer(user, event.syphon);

                return event.getResult() != Event.Result.DENY;
            }

            int amount = syphon(toSyphon);
            hurtPlayer(user, toSyphon - amount);

            return true;
        }

        return false;
    }

    public void hurtPlayer(EntityPlayer user, float syphon)
    {
        if (user != null)
        {
            if (syphon < 100 && syphon > 0)
            {
                if (!user.capabilities.isCreativeMode)
                {
                    user.hurtResistantTime = 0;
                    user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 1.0F);
                }

            } else if (syphon >= 100)
            {
                if (!user.capabilities.isCreativeMode)
                {
                    for (int i = 0; i < ((syphon + 99) / 100); i++)
                    {
                        user.hurtResistantTime = 0;
                        user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 1.0F);
                    }
                }
            }
        }
    }

    public void causeNauseaToPlayer()
    {
        if (getPlayer() != null)
        {
            PotionEffect effect = ConfigHandler.antiHitler ? new PotionEffect(Potion.weakness.getId(), 99, 127) : new PotionEffect(Potion.confusion.getId(), 99);
            getPlayer().addPotionEffect(effect);
        }
    }

    public SoulNetwork setCurrentEssence(int currentEssence)
    {
        this.currentEssence = currentEssence;
        markDirty();
        return this;
    }

    public SoulNetwork setOrbTier(int orbTier)
    {
        this.orbTier = orbTier;
        markDirty();
        return this;
    }
}
