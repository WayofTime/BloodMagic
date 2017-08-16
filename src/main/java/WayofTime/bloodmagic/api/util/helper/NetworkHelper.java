package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.SoulNetworkEvent;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.saving.BMWorldSavedData;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class NetworkHelper
{
    // Get

    /**
     * Gets the SoulNetwork for the player.
     * 
     * @param uuid
     *        - The UUID of the SoulNetwork owner - this is UUID.toString().
     * 
     * @return - The SoulNetwork for the given name.
     */
    public static SoulNetwork getSoulNetwork(String uuid)
    {
        World world = DimensionManager.getWorld(0);
        if (world == null || world.getMapStorage() == null) //Hack-ish way to fix the lava crystal.
            return new BMWorldSavedData().getNetwork(UUID.fromString(uuid));

        BMWorldSavedData saveData = (BMWorldSavedData) world.getMapStorage().getOrLoadData(BMWorldSavedData.class, BMWorldSavedData.ID);

        if (saveData == null)
        {
            saveData = new BMWorldSavedData();
            world.getMapStorage().setData(BMWorldSavedData.ID, saveData);
        }

        return saveData.getNetwork(UUID.fromString(uuid));
    }

    /**
     * @see NetworkHelper#getSoulNetwork(String)
     * 
     * @param uuid
     *        - The Player's Mojang UUID
     */
    public static SoulNetwork getSoulNetwork(UUID uuid)
    {
        return getSoulNetwork(uuid.toString());
    }

    /**
     * @see NetworkHelper#getSoulNetwork(String)
     * 
     * @param player
     *        - The Player
     */
    public static SoulNetwork getSoulNetwork(EntityPlayer player)
    {
        return getSoulNetwork(PlayerHelper.getUUIDFromPlayer(player));
    }

    /**
     * Gets the current orb tier of the SoulNetwork.
     * 
     * @param soulNetwork
     *        - SoulNetwork to get the tier of.
     * 
     * @return - The Orb tier of the given SoulNetwork
     */
    public static int getCurrentMaxOrb(SoulNetwork soulNetwork)
    {
        return soulNetwork.getOrbTier();
    }

    public static int getMaximumForTier(int tier)
    {
        int ret = 0;

        if (tier > OrbRegistry.getTierMap().size() || tier < 0)
            return ret;

        for (ItemStack orbStack : OrbRegistry.getOrbsForTier(tier)) {
            BloodOrb orb = ((IBloodOrb) orbStack.getItem()).getOrb(orbStack);
            if (orb.getCapacity() > ret)
                ret = orb.getCapacity();
        }

        return ret;
    }

    // Syphon

    /**
     * Syphons from the player and damages them if there was not enough stored
     * LP.
     * 
     * Handles null-checking the player for you.
     * 
     * @param soulNetwork
     *        - SoulNetwork to syphon from
     * @param user
     *        - User of the item.
     * @param toSyphon
     *        - Amount of LP to syphon
     * 
     * @return - Whether the action should be performed.
     * @deprecated Use {@link #getSoulNetwork(EntityPlayer)} and {@link SoulNetwork#syphonAndDamage(EntityPlayer, int)}
     */
    @Deprecated
    public static boolean syphonAndDamage(SoulNetwork soulNetwork, EntityPlayer user, int toSyphon)
    {

//        if (soulNetwork.getPlayer() == null)
//        {
//            soulNetwork.syphon(toSyphon);
//            return true;
//        }

        return soulNetwork.syphonAndDamage(user, toSyphon);
    }

    /**
     * Syphons a player from within a container.
     * 
     * @param stack
     *        - ItemStack in the Container.
     * @param toSyphon
     *        - Amount of LP to syphon
     * 
     * @return - If the syphon was successful.
     */
    public static boolean syphonFromContainer(ItemStack stack, int toSyphon) //TODO: Change to a String, int?
    {
        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);

        if (Strings.isNullOrEmpty(ownerName))
            return false;

        SoulNetwork network = getSoulNetwork(ownerName);

        SoulNetworkEvent.ItemDrainInContainerEvent event = new SoulNetworkEvent.ItemDrainInContainerEvent(stack, ownerName, toSyphon);

        return !(MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY) && network.syphon(event.syphon) >= toSyphon;
    }

    /**
     * Checks if the ItemStack has a user to be syphoned from.
     * 
     * @param stack
     *        - ItemStack to check
     * @param toSyphon
     *        - Amount of LP to syphon
     * 
     * @return - If syphoning is possible
     */
    public static boolean canSyphonFromContainer(ItemStack stack, int toSyphon)
    {
        stack = NBTHelper.checkNBT(stack);
        String ownerName = stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);

        if (Strings.isNullOrEmpty(ownerName))
            return false;

        SoulNetwork network = getSoulNetwork(ownerName);
        return network.getCurrentEssence() >= toSyphon;
    }

    // Set

    /**
     * Sets the orb tier of the SoulNetwork to the given orb. Will not set if
     * the given tier is lower than the current tier.
     * 
     * @param soulNetwork
     *        - SoulNetwork to set the orb tier of
     * @param maxOrb
     *        - Tier of orb to set to
     */
    public static void setMaxOrb(SoulNetwork soulNetwork, int maxOrb)
    {
        soulNetwork.setOrbTier(Math.max(maxOrb, soulNetwork.getOrbTier()));
    }
}
