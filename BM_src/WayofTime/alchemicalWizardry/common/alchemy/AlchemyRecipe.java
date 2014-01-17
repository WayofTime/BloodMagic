package WayofTime.alchemicalWizardry.common.alchemy;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AlchemyRecipe
{
	private ItemStack output;
	private ItemStack[] recipe;
	private int bloodOrbLevel;
	private int amountNeeded;

	public AlchemyRecipe(ItemStack output, int amountNeeded, ItemStack[] recipe, int bloodOrbLevel)
	{
		this.output = output;
		this.recipe = recipe;
		this.amountNeeded = amountNeeded;
		this.bloodOrbLevel = bloodOrbLevel;
	}

	public boolean doesRecipeMatch(ItemStack[] items, int slottedBloodOrbLevel)
	{
		if (slottedBloodOrbLevel < bloodOrbLevel)
		{
			return false;
		}

		ItemStack[] recipe = new ItemStack[5];

		if (items.length < 5)
		{
			return false;
		}

		if (this.recipe.length != 5)
		{
			ItemStack[] newRecipe = new ItemStack[5];

			for (int i = 0; i < 5; i++)
			{
				if (i + 1 > this.recipe.length)
				{
					newRecipe[i] = null;
				}
				else
				{
					newRecipe[i] = this.recipe[i];
				}
			}

			recipe = newRecipe;
		}
		else
		{
			recipe = this.recipe;
		}

		boolean[] checkList = new boolean[5];

		for (int i = 0; i < 5; i++)
		{
			checkList[i] = false;
		}

		for (int i = 0; i < 5; i++)
		{
			ItemStack recipeItemStack = recipe[i];

			if (recipeItemStack == null)
			{
				continue;
			}

			boolean test = false;

			for (int j = 0; j < 5; j++)
			{
				if (checkList[j])
				{
					continue;
				}

				ItemStack checkedItemStack = items[j];

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
				}
				else if (!(checkedItemStack.getItem() instanceof ItemBlock))
				{
					quickTest = true;
				}

				if (!quickTest)
				{
					continue;
				}

				if ((checkedItemStack.getItemDamage() == recipeItemStack.getItemDamage() || OreDictionary.WILDCARD_VALUE == recipeItemStack.getItemDamage()) && checkedItemStack.itemID == recipeItemStack.itemID)
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
		//		if(slottedBloodOrbLevel<bloodOrbLevel)
			//		{
			//			return false;
			//		}
		//
		//		if(items.length<5)
			//		{
			//			return false;
			//		}
		//
		//		for(int i=0;i<5;i++)
			//		{
			//			ItemStack itemStackR = this.recipe[i];
			//			ItemStack itemStackC = items[i];
			//
			//			if(itemStackR==null&&itemStackC==null)
				//			{
				//				continue;
				//			}
			//
			//			if(itemStackR==null||itemStackC==null)
				//			{
				//				return false;
				//			}
			//
			//			Item itemR = itemStackR.getItem();
			//			Item itemC = itemStackC.getItem();
			//
			//			if(itemR.equals(itemC))
				//			{
				//				continue;
				//			}else
					//			{
					//				return false;
					//			}
			//		}
		//
		//		return true;
	}

	public ItemStack getResult()
	{
		return output.copy();
	}

	public int getAmountNeeded()
	{
		return amountNeeded;
	}

	public ItemStack[] getRecipe()
	{
		return recipe;
	}
}
