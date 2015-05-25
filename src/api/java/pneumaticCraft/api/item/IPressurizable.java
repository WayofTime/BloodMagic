package pneumaticCraft.api.item;

import net.minecraft.item.ItemStack;

/**
 * Any item implementing this interface will be able to (dis)charge in a Charging Station.
 */
public interface IPressurizable{
    /**
     * This method should return the current pressure of the ItemStack given.
     * 
     * @param iStack Stack the pressure is asked from.
     * @return Pressure in bar.
     */
    public float getPressure(ItemStack iStack);

    /**
     * this method is used to charge or discharge a pneumatic item. when the
     * value is negative the item should be discharging
     * 
     * @param iStack the ItemStack which has to be (dis)charged.
     * @param amount amount in mL that the item is (dis)charging.
     */
    public void addAir(ItemStack iStack, int amount);

    /**
     * This method should return the maximum pressure of a pneumatic item. If it
     * has reached this maximum, it won't explode, but it wouldn't (try to)
     * charge either.
     * 
     * @param iStack the stack from which the maximum pressure is asked.
     * @return maximum pressure in bar.
     */
    public float maxPressure(ItemStack iStack);
}
