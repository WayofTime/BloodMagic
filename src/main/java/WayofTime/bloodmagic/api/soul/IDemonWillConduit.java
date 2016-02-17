package WayofTime.bloodmagic.api.soul;

/**
 * Implement this interface on a block that can accept and store Demonic Will.
 * 
 */
public interface IDemonWillConduit
{
    public int getWeight();

    public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill);

    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain);

    public boolean canFill(EnumDemonWillType type);

    public boolean canDrain(EnumDemonWillType type);

    public double getCurrentWill(EnumDemonWillType type);
}
