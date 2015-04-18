package WayofTime.alchemicalWizardry.client.renderer

import WayofTime.alchemicalWizardry.ModBlocks
import WayofTime.alchemicalWizardry.api.rituals.{RitualComponent, IMasterRitualStone}
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent

import scala.collection.JavaConversions

class RitualDivinerRender {

  @SubscribeEvent
  def render(e: RenderWorldLastEvent)
  {
    val mc: Minecraft = Minecraft.getMinecraft
    val p: EntityClientPlayerMP = mc.thePlayer
    val w: World = p.worldObj

    if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
      return

    val te: TileEntity = w.getTileEntity(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ)

    if (!te.isInstanceOf[IMasterRitualStone])
      return

    val v3 = new Vector3(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ)
    val (px, py, pz) = (p.lastTickPosX + (p.posX - p.lastTickPosX) * e.partialTicks, p.lastTickPosY + (p.posY - p.lastTickPosY) * e.partialTicks, p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * e.partialTicks)

    if (p.inventory.getCurrentItem != null && p.inventory.getCurrentItem.getItem.isInstanceOf[ItemRitualDiviner]) {
      val d = p.inventory.getCurrentItem.getItem.asInstanceOf[ItemRitualDiviner]
      val dir = d.getDirection(p.inventory.getCurrentItem)

      val r = Helper.getEffectFromString(d.getCurrentRitual(p.inventory.getCurrentItem))
      if (r == null)
        return

      for (x <- JavaConversions.asScalaBuffer[RitualComponent](r.getRitualComponentList))
      {
        val v: Vector3 = v3 + (x.getX(dir), x.getY, x.getZ(dir))
        val min = (v.x - px, v.y - py, v.z - pz)

        if (!w.getBlock(v.x, v.y, v.z).isOpaqueCube)
          Render.drawFakeBlock(v, ModBlocks.ritualStone, x.getStoneType, min, w)
      }
    }
  }
}