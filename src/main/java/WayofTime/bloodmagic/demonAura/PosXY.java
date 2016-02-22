package WayofTime.bloodmagic.demonAura;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PosXY implements Comparable<PosXY>
{
    public int x;
    public int y;

    @Override
    public int compareTo(PosXY c)
    {
        return this.y == c.y ? this.x - c.x : this.y - c.y;
    }

    public float getDistanceSquared(int x, int z)
    {
        float f = this.x - x;
        float f2 = this.y - z;
        return f * f + f2 * f2;
    }

    public float getDistanceSquaredToChunkCoordinates(PosXY c)
    {
        return getDistanceSquared(c.x, c.y);
    }
}
