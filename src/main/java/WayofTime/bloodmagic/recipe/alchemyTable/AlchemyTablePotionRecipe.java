package WayofTime.bloodmagic.recipe.alchemyTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.registry.ModItems;

public class AlchemyTablePotionRecipe extends AlchemyTableRecipe
{
    public static final ItemStack basePotionFlaskStack = new ItemStack(ModItems.potionFlask, 1, OreDictionary.WILDCARD_VALUE);
    protected PotionEffect baseEffect;

    public static final int temporaryMaximumEffectsOnThePotionFlaskYesThisIsALongFieldItIsJustSoIRemember = 3;

    protected double baseAddedImpurity = 5;

    public AlchemyTablePotionRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect)
    {
        super(basePotionFlaskStack, lpDrained, ticksRequired, tierRequired);

        ArrayList<Object> recipe = new ArrayList<Object>();
        for (ItemStack stack : inputItems)
        {
            recipe.add(stack);
        }
        recipe.add(basePotionFlaskStack);

        this.input = recipe;
        this.baseEffect = baseEffect;
    }

    public AlchemyTablePotionRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect)
    {
        this(lpDrained, ticksRequired, tierRequired, Arrays.asList(inputItem), baseEffect);
    }

    @Override
    public ItemStack getRecipeOutput(List<ItemStack> inputList)
    {
        int flaskLocation = -1;
        for (int x = 0; x < inputList.size(); x++)
        {
            ItemStack slot = inputList.get(x);

            if (slot != null)
            {
                boolean match = slot.getItem() == ModItems.potionFlask;

                if (match)
                {
                    flaskLocation = x;
                    continue;
                }
            }
        }

        if (flaskLocation != -1)
        {
            return getModifiedFlaskForInput(inputList.get(flaskLocation));
        }

        return getModifiedFlaskForInput(null);
    }

    @Override
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
                        if (next instanceof ItemStack && ((ItemStack) next).getItem() == ModItems.potionFlask)
                        {
                            if (!isPotionFlaskValidInput(slot))
                            {
                                break;
                            }
                        }

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

    public boolean isPotionFlaskValidInput(ItemStack stack)
    {
        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(stack);
        if (effectList.size() >= temporaryMaximumEffectsOnThePotionFlaskYesThisIsALongFieldItIsJustSoIRemember)
        {
            return false;
        }

        for (PotionEffect eff : effectList)
        {
            if (eff.getPotion() == baseEffect.getPotion())
            {
                return false;
            }
        }

        return true;
    }

    public ItemStack getModifiedFlaskForInput(ItemStack inputStack)
    {
        if (inputStack == null)
        {
            ItemStack outputStack = new ItemStack(ModItems.potionFlask);

            List<PotionEffect> effectList = new ArrayList<PotionEffect>();
            effectList.add(baseEffect);

            PotionUtils.appendEffects(outputStack, effectList);

            return outputStack;
        }

        ItemStack outputStack = inputStack.copy();

        List<PotionEffect> effectList = PotionUtils.getEffectsFromStack(outputStack);
        effectList.add(baseEffect);

        PotionUtils.appendEffects(outputStack, effectList);

        return outputStack;
    }
}
