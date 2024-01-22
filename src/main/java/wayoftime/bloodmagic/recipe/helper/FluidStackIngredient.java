package wayoftime.bloodmagic.recipe.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import wayoftime.bloodmagic.util.Constants;

/**
 * Created by Thiakil on 12/07/2019.
 */
public abstract class FluidStackIngredient implements InputIngredient<FluidStack>
{

	public static FluidStackIngredient from(@Nonnull Fluid instance, int amount)
	{
		return from(new FluidStack(instance, amount));
	}

	public static FluidStackIngredient from(@Nonnull FluidStack instance)
	{
		return new Single(instance);
	}

	public static FluidStackIngredient from(@Nonnull TagKey<Fluid> fluidTag, int minAmount)
	{
		return new Tagged(fluidTag, minAmount);
	}

	public static FluidStackIngredient read(FriendlyByteBuf buffer)
	{
		// TODO: Allow supporting serialization of different types than just the ones we
		// implement?
		IngredientType type = buffer.readEnum(IngredientType.class);
		if (type == IngredientType.SINGLE)
		{
			return Single.read(buffer);
		} else if (type == IngredientType.TAGGED)
		{
			return Tagged.read(buffer);
		}
		return Multi.read(buffer);
	}

	public static FluidStackIngredient deserialize(@Nullable JsonElement json)
	{
		if (json == null || json.isJsonNull())
		{
			throw new JsonSyntaxException("Ingredient cannot be null");
		}
		if (json.isJsonArray())
		{
			JsonArray jsonArray = json.getAsJsonArray();
			int size = jsonArray.size();
			if (size == 0)
			{
				throw new JsonSyntaxException("Ingredient array cannot be empty, at least one ingredient must be defined");
			} else if (size > 1)
			{
				FluidStackIngredient[] ingredients = new FluidStackIngredient[size];
				for (int i = 0; i < size; i++)
				{
					// Read all the ingredients
					ingredients[i] = deserialize(jsonArray.get(i));
				}
				return createMulti(ingredients);
			}
			// If we only have a single element, just set our json as that so that we don't
			// have to use Multi for efficiency reasons
			json = jsonArray.get(0);
		}
		if (!json.isJsonObject())
		{
			throw new JsonSyntaxException("Expected fluid to be object or array of objects");
		}
		JsonObject jsonObject = json.getAsJsonObject();
		if (jsonObject.has(Constants.JSON.FLUID) && jsonObject.has(Constants.JSON.TAG))
		{
			throw new JsonParseException("An ingredient entry is either a tag or an fluid, not both");
		} else if (jsonObject.has(Constants.JSON.FLUID))
		{
			return from(SerializerHelper.deserializeFluid(jsonObject));
		} else if (jsonObject.has(Constants.JSON.TAG))
		{
			if (!jsonObject.has(Constants.JSON.AMOUNT))
			{
				throw new JsonSyntaxException("Expected to receive a amount that is greater than zero");
			}
			JsonElement count = jsonObject.get(Constants.JSON.AMOUNT);
			if (!GsonHelper.isNumberValue(count))
			{
				throw new JsonSyntaxException("Expected amount to be a number greater than zero.");
			}
			int amount = count.getAsJsonPrimitive().getAsInt();
			if (amount < 1)
			{
				throw new JsonSyntaxException("Expected amount to be greater than zero.");
			}
			ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, Constants.JSON.TAG));

			ITagManager<Fluid> tagManager = ForgeRegistries.FLUIDS.tags();

			TagKey<Fluid> tag = tagManager.createTagKey(resourceLocation);
			if (tag == null)
			{
				throw new JsonSyntaxException("Unknown fluid tag '" + resourceLocation + "'");
			}
			return from(tag, amount);
		}
		throw new JsonSyntaxException("Expected to receive a resource location representing either a tag or a fluid.");
	}

	public static FluidStackIngredient createMulti(FluidStackIngredient... ingredients)
	{
		if (ingredients.length == 0)
		{
			// TODO: Throw error
		} else if (ingredients.length == 1)
		{
			return ingredients[0];
		}
		List<FluidStackIngredient> cleanedIngredients = new ArrayList<>();
		for (FluidStackIngredient ingredient : ingredients)
		{
			if (ingredient instanceof Multi)
			{
				// Don't worry about if our inner ingredients are multi as well, as if this is
				// the only external method for
				// creating a multi ingredient, then we are certified they won't be of a higher
				// depth
				cleanedIngredients.addAll(Arrays.asList(((Multi) ingredient).ingredients));
			} else
			{
				cleanedIngredients.add(ingredient);
			}
		}
		// There should be more than a single fluid or we would have split out earlier
		return new Multi(cleanedIngredients.toArray(new FluidStackIngredient[0]));
	}

	public static class Single extends FluidStackIngredient
	{

		@Nonnull
		private final FluidStack fluidInstance;

		public Single(@Nonnull FluidStack fluidInstance)
		{
			this.fluidInstance = Objects.requireNonNull(fluidInstance);
		}

		@Override
		public boolean test(@Nonnull FluidStack fluidStack)
		{
			return testType(fluidStack) && fluidStack.getAmount() >= fluidInstance.getAmount();
		}

		@Override
		public boolean testType(@Nonnull FluidStack fluidStack)
		{
			return Objects.requireNonNull(fluidStack).isFluidEqual(fluidInstance);
		}

		@Nonnull
		@Override
		public FluidStack getMatchingInstance(@Nonnull FluidStack fluidStack)
		{
			return test(fluidStack) ? fluidInstance : FluidStack.EMPTY;
		}

		@Nonnull
		@Override
		public List<FluidStack> getRepresentations()
		{
			return Collections.singletonList(fluidInstance);
		}

		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeEnum(IngredientType.SINGLE);
			fluidInstance.writeToPacket(buffer);
		}

		@Nonnull
		@Override
		public JsonElement serialize()
		{
			JsonObject json = new JsonObject();
			json.addProperty(Constants.JSON.AMOUNT, fluidInstance.getAmount());
			json.addProperty(Constants.JSON.FLUID, ForgeRegistries.FLUIDS.getKey(fluidInstance.getFluid()).toString());
			if (fluidInstance.hasTag())
			{
				json.addProperty(Constants.JSON.NBT, fluidInstance.getTag().toString());
			}
			return json;
		}

		public static Single read(FriendlyByteBuf buffer)
		{
			return new Single(FluidStack.readFromPacket(buffer));
		}
	}

	public static class Tagged extends FluidStackIngredient
	{
		private final ITag<Fluid> tag;
		private final int amount;

		private Tagged(TagKey<Fluid> tag, int amount)
		{
//			this(TagUtils.tag(ForgeRegistries.FLUIDS, tag), amount);
			this(ForgeRegistries.FLUIDS.tags().getTag(tag), amount);
		}

		private Tagged(ITag<Fluid> tag, int amount)
		{
			this.tag = tag;
			this.amount = amount;
		}

		@Override
		public boolean test(FluidStack fluidStack)
		{
			return testType(fluidStack) && fluidStack.getAmount() >= amount;
		}

		@Override
		public boolean testType(FluidStack fluidStack)
		{
			return tag.contains(Objects.requireNonNull(fluidStack).getFluid());
		}

		@Override
		public FluidStack getMatchingInstance(FluidStack fluidStack)
		{
			if (test(fluidStack))
			{
				// Our fluid is in the tag, so we make a new stack with the given amount
				return new FluidStack(fluidStack, amount);
			}
			return FluidStack.EMPTY;
		}

		@Override
		public List<@NonNull FluidStack> getRepresentations()
		{
			// TODO: Can this be cached some how
			List<@NonNull FluidStack> representations = new ArrayList<>();
			for (Fluid fluid : tag)
			{
				representations.add(new FluidStack(fluid, amount));
			}
			return representations;
		}

		/**
		 * For use in recipe input caching.
		 */
		public Iterable<Fluid> getRawInput()
		{
			return tag;
		}

		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeEnum(IngredientType.TAGGED);
			buffer.writeResourceLocation(tag.getKey().location());
			buffer.writeVarInt(amount);
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject json = new JsonObject();
			json.addProperty(Constants.JSON.AMOUNT, amount);
			json.addProperty(Constants.JSON.TAG, tag.getKey().location().toString());
			return json;
		}
//		@Nonnull
//		private final TagKey<Fluid> tag;
//		private final int amount;
//
//		public Tagged(@Nonnull TagKey<Fluid> tag, int amount)
//		{
//			this.tag = tag;
//			this.amount = amount;
//		}
//
//		@Override
//		public boolean test(@Nonnull FluidStack fluidStack)
//		{
//			return testType(fluidStack) && fluidStack.getAmount() >= amount;
//		}
//
//		@Override
//		public boolean testType(@Nonnull FluidStack fluidStack)
//		{
//			return Objects.requireNonNull(fluidStack).getFluid().is(tag);
//		}
//
//		@Nonnull
//		@Override
//		public FluidStack getMatchingInstance(@Nonnull FluidStack fluidStack)
//		{
//			if (test(fluidStack))
//			{
//				// Our fluid is in the tag so we make a new stack with the given amount
//				return new FluidStack(fluidStack, amount);
//			}
//			return FluidStack.EMPTY;
//		}
//
//		@Nonnull
//		@Override
//		public List<FluidStack> getRepresentations()
//		{
//			// TODO: Can this be cached some how
//			List<FluidStack> representations = new ArrayList<>();
//			for (Fluid fluid : TagResolverHelper.getRepresentations(tag))
//			{
//				representations.add(new FluidStack(fluid, amount));
//			}
//			return representations;
//		}
//
//		@Override
//		public void write(FriendlyByteBuf buffer)
//		{
//			buffer.writeEnum(IngredientType.TAGGED);
//			buffer.writeResourceLocation(SerializationTags.getInstance().getFluids().getIdOrThrow(tag));
//			buffer.writeVarInt(amount);
//		}
//
//		@Nonnull
//		@Override
//		public JsonElement serialize()
//		{
//			JsonObject json = new JsonObject();
//			json.addProperty(Constants.JSON.AMOUNT, amount);
//			json.addProperty(Constants.JSON.TAG, SerializationTags.getInstance().getFluids().getIdOrThrow(tag).toString());
//			return json;
//		}
//
//		public static Tagged read(FriendlyByteBuf buffer)
//		{
//			return new Tagged(FluidTags.bind(buffer.readResourceLocation().toString()), buffer.readVarInt());
//		}
	}

	public static class Multi extends FluidStackIngredient
	{

		private final FluidStackIngredient[] ingredients;

		protected Multi(@Nonnull FluidStackIngredient... ingredients)
		{
			this.ingredients = ingredients;
		}

		@Override
		public boolean test(@Nonnull FluidStack stack)
		{
			return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.test(stack));
		}

		@Override
		public boolean testType(@Nonnull FluidStack stack)
		{
			return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.testType(stack));
		}

		@Nonnull
		@Override
		public FluidStack getMatchingInstance(@Nonnull FluidStack stack)
		{
			for (FluidStackIngredient ingredient : ingredients)
			{
				FluidStack matchingInstance = ingredient.getMatchingInstance(stack);
				if (!matchingInstance.isEmpty())
				{
					return matchingInstance;
				}
			}
			return FluidStack.EMPTY;
		}

		@Nonnull
		@Override
		public List<FluidStack> getRepresentations()
		{
			List<FluidStack> representations = new ArrayList<>();
			for (FluidStackIngredient ingredient : ingredients)
			{
				representations.addAll(ingredient.getRepresentations());
			}
			return representations;
		}

		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeEnum(IngredientType.MULTI);
			buffer.writeVarInt(ingredients.length);
			for (FluidStackIngredient ingredient : ingredients)
			{
				ingredient.write(buffer);
			}
		}

		@Nonnull
		@Override
		public JsonElement serialize()
		{
			JsonArray json = new JsonArray();
			for (FluidStackIngredient ingredient : ingredients)
			{
				json.add(ingredient.serialize());
			}
			return json;
		}

		public static FluidStackIngredient read(FriendlyByteBuf buffer)
		{
			FluidStackIngredient[] ingredients = new FluidStackIngredient[buffer.readVarInt()];
			for (int i = 0; i < ingredients.length; i++)
			{
				ingredients[i] = FluidStackIngredient.read(buffer);
			}
			return createMulti(ingredients);
		}
	}

	private enum IngredientType
	{
		SINGLE,
		TAGGED,
		MULTI
	}
}