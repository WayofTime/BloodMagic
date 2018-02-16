package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.iface.IItemLPContainer;
import WayofTime.bloodmagic.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHelper {
    // IItemLPContainer
    public static class LPContainer {
        /**
         * Attempts to fill an altar with the contained LP
         *
         * @param altar     - The altar in question
         * @param itemStack - The {@link IItemLPContainer} ItemStack filling the altar
         * @param world     - The world
         * @param altarPos  - The position of the altar
         * @return Whether or not the altar was filled (or at least attempted)
         */
        public static boolean tryAndFillAltar(IBloodAltar altar, ItemStack itemStack, World world, BlockPos altarPos) {
            if (itemStack.getItem() instanceof IItemLPContainer) {
                if (!altar.isActive()) {
                    IItemLPContainer fillable = (IItemLPContainer) itemStack.getItem();
                    int amount = fillable.getStoredLP(itemStack);

                    if (amount > 0) {
                        int filledAmount = altar.fillMainTank(amount);
                        amount -= filledAmount;
                        fillable.setStoredLP(itemStack, amount);
                        world.notifyBlockUpdate(altarPos, world.getBlockState(altarPos), world.getBlockState(altarPos), 3);
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * Adds the given LP into the {@link IItemLPContainer}'s storage
         *
         * @param stack       - The item in question
         * @param toAdd       - How much LP should be added to the item
         * @param maxCapacity - The item's maximum holding capacity
         * @return Whether or not LP was added to the item
         */
        public static boolean addLPToItem(ItemStack stack, int toAdd, int maxCapacity) {
            if (stack.getItem() instanceof IItemLPContainer) {
                IItemLPContainer fillable = (IItemLPContainer) stack.getItem();
                stack = NBTHelper.checkNBT(stack);

                if (toAdd < 0)
                    toAdd = 0;

                if (toAdd > maxCapacity)
                    toAdd = maxCapacity;

                fillable.setStoredLP(stack, Math.min(fillable.getStoredLP(stack) + toAdd, maxCapacity));
                return true;
            }

            return false;
        }
    }

    public static class LivingUpgrades {
        public static LivingArmourUpgrade getUpgrade(ItemStack stack) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                String key = getKey(stack);
                int level = getLevel(stack);

                return LivingArmourHandler.generateUpgradeFromKey(key, level);
            }

            return null;
        }

        public static void setUpgrade(ItemStack stack, LivingArmourUpgrade upgrade) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                setKey(stack, upgrade.getUniqueIdentifier());
                setLevel(stack, upgrade.getUpgradeLevel());
            }
        }

        public static void setKey(ItemStack stack, String key) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                NBTHelper.checkNBT(stack);
                NBTTagCompound tag = stack.getTagCompound();

                tag.setString("key", key);
            }
        }

        public static String getKey(ItemStack stack) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                NBTHelper.checkNBT(stack);
                NBTTagCompound tag = stack.getTagCompound();

                return tag.getString("key");
            }

            return "";
        }

        public static void setLevel(ItemStack stack, int level) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                NBTHelper.checkNBT(stack);
                NBTTagCompound tag = stack.getTagCompound();

                tag.setInteger("level", level);
            }
        }

        public static int getLevel(ItemStack stack) {
            if (stack.getItem() instanceof ItemUpgradeTome || stack.getItem() instanceof IUpgradeTrainer) {
                NBTHelper.checkNBT(stack);
                NBTTagCompound tag = stack.getTagCompound();

                return tag.getInteger("level");
            }

            return 0;
        }
    }
}
