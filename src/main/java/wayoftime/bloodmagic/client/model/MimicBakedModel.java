package wayoftime.bloodmagic.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.common.block.BlockMimic;
import wayoftime.bloodmagic.common.tile.TileMimic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MimicBakedModel implements IDynamicBakedModel
{
	public final ResourceLocation texture;

	public MimicBakedModel(ResourceLocation texture)
	{
		this.texture = texture;
	}

	private TextureAtlasSprite getTexture()
	{
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
	}

	@Override
	public boolean usesBlockLight()
	{
		return false;
	}

	private void putVertex(QuadBakingVertexConsumer quadBaker, Vec3 normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b)
	{
		quadBaker.vertex(x, y, z);
		quadBaker.normal((float) normal.x, (float) normal.y, (float) normal.z);
		quadBaker.color(r, g, b, 1);
		quadBaker.uv(sprite.getU(u), sprite.getV(v));
		quadBaker.setSprite(sprite);
		quadBaker.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
		quadBaker.endVertex();
	}

	private BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite)
	{
		Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
		int tw = sprite.getX();
		int th = sprite.getY();

		QuadBakingVertexConsumer.Buffered quadBaker = new QuadBakingVertexConsumer.Buffered();
		putVertex(quadBaker, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(quadBaker, normal, v2.x, v2.y, v2.z, 0, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(quadBaker, normal, v3.x, v3.y, v3.z, tw, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(quadBaker, normal, v4.x, v4.y, v4.z, tw, 0, sprite, 1.0f, 1.0f, 1.0f);
		return quadBaker.getQuad();
	}

	private static Vec3 v(double x, double y, double z)
	{
		return new Vec3(x, y, z);
	}


	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType layer) {

		BlockState mimic = extraData.get(TileMimic.MIMIC);
		if (mimic != null && !(mimic.getBlock() instanceof BlockMimic))
		{
			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(mimic);
			if (layer == null || model.getRenderTypes(mimic, rand, extraData).contains(layer))
			{
				try
				{
					return model.getQuads(mimic, side, rand, ModelData.EMPTY, layer);
				} catch (Exception e)
				{
					return Collections.emptyList();
				}
			}
			return Collections.emptyList();
		}

		if (side != null || (layer != null && !layer.equals(RenderType.solid())))
		{
			return Collections.emptyList();
		}

		TextureAtlasSprite texture = getTexture();
		List<BakedQuad> quads = new ArrayList<>();
		double l = 0;
		double r = 1;
//		double l = .2;
//		double r = 1 - .2;
		quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), texture));
		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), texture));
		quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), texture));
		quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), texture));
		quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), texture));
		quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), texture));

		return quads;
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
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return getTexture();
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}

	@Override
	public ItemTransforms getTransforms()
	{
		return ItemTransforms.NO_TRANSFORMS;
	}

}
