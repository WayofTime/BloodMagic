package wayoftime.bloodmagic.compat.patchouli.processors;

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
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

public class TartaricForgeProcessor implements IComponentProcessor
{
	private RecipeTartaricForge recipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(id).get();
		if (recipe.getType().equals(BloodMagicRecipeType.TARTARICFORGE))
		{
			this.recipe = (RecipeTartaricForge) recipe;
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Hellfire Forge recipe " + id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipe == null)
		{
			return null;
		} else if (key.equals("input1")) // todo: Handle all inputs in one entry.
		{
			if (recipe.getInput().size() >= 1)
			{
				return IVariable.from(recipe.getInput().get(0).getMatchingStacks()[0]); // todo: how to show all
																						// variants.
			} else
			{
				return null;
			}

		} else if (key.equals("input2"))
		{
			if (recipe.getInput().size() >= 2)
			{
				return IVariable.from(recipe.getInput().get(1).getMatchingStacks()[0]);
			} else
			{
				return null;
			}

		} else if (key.equals("input3"))
		{
			if (recipe.getInput().size() >= 3)
			{
				return IVariable.from(recipe.getInput().get(2).getMatchingStacks()[0]);
			} else
			{
				return null;
			}

		} else if (key.equals("input4"))
		{
			if (recipe.getInput().size() >= 4)
			{
				return IVariable.from(recipe.getInput().get(3).getMatchingStacks()[0]);
			} else
			{
				return null;
			}

		}

		else if (key.equals("output"))
		{
			return IVariable.from(recipe.getOutput());
		} else if (key.equals("willrequired"))
		{
			return IVariable.wrap(recipe.getMinimumSouls());
		} else if (key.equals("willdrain"))
		{
			return IVariable.wrap(recipe.getSoulDrain());
		} else if (key.equals("will"))
		{
			if (recipe.getMinimumSouls() <= 1)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.MONSTER_SOUL_RAW.get()));
			} else if (recipe.getMinimumSouls() <= 64)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.PETTY_GEM.get()));
			} else if (recipe.getMinimumSouls() <= 256)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.LESSER_GEM.get()));
			} else if (recipe.getMinimumSouls() <= 1024)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.COMMON_GEM.get()));
			} else if (recipe.getMinimumSouls() <= 4096)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.GREATER_GEM.get()));
				// } else if (recipe.getMinimumSouls() <= 16384) {
				// return IVariable.from(new ItemStack(BloodMagicItems.GRAND_GEM.get()));
				// }
			} else
			{
				LogManager.getLogger().warn("Could not find a large enough Tartaric Gem for " + recipe.getId());
				return IVariable.from(new ItemStack(Items.BARRIER));

			}
		}
		return null;
	}

}
