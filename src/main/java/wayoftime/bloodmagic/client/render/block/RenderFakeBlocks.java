package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class RenderFakeBlocks
{
	public static void drawFakeBlock(TextureAtlasSprite texture, double minX, double minY, double minZ)
	{
		if (texture == null)
			return;

		double maxX = minX + 1;
		double maxY = minY + 1;
		double maxZ = minZ + 1;
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder wr = tessellator.getBuilder();

		Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture.atlasLocation());

		wr.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//		wr.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);

		float texMinU = texture.getU0();
		float texMinV = texture.getV0();
		float texMaxU = texture.getU1();
		float texMaxV = texture.getV1();

		wr.vertex(minX, minY, minZ).uv(texMinU, texMinV).endVertex();
		wr.vertex(maxX, minY, minZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(maxX, minY, maxZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(minX, minY, maxZ).uv(texMinU, texMaxV).endVertex();

		wr.vertex(minX, maxY, maxZ).uv(texMinU, texMaxV).endVertex();
		wr.vertex(maxX, maxY, maxZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(maxX, maxY, minZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(minX, maxY, minZ).uv(texMinU, texMinV).endVertex();

		wr.vertex(maxX, minY, minZ).uv(texMinU, texMaxV).endVertex();
		wr.vertex(minX, minY, minZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(minX, maxY, minZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(maxX, maxY, minZ).uv(texMinU, texMinV).endVertex();

		wr.vertex(minX, minY, maxZ).uv(texMinU, texMaxV).endVertex();
		wr.vertex(maxX, minY, maxZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(maxX, maxY, maxZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(minX, maxY, maxZ).uv(texMinU, texMinV).endVertex();

		wr.vertex(minX, minY, minZ).uv(texMinU, texMaxV).endVertex();
		wr.vertex(minX, minY, maxZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(minX, maxY, maxZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(minX, maxY, minZ).uv(texMinU, texMinV).endVertex();

		wr.vertex(maxX, minY, maxZ).uv(texMinU, texMaxV).endVertex();
		wr.vertex(maxX, minY, minZ).uv(texMaxU, texMaxV).endVertex();
		wr.vertex(maxX, maxY, minZ).uv(texMaxU, texMinV).endVertex();
		wr.vertex(maxX, maxY, maxZ).uv(texMinU, texMinV).endVertex();

		tessellator.end();
	}
}
