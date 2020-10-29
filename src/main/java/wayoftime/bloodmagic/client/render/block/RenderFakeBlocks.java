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
		BufferBuilder wr = tessellator.getBuffer();

		Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(texture.getName());

		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		float texMinU = texture.getMinU();
		float texMinV = texture.getMinV();
		float texMaxU = texture.getMaxU();
		float texMaxV = texture.getMaxV();

		wr.pos(minX, minY, minZ).tex(texMinU, texMinV).endVertex();
		wr.pos(maxX, minY, minZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(maxX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(minX, minY, maxZ).tex(texMinU, texMaxV).endVertex();

		wr.pos(minX, maxY, maxZ).tex(texMinU, texMaxV).endVertex();
		wr.pos(maxX, maxY, maxZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(maxX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(minX, maxY, minZ).tex(texMinU, texMinV).endVertex();

		wr.pos(maxX, minY, minZ).tex(texMinU, texMaxV).endVertex();
		wr.pos(minX, minY, minZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(minX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(maxX, maxY, minZ).tex(texMinU, texMinV).endVertex();

		wr.pos(minX, minY, maxZ).tex(texMinU, texMaxV).endVertex();
		wr.pos(maxX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(maxX, maxY, maxZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(minX, maxY, maxZ).tex(texMinU, texMinV).endVertex();

		wr.pos(minX, minY, minZ).tex(texMinU, texMaxV).endVertex();
		wr.pos(minX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(minX, maxY, maxZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(minX, maxY, minZ).tex(texMinU, texMinV).endVertex();

		wr.pos(maxX, minY, maxZ).tex(texMinU, texMaxV).endVertex();
		wr.pos(maxX, minY, minZ).tex(texMaxU, texMaxV).endVertex();
		wr.pos(maxX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
		wr.pos(maxX, maxY, maxZ).tex(texMinU, texMinV).endVertex();

		tessellator.draw();
	}
}
