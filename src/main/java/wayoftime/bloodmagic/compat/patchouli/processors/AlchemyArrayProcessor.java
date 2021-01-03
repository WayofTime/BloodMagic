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
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;

public class AlchemyArrayProcessor implements IComponentProcessor
{
	private RecipeAlchemyArray recipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends IRecipe<?>> recipeHandler = Minecraft.getInstance().world.getRecipeManager().getRecipe(id);
		if (recipeHandler.isPresent())
		{
			IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(id).get();
			if (recipe.getType().equals(BloodMagicRecipeType.ARRAY))
			{
				this.recipe = (RecipeAlchemyArray) recipe;
			}
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Alchemy Array recipe {}", id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipe == null)
		{
			return null;
		}
		switch (key)
		{
		case "baseinput":
			return IVariable.wrapList(Arrays.stream(recipe.getBaseInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "addedinput":
			return IVariable.wrapList(Arrays.stream(recipe.getAddedInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "output":
			return IVariable.from(recipe.getOutput());
		default:
			return null;
		}
	}

}
