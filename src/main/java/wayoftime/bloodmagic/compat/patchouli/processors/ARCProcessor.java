package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ARCProcessor implements IComponentProcessor
{
	private RecipeARC recipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends IRecipe<?>> recipeHandler = Minecraft.getInstance().world.getRecipeManager().getRecipe(id);
		if (recipeHandler.isPresent())
		{
			IRecipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.ARC))
			{
				this.recipe = (RecipeARC) recipe;
			}
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Alchemical Reaction Chamber recipe {}", id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipe == null)
		{
			return null;
		} else if (key.startsWith("output"))
		{
			int index = Integer.parseInt(key.substring(6)) - 1;
			if (recipe.getAllListedOutputs().size() > index)
			{
				return IVariable.from(recipe.getAllListedOutputs().get(index));
			} else
			{
				return null;
			}
		} else if (key.startsWith("chance"))
		{
			int index = Integer.parseInt(key.substring(6)) - 2; // Index 0 = 2nd output.
			if (recipe.getAllOutputChances().length > index)
			{
				double chance = recipe.getAllOutputChances()[index] * 100;
				if (chance < 1)
				{
					return IVariable.wrap("<1");
				}
				return IVariable.wrap(Math.round(chance));
			}
		} else if (key.startsWith("show_chance"))
		{
			int index = Integer.parseInt(key.substring(11)) - 2; // Index 0 = 2nd output.
			if (recipe.getAllOutputChances().length > index)
			{
				return IVariable.wrap(true);
			}
		}
		switch (key)
		{
		case "show_fluid_tooltip":
		{
			if (recipe.getFluidIngredient() != null || recipe.getFluidOutput() != FluidStack.EMPTY)
			{
				return IVariable.wrap(true);
			}
			return IVariable.wrap(false);
		}
		case "input":
			return IVariable.wrapList(Arrays.stream(recipe.getInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "tool":
			return IVariable.wrapList(Arrays.stream(recipe.getTool().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "tooltip_fluid_input":
		{
			if (recipe.getFluidIngredient() != null)
			{
				FluidStack fluid = recipe.getFluidIngredient().getRepresentations().get(0);
				String i18nFluidName = TextHelper.localize(fluid.getTranslationKey());
				return IVariable.wrap(TextHelper.localize("patchouli.bloodmagic.arc_processor.fluid", fluid.getAmount(), i18nFluidName));
			} else
			{
				return IVariable.wrap(TextHelper.localize("patchouli.bloodmagic.arc_processor.no_fluid"));
			}
		}
		case "tooltip_fluid_output":
		{
			if (recipe.getFluidOutput() != FluidStack.EMPTY)
			{
				FluidStack fluid = recipe.getFluidOutput();
				String i18nFluidName = TextHelper.localize(fluid.getTranslationKey());
				return IVariable.wrap(TextHelper.localize("patchouli.bloodmagic.arc_processor.fluid", fluid.getAmount(), i18nFluidName));
			} else
			{
				return IVariable.wrap(TextHelper.localize("patchouli.bloodmagic.arc_processor.no_fluid"));
			}
		}
		default:
			return null;
		}
	}
}
