package WayofTime.bloodmagic.api.soul;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This class provides several helper methods in order to handle soul
 * consumption and use for a player. This refers to the Soul System, meaning
 * Monster Souls and Soul Gems, etc. The Soul Network's helper methods are found
 * in WayofTime.bloodmagic.api.network
 * 
 * @author WayofTime
 * 
 */
public class PlayerSoulHandler
{
    public static double getTotalSouls(EntityPlayer player)
    {
        ItemStack[] inventory = player.inventory.mainInventory;
        double souls = 0;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof ISoul)
                {
                    souls += ((ISoul) stack.getItem()).getSouls(stack);
                } else if (stack.getItem() instanceof ISoulGem)
                {
                    souls += ((ISoulGem) stack.getItem()).getSouls(stack);
                }
            }
        }

        return souls;
    }

    /**
     * 
     * @param player
     * @param amount
     * @return - amount consumed
     */
    public static double consumeSouls(EntityPlayer player, double amount)
    {
        double consumed = 0;

        ItemStack[] inventory = player.inventory.mainInventory;

        for (int i = 0; i < inventory.length; i++)
        {
            if (consumed >= amount)
            {
                return consumed;
            }

            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof ISoul)
                {
                    consumed += ((ISoul) stack.getItem()).drainSouls(stack, amount - consumed);
                    if (((ISoul) stack.getItem()).getSouls(stack) <= 0)
                    {
                        inventory[i] = null;
                    }
                } else if (stack.getItem() instanceof ISoulGem)
                {
                    consumed += ((ISoulGem) stack.getItem()).drainSouls(stack, amount - consumed);
                }
            }
        }

        return consumed;
    }

    /**
     * Adds an ISoul contained in an ItemStack to one of the Soul Gems in the
     * player's inventory.
     * 
     * @param player
     * @param soulStack
     *        - ItemStack that contains an ISoul to be added
     * @return
     */
    public static ItemStack addSouls(EntityPlayer player, ItemStack soulStack)
    {
        if (soulStack == null)
        {
            return null;
        }

        ItemStack[] inventory = player.inventory.mainInventory;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof ISoulGem)
                {
                    ItemStack newStack = ((ISoulGem) stack.getItem()).fillSoulGem(stack, soulStack);
                    if (newStack == null)
                    {
                        return null;
                    }
                }
            }
        }

        return soulStack;
    }

    public static double addSouls(EntityPlayer player, double amount)
    {
        ItemStack[] inventory = player.inventory.mainInventory;
        double remaining = amount;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof ISoulGem)
                {
                    double souls = ((ISoulGem) stack.getItem()).getSouls(stack);
                    double fill = Math.min(((ISoulGem) stack.getItem()).getMaxSouls(stack) - souls, remaining);
                    ((ISoulGem) stack.getItem()).setSouls(stack, fill + souls);
                    remaining -= fill;

                    if (remaining <= 0)
                    {
                        break;
                    }
                }
            }
        }

        return amount - remaining;
    }
}
