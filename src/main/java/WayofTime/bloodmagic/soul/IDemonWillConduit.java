package WayofTime.bloodmagic.soul;

/**
 * Implement this interface on a block that can accept and store Demonic Will.
 */
public interface IDemonWillConduit {
    int getWeight();

    double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill);

    double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain);

    boolean canFill(EnumDemonWillType type);

    boolean canDrain(EnumDemonWillType type);

    double getCurrentWill(EnumDemonWillType type);
}
