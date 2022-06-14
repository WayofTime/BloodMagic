package wayoftime.bloodmagic.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

public class MimicModelLoader implements IModelLoader<MimicModelGeometry>
{
	public final ResourceLocation texture;

	public MimicModelLoader(ResourceLocation texture)
	{
		this.texture = texture;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{

	}

	@Override
	public MimicModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
	{
		return new MimicModelGeometry(texture);
	}
}