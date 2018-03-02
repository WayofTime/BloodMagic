package WayofTime.bloodmagic.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TartaricForgeRecipe {
    protected ItemStack output = null;
    protected List<Object> input = new ArrayList<>();
    protected double minimumSouls;
    protected double soulsDrained;

    public TartaricForgeRecipe(Block result, double minSouls, double drain, Object... recipe) {
        this(new ItemStack(result), minSouls, drain, recipe);
    }

    public TartaricForgeRecipe(Item result, double minSouls, double drain, Object... recipe) {
        this(new ItemStack(result), minSouls, drain, recipe);
    }

    public TartaricForgeRecipe(ItemStack result, double minSouls, double drain, Object... recipe) {
        output = result.copy();
        this.minimumSouls = minSouls;
        this.soulsDrained = drain;
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
                StringBuilder ret = new StringBuilder("Invalid soul forge recipe: ");
                for (Object tmp : recipe) {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
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

    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    /**
     * Used to check if a recipe matches current crafting inventory. World and
     * BlockPos are for future-proofing
     */
    @SuppressWarnings("unchecked")
    public boolean matches(List<ItemStack> checkedList, World world, BlockPos pos) {
        ArrayList<Object> required = new ArrayList<>(input);

        for (ItemStack slot : checkedList) {
            if (slot != null) {
                boolean inRecipe = false;

                for (Object aRequired : required) {
                    boolean match = false;

                    if (aRequired instanceof ItemStack) {
                        match = OreDictionary.itemMatches((ItemStack) aRequired, slot, false);
                    } else if (aRequired instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) aRequired).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(aRequired);
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
        return this.input;
    }

    public double getMinimumSouls() {
        return minimumSouls;
    }

    public double getSoulsDrained() {
        return soulsDrained;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("output", output)
                .append("input", input)
                .append("minimumSouls", minimumSouls)
                .append("soulsDrained", soulsDrained)
                .append("recipeSize", getRecipeSize())
                .append("recipeOutput", getRecipeOutput())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TartaricForgeRecipe)) return false;

        TartaricForgeRecipe that = (TartaricForgeRecipe) o;

        if (Double.compare(that.minimumSouls, minimumSouls) != 0) return false;
        if (Double.compare(that.soulsDrained, soulsDrained) != 0) return false;
        if (output != null ? !output.equals(that.output) : that.output != null) return false;
        return input != null ? input.equals(that.input) : that.input == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = output != null ? output.hashCode() : 0;
        result = 31 * result + (input != null ? input.hashCode() : 0);
        temp = Double.doubleToLongBits(minimumSouls);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(soulsDrained);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}