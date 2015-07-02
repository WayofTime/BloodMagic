package WayofTime.alchemicalWizardry.api;

/*
 *  Created in Scala by Alex-Hawks
 *  Translated and implemented by Arcaratus
 */
public class Vector3
{
    public int x, y, z;

    public Vector3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 vec1)
    {
        return new Vector3(this.x + vec1.x, this.y + vec1.y, this.z + vec1.z);
    }

    @Override
    public String toString()
    {
        return "V3(" + x + "}, " + y + "}," + z + "})";
    }

    private boolean canEqual(Object object)
    {
        return object instanceof Vector3;
    }

    @Override
    public boolean equals(Object object)
    {
        return object == this || object instanceof Vector3 && canEqual(this) && this.x == ((Vector3) object).x && this.y == ((Vector3) object).y && this.z == ((Vector3) object).z;
    }

    @Override
    public int hashCode()
    {
        return 48131 * x - 95021 * y + z;
    }
}
