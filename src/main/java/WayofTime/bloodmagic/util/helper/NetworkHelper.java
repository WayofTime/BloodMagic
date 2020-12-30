package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.event.SoulNetworkEvent;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.core.data.BMWorldSavedData;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public class NetworkHelper {
    // Get

    /**
     * Gets the SoulNetwork for the player.
     *
     * @param uuid - The UUID of the SoulNetwork owner - this is UUID.toString().
     * @return - The SoulNetwork for the given name.
     */
    public static SoulNetwork getSoulNetwork(String uuid) {
        World world = DimensionManager.getWorld(0);
        if (world == null || world.getMapStorage() == null) //Hack-ish way to fix the lava crystal.
            return new BMWorldSavedData().getNetwork(UUID.fromString(uuid));

        BMWorldSavedData saveData = (BMWorldSavedData) world.getMapStorage().getOrLoadData(BMWorldSavedData.class, BMWorldSavedData.ID);

        if (saveData == null) {
            saveData = new BMWorldSavedData();
            world.getMapStorage().setData(BMWorldSavedData.ID, saveData);
        }

        return saveData.getNetwork(UUID.fromString(uuid));
    }

    /**
     * @param uuid - The Player's Mojang UUID
     * @see NetworkHelper#getSoulNetwork(String)
     */
    public static SoulNetwork getSoulNetwork(UUID uuid) {
        return getSoulNetwork(uuid.toString());
    }

    /**
     * @param player - The Player
     * @see NetworkHelper#getSoulNetwork(String)
     */
    public static SoulNetwork getSoulNetwork(EntityPlayer player) {
        return getSoulNetwork(PlayerHelper.getUUIDFromPlayer(player));
    }

    public static SoulNetwork getSoulNetwork(Binding binding) {
        return getSoulNetwork(binding.getOwnerId());
    }

    /**
     * Gets the current orb tier of the SoulNetwork.
     *
     * @param soulNetwork - SoulNetwork to get the tier of.
     * @return - The Orb tier of the given SoulNetwork
     */
    public static int getCurrentMaxOrb(SoulNetwork soulNetwork) {
        return soulNetwork.getOrbTier();
    }

    public static int getMaximumForTier(int tier) {
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
     * <p>
     * Handles null-checking the player for you.
     *
     * @param soulNetwork - SoulNetwork to syphon from
     * @param user        - User of the item.
     * @param toSyphon    - Amount of LP to syphon
     * @return - Whether the action should be performed.
     * @deprecated Use {@link #getSoulNetwork(EntityPlayer)} and {@link SoulNetwork#syphonAndDamage$(EntityPlayer, SoulTicket)}
     */
    @Deprecated
    public static boolean syphonAndDamage(SoulNetwork soulNetwork, EntityPlayer user, int toSyphon) {

//        if (soulNetwork.getNewOwner() == null)
//        {
//            soulNetwork.syphon(toSyphon);
//            return true;
//        }

        return soulNetwork.syphonAndDamage(user, toSyphon);
    }

    /**
     * Syphons a player from within a container.
     *
     * @param stack  - ItemStack in the Container.
     * @param ticket - SoulTicket to syphon
     * @return - If the syphon was successful.
     */
    public static boolean syphonFromContainer(ItemStack stack, SoulTicket ticket) {
        if (!(stack.getItem() instanceof IBindable))
            return false;

        Binding binding = ((IBindable) stack.getItem()).getBinding(stack);
        if (binding == null)
            return false;

        SoulNetwork network = getSoulNetwork(binding);
        SoulNetworkEvent.Syphon.Item event = new SoulNetworkEvent.Syphon.Item(network, ticket, stack);

        return !MinecraftForge.EVENT_BUS.post(event) && network.syphon(event.getTicket(), true) >= ticket.getAmount();
    }

    /**
     * Checks if the ItemStack has a user to be syphoned from.
     *
     * @param stack    - ItemStack to check
     * @param toSyphon - Amount of LP to syphon
     * @return - If syphoning is possible
     */
    public static boolean canSyphonFromContainer(ItemStack stack, int toSyphon) {
        if (!(stack.getItem() instanceof IBindable))
            return false;

        Binding binding = ((IBindable) stack.getItem()).getBinding(stack);
        if (binding == null)
            return false;

        SoulNetwork network = getSoulNetwork(binding);
        return network.getCurrentEssence() >= toSyphon;
    }

    // Set

    /**
     * Sets the orb tier of the SoulNetwork to the given orb. Will not set if
     * the given tier is lower than the current tier.
     *
     * @param soulNetwork - SoulNetwork to set the orb tier of
     * @param maxOrb      - Tier of orb to set to
     */
    public static void setMaxOrb(SoulNetwork soulNetwork, int maxOrb) {
        soulNetwork.setOrbTier(Math.max(maxOrb, soulNetwork.getOrbTier()));
    }
}
