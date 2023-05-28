package wayoftime.bloodmagic.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class SigilHoldingModelLoader implements IGeometryLoader<SigilHoldingModelGeometry>
{
	public final ResourceLocation texture;

	public SigilHoldingModelLoader(ResourceLocation texture)
	{
		this.texture = texture;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{

	}

	@Override
	public SigilHoldingModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
	{
		return new SigilHoldingModelGeometry(texture);
	}
}