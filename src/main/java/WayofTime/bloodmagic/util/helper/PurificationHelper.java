package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.util.Constants;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;

public class PurificationHelper {
    public static double getCurrentPurity(AnimalEntity animal) {
        CompoundNBT data = animal.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_PURITY)) {
            return data.getDouble(Constants.NBT.CURRENT_PURITY);
        }

        return 0;
    }

    public static void setCurrentPurity(AnimalEntity animal, double amount) {
        CompoundNBT data = animal.getEntityData();
        data.setDouble(Constants.NBT.CURRENT_PURITY, amount);
    }

    public static double addPurity(AnimalEntity animal, double added, double max) {
        double currentPurity = getCurrentPurity(animal);
        double newAmount = Math.min(max, currentPurity + added);

        if (newAmount < max) {
            setCurrentPurity(animal, newAmount);
            return newAmount - currentPurity;
        }

        return 0;
    }
}