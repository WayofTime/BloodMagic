package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;

public class AlchemyTableProcessor implements IComponentProcessor
{
	private List<RecipeAlchemyTable> recipes;
	private int recipeIndex;

	@Override
	public void setup(IVariableProvider variables)
	{
		List<String> names = variables.get("recipes").asStream().map(IVariable::asString).collect(Collectors.toList());
		this.recipes = new ArrayList<>();
		for (String name : names)
		{
			IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(new ResourceLocation(name)).get();
			if (recipe.getType().equals(BloodMagicRecipeType.ALCHEMYTABLE))
			{
				recipes.add((RecipeAlchemyTable) recipe);

			} else
			{
				LogManager.getLogger().warn("Guidebook does not recognize " + name + " as an Alchemy Table recipe.");
			}
		}
		if (recipes.size() > 3)
		{
			LogManager.getLogger().warn("Guidebook was given too many Alchemy Table recipes on page starting with " + recipes.get(0).getId() + ". Max is 3.");
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipes.isEmpty())
		{
			return null;
		}
		if (key.startsWith("input"))
		{
			if (indexManager(key, "input".length()))
			{
				int ingredientIndex = Integer.parseInt(key.substring("input".length() + 1)) - 1;
				if (recipes.get(recipeIndex).getInput().size() > ingredientIndex)
				{
					return IVariable.wrapList(Arrays.stream(recipes.get(recipeIndex).getInput().get(ingredientIndex).getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
				}
			}
			return null;
		}
		if (key.startsWith("output"))
		{
			if (indexManager(key, "output".length()))
			{
				return IVariable.from(recipes.get(recipeIndex).getOutput());
			}
		}
		if (key.startsWith("orb"))
		{
			if (indexManager(key, "orb".length()))
			{
				switch (recipes.get(recipeIndex).getMinimumTier())
				{
				case 0: // same as case 1.
				case 1:
					return IVariable.from(new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()));
				case 2:
					return IVariable.from(new ItemStack(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()));
				case 3:
					return IVariable.from(new ItemStack(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()));
				case 4:
					return IVariable.from(new ItemStack(BloodMagicItems.MASTER_BLOOD_ORB.get()));
				// case 5: return IVariable.from(new
				// ItemStack(BloodMagicItems.ARCHMAGES_BLOOD_ORB.get()));
				// case 6: return IVariable.from(new
				// ItemStack(BloodMagicItems.TRANSCENDENT_BLOOD_ORB.get()));
				default:
					LogManager.getLogger().warn("Guid couldn't find sufficient Blood Orb for " + recipes.get(recipeIndex).getId());
					return IVariable.from(new ItemStack(Items.BARRIER));
				}
			}
		}
		if (key.startsWith("show"))
		{
			return IVariable.wrap(Integer.parseInt(key.substring("show".length())) <= recipes.size());
		}
		if (key.startsWith("syphon"))
		{
			if (indexManager(key, "syphon".length()))
			{
				return IVariable.wrap(recipes.get(recipeIndex).getSyphon());
			}
		}
		if (key.startsWith("time"))
		{
			if (indexManager(key, "time".length()))
			{
				return IVariable.wrap(recipes.get(recipeIndex).getTicks());
			}
		}

		return null;
	}

	private boolean indexManager(String key, int length)
	{ // sets and validates recipeIndex.
		recipeIndex = Integer.parseInt(key.substring(length, length + 1)) - 1;
		if (recipes.size() > recipeIndex)
		{
			return true;
		} else
		{
			return false;
		}
	}

}
