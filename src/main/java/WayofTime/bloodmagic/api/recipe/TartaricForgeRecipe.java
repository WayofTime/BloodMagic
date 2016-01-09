package WayofTime.bloodmagic.api.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class TartaricForgeRecipe
{
    protected ItemStack output = null;
    protected ArrayList<Object> input = new ArrayList<Object>();
    @Getter
    protected double minimumSouls;
    @Getter
    protected double soulsDrained;

    public TartaricForgeRecipe(Block result, double minSouls, double drain, Object... recipe)
    {
        this(new ItemStack(result), minSouls, drain, recipe);
    }

    public TartaricForgeRecipe(Item result, double minSouls, double drain, Object... recipe)
    {
        this(new ItemStack(result), minSouls, drain, recipe);
    }

    public TartaricForgeRecipe(ItemStack result, double minSouls, double drain, Object... recipe)
    {
        output = result.copy();
        this.minimumSouls = minSouls;
        this.soulsDrained = drain;
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack) in).copy());
            } else if (in instanceof Item)
            {
                input.add(new ItemStack((Item) in));
            } else if (in instanceof Block)
            {
                input.add(new ItemStack((Block) in));
            } else if (in instanceof String)
            {
                input.add(OreDictionary.getOres((String) in));
            } else
            {
                String ret = "Invalid soul forge recipe: ";
                for (Object tmp : recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return input.size();
    }

    public ItemStack getRecipeOutput()
    {
        return output.copy();
    }

    /**
     * Used to check if a recipe matches current crafting inventory. World and
     * BlockPos are for future-proofing
     */
    @SuppressWarnings("unchecked")
    public boolean matches(List<ItemStack> checkedList, World world, BlockPos pos)
    {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (int x = 0; x < checkedList.size(); x++)
        {
            ItemStack slot = checkedList.get(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    } else if (next instanceof List)
                    {
                        Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
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
    public ArrayList<Object> getInput()
    {
        return this.input;
    }
}