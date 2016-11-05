package WayofTime.bloodmagic.api.util.helper;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;

public class PurificationHelper
{
    public static double getCurrentPurity(EntityAnimal animal)
    {
        NBTTagCompound data = animal.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_PURITY))
        {
            return data.getDouble(Constants.NBT.CURRENT_PURITY);
        }

        return 0;
    }

    public static void setCurrentPurity(EntityAnimal animal, double amount)
    {
        NBTTagCompound data = animal.getEntityData();
        data.setDouble(Constants.NBT.CURRENT_PURITY, amount);
    }

    public static double addPurity(EntityAnimal animal, double added, double max)
    {
        double currentPurity = getCurrentPurity(animal);
        double newAmount = Math.min(max, currentPurity + added);

        if (newAmount < max)
        {
            setCurrentPurity(animal, newAmount);
            return newAmount - currentPurity;
        }

        return 0;
    }
}