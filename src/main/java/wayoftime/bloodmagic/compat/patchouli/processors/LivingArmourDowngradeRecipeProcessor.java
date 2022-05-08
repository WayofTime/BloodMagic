package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;

/*
 * Example Page:
 * 
 * {
 *   "type": "crafting_downgrade",    // Corresponding Template.
 *   "heading": "Title",    // (Optional) Title.
 *   "recipe": "recipe_id",    // Recipe ID.
 *   "text":  "Extra text."    //(Optional) Extra text to go under the entry.
 * },
 */

public class LivingArmourDowngradeRecipeProcessor implements IComponentProcessor
{
	private RecipeLivingDowngrade recipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends IRecipe<?>> recipeHandler = Minecraft.getInstance().world.getRecipeManager().getRecipe(id);
		if (recipeHandler.isPresent())
		{
			IRecipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.LIVINGDOWNGRADE))
			{
				this.recipe = (RecipeLivingDowngrade) recipe;
			}
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Blood Altar recipe{}", id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipe == null)
		{
			return null;
		}
		if (key.equals("input"))
			return IVariable.wrapList(Arrays.stream(recipe.getInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		else
			return null;
	}
}
