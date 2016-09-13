package WayofTime.bloodmagic.gson;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Type;

public class Serializers
{
    // Serializers
    public static final SerializerBase<EnumFacing> FACING_SERIALIZER = new SerializerBase<EnumFacing>()
    {
        @Override
        public Class<EnumFacing> getType()
        {
            return EnumFacing.class;
        }

        @Override
        public EnumFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return EnumFacing.byName(json.getAsString());
        }
    };
    public static final SerializerBase<ResourceLocation> RESOURCELOCATION_SERIALIZER = new SerializerBase<ResourceLocation>()
    {
        @Override
        public Class<ResourceLocation> getType()
        {
            return ResourceLocation.class;
        }

        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            String domain = json.getAsJsonObject().get("domain").getAsString();
            String path = json.getAsJsonObject().get("path").getAsString();
            return new ResourceLocation(domain, path);
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject object = new JsonObject();
            object.addProperty("domain", src.getResourceDomain());
            object.addProperty("path", src.getResourcePath());
            return object;
        }
    };
    public static final SerializerBase<ItemStack> ITEMMETA_SERIALIZER = new SerializerBase<ItemStack>() {
        @Override
        public Class<ItemStack> getType() {
            return ItemStack.class;
        }

        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ResourceLocation registryName = context.deserialize(json.getAsJsonObject().get("registryName").getAsJsonObject(), ResourceLocation.class);
            int meta = json.getAsJsonObject().get("meta").getAsInt();
            return new ItemStack(ForgeRegistries.ITEMS.getValue(registryName), 1, meta);
        }

        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("registryName", context.serialize(src.getItem().getRegistryName()));
            jsonObject.addProperty("meta", src.getItemDamage());
            return jsonObject;
        }
    };

    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(FACING_SERIALIZER.getType(), FACING_SERIALIZER)
            .registerTypeAdapter(RESOURCELOCATION_SERIALIZER.getType(), RESOURCELOCATION_SERIALIZER)
            .registerTypeAdapter(ITEMMETA_SERIALIZER.getType(), ITEMMETA_SERIALIZER)
            .create();
}
