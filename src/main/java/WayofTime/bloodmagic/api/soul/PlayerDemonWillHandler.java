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
public class PlayerDemonWillHandler
{
    public static double getTotalDemonWill(EnumDemonWillType type, EntityPlayer player)
    {
        ItemStack[] inventory = player.inventory.mainInventory;
        double souls = 0;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof IDemonWill)
                {
                    souls += ((IDemonWill) stack.getItem()).getWill(stack);
                } else if (stack.getItem() instanceof IDemonWillGem)
                {
                    souls += ((IDemonWillGem) stack.getItem()).getWill(type, stack);
                }
            }
        }

        return souls;
    }

    /**
     * Checks if the player's Tartaric gems are completely full. If so, it will
     * return true.
     * 
     */
    public static boolean isDemonWillFull(EnumDemonWillType type, EntityPlayer player)
    {
        ItemStack[] inventory = player.inventory.mainInventory;

        boolean hasGem = false;
        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    hasGem = true;
                    if (((IDemonWillGem) stack.getItem()).getWill(type, stack) < ((IDemonWillGem) stack.getItem()).getMaxWill(type, stack))
                    {
                        return false;
                    }
                }
            }
        }

        return hasGem;
    }

    /**
     * 
     * @param player
     * @param amount
     * @return - amount consumed
     */
    public static double consumeDemonWill(EnumDemonWillType type, EntityPlayer player, double amount)
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
                if (stack.getItem() instanceof IDemonWill)
                {
                    consumed += ((IDemonWill) stack.getItem()).drainWill(stack, amount - consumed);
                    if (((IDemonWill) stack.getItem()).getWill(stack) <= 0)
                    {
                        inventory[i] = null;
                    }
                } else if (stack.getItem() instanceof IDemonWillGem)
                {
                    consumed += ((IDemonWillGem) stack.getItem()).drainWill(type, stack, amount - consumed);
                }
            }
        }

        return consumed;
    }

    /**
     * Adds an IDemonWill contained in an ItemStack to one of the Soul Gems in
     * the player's inventory.
     * 
     * @param player
     * @param soulStack
     *        - ItemStack that contains an IDemonWill to be added
     * @return
     */
    public static ItemStack addDemonWill(EntityPlayer player, ItemStack soulStack)
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
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    ItemStack newStack = ((IDemonWillGem) stack.getItem()).fillDemonWillGem(stack, soulStack);
                    if (newStack == null)
                    {
                        return null;
                    }
                }
            }
        }

        return soulStack;
    }

    public static double addDemonWill(EnumDemonWillType type, EntityPlayer player, double amount)
    {
        ItemStack[] inventory = player.inventory.mainInventory;
        double remaining = amount;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null)
            {
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    double souls = ((IDemonWillGem) stack.getItem()).getWill(type, stack);
                    double fill = Math.min(((IDemonWillGem) stack.getItem()).getMaxWill(type, stack) - souls, remaining);
                    ((IDemonWillGem) stack.getItem()).setWill(type, stack, fill + souls);
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

    public static double addDemonWill(EnumDemonWillType type, EntityPlayer player, double amount, ItemStack ignored)
    {
        ItemStack[] inventory = player.inventory.mainInventory;
        double remaining = amount;

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (stack != null && !stack.equals(ignored))
            {
                if (stack.getItem() instanceof IDemonWillGem)
                {
                    double souls = ((IDemonWillGem) stack.getItem()).getWill(type, stack);
                    double fill = Math.min(((IDemonWillGem) stack.getItem()).getMaxWill(type, stack) - souls, remaining);
                    ((IDemonWillGem) stack.getItem()).setWill(type, stack, fill + souls);
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
