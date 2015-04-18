package WayofTime.alchemicalWizardry.client.renderer

import net.minecraft.block.Block
import net.minecraft.client.renderer.Tessellator
import net.minecraft.world.World

object Render {

  def drawFakeBlock(v:Vector3, b:Block, meta:Int, min:(Double, Double, Double), w: World): Unit = {

    val (minX, minY, minZ) = min
    val (maxX, maxY, maxZ) = (minX + 1, minY + 1, minZ + 1)

    val tessellator = Tessellator.instance

    tessellator.startDrawingQuads()
//    tessellator.setBrightness(b.getMixedBrightnessForBlock(w, v.x, v.y, v.z))
    tessellator.setColorRGBA(255, 255, 255, 26)

    var tex: (Float, Float, Float, Float) = null

    def textureMinU = tex._1
    def textureMaxU = tex._2
    def textureMinV = tex._3
    def textureMaxV = tex._4

    tex = g(b, meta, 0)
    tessellator.addVertexWithUV(minX, minY, minZ, textureMinU, textureMinV)
    tessellator.addVertexWithUV(maxX, minY, minZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(maxX, minY, maxZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(minX, minY, maxZ, textureMinU, textureMaxV)

    tex = g(b, meta, 1)
    tessellator.addVertexWithUV(minX, maxY, maxZ, textureMinU, textureMaxV)
    tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(maxX, maxY, minZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(minX, maxY, minZ, textureMinU, textureMinV)

    tex = g(b, meta, 2)
    tessellator.addVertexWithUV(maxX, minY, minZ, textureMinU, textureMaxV)
    tessellator.addVertexWithUV(minX, minY, minZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(minX, maxY, minZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(maxX, maxY, minZ, textureMinU, textureMinV)

    tex = g(b, meta, 3)
    tessellator.addVertexWithUV(minX, minY, maxZ, textureMinU, textureMaxV)
    tessellator.addVertexWithUV(maxX, minY, maxZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(minX, maxY, maxZ, textureMinU, textureMinV)

    tex = g(b, meta, 4)
    tessellator.addVertexWithUV(minX, minY, minZ, textureMinU, textureMaxV)
    tessellator.addVertexWithUV(minX, minY, maxZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(minX, maxY, maxZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(minX, maxY, minZ, textureMinU, textureMinV)

    tex = g(b, meta, 5)
    tessellator.addVertexWithUV(maxX, minY, maxZ, textureMinU, textureMaxV)
    tessellator.addVertexWithUV(maxX, minY, minZ, textureMaxU, textureMaxV)
    tessellator.addVertexWithUV(maxX, maxY, minZ, textureMaxU, textureMinV)
    tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMinU, textureMinV)

    tessellator.draw
  }

  def g(b: Block, m: Int, s: Int): (Float, Float, Float, Float) = {
    val i = b.getIcon(s, m)
    (i.getMinU, i.getMaxU, i.getMinV, i.getMaxV)
  }
}