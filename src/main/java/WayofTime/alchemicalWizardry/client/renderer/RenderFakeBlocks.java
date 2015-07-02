package WayofTime.alchemicalWizardry.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/*
 *  Created in Scala by Alex-Hawks
 *  Translated and implemented by Arcaratus
 */
public class RenderFakeBlocks
{
    public static void drawFakeBlock(WayofTime.alchemicalWizardry.api.Vector3 vector3, Block block, int meta, double minX, double minY, double minZ, World world)
    {
        double maxX = minX + 1;
        double maxY = minY + 1;
        double maxZ = minZ + 1;
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 200);

        float texMinU, texMaxU, texMinV, texMaxV;

        texMinU = getMinU(block, meta, 0);
        texMaxU = getMaxU(block, meta, 0);
        texMinV = getMinV(block, meta, 0);
        texMaxV = getMaxV(block, meta, 0);
        tessellator.addVertexWithUV(minX, minY, minZ, texMinU, texMinV);
        tessellator.addVertexWithUV(maxX, minY, minZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(minX, minY, maxZ, texMinU, texMaxV);

        texMinU = getMinU(block, meta, 1);
        texMaxU = getMaxU(block, meta, 1);
        texMinV = getMinV(block, meta, 1);
        texMaxV = getMaxV(block, meta, 1);
        tessellator.addVertexWithUV(minX, maxY, maxZ, texMinU, texMaxV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(minX, maxY, minZ, texMinU, texMinV);

        texMinU = getMinU(block, meta, 2);
        texMaxU = getMaxU(block, meta, 2);
        texMinV = getMinV(block, meta, 2);
        texMaxV = getMaxV(block, meta, 2);
        tessellator.addVertexWithUV(maxX, minY, minZ, texMinU, texMaxV);
        tessellator.addVertexWithUV(minX, minY, minZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, texMinU, texMinV);

        texMinU = getMinU(block, meta, 3);
        texMaxU = getMaxU(block, meta, 3);
        texMinV = getMinV(block, meta, 3);
        texMaxV = getMaxV(block, meta, 3);
        tessellator.addVertexWithUV(minX, minY, maxZ, texMinU, texMaxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, texMinU, texMinV);

        texMinU = getMinU(block, meta, 4);
        texMaxU = getMaxU(block, meta, 4);
        texMinV = getMinV(block, meta, 4);
        texMaxV = getMaxV(block, meta, 4);
        tessellator.addVertexWithUV(minX, minY, minZ, texMinU, texMaxV);
        tessellator.addVertexWithUV(minX, minY, maxZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(minX, maxY, minZ, texMinU, texMinV);

        texMinU = getMinU(block, meta, 5);
        texMaxU = getMaxU(block, meta, 5);
        texMinV = getMinV(block, meta, 5);
        texMaxV = getMaxV(block, meta, 5);
        tessellator.addVertexWithUV(maxX, minY, maxZ, texMinU, texMaxV);
        tessellator.addVertexWithUV(maxX, minY, minZ, texMaxU, texMaxV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, texMaxU, texMinV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, texMinU, texMinV);

        tessellator.draw();
    }

    private static float getMinU(Block block, int meta, int side)
    {
        IIcon icon = block.getIcon(side, meta);
        return icon.getMinU();
    }

    private static float getMaxU(Block block, int meta, int side)
    {
        IIcon icon = block.getIcon(side, meta);
        return icon.getMaxU();
    }

    private static float getMinV(Block block, int meta, int side)
    {
        IIcon icon = block.getIcon(side, meta);
        return icon.getMinV();
    }

    private static float getMaxV(Block block, int meta, int side)
    {
        IIcon icon = block.getIcon(side, meta);
        return icon.getMaxV();
    }
}
