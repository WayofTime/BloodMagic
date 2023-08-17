package wayoftime.bloodmagic.client.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

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
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
		bakedBaseModel = unbakedBaseModel.bake(baker, spriteGetter, modelState, baseModelLoc);

		return new SigilHoldingBakedModel(baseModelLoc, bakedBaseModel);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		unbakedBaseModel = modelGetter.apply(baseModelLoc);
		unbakedBaseModel.resolveParents(modelGetter);
	}
}
