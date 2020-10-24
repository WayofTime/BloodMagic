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
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
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
	protected final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
	protected final ResourceLocation serializerName;

	protected BloodMagicRecipeBuilder(ResourceLocation serializerName)
	{
		this.serializerName = serializerName;
	}

	public BUILDER addCriterion(RecipeCriterion criterion)
	{
		return addCriterion(criterion.name, criterion.criterion);
	}

	public BUILDER addCriterion(String name, ICriterionInstance criterion)
	{
		advancementBuilder.withCriterion(name, criterion);
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

	public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id)
	{
		validate(id);
		if (hasCriteria())
		{
			// If there is a way to "unlock" this recipe then add an advancement with the
			// criteria
			advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
		}
		consumer.accept(getResult(id));
	}

	protected abstract class RecipeResult implements IFinishedRecipe
	{

		private final ResourceLocation id;

		public RecipeResult(ResourceLocation id)
		{
			this.id = id;
		}

		@Override
		public JsonObject getRecipeJson()
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
			this.serialize(jsonObject);
			return jsonObject;
		}

		@Nonnull
		@Override
		public IRecipeSerializer<?> getSerializer()
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
		public ResourceLocation getID()
		{
			return this.id;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return hasCriteria() ? advancementBuilder.serialize() : null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
		}
	}
}