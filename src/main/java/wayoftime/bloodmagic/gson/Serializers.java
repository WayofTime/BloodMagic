package wayoftime.bloodmagic.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class Serializers
{
	// Data serializers
	public static final EntityDataSerializer<EnumDemonWillType> WILL_TYPE_SERIALIZER = new EntityDataSerializer<EnumDemonWillType>()
	{
		@Override
		public void write(FriendlyByteBuf buf, EnumDemonWillType value)
		{
			buf.writeEnum(value);
		}

		@Override
		public EnumDemonWillType read(FriendlyByteBuf buf)
		{
			return buf.readEnum(EnumDemonWillType.class);
		}

		@Override
		public EntityDataAccessor<EnumDemonWillType> createAccessor(int id)
		{
			return new EntityDataAccessor<>(id, this);
		}

		@Override
		public EnumDemonWillType copy(EnumDemonWillType value)
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
			jsonObject.add("registryName", context.serialize(ForgeRegistries.ITEMS.getKey(src.getItem())));
			jsonObject.addProperty("meta", src.getDamageValue());
			return jsonObject;
		}
	};
	public static final SerializerBase<BlockPos> BLOCKPOS_SERIALIZER = new SerializerBase<BlockPos>()
	{
		@Override
		public Class<BlockPos> getType()
		{
			return BlockPos.class;
		}

		@Override
		public BlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			int x = json.getAsJsonObject().get("x").getAsInt();
			int y = json.getAsJsonObject().get("y").getAsInt();
			int z = json.getAsJsonObject().get("z").getAsInt();
			return new BlockPos(x, y, z);
		}

		@Override
		public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject object = new JsonObject();
			object.addProperty("x", src.getX());
			object.addProperty("y", src.getY());
			object.addProperty("z", src.getZ());
			return object;
		}
	};

	public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(FACING_SERIALIZER.getType(), FACING_SERIALIZER).registerTypeAdapter(RESOURCELOCATION_SERIALIZER.getType(), RESOURCELOCATION_SERIALIZER).registerTypeAdapter(ITEMMETA_SERIALIZER.getType(), ITEMMETA_SERIALIZER).registerTypeAdapter(BLOCKPOS_SERIALIZER.getType(), BLOCKPOS_SERIALIZER).create();

	static
	{
		EntityDataSerializers.registerSerializer(WILL_TYPE_SERIALIZER);
	}
}