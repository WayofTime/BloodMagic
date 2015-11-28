package WayofTime.bloodmagic.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHolder {

    public static final String NBT_OWNER = "ownerName";
    public static final String NBT_USES = "uses";
    public static final String NBT_UNUSABLE  = "unusable";
    public static final String NBT_SACRIFICE = "sacrifice";
    public static final String NBT_DIMID = "dimensionId";
    public static final String NBT_COORDX = "xCoord";
    public static final String NBT_COORDY = "yCoord";
    public static final String NBT_COORDZ = "zCoord";
    public static final String NBT_ORBTIER = "orbTier";
    public static final String NBT_CURRENTESSENCE = "currentEssence";
    public static final String NBT_CURRENTRITUAL = "currentRitual";
    public static final String NBT_RUNNING = "isRunning";
    public static final String NBT_RUNTIME = "runtime";
    public static final String NBT_REAGENTTANK = "reagentTanks";
    public static final String NBT_CURRENT_INCENSE = "BM:CurrentIncense";
    public static final String NBT_EMPTY = "Empty";
    public static final String NBT_OUTPUT_AMOUNT = "outputAmount";
    public static final String NBT_INPUT_AMOUNT = "inputAmount";

    //Altar Shtuff
    public static final String NBT_ALTAR_TIER = "upgradeLevel";
    public static final String NBT_ALTAR_ACTIVE = "isActive";
    public static final String NBT_ALTAR_LIQUID_REQ = "liquidRequired";
    public static final String NBT_ALTAR_FILLABLE = "canBeFilled";
    public static final String NBT_ALTAR_UPGRADED = "isUpgraded";
    public static final String NBT_ALTAR_CONSUMPTION_RATE = "consumptionRate";
    public static final String NBT_ALTAR_DRAIN_RATE = "drainRate";
    public static final String NBT_ALTAR_CONSUMPTION_MULTIPLIER = "consumptionMultiplier";
    public static final String NBT_ALTAR_EFFICIENCY_MULTIPLIER = "efficiencyMultiplier";
    public static final String NBT_ALTAR_SELF_SACRIFICE_MULTIPLIER = "selfSacrificeMultiplier";
    public static final String NBT_ALTAR_SACRIFICE_MULTIPLIER = "sacrificeMultiplier";
    public static final String NBT_ALTAR_CAPACITY_MULTIPLIER = "capacityMultiplier";
    public static final String NBT_ALTAR_ORB_CAPACITY_MULTIPLIER = "orbCapacityMultiplier";
    public static final String NBT_ALTAR_DISLOCATION_MULTIPLIER = "dislocationMultiplier";
    public static final String NBT_ALTAR_CAPACITY = "capacity";
    public static final String NBT_ALTAR_BUFFER_CAPACITY = "bufferCapacity";
    public static final String NBT_ALTAR_PROGRESS = "progress";
    public static final String NBT_ALTAR_IS_RESULT_BLOCK = "isResultBlock";
    public static final String NBT_ALTAR_LOCKDOWN_DURATION = "lockdownDuration";
    public static final String NBT_ALTAR_ACCELERATION_UPGRADES = "accelerationUpgrades";
    public static final String NBT_ALTAR_DEMON_BLOOD_DURATION = "demonBloodDuration";
    public static final String NBT_ALTAR_COOLDOWN_AFTER_CRAFTING = "cooldownAfterCrafting";

    public static final String NBT_STORED_LP = "storedLP";

    public static ItemStack checkNBT(ItemStack stack) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        return stack;
    }
}
