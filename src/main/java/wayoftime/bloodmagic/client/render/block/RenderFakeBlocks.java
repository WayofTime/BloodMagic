package wayoftime.bloodmagic.client.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderFakeBlocks
{
	public static void drawFakeBlock(TextureAtlasSprite texture, double minX, double minY, double minZ)
	{
		if (texture == null)
			return;

		double maxX = minX + 1;
		double maxY = minY + 1;
		double maxZ = minZ + 1;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder wr = tessellator.getBuilder();

		Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(texture.getName());

		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

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
