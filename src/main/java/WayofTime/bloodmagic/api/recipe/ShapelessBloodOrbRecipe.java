package WayofTime.bloodmagic.api.recipe;

import WayofTime.bloodmagic.api.orb.IBloodOrb;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Shapeless Blood Orb Recipe Handler by joshie *
 */
public class ShapelessBloodOrbRecipe implements IRecipe
{
    private ItemStack output = null;
    private ArrayList<Object> input = new ArrayList<Object>();
    private int tier;

    public ShapelessBloodOrbRecipe(Block result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public ShapelessBloodOrbRecipe(Item result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public ShapelessBloodOrbRecipe(ItemStack result, Object... recipe)
    {
        output = result.copy();
        for (Object in : recipe)
        {
            if (in instanceof IBloodOrb || (in instanceof ItemStack && ((ItemStack) in).getItem() instanceof IBloodOrb))
            {
                // If the item is an instanceof IBloodOrb then save the level of
                // the orb.
                if (in instanceof ItemStack)
                {
                    ItemStack inStack = (ItemStack) in;
                    tier = ((IBloodOrb) inStack.getItem()).getOrbLevel(inStack.getItemDamage());
                    input.add(tier);
                } else
                {
                    tier = ((IBloodOrb) in).getOrbLevel(0);
                    input.add(tier);
                }
            } else if (in instanceof ItemStack)
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
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp : recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    ShapelessBloodOrbRecipe(ShapelessRecipes recipe, Map<ItemStack, String> replacements)
    {
        output = recipe.getRecipeOutput();

        for (ItemStack ingred : recipe.recipeItems)
        {
            Object finalObj = ingred;
            for (Entry<ItemStack, String> replace : replacements.entrySet())
            {
                if (OreDictionary.itemMatches(replace.getKey(), ingred, false))
                {
                    finalObj = OreDictionary.getOres(replace.getValue());
                    break;
                }
            }
            input.add(finalObj);
        }
    }

    @Override
    public int getRecipeSize()
    {
        return input.size();
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return output;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        return output.copy();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world)
    {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    // If target is integer, then we should be check the blood
                    // orb value of the item instead
                    if (next instanceof Integer)
                    {
                        if (slot.getItem() instanceof IBloodOrb)
                        {
                            IBloodOrb orb = (IBloodOrb) slot.getItem();
                            if (orb.getOrbLevel(slot.getItemDamage()) < (Integer) next)
                            {
                                return false;
                            }
                        } else
                            return false;
                        match = true;
                    } else if (next instanceof ItemStack)
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

    public ArrayList<Object> getInput()
    {
        return this.input;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    public int getTier()
    {
        return tier;
    }
}