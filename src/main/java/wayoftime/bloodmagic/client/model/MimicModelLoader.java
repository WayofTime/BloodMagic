package wayoftime.bloodmagic.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

public class MimicModelLoader implements IModelLoader<MimicModelGeometry>
{
	public final ResourceLocation texture;

	public MimicModelLoader(ResourceLocation texture)
	{
		this.texture = texture;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{

	}

	@Override
	public MimicModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
	{
		return new MimicModelGeometry(texture);
	}
}