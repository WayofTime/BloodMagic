package WayofTime.bloodmagic.api;

import net.minecraft.potion.Potion;

import java.util.Locale;

public class Constants {

    public static class NBT {

        public static final String OWNER_NAME = "ownerName";
        public static final String USES = "uses";
        public static final String UNUSABLE = "unusable";
        public static final String SACRIFICE = "sacrifice";
        public static final String DIMENSION_ID = "dimensionId";
        public static final String X_COORD = "xCoord";
        public static final String Y_COORD = "yCoord";
        public static final String Z_COORD = "zCoord";
        public static final String ORB_TIER = "orbTier";
        public static final String CURRENT_ESSENCE = "currentEssence";
        public static final String CURRENT_RITUAL = "currentRitual";
        public static final String IS_RUNNING = "isRunning";
        public static final String RUNTIME = "runtime";
        public static final String REAGENT_TANKS = "reagentTanks";
        public static final String CURRENT_INCENSE = "BM:CurrentIncense";
        public static final String EMPTY = "Empty";
        public static final String OUTPUT_AMOUNT = "outputAmount";
        public static final String INPUT_AMOUNT = "inputAmount";
        public static final String STORED_LP = "storedLP";

        public static final String ALTAR_TIER = "upgradeLevel";
        public static final String ALTAR_ACTIVE = "isActive";
        public static final String ALTAR_LIQUID_REQ = "liquidRequired";
        public static final String ALTAR_FILLABLE = "canBeFilled";
        public static final String ALTAR_UPGRADED = "isUpgraded";
        public static final String ALTAR_CONSUMPTION_RATE = "consumptionRate";
        public static final String ALTAR_DRAIN_RATE = "drainRate";
        public static final String ALTAR_CONSUMPTION_MULTIPLIER = "consumptionMultiplier";
        public static final String ALTAR_EFFICIENCY_MULTIPLIER = "efficiencyMultiplier";
        public static final String ALTAR_SELF_SACRIFICE_MULTIPLIER = "selfSacrificeMultiplier";
        public static final String ALTAR_SACRIFICE_MULTIPLIER = "sacrificeMultiplier";
        public static final String ALTAR_CAPACITY_MULTIPLIER = "capacityMultiplier";
        public static final String ALTAR_ORB_CAPACITY_MULTIPLIER = "orbCapacityMultiplier";
        public static final String ALTAR_DISLOCATION_MULTIPLIER = "dislocationMultiplier";
        public static final String ALTAR_CAPACITY = "capacity";
        public static final String ALTAR_BUFFER_CAPACITY = "bufferCapacity";
        public static final String ALTAR_PROGRESS = "progress";
        public static final String ALTAR_IS_RESULT_BLOCK = "isResultBlock";
        public static final String ALTAR_LOCKDOWN_DURATION = "lockdownDuration";
        public static final String ALTAR_ACCELERATION_UPGRADES = "accelerationUpgrades";
        public static final String ALTAR_DEMON_BLOOD_DURATION = "demonBloodDuration";
        public static final String ALTAR_COOLDOWN_AFTER_CRAFTING = "cooldownAfterCrafting";

        public static final String ALTARMAKER_CURRENT_TIER = "currentTier";

        public static final String PROJECTILE_TICKS_IN_AIR = "projectileTicksInAir";
        public static final String PROJECTILE_MAX_TICKS_IN_AIR = "projectileMaxTicksInAir";

        public static final String TICKS_REMAINING = "ticksRemaining";
        public static final String CONTAINED_BLOCK_NAME = "containedBlockName";
        public static final String CONTAINED_BLOCK_META = "containedBlockMeta";
    }

    public static class Mod {

        public static final String MODID = "BloodMagic";
        public static final String DOMAIN = MODID.toLowerCase(Locale.ENGLISH) + ":";
        public static final String NAME = "Blood Magic: Alchemical Wizardry";
        public static final String VERSION = "@VERSION@";
        public static final String DEPEND = "";
    }

    public static class Compat {
        public static final String JEI_CATEGORY_ALTAR = Mod.MODID + ":altar";
        public static final String JEI_CATEGORY_BINDING = Mod.MODID + ":binding";
    }

    public static class Misc {
        public static final int POTION_ARRAY_SIZE = Potion.potionTypes.length;
    }
}
