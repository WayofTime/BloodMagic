package WayofTime.bloodmagic.gson;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Type;

public class Serializers {
    // Data serializers
    public static final DataSerializer<EnumDemonWillType> WILL_TYPE_SERIALIZER = new DataSerializer<EnumDemonWillType>() {
        @Override
        public void write(PacketBuffer buf, EnumDemonWillType value) {
            buf.writeEnumValue(value);
        }

        @Override
        public EnumDemonWillType read(PacketBuffer buf) {
            return buf.readEnumValue(EnumDemonWillType.class);
        }

        @Override
        public DataParameter<EnumDemonWillType> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public EnumDemonWillType copyValue(EnumDemonWillType value) {
            return EnumDemonWillType.valueOf(value.name());
        }
    };

    // Serializers
    public static final SerializerBase<EnumFacing> FACING_SERIALIZER = new SerializerBase<EnumFacing>() {
        @Override
        public Class<EnumFacing> getType() {
            return EnumFacing.class;
        }

        @Override
        public EnumFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return EnumFacing.byName(json.getAsString());
        }
    };
    public static final SerializerBase<ResourceLocation> RESOURCELOCATION_SERIALIZER = new SerializerBase<ResourceLocation>() {
        @Override
        public Class<ResourceLocation> getType() {
            return ResourceLocation.class;
        }

        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String domain = json.getAsJsonObject().get("domain").getAsString();
            String path = json.getAsJsonObject().get("path").getAsString();
            return new ResourceLocation(domain, path);
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("domain", src.getNamespace());
            object.addProperty("path", src.getPath());
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

    public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(FACING_SERIALIZER.getType(), FACING_SERIALIZER).registerTypeAdapter(RESOURCELOCATION_SERIALIZER.getType(), RESOURCELOCATION_SERIALIZER).registerTypeAdapter(ITEMMETA_SERIALIZER.getType(), ITEMMETA_SERIALIZER).create();

    static {
        DataSerializers.registerSerializer(WILL_TYPE_SERIALIZER);
    }
}
