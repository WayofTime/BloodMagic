package wayoftime.bloodmagic.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class MimicModelGeometry implements IModelGeometry<MimicModelGeometry>
{
	public final ResourceLocation texture;

	public MimicModelGeometry(ResourceLocation texture)
	{
		this.texture = texture;
	}

	@Override
	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, java.util.function.Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
	{
		return new MimicBakedModel(texture);
	}

	@Override
	public Collection<RenderMaterial> getTextures(IModelConfiguration owner, java.util.function.Function<ResourceLocation, IUnbakedModel> modelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors)
	{
		return Collections.singletonList(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, texture));
	}
}
