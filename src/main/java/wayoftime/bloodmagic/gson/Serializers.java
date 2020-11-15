package wayoftime.bloodmagic.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.will.EnumDemonWillType;

public class Serializers
{
	// Data serializers
	public static final IDataSerializer<EnumDemonWillType> WILL_TYPE_SERIALIZER = new IDataSerializer<EnumDemonWillType>()
	{
		@Override
		public void write(PacketBuffer buf, EnumDemonWillType value)
		{
			buf.writeEnumValue(value);
		}

		@Override
		public EnumDemonWillType read(PacketBuffer buf)
		{
			return buf.readEnumValue(EnumDemonWillType.class);
		}

		@Override
		public DataParameter<EnumDemonWillType> createKey(int id)
		{
			return new DataParameter<>(id, this);
		}

		@Override
		public EnumDemonWillType copyValue(EnumDemonWillType value)
		{
			return EnumDemonWillType.valueOf(value.name());
		}
	};

	// Serializers
	public static final SerializerBase<Direction> FACING_SERIALIZER = new SerializerBase<Direction>()
	{
		@Override
		public Class<Direction> getType()
		{
			return Direction.class;
		}

		@Override
		public Direction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			return Direction.byName(json.getAsString());
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
		public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			String domain = json.getAsJsonObject().get("domain").getAsString();
			String path = json.getAsJsonObject().get("path").getAsString();
			return new ResourceLocation(domain, path);
		}

		@Override
		public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject object = new JsonObject();
			object.addProperty("domain", src.getNamespace());
			object.addProperty("path", src.getPath());
			return object;
		}
	};
	public static final SerializerBase<ItemStack> ITEMMETA_SERIALIZER = new SerializerBase<ItemStack>()
	{
		@Override
		public Class<ItemStack> getType()
		{
			return ItemStack.class;
		}

		@Override
		public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			ResourceLocation registryName = context.deserialize(json.getAsJsonObject().get("registryName").getAsJsonObject(), ResourceLocation.class);
			int meta = json.getAsJsonObject().get("meta").getAsInt();
			return new ItemStack(ForgeRegistries.ITEMS.getValue(registryName), 1);
		}

		@Override
		public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("registryName", context.serialize(src.getItem().getRegistryName()));
			jsonObject.addProperty("meta", src.getDamage());
			return jsonObject;
		}
	};

	public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(FACING_SERIALIZER.getType(), FACING_SERIALIZER).registerTypeAdapter(RESOURCELOCATION_SERIALIZER.getType(), RESOURCELOCATION_SERIALIZER).registerTypeAdapter(ITEMMETA_SERIALIZER.getType(), ITEMMETA_SERIALIZER).create();

	static
	{
		DataSerializers.registerSerializer(WILL_TYPE_SERIALIZER);
	}
}