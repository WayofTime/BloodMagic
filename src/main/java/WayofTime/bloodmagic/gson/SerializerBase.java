package WayofTime.bloodmagic.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

public abstract class SerializerBase<T> implements JsonDeserializer<T>, JsonSerializer<T> {
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, getType());
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }

    public abstract Class<T> getType();
}
