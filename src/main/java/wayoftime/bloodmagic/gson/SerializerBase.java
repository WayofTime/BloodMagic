package wayoftime.bloodmagic.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public abstract class SerializerBase<T> implements JsonDeserializer<T>, JsonSerializer<T>
{
	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		return context.deserialize(json, getType());
	}

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
	{
		return context.serialize(src);
	}

	public abstract Class<T> getType();
}
