package wayoftime.bloodmagic.api;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.JSONUtils;
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
		return ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, key));
	}

	public static JsonElement serializeItemStack(@Nonnull ItemStack stack)
	{
		JsonObject json = new JsonObject();
		json.addProperty(Constants.JSON.ITEM, stack.getItem().getRegistryName().toString());
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
}
