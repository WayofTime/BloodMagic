package WayofTime.bloodmagic.gson;

import java.lang.reflect.Type;

import net.minecraft.util.EnumFacing;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EnumFacingTypeAdapter implements JsonDeserializer<EnumFacing>, JsonSerializer<EnumFacing>
{
    @Override
    public EnumFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        String str = json.getAsString();

        return EnumFacing.byName(str);
    }

    @Override
    public JsonElement serialize(EnumFacing src, Type typeOfSrc, JsonSerializationContext context)
    {
        // Not necessary, since this is only used for deserialization.
        return null;
    }
}
