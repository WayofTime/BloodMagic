package WayofTime.alchemicalWizardry.api.summoningRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class SummoningRegistryComponent
{
    public ItemStack[] ring1 = new ItemStack[6];
    public ItemStack[] ring2 = new ItemStack[6];
    public ItemStack[] ring3 = new ItemStack[6];
    public SummoningHelper summoningHelper;
    public int summoningCost;
    public int bloodOrbLevel;

    public SummoningRegistryComponent(SummoningHelper s, ItemStack[] newRing1, ItemStack[] newRing2, ItemStack[] newRing3, int amount, int bloodOrbLevel)
    {
        this.summoningHelper = s;
        this.ring1 = newRing1;
        this.ring2 = newRing2;
        this.ring3 = newRing3;
        this.summoningCost = amount;
        this.bloodOrbLevel = bloodOrbLevel;

        if (this.ring1.length != 6)
        {
            ItemStack[] newRecipe = new ItemStack[6];

            for (int i = 0; i < 6; i++)
            {
                if (i + 1 > this.ring1.length)
                {
                    newRecipe[i] = null;
                } else
                {
                    newRecipe[i] = this.ring1[i];
                }
            }

            this.ring1 = newRecipe;
        }

        if (this.ring2.length != 6)
        {
            ItemStack[] newRecipe = new ItemStack[6];

            for (int i = 0; i < 6; i++)
            {
                if (i + 1 > this.ring2.length)
                {
                    newRecipe[i] = null;
                } else
                {
                    newRecipe[i] = this.ring2[i];
                }
            }

            this.ring2 = newRecipe;
        }

        if (this.ring3.length != 6)
        {
            ItemStack[] newRecipe = new ItemStack[6];

            for (int i = 0; i < 6; i++)
            {
                if (i + 1 > this.ring3.length)
                {
                    newRecipe[i] = null;
                } else
                {
                    newRecipe[i] = this.ring3[i];
                }
            }

            this.ring3 = newRecipe;
        }
    }

    public boolean compareRing(int ring, ItemStack[] checkedRingRecipe)
    {
        ItemStack[] recipe;

        if (checkedRingRecipe.length < 6)
        {
            return false;
        }

        switch (ring)
        {
            case 1:
                recipe = ring1;
                break;

            case 2:
                recipe = ring2;
                break;

            case 3:
                recipe = ring3;
                break;

            default:
                recipe = ring1;
        }

        if (recipe.length != 6)
        {
            ItemStack[] newRecipe = new ItemStack[6];

            for (int i = 0; i < 6; i++)
            {
                if (i + 1 > recipe.length)
                {
                    newRecipe[i] = null;
                } else
                {
                    newRecipe[i] = recipe[i];
                }
            }

            recipe = newRecipe;
        }

        boolean[] checkList = new boolean[6];

        for (int i = 0; i < 6; i++)
        {
            checkList[i] = false;
        }

        for (int i = 0; i < 6; i++)
        {
            ItemStack recipeItemStack = recipe[i];

            if (recipeItemStack == null)
            {
                continue;
            }

            boolean test = false;

            for (int j = 0; j < 6; j++)
            {
                if (checkList[j])
                {
                    continue;
                }

                ItemStack checkedItemStack = checkedRingRecipe[j];

                if (checkedItemStack == null)
                {
                    continue;
                }

                boolean quickTest = false;

                if (recipeItemStack.getItem() instanceof ItemBlock)
                {
                    if (checkedItemStack.getItem() instanceof ItemBlock)
                    {
                        quickTest = true;
                    }
                } else if (!(checkedItemStack.getItem() instanceof ItemBlock))
                {
                    quickTest = true;
                }

                if (!quickTest)
                {
                    continue;
                }

                if ((checkedItemStack.getItemDamage() == recipeItemStack.getItemDamage() || OreDictionary.WILDCARD_VALUE == recipeItemStack.getItemDamage()) && checkedItemStack.getItem() == recipeItemStack.getItem())
                {
                    test = true;
                    checkList[j] = true;
                    break;
                }
            }

            if (!test)
            {
                return false;
            }
        }

        return true;
    }

    public int getSummoningCost()
    {
        return summoningCost;
    }

    public EntityLivingBase getEntity(World world)
    {
        return this.summoningHelper.getEntity(world);
    }

    public int getBloodOrbLevel()
    {
        return this.bloodOrbLevel;
    }

    public ItemStack[] getRingRecipeForRing(int ring)
    {
        switch (ring)
        {
            case 1:
                return ring1;

            case 2:
                return ring2;

            case 3:
                return ring3;

            default:
                return null;
        }
    }

    public int getSummoningHelperID()
    {
        return this.summoningHelper.getSummoningHelperID();
    }
}
