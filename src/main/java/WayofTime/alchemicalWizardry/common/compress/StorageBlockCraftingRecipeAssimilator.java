package WayofTime.alchemicalWizardry.common.compress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class StorageBlockCraftingRecipeAssimilator 
{
	public List<IRecipe> getPackingOptions() 
	{
		List<IRecipe> packingRecipes = new ArrayList<IRecipe>();
		List<IRecipe> unpackingRecipes = new ArrayList<IRecipe>();
		
		List<IRecipe> returnedRecipes = new ArrayList();

		for (IRecipe recipe : getCraftingRecipes()) {
			ItemStack output = recipe.getRecipeOutput();
			if (output == null || output.getItem() == null) continue;

			if (output.stackSize == 1 && isPossiblePackingRecipe(recipe)) {
				packingRecipes.add(recipe);
			} else if ((output.stackSize == 4 || output.stackSize == 9) && recipe.getRecipeSize() == 1) {
				unpackingRecipes.add(recipe);
			}
		}

		packingRecipes.addAll(unpackingRecipes);
		
		return packingRecipes;
	}

	@SuppressWarnings("unchecked")
	private List<IRecipe> getCraftingRecipes() {
		return CraftingManager.getInstance().getRecipeList();
	}

	private Container makeDummyContainer() {
		return new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer p_75145_1_) {
				return true;
			}
		};
	}

	private boolean isPossiblePackingRecipe(IRecipe recipe) {
		if (recipe.getRecipeSize() < 4) return false;

		List<?> inputs;

		if (recipe instanceof ShapedRecipes) {
			inputs = Arrays.asList(((ShapedRecipes) recipe).recipeItems);
		} else if (recipe instanceof ShapelessRecipes) {
			inputs = ((ShapelessRecipes) recipe).recipeItems;
		} else if (recipe instanceof ShapedOreRecipe) {
			inputs = Arrays.asList(((ShapedOreRecipe) recipe).getInput());
		} else if (recipe instanceof ShapelessOreRecipe) {
			inputs = ((ShapelessOreRecipe) recipe).getInput();
		} else {
			return true;
		}

		return areInputsIdentical(inputs);
	}

	@SuppressWarnings("unchecked")
	private boolean areInputsIdentical(List<?> inputs) {
		List<ItemStack> options = null;

		for (Object input : inputs) {
			if (input == null) continue;

			List<ItemStack> offers;

			if (input instanceof ItemStack) {
				offers = Arrays.asList((ItemStack) input);
			} else if (input instanceof List) {
				offers = (List<ItemStack>) input;

				if (offers.isEmpty()) return false;
			} else {
				throw new RuntimeException("invalid input: "+input.getClass());
			}

			if (options == null) {
				options = new ArrayList<ItemStack>(offers);
				continue;
			}

			for (Iterator<ItemStack> it = options.iterator(); it.hasNext(); ) {
				ItemStack stackReq = it.next();
				boolean found = false;

				for (ItemStack stackCmp : offers) {
					if (areInputsIdentical(stackReq, stackCmp)) {
						found = true;
						break;
					}
				}

				if (!found) {
					it.remove();

					if (options.isEmpty()) return false;
				}
			}
		}

		return true;
	}

	private boolean areInputsIdentical(ItemStack a, ItemStack b) {
		if (a.getItem() != b.getItem()) return false;

		int dmgA = a.getItemDamage();
		int dmgB = b.getItemDamage();

		return dmgA == dmgB || dmgA == OreDictionary.WILDCARD_VALUE || dmgB == OreDictionary.WILDCARD_VALUE;
	}
}