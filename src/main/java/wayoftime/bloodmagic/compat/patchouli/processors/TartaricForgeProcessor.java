package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

/*
 * Example Page:
 * 
 * {
 *   "type": "bloodmagic:crafting_soulforge",    // Corresponding Template.
 *   "heading": "Title",    // (Optional) Title.
 *   "recipe": "recipe_id",    // Recipe ID.
 *   "text": "Extra text."    // (Optional) Extra text to go under the entry.
 * },
 */

public class TartaricForgeProcessor implements IComponentProcessor
{
	private RecipeTartaricForge recipe;

	@Override
	public void setup(Level level, IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		Optional<? extends Recipe<?>> recipeHandler = Minecraft.getInstance().level.getRecipeManager().byKey(id);
		if (recipeHandler.isPresent())
		{
			Recipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.TARTARICFORGE.get()))
			{
				this.recipe = (RecipeTartaricForge) recipe;
			}
		}
		if (this.recipe == null)
		{
			LogManager.getLogger().warn("Guidebook missing Hellfire Forge recipe {}", id);
		}
	}

	@Override
	public IVariable process(Level level,String key)
	{
		if (recipe == null)
		{
			return null;
		}
		if (key.startsWith("input"))
		{
			int index = Integer.parseInt(key.substring(5)) - 1;
			if (recipe.getInput().size() > index)
			{
				return IVariable.wrapList(Arrays.stream(recipe.getInput().get(index).getItems()).map(IVariable::from).collect(Collectors.toList()));
			} else
			{
				return null;
			}
		}
		switch (key)
		{
		case "output":
			return IVariable.from(recipe.getOutput());
		case "willrequired":
			return IVariable.wrap(recipe.getMinimumSouls());
		case "willdrain":
			return IVariable.wrap(recipe.getSoulDrain());
		case "will":
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
				LogManager.getLogger().warn("Guidebook could not find a large enough Tartaric Gem for {}", recipe.getId());
				return IVariable.from(new ItemStack(Items.BARRIER));

			}
		default:
			return null;
		}
	}

}
