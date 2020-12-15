package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
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
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

public class ForgeAndArrayProcessor implements IComponentProcessor
{
	private RecipeAlchemyArray arecipe;
	private RecipeTartaricForge frecipe;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation aid = new ResourceLocation(variables.get("recipe_array").asString());
		IRecipe<?> arecipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(aid).get();
		if (arecipe.getType().equals(BloodMagicRecipeType.ARRAY))
		{
			this.arecipe = (RecipeAlchemyArray) arecipe;
		}
		if (this.arecipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Alchemy Array recipe " + aid);
		}

		ResourceLocation fid = new ResourceLocation(variables.get("recipe_forge").asString());
		IRecipe<?> frecipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(fid).get();
		if (frecipe.getType().equals(BloodMagicRecipeType.TARTARICFORGE))
		{
			this.frecipe = (RecipeTartaricForge) frecipe;
		}
		if (this.frecipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Hellfire Forge recipe " + fid);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (arecipe == null || frecipe == null)
		{
			return null;
		}
		if (key.startsWith("forge_input"))
		{
			int index = Integer.parseInt(key.substring(11)) - 1;
			if (frecipe.getInput().size() > index)
			{
				return IVariable.wrapList(Arrays.stream(frecipe.getInput().get(index).getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
			} else
			{
				return null;
			}
		}
		switch (key)
		{
		case "forge_output":
			return IVariable.from(frecipe.getOutput());
		case "forge_willrequired":
			return IVariable.wrap(frecipe.getMinimumSouls());
		case "forge_willdrain":
			return IVariable.wrap(frecipe.getSoulDrain());
		case "forge_will":
			if (frecipe.getMinimumSouls() <= 1)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.MONSTER_SOUL_RAW.get()));
			} else if (frecipe.getMinimumSouls() <= 64)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.PETTY_GEM.get()));
			} else if (frecipe.getMinimumSouls() <= 256)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.LESSER_GEM.get()));
			} else if (frecipe.getMinimumSouls() <= 1024)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.COMMON_GEM.get()));
			} else if (frecipe.getMinimumSouls() <= 4096)
			{
				return IVariable.from(new ItemStack(BloodMagicItems.GREATER_GEM.get()));
				// } else if (frecipe.getMinimumSouls() <= 16384) {
				// return IVariable.from(new ItemStack(BloodMagicItems.GRAND_GEM.get()));
				// }
			} else
			{
				LogManager.getLogger().warn("Guidebook could not find a large enough Tartaric Gem for " + frecipe.getId());
				return IVariable.from(new ItemStack(Items.BARRIER));

			}
		case "array_baseinput":
			return IVariable.wrapList(Arrays.stream(arecipe.getBaseInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "array_addedinput":
			return IVariable.wrapList(Arrays.stream(arecipe.getAddedInput().getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		case "array_output":
			return IVariable.from(arecipe.getOutput());
		default:
			return null;
		}
	}

}
