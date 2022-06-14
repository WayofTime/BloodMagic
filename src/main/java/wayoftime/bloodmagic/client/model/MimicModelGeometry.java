package wayoftime.bloodmagic.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
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
	public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, java.util.function.Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		return new MimicBakedModel(texture);
	}

	@Override
	public Collection<Material> getTextures(IModelConfiguration owner, java.util.function.Function<ResourceLocation, UnbakedModel> modelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors)
	{
		return Collections.singletonList(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
	}
}
