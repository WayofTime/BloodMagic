package wayoftime.bloodmagic.common.data.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.Constants;

public abstract class BloodMagicRecipeBuilder<BUILDER extends BloodMagicRecipeBuilder<BUILDER>>
{

	protected static ResourceLocation bmSerializer(String name)
	{
		return new ResourceLocation(BloodMagic.MODID, name);
	}

	protected final List<ICondition> conditions = new ArrayList<>();
	protected final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
	protected final ResourceLocation serializerName;

	protected BloodMagicRecipeBuilder(ResourceLocation serializerName)
	{
		this.serializerName = serializerName;
	}

	public BUILDER addCriterion(RecipeCriterion criterion)
	{
		return addCriterion(criterion.name, criterion.criterion);
	}

	public BUILDER addCriterion(String name, CriterionTriggerInstance criterion)
	{
		advancementBuilder.addCriterion(name, criterion);
		return (BUILDER) this;
	}

	public BUILDER addCondition(ICondition condition)
	{
		conditions.add(condition);
		return (BUILDER) this;
	}

	protected boolean hasCriteria()
	{
		return !advancementBuilder.getCriteria().isEmpty();
	}

	protected abstract RecipeResult getResult(ResourceLocation id);

	protected void validate(ResourceLocation id)
	{
	}

	public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id)
	{
		validate(id);
		if (hasCriteria())
		{
			// If there is a way to "unlock" this recipe then add an advancement with the
			// criteria
			advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		}
		consumer.accept(getResult(id));
	}

	protected abstract class RecipeResult implements FinishedRecipe
	{

		private final ResourceLocation id;

		public RecipeResult(ResourceLocation id)
		{
			this.id = id;
		}

		@Override
		public JsonObject serializeRecipe()
		{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(Constants.JSON.TYPE, serializerName.toString());
			if (!conditions.isEmpty())
			{
				JsonArray conditionsArray = new JsonArray();
				for (ICondition condition : conditions)
				{
					conditionsArray.add(CraftingHelper.serialize(condition));
				}
				jsonObject.add(Constants.JSON.CONDITIONS, conditionsArray);
			}
			this.serializeRecipeData(jsonObject);
			return jsonObject;
		}

		@Nonnull
		@Override
		public RecipeSerializer<?> getType()
		{
			// Note: This may be null if something is screwed up but this method isn't
			// actually used so it shouldn't matter
			// and in fact it will probably be null if only the API is included. But again,
			// as we manually just use
			// the serializer's name this should not effect us
			return ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
		}

		@Nonnull
		@Override
		public ResourceLocation getId()
		{
			return this.id;
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return hasCriteria() ? advancementBuilder.serializeToJson() : null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
		}
	}
}