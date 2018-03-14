package WayofTime.bloodmagic.recipe;

import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LivingArmourDowngradeRecipe {
    protected LivingArmourUpgrade upgrade = null;
    protected ItemStack keyStack = ItemStack.EMPTY;
    protected List<Object> input = new ArrayList<>();

    public LivingArmourDowngradeRecipe(LivingArmourUpgrade upgrade, ItemStack keyStack, Object... recipe) {
        this.upgrade = upgrade;
        this.keyStack = keyStack;
        for (Object in : recipe) {
            if (in instanceof ItemStack) {
                input.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                input.add(new ItemStack((Item) in));
            } else if (in instanceof Block) {
                input.add(new ItemStack((Block) in));
            } else if (in instanceof String) {
                input.add(OreDictionary.getOres((String) in));
            } else {
                StringBuilder ret = new StringBuilder("Invalid living armour downgrade recipe: ");
                for (Object tmp : recipe) {
                    ret.append(tmp).append(", ");
                }
                ret.append(upgrade.toString());
                throw new RuntimeException(ret.toString());
            }
        }
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return input.size();
    }

    public LivingArmourUpgrade getRecipeOutput() {
        return upgrade;
    }

    /**
     * Used to check if a recipe matches current crafting inventory. World and
     * BlockPos are for future-proofing
     */
    @SuppressWarnings("unchecked")
    public boolean matches(ItemStack key, List<ItemStack> checkedList, World world, BlockPos pos) {
        if (!OreDictionary.itemMatches(keyStack, key, false)) {
            return false;
        }

        ArrayList<Object> required = new ArrayList<>(input);

        for (ItemStack slot : checkedList) {
            if (slot != null) {
                boolean inRecipe = false;

                for (Object aRequired : required) {
                    boolean match = false;

                    Object next = aRequired;

                    if (next instanceof ItemStack) {
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    } else if (next instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should
     * never manipulate the values in this array as it will effect the recipe
     * itself.
     *
     * @return The recipes input vales.
     */
    public List<Object> getInput() {
        return ImmutableList.copyOf(input);
    }

    public ItemStack getKey() {
        return this.keyStack;
    }

    public void consumeInventory(IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem().hasContainerItem(stack)) {
                inv.extractItem(i, stack.getCount(), false);
                inv.insertItem(i, stack.getItem().getContainerItem(stack), false);
            } else {
                inv.extractItem(i, 1, false);
            }
        }
    }

    protected ItemStack getContainerItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copyStack = stack.copy();

        if (copyStack.getItem().hasContainerItem(stack)) {
            return copyStack.getItem().getContainerItem(copyStack);
        }

        copyStack.shrink(1);
        if (copyStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return copyStack;
    }
}