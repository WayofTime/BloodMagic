package wayoftime.bloodmagic.client.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class MimicModelGeometry implements IUnbakedGeometry<MimicModelGeometry>
{
	public final ResourceLocation texture;

	public MimicModelGeometry(ResourceLocation texture)
	{
		this.texture = texture;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
		return new MimicBakedModel(texture);
	}

	//TODO: SEE IF THIS IS STILL NEEDED
//	@Override
//	public Collection<Material> getMaterials(IGeometryBakingContext owner, java.util.function.Function<ResourceLocation, UnbakedModel> modelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors)
//	{
//		return Collections.singletonList(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
//	}
}
