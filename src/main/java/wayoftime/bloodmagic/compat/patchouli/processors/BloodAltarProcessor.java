package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;

/*
 * Example Page:
 * 
 * {
 *   "type": "bloodmagic:crafting_altar",    // Corresponding Template.
 *   "heading": "Title",    // (Optional) Title.
 *   "recipe": "recipe_id",    // Recipe ID.
 *   "text": "Extra text."    // (Optional) Extra text to go under the entry.
 * },
 */

public class BloodAltarProcessor implements IComponentProcessor
{
	private RecipeBloodAltar recipe;

	@Override
	public void setup(Level level, IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends Recipe<?>> recipeHandler = Minecraft.getInstance().level.getRecipeManager().byKey(id);
		if (recipeHandler.isPresent())
		{
			Recipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.ALTAR.get()))
			{
				this.recipe = (RecipeBloodAltar) recipe;
			}
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Blood Altar recipe {}", id);
		}
	}

	@Override
	public IVariable process(Level level,String key)
	{
		if (recipe == null)
		{
			return null;
		}
		switch (key)
		{
		case "input":
			return IVariable.wrapList(Arrays.stream(recipe.getInput().getItems()).map(IVariable::from).collect(Collectors.toList()));
		case "output":
			return IVariable.from(recipe.getOutput());
		case "tier":
			return IVariable.wrap(recipe.getMinimumTier() + 1);
		case "lp":
			return IVariable.wrap(recipe.getSyphon());
		default:
			return null;
		}
	}

}
