package WayofTime.alchemicalWizardry.client.renderer

class Vector3 (val x: Int, val y: Int, val z: Int) {

  def +(w: (Int,Int,Int)): Vector3 = new Vector3(x + w._1, y+ w._2, z + w._3)

  override def toString = s"V3(${x}},${y}},${z}})"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Vector3]

  override def equals(other: Any): Boolean = other match {
    case that: Vector3 =>
      (that canEqual this) &&
        x == that.x &&
        y == that.y &&
        z == that.z
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(x, y, z)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}