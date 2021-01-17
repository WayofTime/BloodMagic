package wayoftime.bloodmagic.util;

import wayoftime.bloodmagic.BloodMagic;

public class Constants
{
	public static final String SPEED_RUNE = "speed_rune";

	public static class NBT
	{
		public static final String OWNER_UUID = "ownerUUID";
		public static final String OWNER_NAME = "ownerNAME";
		public static final String USES = "uses";
		public static final String ACTIVATED = "activated";
		public static final String UNUSABLE = "unusable";
		public static final String SACRIFICE = "sacrifice";
		public static final String DIMENSION_ID = "dimensionId";
		public static final String X_COORD = "xCoord";
		public static final String Y_COORD = "yCoord";
		public static final String Z_COORD = "zCoord";
		public static final String PORTAL_LOCATION = "portalLocation";
		public static final String ORB_TIER = "orbTier";
		public static final String CURRENT_ESSENCE = "currentEssence";
		public static final String CURRENT_RITUAL = "currentRitual";
		public static final String CURRENT_RITUAL_TAG = "currentRitualTag";
		public static final String IS_RUNNING = "isRunning";
		public static final String IS_REDSTONED = "isStoned";
		public static final String RUNTIME = "runtime";
		public static final String DIRECTION = "direction";
		public static final String REAGENT_TANKS = "reagentTanks";
		public static final String CURRENT_INCENSE = "BM:CurrentIncense";
		public static final String MAX_INCENSE = "BM:MaxIncenseFromLastAltar";
		public static final String HAS_MAX_INCENSE = "BM:CurrentIsMaxIncense";
		public static final String CURRENT_PURITY = "BM:CurrentPurity";
		public static final String EMPTY = "Empty";
		public static final String OUTPUT_AMOUNT = "outputAmount";
		public static final String INPUT_AMOUNT = "inputAmount";
		public static final String STORED_LP = "storedLP";
		public static final String RITUAL_READER = "ritualReaderState";
		public static final String ITEMS = "Items";
		public static final String SLOT = "Slot";

		public static final String ALTAR = "bloodAltar";
		public static final String ALTAR_TIER = "upgradeLevel";
		public static final String ALTAR_ACTIVE = "isActive";
		public static final String ALTAR_LIQUID_REQ = "liquidRequired";
		public static final String ALTAR_FILLABLE = "fillable";
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
		public static final String ALTAR_TOTAL_CHARGE = "totalCharge";
		public static final String ALTAR_MAX_CHARGE = "maxCharge";
		public static final String ALTAR_CHARGE_RATE = "chargeRate";
		public static final String ALTAR_CHARGE_FREQUENCY = "chargeFrequency";
		public static final String ALTAR_CURRENT_TIER_DISPLAYED = "currentTierDisplayed";

		public static final String ALTARMAKER_CURRENT_TIER = "currentTier";

		public static final String PROJECTILE_TICKS_IN_AIR = "projectileTicksInAir";
		public static final String PROJECTILE_MAX_TICKS_IN_AIR = "projectileMaxTicksInAir";

		public static final String TICKS_REMAINING = "ticksRemaining";
		public static final String CONTAINED_BLOCK_NAME = "containedBlockName";
		public static final String CONTAINED_BLOCK_META = "containedBlockMeta";
		public static final String CONTAINED_TILE_ENTITY = "containedTileEntity";

		public static final String PREVIOUS_INPUT = "previousInput";

		public static final String LIVING_ARMOUR = "livingArmour";

		public static final String CHARGE_TIME = "chargeTime";
		public static final String HELD_DOWN = "heldDown";

		public static final String UPGRADE_POISON_TIMER = "poisonTimer";
		public static final String UPGRADE_FIRE_TIMER = "fireTimer";

		public static final String SOULS = "souls";
		public static final String SOUL_SWORD_DAMAGE = "soulSwordDamage";
		public static final String SOUL_SWORD_ACTIVE_DRAIN = "soulSwordActiveDrain";
		public static final String SOUL_SWORD_DROP = "soulSwordDrop";
		public static final String SOUL_SWORD_STATIC_DROP = "soulSwordStaticDrop";
		public static final String SOUL_SWORD_HEALTH = "soulSwordHealth";
		public static final String SOUL_SWORD_ATTACK_SPEED = "soulSwordAttackSpeed";
		public static final String SOUL_SWORD_SPEED = "soulSwordSpeed";
		public static final String SOUL_SWORD_DIG_SPEED = "soulSwordDigSpeed";
		public static final String WILL_TYPE = "demonWillType";

		public static final String SOUL_FORGE_BURN = "burnTime";
		public static final String SOUL_FORGE_CONSUMED = "consumedSouls";

		public static final String ARC_PROGRESS = "progress";

		public static final String ROUTING_MASTER = "master";
		public static final String ROUTING_CONNECTION = "connections";
		public static final String ROUTING_PRIORITY = "prioritiesPeople";
		public static final String ROUTING_MASTER_GENERAL = "generalList";
		public static final String ROUTING_MASTER_INPUT = "inputList";
		public static final String ROUTING_MASTER_OUTPUT = "outputList";

		public static final String GHOST_STACK_SIZE = "stackSize";

		public static final String ITEM_INVENTORY = "itemInventory";

		public static final String BLOCKPOS_CONNECTION = "connections";

		public static final String CURRENT_SIGIL = "currentSigil";
		public static final String MOST_SIG = "mostSig";
		public static final String LEAST_SIG = "leastSig";
		public static final String COLOR = "color";

		public static final String POTION_AUGMENT_LENGHT = "length:";
		public static final String POTION_AUGMENT_STRENGTH = "strength:";
		public static final String POTION_IMPURITY = "impurity";

		public static final String TANK = "tank";

		public static final String BREATH = "breath";

		public static final String ANOINTMENTS = "anointment_holder";
	}

	public static class JSON
	{
		public static final String INPUT = "input";
		public static final String TOOL = "tool";
		public static final String BASEINPUT = "baseinput";
		public static final String ADDEDINPUT = "addedinput";
		public static final String ADDEDOUTPUT = "addedoutput";
		public static final String OUTPUT = "output";
		public static final String ITEM = "item";
		public static final String COUNT = "count";
		public static final String NBT = "nbt";
		public static final String TAG = "tag";
		public static final String TYPE = "type";
		public static final String TEXTURE = "texture";
		public static final String CONDITIONS = "conditions";
		public static final String CHANCE = "chance";
		public static final String FLUID = "fluid";
		public static final String AMOUNT = "amount";
		public static final String INPUT_FLUID = "inputfluid";
		public static final String OUTPUT_FLUID = "outputfluid";

		public static final String SYPHON = "syphon";
		public static final String TICKS = "ticks";

		public static final String ALTAR_TIER = Constants.NBT.ALTAR_TIER;
		public static final String ALTAR_SYPHON = "altarSyphon";
		public static final String ALTAR_CONSUMPTION_RATE = Constants.NBT.ALTAR_CONSUMPTION_RATE;
		public static final String ALTAR_DRAIN_RATE = Constants.NBT.ALTAR_DRAIN_RATE;

		public static final String TARTARIC_DRAIN = "drain";
		public static final String TARTARIC_MINIMUM = "minimumDrain";
	}

	public static class Compat
	{
		public static final String JEI_CATEGORY_ALTAR = "altar";
		public static final String JEI_CATEGORY_BINDING = "binding";
		public static final String JEI_CATEGORY_ALCHEMYARRAY = "alchemyarray";
		public static final String JEI_CATEGORY_SOULFORGE = "soulforge";
		public static final String JEI_CATEGORY_ALCHEMYTABLE = "alchemytable";
		public static final String JEI_CATEGORY_ARMOURDOWNGRADE = "armourdowngrade";
		public static final String JEI_CATEGORY_ARC = "arc";

		public static final String WAILA_CONFIG_ALTAR = BloodMagic.MODID + ".bloodaltar";
		public static final String WAILA_CONFIG_TELEPOSER = BloodMagic.MODID + ".teleposer";
		public static final String WAILA_CONFIG_RITUAL = BloodMagic.MODID + ".ritualController";
		public static final String WAILA_CONFIG_ARRAY = BloodMagic.MODID + ".array";
		public static final String WAILA_CONFIG_BLOOD_TANK = BloodMagic.MODID + ".bloodTank";
	}
}
