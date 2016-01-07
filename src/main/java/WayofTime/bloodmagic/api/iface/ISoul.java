package WayofTime.bloodmagic.api.iface;

import net.minecraft.item.ItemStack;

public interface ISoul
{
    public double getSouls(ItemStack soulStack);

    public void setSouls(ItemStack soulStack, double souls);

    /**
     * Drains the souls from the soulStack. If all of the souls are drained, the
     * soulStack will be removed.
     * 
     * @param soulStack
     * @param drainAmount
     * @return The number of souls drained.
     */
    public double drainSouls(ItemStack soulStack, double drainAmount);

    /**
     * Creates a new ItemStack with the specified number of souls.
     * Implementation should respect the number requested.
     * 
     * @param meta
     * @param number
     * @return
     */
    public ItemStack createSoul(int meta, double number);
}
