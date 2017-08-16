package WayofTime.bloodmagic.demonAura;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PosXY implements Comparable<PosXY> {
    public int x;
    public int y;

    public PosXY() {
    }

    public PosXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(PosXY c) {
        return this.y == c.y ? this.x - c.x : this.y - c.y;
    }

    public float getDistanceSquared(int x, int z) {
        float f = this.x - x;
        float f2 = this.y - z;
        return f * f + f2 * f2;
    }

    public float getDistanceSquaredToChunkCoordinates(PosXY c) {
        return getDistanceSquared(c.x, c.y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("x", x)
                .append("y", y)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PosXY)) return false;

        PosXY posXY = (PosXY) o;

        if (x != posXY.x) return false;
        return y == posXY.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
