package wayoftime.bloodmagic.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class MimicModelLoader implements IGeometryLoader<MimicModelGeometry>
{
	public final ResourceLocation texture;

	public MimicModelLoader(ResourceLocation texture)
	{
		this.texture = texture;
	}


	@Override
	public MimicModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new MimicModelGeometry(texture);
	}
}