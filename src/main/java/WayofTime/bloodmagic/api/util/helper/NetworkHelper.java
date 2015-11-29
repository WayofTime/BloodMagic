package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.AddToNetworkEvent;
import WayofTime.bloodmagic.api.event.SoulNetworkEvent;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NetworkHelper {

    // Get

    public static SoulNetwork getSoulNetwork(String ownerName, World world) {
        SoulNetwork network = (SoulNetwork) world.getMapStorage().loadData(SoulNetwork.class, ownerName);

        if (network == null) {
            network = new SoulNetwork(ownerName);
            world.getMapStorage().setData(ownerName, network);
        }

        return network;
    }

    public static int getCurrentMaxOrb(SoulNetwork soulNetwork) {
        return soulNetwork.getOrbTier();
    }

    // Syphon

    /**
     * Handles null-checking the player for you.
     *
     * @return - Whether the action should be performed.
     */
    public static boolean syphonAndDamage(SoulNetwork soulNetwork, int toSyphon) {
        if (soulNetwork.getPlayer() == null) {
            soulNetwork.syphon(toSyphon);
            return true;
        }

        return soulNetwork.syphonAndDamage(toSyphon);
    }

    public static boolean syphonFromContainer(ItemStack stack, SoulNetwork soulNetwork, int toSyphon) {
        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_NAME);

        if (Strings.isNullOrEmpty(ownerName))
            return false;

        SoulNetworkEvent.ItemDrainInContainerEvent event = new SoulNetworkEvent.ItemDrainInContainerEvent(stack, ownerName, toSyphon);

        return !(MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY) && soulNetwork.syphon(event.syphon) >= toSyphon;
    }

    // Set

    public static void setMaxOrbToMax(SoulNetwork soulNetwork, int maxOrb) {
        soulNetwork.setOrbTier(Math.max(maxOrb, soulNetwork.getOrbTier()));
        soulNetwork.markDirty();
    }

    // TODO - Remove everything below. It is deprecated and should not be used.

    /**
     * Master method used to syphon from the player's network, and will damage them accordingly if they do not have enough LP.
     * Does not drain on the client side.
     *
     * @param stack  Owned itemStack
     * @param player Player using the item
     *
     * @return True if the action should be executed and false if it should not. Always returns false if client-sided.
     */
    @Deprecated
    public static boolean syphonAndDamageFromNetwork(ItemStack stack, EntityPlayer player, int syphon) {
        if (player.worldObj.isRemote)
            return false;

        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_NAME);

        if (!Strings.isNullOrEmpty(ownerName)) {
            SoulNetworkEvent.ItemDrainNetworkEvent event = new SoulNetworkEvent.ItemDrainNetworkEvent(player, ownerName, stack, syphon);

            if (MinecraftForge.EVENT_BUS.post(event))
                return false;

            int drainAmount = syphonFromNetwork(event.ownerName, event.syphon);

            if (drainAmount == 0 || event.shouldDamage)
                hurtPlayer(player, event.syphon);

            //The event has been told to prevent the action but allow all repercussions of using the item.
            return event.getResult() != Event.Result.DENY;
        }

        int amount = NetworkHelper.syphonFromNetwork(stack, syphon);

        hurtPlayer(player, syphon - amount);

        return true;
    }

    @Deprecated
    public static boolean syphonFromNetworkWhileInContainer(ItemStack stack, int syphon) {
        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_NAME);

        if (Strings.isNullOrEmpty(ownerName))
            return false;

        SoulNetworkEvent.ItemDrainInContainerEvent event = new SoulNetworkEvent.ItemDrainInContainerEvent(stack, ownerName, syphon);

        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
            return false;

        return syphonFromNetwork(event.ownerName, event.syphon) >= syphon;
    }

    @Deprecated
    public static int syphonFromNetwork(ItemStack stack, int syphon) {
        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_NAME);
        if (!Strings.isNullOrEmpty(ownerName))
            return syphonFromNetwork(ownerName, syphon);

        return 0;
    }

    @Deprecated
    public static int syphonFromNetwork(String ownerName, int syphon) {
        if (MinecraftServer.getServer() == null)
            return 0;

        World world = MinecraftServer.getServer().worldServers[0];
        SoulNetwork network = (SoulNetwork) world.loadItemData(SoulNetwork.class, ownerName);

        if (network == null) {
            network = new SoulNetwork(ownerName);
            world.setItemData(ownerName, network);
        }

        if (network.getCurrentEssence() >= syphon) {
            network.setCurrentEssence(network.getCurrentEssence() - syphon);
            network.markDirty();
            return syphon;
        }

        return 0;
    }

    // Add

    /**
     * A method to add to an owner's network up to a maximum value.
     *
     * @return amount added to the network
     */
    @Deprecated
    public static int addCurrentEssenceToMaximum(String ownerName, int addedEssence, int maximum) {
        AddToNetworkEvent event = new AddToNetworkEvent(ownerName, addedEssence, maximum);

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

    // Get

    @Deprecated
    public static int getCurrentEssence(String ownerName) {
        if (MinecraftServer.getServer() == null)
            return 0;

        World world = MinecraftServer.getServer().worldServers[0];
        SoulNetwork network = (SoulNetwork) world.loadItemData(SoulNetwork.class, ownerName);

        if (network == null) {
            network = new SoulNetwork(ownerName);
            world.setItemData(ownerName, network);
        }

        return network.getCurrentEssence();
    }

    // Do damage

    @Deprecated
    public static void hurtPlayer(EntityPlayer user, int energySyphoned) {
        if (energySyphoned < 100 && energySyphoned > 0) {
            if (!user.capabilities.isCreativeMode) {
                user.setHealth((user.getHealth() - 1));

                if (user.getHealth() <= 0.0005f)
                    user.onDeath(BloodMagicAPI.getDamageSource());
            }
        } else if (energySyphoned >= 100) {
            if (!user.capabilities.isCreativeMode) {
                for (int i = 0; i < ((energySyphoned + 99) / 100); i++) {
                    user.setHealth((user.getHealth() - 1));

                    if (user.getHealth() <= 0.0005f) {
                        user.onDeath(BloodMagicAPI.getDamageSource());
                        break;
                    }
                }
            }
        }
    }

    @Deprecated
    public static void hurtPlayer(EntityPlayer user, float damage) {
        if (!user.capabilities.isCreativeMode) {
            user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F);
            user.setHealth((user.getHealth() - damage));
        }
    }
}
