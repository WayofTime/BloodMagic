package WayofTime.bloodmagic.api;

import java.util.Locale;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Constants
{
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
    }

    public static class Mod
    {
        public static final String MODID = "BloodMagic";
        public static final String DOMAIN = MODID.toLowerCase(Locale.ENGLISH) + ":";
        public static final String NAME = "Blood Magic: Alchemical Wizardry";
        public static final String VERSION = "@VERSION@";
        public static final String DEPEND = "required-after:Forge@[12.16.0.1840,);after:JEI@[2.23.0,);required-after:guideapi;";
    }

    public static final class Gui
    {
        public static final int TELEPOSER_GUI = 0;
        public static final int SOUL_FORGE_GUI = 1;
        public static final int ROUTING_NODE_GUI = 2;
        public static final int MASTER_ROUTING_NODE_GUI = 3;
        public static final int ALCHEMY_TABLE_GUI = 4;
        public static final int SIGIL_HOLDING_GUI = 5;
    }

    public static class Compat
    {
        public static final String JEI_CATEGORY_ALTAR = Mod.MODID + ":altar";
        public static final String JEI_CATEGORY_BINDING = Mod.MODID + ":binding";
        public static final String JEI_CATEGORY_ALCHEMYARRAY = Mod.MODID + ":alchemyArray";
        public static final String JEI_CATEGORY_SOULFORGE = Mod.MODID + ":soulForge";
        public static final String JEI_CATEGORY_ALCHEMYTABLE = Mod.MODID + ":salchemyTable";
        public static final String JEI_CATEGORY_ARMOURDOWNGRADE = Mod.MODID + ":armourDowngrade";

        public static final String WAILA_CONFIG_BYPASS_SNEAK = Mod.MODID + ".bypassSneak";
        public static final String WAILA_CONFIG_ALTAR = Mod.MODID + ".bloodAltar";
        public static final String WAILA_CONFIG_TELEPOSER = Mod.MODID + ".teleposer";
        public static final String WAILA_CONFIG_RITUAL = Mod.MODID + ".ritualController";
        public static final String WAILA_CONFIG_ARRAY = Mod.MODID + ".array";
        public static final String WAILA_CONFIG_BLOOD_TANK = Mod.MODID + ".bloodTank";

        public static final Item THAUMCRAFT_GOGGLES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("Thaumcraft", "goggles"));
    }

    public static class Misc
    {
        public static final int POTION_ARRAY_SIZE = 256;
        public static final float ALTERED_STEP_HEIGHT = 1.00314159f;
        public static final int NIGHT_VISION_CONSTANT_BEGIN = 30002;
        public static final int NIGHT_VISION_CONSTANT_END = 30000;
    }

    public enum BloodMagicItem
    {
        ACTIVATION_CRYSTAL("ItemActivationCrystal"),
        ALTAR_MAKER("ItemAltarMaker"),
        ARCANE_ASHES("ItemArcaneAshes"),
        BLOOD_ORB("ItemBloodOrb"),
        BOUND_AXE("ItemBoundAxe"),
        BLOOD_SHARD("ItemBloodShard"),
        BOUND_PICKAXE("ItemBoundPickaxe"),
        BOUND_SHOVEL("ItemBoundShovel"),
        BOUND_SWORD("ItemBoundSword"),
        /**
         * @deprecated - Use
         *             {@code UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, BloodMagicAPI.getLifeEssence())}
         **/
        @Deprecated
        BUCKET_ESSENCE("ItemBucketEssence"),
        COMPONENT("ItemComponent"),
        CUTTING_FLUID("ItemCuttingFluid"),
        DEMON_CRYSTAL("ItemDemonCrystal"),
        DAGGER_OF_SACRIFICE("ItemDaggerOfSacrifice"),
        INSCRIPTION_TOOL("ItemInscriptionTool"),
        LAVA_CRYSTAL("ItemLavaCrystal"),
        LIVING_ARMOR_HELMET("ItemLivingArmourHelmet"),
        LIVING_ARMOR_CHEST("ItemLivingArmourChest"),
        LIVING_ARMOR_LEGS("ItemLivingArmourLegs"),
        LIVING_ARMOR_BOOTS("ItemLivingArmourBoots"),
        MONSTER_SOUL("ItemMonsterSoul"),
        NODE_ROUTER("ItemNodeRouter"),
        RITUAL_DIVINER("ItemRitualDiviner"),
        ROUTER_FILTER("ItemRouterFilter"),
        SACRIFICIAL_DAGGER("ItemSacrificialDagger"),
        SACRIFICE_PACK("ItemPackSacrifice"),
        SELF_SACRIFICE_PACK("ItemPackSelfSacrifice"),
        SENTIENT_ARMOR_HELMET("ItemSentientArmourHelmet"),
        SENTIENT_ARMOR_CHEST("ItemSentientArmourChest"),
        SENTIENT_ARMOR_LEGS("ItemSentientArmourLegs"),
        SENTIENT_ARMOR_BOOTS("ItemSentientArmourBoots"),
        SENTIENT_ARMOR_GEM("ItemSentientArmourGem"),
        SENTIENT_AXE("ItemSentientAxe"),
        SENTIENT_BOW("ItemSentientBow"),
        SENTIENT_PICKAXE("ItemSentientPickaxe"),
        SENTIENT_SHOVEL("ItemSentientShovel"),
        SENTIENT_SWORD("ItemSentientSword"),
        SOUL_GEM("ItemSoulGem"),
        SOUL_SNARE("ItemSoulSnare"),
        SIGIL_AIR("ItemSigilAir"),
        SIGIL_BLOOD_LIGHT("ItemSigilBloodLight"),
        SIGIL_COMPRESSION("ItemSigilCompression"),
        SIGIL_DIVINATION("ItemSigilDivination"),
        SIGIL_ELEMENTAL_AFFINITY("ItemSigilElementalAffinity"),
        SIGIL_ENDER_SEVERANCE("ItemSigilEnderSeverance"),
        SIGIL_FAST_MINER("ItemSigilFastMiner"),
        SIGIL_GREEN_GROVE("ItemSigilGreenGrove"),
        SIGIL_HASTE("ItemSigilHaste"),
        SIGIL_LAVA("ItemSigilLava"),
        SIGIL_MAGNETISM("ItemSigilMagnetism"),
        SIGIL_PHANTOM_BRIDGE("ItemSigilPhantomBridge"),
        SIGIL_SEER("ItemSigilSeer"),
        SIGIL_SUPPRESION("ItemSigilSuppression"),
        SIGIL_VOID("ItemSigilVoid"),
        SIGIL_WATER("ItemSigilWater"),
        SIGIL_WHIRLWIND("ItemSigilWhirlwind"),
        SLATE("ItemSlate"),
        TELEPOSITION_FOCUS("ItemTelepositionFocus"),
        UPGRADE_TOME("ItemUpgradeTome"),
        UPGRADE_TRAINER("ItemUpgradeTrainer"),
        SIGIL_TELEPOSITION("ItemSigilTeleposition"),
        EXPERIENCE_TOME("ItemExperienceBook"),
        SIGIL_TRANSPOSITION("ItemSigilTransposition"),
        RITUAL_READER("ItemRitualReader"),
        SANGUINE_BOOK("ItemSanguineBook"),
        SIGIL_HOLDING("ItemSigilHolding"),
        ARMOUR_POINTS_UPGRADE("ItemLivingArmourPointsUpgrade"),
        DEMON_WILL_GAUGE("ItemDemonWillGauge"),
        POTION_FLASK("ItemPotionFlask"),
        SIGIL_CLAW("ItemSigilClaw"),
        SIGIL_BOUNCE("ItemSigilBounce"),
        SIGIL_FROST("ItemSigilFrost");

        @Getter
        private final String regName;

        BloodMagicItem(String regName)
        {
            this.regName = regName;
        }

        public Item getItem()
        {
            return BloodMagicAPI.getItem(getRegName());
        }
    }

    public enum BloodMagicBlock
    {
        ALCHEMY_ARRAY("BlockAlchemyArray"),
        ALTAR("BlockAltar"),
        BLOOD_LIGHT("BlockBloodLight"),
        BLOOD_RUNE("BlockBloodRune"),
        BLOOD_STONE("BlockBloodStoneBrick"),
        CRYSTAL("BlockCrystal"),
        INPUT_ROUTING_NODE("BlockInputRoutingNode"),
        ITEM_ROUTING_NODE("BlockItemRoutingNode"),
        LIFE_ESSENCE("BlockLifeEssence"),
        MASTER_ROUTING_NODE("BlockMasterRoutingNode"),
        OUTPUT_ROUTING_NODE("BlockOutputRoutingNode"),
        @Deprecated
        PEDESTAL("BlockPedestal"),
        /** No longer included in the mod. */
        PHANTOM("BlockPhantom"),
        RITUAL_CONTROLLER("BlockRitualController"),
        RITUAL_STONE("BlockRitualStone"),
        SOUL_FORGE("BlockSoulForge"),
        SPECTRAL("BlockSpectral"),
        TELEPOSER("BlockTeleposer"),
        INCENSE_ALTAR("BlockIncenseAltar"),
        PATH("BlockPath"),
        DEMON_CRUCIBLE("BlockDemonCrucible"),
        DEMON_PYLON("BlockDemonPylon"),
        DEMON_CRYSTALLIZER("BlockDemonCrystallizer"),
        DEMON_CRYSTAL("BlockDemonCrystal"),
        DIMENSIONAL_PORTAL("BlockDimensionalPortal"),
        BLOOD_TANK("BlockBloodTank"),
        MIMIC("BlockMimic"),
        ALCHEMY_TABLE("BlockAlchemyTable"),
        DEMON_BRICK_1("BlockDemonBricks1"),
        DEMON_BRICK_2("BlockDemonBricks2"),
        DEMON_BLOCK_EXTRA("BlockDemonExtra"),
        DEMON_PILLAR_1("BlockPillar1"),
        DEMON_PILLAR_2("BlockPillar2"),
        DEMON_PILLAR_CAP_1("BlockPillarCap1"),
        DEMON_PILLAR_CAP_2("BlockPillarCap2"),
        DEMON_PILLAR_CAP_3("BlockPillarCap3"),
        DEMON_WALL_1("BlockWall1"),
        DEMON_STAIRS_1("BlockStairs1"),
        DEMON_STAIRS_2("BlockStairs2"),
        DEMON_STAIRS_3("BlockStairs3"),
        DEMON_LIGHT("BlockDemonLight"),
        INVERSION_PILLAR("BlockInversionPillar"),
        INVERSION_PILLAR_END("BlockInversionPillarEnd");

        @Getter
        private final String regName;

        BloodMagicBlock(String regName)
        {
            this.regName = regName;
        }

        public Block getBlock()
        {
            return BloodMagicAPI.getBlock(getRegName());
        }
    }
}
