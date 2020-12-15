package wayoftime.bloodmagic.compat.patchouli.processors;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;

public class AltarProcessor implements IComponentProcessor
{
	private RecipeBloodAltar recipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(id).get();
		if (recipe.getType().equals(BloodMagicRecipeType.ALTAR))
		{
			this.recipe = (RecipeBloodAltar) recipe;
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Blood Altar recipe " + id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (recipe == null)
		{
			return null;
		} else if (key.equals("input"))
		{
			return IVariable.from(recipe.getInput().getMatchingStacks()[0]);

		} else if (key.equals("output"))
		{
			return IVariable.from(recipe.getOutput());
		} else if (key.equals("tier"))
		{
			return IVariable.wrap(recipe.getMinimumTier() + 1);
		} else if (key.equals("LP"))
		{
			return IVariable.wrap(recipe.getSyphon());
		}
		return null;
	}

}
