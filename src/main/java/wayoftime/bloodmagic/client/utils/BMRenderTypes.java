package wayoftime.bloodmagic.client.utils;

import java.util.OptionalDouble;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderState.AlphaState;
import net.minecraft.client.renderer.RenderState.CullState;
import net.minecraft.client.renderer.RenderState.FogState;
import net.minecraft.client.renderer.RenderState.LightmapState;
import net.minecraft.client.renderer.RenderState.LineState;
import net.minecraft.client.renderer.RenderState.ShadeModelState;
import net.minecraft.client.renderer.RenderState.TextureState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class BMRenderTypes
{
	public static final RenderType SOLID_FULLBRIGHT;
	public static final RenderType TRANSLUCENT_LINES;
	public static final RenderType LINES;
	public static final RenderType TRANSLUCENT_TRIANGLES;
	public static final RenderType TRANSLUCENT_POSITION_COLOR;
	public static final RenderType TRANSLUCENT_NO_DEPTH;
	public static final RenderType CHUNK_MARKER;
	public static final RenderType VEIN_MARKER;
	public static final RenderType POSITION_COLOR_TEX_LIGHTMAP;
	public static final RenderType POSITION_COLOR_LIGHTMAP;
	public static final RenderType ITEM_DAMAGE_BAR;
	protected static final RenderState.ShadeModelState SHADE_ENABLED = new RenderState.ShadeModelState(true);
	protected static final RenderState.TextureState BLOCK_SHEET_MIPPED = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS, false, true);
	protected static final RenderState.LightmapState LIGHTMAP_DISABLED = new RenderState.LightmapState(false);
	protected static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
	}, RenderSystem::disableBlend);
	protected static final RenderState.TransparencyState NO_TRANSPARENCY = new RenderState.TransparencyState("no_transparency", RenderSystem::disableBlend, () -> {
	});
	protected static final RenderState.DepthTestState DEPTH_ALWAYS = new RenderState.DepthTestState("", GL11.GL_ALWAYS);

	static
	{
		RenderType.State fullbrightSolidState = RenderType.State.builder().setShadeModelState(SHADE_ENABLED).setLightmapState(LIGHTMAP_DISABLED).setTextureState(BLOCK_SHEET_MIPPED).createCompositeState(true);
		SOLID_FULLBRIGHT = RenderType.create(BloodMagic.MODID + ":block_fullbright", DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256, fullbrightSolidState);
		RenderType.State translucentNoDepthState = RenderType.State.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLineState(new LineState(OptionalDouble.of(2))).setTextureState(new TextureState()).setDepthTestState(DEPTH_ALWAYS).createCompositeState(false);
		RenderType.State translucentNoTextureState = RenderType.State.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(new TextureState()).createCompositeState(false);
		TRANSLUCENT_LINES = RenderType.create(BloodMagic.MODID + ":translucent_lines", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, translucentNoDepthState);
		LINES = RenderType.create(BloodMagic.MODID + ":lines", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.State.builder().createCompositeState(false));
		TRANSLUCENT_TRIANGLES = RenderType.create(BloodMagic.MODID + ":translucent_triangle_fan", DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, translucentNoDepthState);
		TRANSLUCENT_POSITION_COLOR = RenderType.create(BloodMagic.MODID + ":translucent_pos_color", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, translucentNoTextureState);
		TRANSLUCENT_NO_DEPTH = RenderType.create(BloodMagic.MODID + ":translucent_no_depth", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, translucentNoDepthState);
		RenderType.State chunkMarkerState = RenderType.State.builder().setTextureState(new TextureState()).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(new CullState(false)).setShadeModelState(new ShadeModelState(true)).setLineState(new LineState(OptionalDouble.of(5))).createCompositeState(false);
		CHUNK_MARKER = RenderType.create(BloodMagic.MODID + ":chunk_marker", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, chunkMarkerState);
		VEIN_MARKER = RenderType.create(BloodMagic.MODID + ":vein_marker", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINE_LOOP, 256, chunkMarkerState);
		POSITION_COLOR_TEX_LIGHTMAP = RenderType.create(BloodMagic.MODID + ":pos_color_tex_lightmap", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, RenderType.State.builder().setTextureState(new TextureState(PlayerContainer.BLOCK_ATLAS, false, false)).setLightmapState(new LightmapState(true)).createCompositeState(false));
		POSITION_COLOR_LIGHTMAP = RenderType.create(BloodMagic.MODID + ":pos_color_lightmap", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, RenderType.State.builder().setTextureState(new TextureState()).setLightmapState(new LightmapState(true)).createCompositeState(false));
		ITEM_DAMAGE_BAR = RenderType.create(BloodMagic.MODID + ":item_damage_bar", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, RenderType.State.builder().setDepthTestState(DEPTH_ALWAYS).setTextureState(new TextureState()).setAlphaState(new AlphaState(0)).setTransparencyState(NO_TRANSPARENCY).createCompositeState(false));
	}

	public static RenderType getGui(ResourceLocation texture)
	{
		return RenderType.create("gui_" + texture, DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256, RenderType.State.builder().setTextureState(new TextureState(texture, false, false)).setAlphaState(new AlphaState(0.5F)).createCompositeState(false));
	}

	public static RenderType getLines(float lineWidth)
	{
		return RenderType.create("lines_color_pos_" + lineWidth, DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.State.builder().setLineState(new LineState(OptionalDouble.of(lineWidth))).setTextureState(new TextureState()).createCompositeState(false));
	}

	public static RenderType getPoints(float pointSize)
	{
		// Not really a fog state, but using it like this makes using RenderType.State
		// with custom states possible
		FogState setPointSize = new FogState(BloodMagic.MODID + ":pointsize_" + pointSize, () -> GL11.glPointSize(pointSize), () -> {
			GL11.glPointSize(1);
		});
		return RenderType.create("point_pos_color_" + pointSize, DefaultVertexFormats.POSITION_COLOR, GL11.GL_POINTS, 256, RenderType.State.builder().setFogState(setPointSize).setTextureState(new TextureState()).createCompositeState(false));
	}

	public static RenderType getPositionTex(ResourceLocation texture)
	{
		return RenderType.create(BloodMagic.MODID + ":pos_tex_" + texture, DefaultVertexFormats.POSITION_TEX, GL11.GL_QUADS, 256, RenderType.State.builder().setTextureState(new TextureState(texture, false, false)).createCompositeState(false));
	}

	public static RenderType getFullbrightTranslucent(ResourceLocation resourceLocation)
	{
		RenderType.State glState = RenderType.State.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(new TextureState(resourceLocation, false, false)).setLightmapState(new LightmapState(false)).createCompositeState(false);
		return RenderType.create("BloodMagic:fullbright_translucent_" + resourceLocation, DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256, glState);
	}

	public static IRenderTypeBuffer wrapWithStencil(IRenderTypeBuffer in, Consumer<IVertexBuilder> setupStencilArea, String name, int ref)
	{
		return wrapWithAdditional(in, "stencil_" + name + "_" + ref, () -> {
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			RenderSystem.colorMask(false, false, false, false);
			RenderSystem.depthMask(false);
			GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

			GL11.glStencilMask(0xFF);
			RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, true);
			RenderSystem.disableTexture();
			Tessellator tes = Tessellator.getInstance();
			BufferBuilder bb = tes.getBuilder();
			bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			setupStencilArea.accept(bb);
			tes.end();
			RenderSystem.enableTexture();
			RenderSystem.colorMask(true, true, true, true);
			RenderSystem.depthMask(true);
			GL11.glStencilMask(0x00);
			GL11.glStencilFunc(GL11.GL_EQUAL, ref, 0xFF);
		}, () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
	}

	public static IRenderTypeBuffer disableLighting(IRenderTypeBuffer in)
	{
		return wrapWithAdditional(in, "no_lighting", RenderSystem::disableLighting, () -> {
		});
	}

	public static IRenderTypeBuffer disableCull(IRenderTypeBuffer in)
	{
		return wrapWithAdditional(in, "no_cull", RenderSystem::disableCull, RenderSystem::enableCull);
	}

	private static IRenderTypeBuffer wrapWithAdditional(IRenderTypeBuffer in, String name, Runnable setup, Runnable teardown)
	{
		return type -> {
			return in.getBuffer(new RenderType(BloodMagic.MODID + ":" + type + "_" + name, type.format(), type.mode(), type.bufferSize(), type.affectsCrumbling(), false, () -> {
				type.setupRenderState();
				setup.run();
			}, () -> {
				teardown.run();
				type.clearRenderState();
			})
			{
			});
		};
	}
}
