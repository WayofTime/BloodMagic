package WayofTime.bloodmagic.api.network;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.AddToNetworkEvent;
import WayofTime.bloodmagic.api.event.SoulNetworkEvent;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@Getter
@Setter
public class SoulNetwork extends WorldSavedData {

    @Nullable
    private final EntityPlayer player;
    private int currentEssence;
    private int orbTier;

    public SoulNetwork(String name) {
        super(name);

        currentEssence = 0;
        orbTier = 0;
        player = PlayerHelper.getPlayerFromUsername(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        currentEssence = nbttagcompound.getInteger(Constants.NBT.CURRENT_ESSENCE);
        orbTier = nbttagcompound.getInteger(Constants.NBT.ORB_TIER);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger(Constants.NBT.CURRENT_ESSENCE, currentEssence);
        nbttagcompound.setInteger(Constants.NBT.ORB_TIER, orbTier);
    }

    public int addLifeEssence(int toAdd, int maximum) {
        AddToNetworkEvent event = new AddToNetworkEvent(mapName, toAdd, maximum);

        if (MinecraftForge.EVENT_BUS.post(event))
            return 0;

        if (MinecraftServer.getServer() == null)
            return 0;

        World world = MinecraftServer.getServer().worldServers[0];
        SoulNetwork data = (SoulNetwork) world.loadItemData(SoulNetwork.class, event.ownerNetwork);

        if (data == null) {
            data = new SoulNetwork(event.ownerNetwork);
            world.setItemData(event.ownerNetwork, data);
        }

        int currEss = data.getCurrentEssence();

        if (currEss >= event.maximum)
            return 0;

        int newEss = Math.min(event.maximum, currEss + event.addedAmount);
        if (event.getResult() != Event.Result.DENY)
            data.setCurrentEssence(newEss);

        return newEss - currEss;
    }

    /**
     * Used to syphon LP from the network
     */
    public int syphon(int syphon) {
        if (getCurrentEssence() >= syphon) {
            setCurrentEssence(getCurrentEssence() - syphon);
            markDirty();
            return syphon;
        }

        return 0;
    }

    /**
     * If the player exists on the server, syphon the given amount of LP from the player's LP network and
     * damage for any remaining LP required.
     * <p/>
     * Always returns false on the client side.
     *
     * @return - Whether the action should be performed.
     */
    public boolean syphonAndDamage(int toSyphon) {
        if (getPlayer().worldObj.isRemote)
            return false;

        if (!Strings.isNullOrEmpty(mapName)) {
            SoulNetworkEvent.ItemDrainNetworkEvent event = new SoulNetworkEvent.ItemDrainNetworkEvent(player, mapName, getPlayer().getHeldItem(), toSyphon);

            if (MinecraftForge.EVENT_BUS.post(event))
                return false;

            int drainAmount = syphon(event.syphon);

            if (drainAmount == 0 || event.shouldDamage)
                hurtPlayer(event.syphon);

            return event.getResult() != Event.Result.DENY;
        }

        int amount = syphon(toSyphon);
        hurtPlayer(toSyphon - amount);

        return true;
    }

    public void hurtPlayer(int syphon) {
        getPlayer().addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20));
        if (syphon < 100 && syphon > 0) {
            if (!getPlayer().capabilities.isCreativeMode) {
                getPlayer().setHealth((getPlayer().getHealth() - 1));

                if (getPlayer().getHealth() <= 0.0005f)
                    getPlayer().onDeath(BloodMagicAPI.getDamageSource());
            }
        } else if (syphon >= 100) {
            if (!getPlayer().capabilities.isCreativeMode) {
                for (int i = 0; i < ((syphon + 99) / 100); i++) {
                    getPlayer().setHealth((getPlayer().getHealth() - 1));

                    if (getPlayer().getHealth() <= 0.0005f) {
                        getPlayer().onDeath(BloodMagicAPI.getDamageSource());
                        break;
                    }
                }
            }
        }
    }
}
