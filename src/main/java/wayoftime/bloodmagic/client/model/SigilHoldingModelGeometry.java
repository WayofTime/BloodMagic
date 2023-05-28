package wayoftime.bloodmagic.client.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Collection;
import java.util.Set;

public class SigilHoldingModelGeometry implements IUnbakedGeometry<SigilHoldingModelGeometry>
{
	public final ResourceLocation baseModelLoc;

	private UnbakedModel unbakedBaseModel;
	private BakedModel bakedBaseModel;

	public SigilHoldingModelGeometry(ResourceLocation texture)
	{
		this.baseModelLoc = texture;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, java.util.function.Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		bakedBaseModel = unbakedBaseModel.bake(bakery, spriteGetter, modelTransform, baseModelLoc);

		return new SigilHoldingBakedModel(baseModelLoc, bakedBaseModel);
	}

	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext owner, java.util.function.Function<ResourceLocation, UnbakedModel> modelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors)
	{
		unbakedBaseModel = modelGetter.apply(baseModelLoc);

		return unbakedBaseModel.getMaterials(modelGetter, missingTextureErrors);
	}
}
