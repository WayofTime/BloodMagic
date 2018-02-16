package WayofTime.bloodmagic.soul;

import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * This class provides several helper methods in order to handle soul
 * consumption and use for a player. This refers to the Soul System, meaning
 * Monster Souls and Soul Gems, etc. The Soul Network's helper methods are found
 * in {@link NetworkHelper}
 */
public class PlayerDemonWillHandler {
    /**
     * Gets the total amount of Will a player contains in their inventory
     *
     * @param type   - The type of Will to check for
     * @param player - The player to check the will of
     * @return - The amount of will the player contains
     */
    public static double getTotalDemonWill(EnumDemonWillType type, EntityPlayer player) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        double souls = 0;

        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof IDemonWill && ((IDemonWill) stack.getItem()).getType(stack) == type) {
                souls += ((IDemonWill) stack.getItem()).getWill(type, stack);
            } else if (stack.getItem() instanceof IDemonWillGem) {
                souls += ((IDemonWillGem) stack.getItem()).getWill(type, stack);
            }
        }

        return souls;
    }

    public static EnumDemonWillType getLargestWillType(EntityPlayer player) {
        EnumDemonWillType type = EnumDemonWillType.DEFAULT;
        double max = getTotalDemonWill(type, player);

        for (EnumDemonWillType testType : EnumDemonWillType.values()) {
            double value = getTotalDemonWill(testType, player);
            if (value > max) {
                type = testType;
            }
        }

        return type;
    }

    /**
     * Checks if the player's Tartaric gems are completely full.
     *
     * @param type   - The type of Will to check for
     * @param player - The player to check the Will of
     * @return - True if all Will containers are full, false if not.
     */
    public static boolean isDemonWillFull(EnumDemonWillType type, EntityPlayer player) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;

        boolean hasGem = false;
        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof IDemonWillGem) {
                hasGem = true;
                if (((IDemonWillGem) stack.getItem()).getWill(type, stack) < ((IDemonWillGem) stack.getItem()).getMaxWill(type, stack))
                    return false;
            }
        }

        return hasGem;
    }

    /**
     * Consumes Will from the inventory of a given player
     *
     * @param player - The player to consume the will of
     * @param amount - The amount of will to consume
     * @return - The amount of will consumed.
     */
    public static double consumeDemonWill(EnumDemonWillType type, EntityPlayer player, double amount) {
        double consumed = 0;

        NonNullList<ItemStack> inventory = player.inventory.mainInventory;

        for (int i = 0; i < inventory.size(); i++) {
            if (consumed >= amount)
                return consumed;

            ItemStack stack = inventory.get(i);
            if (stack.getItem() instanceof IDemonWill && ((IDemonWill) stack.getItem()).getType(stack) == type) {
                consumed += ((IDemonWill) stack.getItem()).drainWill(type, stack, amount - consumed);
                if (((IDemonWill) stack.getItem()).getWill(type, stack) <= 0)
                    inventory.set(i, ItemStack.EMPTY);
            } else if (stack.getItem() instanceof IDemonWillGem) {
                consumed += ((IDemonWillGem) stack.getItem()).drainWill(type, stack, amount - consumed, true);
            }
        }

        return consumed;
    }

    /**
     * Adds an IDemonWill contained in an ItemStack to one of the Soul Gems in
     * the player's inventory.
     *
     * @param player    - The player to add will to
     * @param willStack - ItemStack that contains an IDemonWill to be added
     * @return - The modified willStack
     */
    public static ItemStack addDemonWill(EntityPlayer player, ItemStack willStack) {
        if (willStack.isEmpty())
            return ItemStack.EMPTY;

        NonNullList<ItemStack> inventory = player.inventory.mainInventory;

        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof IDemonWillGem) {
                ItemStack newStack = ((IDemonWillGem) stack.getItem()).fillDemonWillGem(stack, willStack);
                if (newStack.isEmpty())
                    return ItemStack.EMPTY;
            }
        }

        return willStack;
    }

    /**
     * Adds an IDiscreteDemonWill contained in an ItemStack to one of the Soul
     * Gems in the player's inventory.
     *
     * @param type   - The type of Will to add
     * @param player - The player to check the Will of
     * @param amount - The amount of will to add
     * @return - The amount of will added
     */
    public static double addDemonWill(EnumDemonWillType type, EntityPlayer player, double amount) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        double remaining = amount;

        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof IDemonWillGem) {
                remaining -= ((IDemonWillGem) stack.getItem()).fillWill(type, stack, remaining, true);
                if (remaining <= 0)
                    break;
            }
        }

        return amount - remaining;
    }

    /**
     * Adds an IDiscreteDemonWill contained in an ItemStack to one of the Soul
     * Gems in the player's inventory while ignoring a specified stack.
     *
     * @param type    - The type of Will to add
     * @param player  - The player to check the Will of
     * @param amount  - The amount of will to add
     * @param ignored - A stack to ignore
     * @return - The amount of will added
     */
    public static double addDemonWill(EnumDemonWillType type, EntityPlayer player, double amount, ItemStack ignored) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        double remaining = amount;

        for (ItemStack stack : inventory) {
            if (!stack.equals(ignored) && stack.getItem() instanceof IDemonWillGem) {
                remaining -= ((IDemonWillGem) stack.getItem()).fillWill(type, stack, remaining, true);

                if (remaining <= 0)
                    break;
            }
        }

        return amount - remaining;
    }
}
