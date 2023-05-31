package wayoftime.bloodmagic.recipe.helper;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.util.Constants;

/**
 * Copied liberally from Mekanism. Thanks, pupnewfster!
 *
 */
public class SerializerHelper
{
	private SerializerHelper()
	{
	}

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	private static void validateKey(@Nonnull JsonObject json, @Nonnull String key)
	{
		if (!json.has(key))
		{
			throw new JsonSyntaxException("Missing '" + key + "', expected to find an object");
		}
		if (!json.get(key).isJsonObject())
		{
			throw new JsonSyntaxException("Expected '" + key + "' to be an object");
		}
	}

	public static ItemStack getItemStack(@Nonnull JsonObject json, @Nonnull String key)
	{
		validateKey(json, key);
		return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, key));
	}

	public static JsonElement serializeItemStack(@Nonnull ItemStack stack)
	{
		JsonObject json = new JsonObject();
		json.addProperty(Constants.JSON.ITEM, ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
		if (stack.getCount() > 1)
		{
			json.addProperty(Constants.JSON.COUNT, stack.getCount());
		}
		if (stack.hasTag())
		{
			json.addProperty(Constants.JSON.NBT, stack.getTag().toString());
		}
		return json;
	}

	public static FluidStack getFluidStack(@Nonnull JsonObject json, @Nonnull String key)
	{
		validateKey(json, key);
		return deserializeFluid(GsonHelper.getAsJsonObject(json, key));
	}

	public static FluidStack deserializeFluid(@Nonnull JsonObject json)
	{
		if (!json.has(Constants.JSON.AMOUNT))
		{
			throw new JsonSyntaxException("Expected to receive a amount that is greater than zero");
		}
		JsonElement count = json.get(Constants.JSON.AMOUNT);
		if (!GsonHelper.isNumberValue(count))
		{
			throw new JsonSyntaxException("Expected amount to be a number greater than zero.");
		}
		int amount = count.getAsJsonPrimitive().getAsInt();
		if (amount < 1)
		{
			throw new JsonSyntaxException("Expected amount to be greater than zero.");
		}
		ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, Constants.JSON.FLUID));
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
		if (fluid == null || fluid == Fluids.EMPTY)
		{
			throw new JsonSyntaxException("Invalid fluid type '" + resourceLocation + "'");
		}
		CompoundTag nbt = null;
		if (json.has(Constants.JSON.NBT))
		{
			JsonElement jsonNBT = json.get(Constants.JSON.NBT);
			try
			{
				if (jsonNBT.isJsonObject())
				{
					nbt = TagParser.parseTag(GSON.toJson(jsonNBT));
				} else
				{
					nbt = TagParser.parseTag(GsonHelper.convertToString(jsonNBT, Constants.JSON.NBT));
				}
			} catch (CommandSyntaxException e)
			{
				throw new JsonSyntaxException("Invalid NBT entry for fluid '" + resourceLocation + "'");
			}
		}
		return new FluidStack(fluid, amount, nbt);
	}

	public static JsonElement serializeFluidStack(@Nonnull FluidStack stack)
	{
		JsonObject json = new JsonObject();
		json.addProperty(Constants.JSON.FLUID, ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
		json.addProperty(Constants.JSON.AMOUNT, stack.getAmount());
		if (stack.hasTag())
		{
			json.addProperty(Constants.JSON.NBT, stack.getTag().toString());
		}
		return json;
	}
}
