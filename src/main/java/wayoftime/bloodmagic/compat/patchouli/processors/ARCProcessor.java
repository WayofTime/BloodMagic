package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.util.helper.TextHelper;

/*
 * Example Page:
 * 
 * {
 *   "type": "bloodmagic:crafting_arc",    // Corresponding Template.
 *   "heading": "Title",    // (Optional) Title.
 *   "recipe": "recipe_id",    // Recipe ID.
 *   "text": "Extra text."    // (Optional) Extra text to go under the entry.
 * },
 */

public class ARCProcessor implements IComponentProcessor
{
	private RecipeARC recipe;

	@Override
	public void setup(Level level, IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends Recipe<?>> recipeHandler = Minecraft.getInstance().level.getRecipeManager().byKey(id);
		if (recipeHandler.isPresent())
		{
			Recipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.ARC.get()))
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
	public IVariable process(Level level,String key)
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
			if (recipe.getAllOutputChances().size() > index)
			{
				Pair<Double, Double> chances = recipe.getAllOutputChances().get(index);
				double chance = (chances.getLeft() + chances.getRight()) * 100;
				if (chance < 1)
				{
					return IVariable.wrap("<1");
				}
				return IVariable.wrap(Math.round(chance));
			}
		} else if (key.startsWith("show_chance"))
		{
			int index = Integer.parseInt(key.substring(11)) - 2; // Index 0 = 2nd output.
			if (recipe.getAllOutputChances().size() > index)
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
			return IVariable.wrapList(Arrays.stream(recipe.getInput().getItems()).map(IVariable::from).collect(Collectors.toList()));
		case "tool":
			return IVariable.wrapList(Arrays.stream(recipe.getTool().getItems()).map(IVariable::from).collect(Collectors.toList()));
		case "tooltip_fluid_input":
		{
			if (recipe.getFluidIngredient() != null)
			{
				FluidStack fluid = recipe.getFluidIngredient().getRepresentations().get(0);
				String i18nFluidName = TextHelper.localize(fluid.getTranslationKey());
				return IVariable.wrap(TextHelper.localize("guide.patchouli.bloodmagic.arc_processor.fluid", fluid.getAmount(), i18nFluidName));
			} else
			{
				return IVariable.wrap(TextHelper.localize("guide.patchouli.bloodmagic.arc_processor.no_fluid"));
			}
		}
		case "tooltip_fluid_output":
		{
			if (recipe.getFluidOutput() != FluidStack.EMPTY)
			{
				FluidStack fluid = recipe.getFluidOutput();
				String i18nFluidName = TextHelper.localize(fluid.getTranslationKey());
				return IVariable.wrap(TextHelper.localize("guide.patchouli.bloodmagic.arc_processor.fluid", fluid.getAmount(), i18nFluidName));
			} else
			{
				return IVariable.wrap(TextHelper.localize("guide.patchouli.bloodmagic.arc_processor.no_fluid"));
			}
		}
		default:
			return null;
		}
	}
}
