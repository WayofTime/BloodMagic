package wayoftime.bloodmagic.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class SigilHoldingBakedModel implements BakedModel
{
	public final ResourceLocation baseModelLoc;
	public final BakedModel baseModel;

	public SigilHoldingBakedModel(ResourceLocation baseModelLoc, BakedModel baseModel)
	{
		this.baseModelLoc = baseModelLoc;
		this.baseModel = baseModel;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource randomSource)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean usesBlockLight()
	{
		return false;
	}

	@Override
	public boolean useAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isCustomRenderer()
	{
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return getTexture();
	}

	private TextureAtlasSprite getTexture()
	{
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(baseModelLoc);
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return new SigilHoldingItemOverrides(baseModelLoc, baseModel);
	}

	@Override
	public ItemTransforms getTransforms()
	{
		return ItemTransforms.NO_TRANSFORMS;
	}
}
