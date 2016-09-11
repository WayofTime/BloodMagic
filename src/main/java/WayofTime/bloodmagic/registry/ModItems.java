package WayofTime.bloodmagic.registry;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemArcaneAshes;
import WayofTime.bloodmagic.item.ItemBloodOrb;
import WayofTime.bloodmagic.item.ItemBloodShard;
import WayofTime.bloodmagic.item.ItemBoundAxe;
import WayofTime.bloodmagic.item.ItemBoundPickaxe;
import WayofTime.bloodmagic.item.ItemBoundShovel;
import WayofTime.bloodmagic.item.ItemBoundSword;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDaggerOfSacrifice;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.ItemDemonWillGauge;
import WayofTime.bloodmagic.item.ItemExperienceBook;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import WayofTime.bloodmagic.item.ItemLavaCrystal;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import WayofTime.bloodmagic.item.ItemRitualReader;
import WayofTime.bloodmagic.item.ItemSacrificialDagger;
import WayofTime.bloodmagic.item.ItemSanguineBook;
import WayofTime.bloodmagic.item.ItemSlate;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.item.ItemUpgradeTrainer;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.item.gear.ItemPackSelfSacrifice;
import WayofTime.bloodmagic.item.routing.ItemNodeRouter;
import WayofTime.bloodmagic.item.routing.ItemRouterFilter;
import WayofTime.bloodmagic.item.sigil.ItemSigilAir;
import WayofTime.bloodmagic.item.sigil.ItemSigilBloodLight;
import WayofTime.bloodmagic.item.sigil.ItemSigilCompression;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.item.sigil.ItemSigilElementalAffinity;
import WayofTime.bloodmagic.item.sigil.ItemSigilEnderSeverance;
import WayofTime.bloodmagic.item.sigil.ItemSigilFastMiner;
import WayofTime.bloodmagic.item.sigil.ItemSigilGreenGrove;
import WayofTime.bloodmagic.item.sigil.ItemSigilHaste;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.item.sigil.ItemSigilLava;
import WayofTime.bloodmagic.item.sigil.ItemSigilMagnetism;
import WayofTime.bloodmagic.item.sigil.ItemSigilPhantomBridge;
import WayofTime.bloodmagic.item.sigil.ItemSigilSeer;
import WayofTime.bloodmagic.item.sigil.ItemSigilSuppression;
import WayofTime.bloodmagic.item.sigil.ItemSigilTeleposition;
import WayofTime.bloodmagic.item.sigil.ItemSigilTransposition;
import WayofTime.bloodmagic.item.sigil.ItemSigilVoid;
import WayofTime.bloodmagic.item.sigil.ItemSigilWater;
import WayofTime.bloodmagic.item.sigil.ItemSigilWhirlwind;
import WayofTime.bloodmagic.item.soul.ItemMonsterSoul;
import WayofTime.bloodmagic.item.soul.ItemSentientArmourGem;
import WayofTime.bloodmagic.item.soul.ItemSentientAxe;
import WayofTime.bloodmagic.item.soul.ItemSentientBow;
import WayofTime.bloodmagic.item.soul.ItemSentientPickaxe;
import WayofTime.bloodmagic.item.soul.ItemSentientShovel;
import WayofTime.bloodmagic.item.soul.ItemSentientSword;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.soul.ItemSoulSnare;
import WayofTime.bloodmagic.potion.item.ItemPotionFlask;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class ModItems
{
    public static final BloodOrb ORB_WEAK;
    public static final BloodOrb ORB_APPRENTICE;
    public static final BloodOrb ORB_MAGICIAN;
    public static final BloodOrb ORB_MASTER;
    public static final BloodOrb ORB_ARCHMAGE;
    public static final BloodOrb ORB_TRANSCENDENT;

    public static final Item BLOOD_ORB;
    public static final Item ACTIVATION_CRYSTAL;
    public static final Item SLATE;
    public static final Item INSCRIPTION_TOOL;
    public static final Item SACRIFICIAL_DAGGER;
    public static final Item PACK_SELF_SACRIFICE;
    public static final Item PACK_SACRIFICE;
    public static final Item DAGGER_OF_SACRIFICE;
    public static final Item RITUAL_DIVINER;
    public static final Item RITUAL_READER;
    public static final Item LAVA_CRYSTAL;
    public static final Item BOUND_SWORD;
    public static final Item BOUND_PICKAXE;
    public static final Item BOUND_AXE;
    public static final Item BOUND_SHOVEL;
    public static final Item SIGIL_DIVINATION;
    public static final Item SIGIL_AIR;
    public static final Item SIGIL_WATER;
    public static final Item SIGIL_LAVA;
    public static final Item SIGIL_VOID;
    public static final Item SIGIL_GREEN_GROVE;
    public static final Item SIGIL_BLOOD_LIGHT;
    public static final Item SIGIL_ELEMENTAL_AFFINITY;
    public static final Item SIGIL_HASTE;
    public static final Item SIGIL_MAGNETISM;
    public static final Item SIGIL_SUPPRESSION;
    public static final Item SIGIL_FAST_MINER;
    public static final Item SIGIL_SEER;
    public static final Item SIGIL_ENDER_SEVERANCE;
    public static final Item SIGIL_WHIRLWIND;
    public static final Item SIGIL_PHANTOM_BRIDGE;
    public static final Item SIGIL_COMPRESSION;
    public static final Item SIGIL_HOLDING;
    public static final Item SIGIL_TELEPOSITION;
    public static final Item SIGIL_TRANSPOSITION;
    public static final Item ITEM_COMPONENT;
    public static final Item ITEM_DEMON_CRYSTAL;
    public static final Item TELEPOSITION_FOCUS;
    public static final Item EXPERIENCE_TOME;
    public static final Item BLOOD_SHARD;
    public static final Item LIVING_ARMOUR_HELMET;
    public static final Item LIVING_ARMOUR_CHEST;
    public static final Item LIVING_ARMOUR_LEGS;
    public static final Item LIVING_ARMOUR_BOOTS;
    public static final Item SENTIENT_ARMOUR_HELMET;
    public static final Item SENTIENT_ARMOUR_CHEST;
    public static final Item SENTIENT_ARMOUR_LEGS;
    public static final Item SENTIENT_ARMOUR_BOOTS;
    public static final Item ALTAR_MAKER;
    public static final Item UPGRADE_TOME;
    public static final Item UPGRADE_TRAINER;
    public static final Item ARCANE_ASHES;
    public static final Item MONSTER_SOUL;
    public static final Item SOUL_GEM;
    public static final Item SOUL_SNARE;
    public static final Item SENTIENT_SWORD;
    public static final Item SENTIENT_BOW;
    public static final Item SENTIENT_ARMOUR_GEM;
    public static final Item SENTIENT_AXE;
    public static final Item SENTIENT_PICKAXE;
    public static final Item SENTIENT_SHOVEL;
    public static final Item NODE_ROUTER;
    public static final Item BASE_ITEM_FILTER;
    public static final Item CUTTING_FLUID;
    public static final Item SANGUINE_BOOK;
    public static final Item ITEM_POINTS_UPGRADE;
    public static final Item DEMON_WILL_GAUGE;
    public static final Item POTION_FLASK;

    public static final Item.ToolMaterial BOUND_TOOL_MATERIAL = EnumHelper.addToolMaterial("BoundToolMaterial", 4, 1, 10, 8, 50);
    public static final Item.ToolMaterial SOUL_TOOL_MATERIAL = EnumHelper.addToolMaterial("SoulToolMaterial", 4, 520, 7, 8, 50);

    static
    {
        BLOOD_ORB = registerItem(new ItemBloodOrb(), Constants.BloodMagicItem.BLOOD_ORB.getRegName());
        ORB_WEAK = new BloodOrb("weak", 1, 5000);
        ORB_APPRENTICE = new BloodOrb("apprentice", 2, 25000);
        ORB_MAGICIAN = new BloodOrb("magician", 3, 150000);
        ORB_MASTER = new BloodOrb("master", 4, 1000000);
        ORB_ARCHMAGE = new BloodOrb("archmage", 5, 10000000);
        ORB_TRANSCENDENT = new BloodOrb("transcendent", 6, 30000000);

        ACTIVATION_CRYSTAL = registerItem(new ItemActivationCrystal(), Constants.BloodMagicItem.ACTIVATION_CRYSTAL.getRegName());
        SLATE = registerItem(new ItemSlate(), Constants.BloodMagicItem.SLATE.getRegName());
        INSCRIPTION_TOOL = registerItem(new ItemInscriptionTool(), Constants.BloodMagicItem.INSCRIPTION_TOOL.getRegName());

        SACRIFICIAL_DAGGER = registerItem(new ItemSacrificialDagger(), Constants.BloodMagicItem.SACRIFICIAL_DAGGER.getRegName());
        PACK_SACRIFICE = registerItem(new ItemPackSacrifice(), Constants.BloodMagicItem.SACRIFICE_PACK.getRegName());
        PACK_SELF_SACRIFICE = registerItem(new ItemPackSelfSacrifice(), Constants.BloodMagicItem.SELF_SACRIFICE_PACK.getRegName());
        DAGGER_OF_SACRIFICE = registerItem(new ItemDaggerOfSacrifice(), Constants.BloodMagicItem.DAGGER_OF_SACRIFICE.getRegName());

        RITUAL_DIVINER = registerItem(new ItemRitualDiviner(), Constants.BloodMagicItem.RITUAL_DIVINER.getRegName());
        RITUAL_READER = registerItem(new ItemRitualReader(), Constants.BloodMagicItem.RITUAL_READER.getRegName());

        LAVA_CRYSTAL = registerItem(new ItemLavaCrystal(), Constants.BloodMagicItem.LAVA_CRYSTAL.getRegName());

        BOUND_SWORD = registerItem(new ItemBoundSword(), Constants.BloodMagicItem.BOUND_SWORD.getRegName());
        BOUND_PICKAXE = registerItem(new ItemBoundPickaxe(), Constants.BloodMagicItem.BOUND_PICKAXE.getRegName());
        BOUND_AXE = registerItem(new ItemBoundAxe(), Constants.BloodMagicItem.BOUND_AXE.getRegName());
        BOUND_SHOVEL = registerItem(new ItemBoundShovel(), Constants.BloodMagicItem.BOUND_SHOVEL.getRegName());

        SIGIL_DIVINATION = registerItem(new ItemSigilDivination(), Constants.BloodMagicItem.SIGIL_DIVINATION.getRegName());
        SIGIL_AIR = registerItem(new ItemSigilAir(), Constants.BloodMagicItem.SIGIL_AIR.getRegName());
        SIGIL_WATER = registerItem(new ItemSigilWater(), Constants.BloodMagicItem.SIGIL_WATER.getRegName());
        SIGIL_LAVA = registerItem(new ItemSigilLava(), Constants.BloodMagicItem.SIGIL_LAVA.getRegName());
        SIGIL_VOID = registerItem(new ItemSigilVoid(), Constants.BloodMagicItem.SIGIL_VOID.getRegName());
        SIGIL_GREEN_GROVE = registerItem(new ItemSigilGreenGrove(), Constants.BloodMagicItem.SIGIL_GREEN_GROVE.getRegName());
        SIGIL_BLOOD_LIGHT = registerItem(new ItemSigilBloodLight(), Constants.BloodMagicItem.SIGIL_BLOOD_LIGHT.getRegName());
        SIGIL_ELEMENTAL_AFFINITY = registerItem(new ItemSigilElementalAffinity(), Constants.BloodMagicItem.SIGIL_ELEMENTAL_AFFINITY.getRegName());
        SIGIL_MAGNETISM = registerItem(new ItemSigilMagnetism(), Constants.BloodMagicItem.SIGIL_MAGNETISM.getRegName());
        SIGIL_SUPPRESSION = registerItem(new ItemSigilSuppression(), Constants.BloodMagicItem.SIGIL_SUPPRESION.getRegName());
        SIGIL_HASTE = registerItem(new ItemSigilHaste(), Constants.BloodMagicItem.SIGIL_HASTE.getRegName());
        SIGIL_FAST_MINER = registerItem(new ItemSigilFastMiner(), Constants.BloodMagicItem.SIGIL_FAST_MINER.getRegName());
        SIGIL_SEER = registerItem(new ItemSigilSeer(), Constants.BloodMagicItem.SIGIL_SEER.getRegName());
        SIGIL_PHANTOM_BRIDGE = registerItem(new ItemSigilPhantomBridge(), Constants.BloodMagicItem.SIGIL_PHANTOM_BRIDGE.getRegName());
        SIGIL_WHIRLWIND = registerItem(new ItemSigilWhirlwind(), Constants.BloodMagicItem.SIGIL_WHIRLWIND.getRegName());
        SIGIL_COMPRESSION = registerItem(new ItemSigilCompression(), Constants.BloodMagicItem.SIGIL_COMPRESSION.getRegName());
        SIGIL_ENDER_SEVERANCE = registerItem(new ItemSigilEnderSeverance(), Constants.BloodMagicItem.SIGIL_ENDER_SEVERANCE.getRegName());
        SIGIL_HOLDING = registerItem(new ItemSigilHolding(), Constants.BloodMagicItem.SIGIL_HOLDING.getRegName());

        SIGIL_TELEPOSITION = registerItem(new ItemSigilTeleposition(), Constants.BloodMagicItem.SIGIL_TELEPOSITION.getRegName());
        SIGIL_TRANSPOSITION = registerItem(new ItemSigilTransposition(), Constants.BloodMagicItem.SIGIL_TRANSPOSITION.getRegName());

        ITEM_COMPONENT = registerItem(new ItemComponent(), Constants.BloodMagicItem.COMPONENT.getRegName());
        ITEM_DEMON_CRYSTAL = registerItem(new ItemDemonCrystal(), Constants.BloodMagicItem.DEMON_CRYSTAL.getRegName());
        TELEPOSITION_FOCUS = registerItem(new ItemTelepositionFocus(), Constants.BloodMagicItem.TELEPOSITION_FOCUS.getRegName());
        EXPERIENCE_TOME = registerItem(new ItemExperienceBook(), Constants.BloodMagicItem.EXPERIENCE_TOME.getRegName());

        BLOOD_SHARD = registerItem(new ItemBloodShard(), Constants.BloodMagicItem.BLOOD_SHARD.getRegName());

        LIVING_ARMOUR_HELMET = registerItem(new ItemLivingArmour(EntityEquipmentSlot.HEAD), "ItemLivingArmourHelmet");
        LIVING_ARMOUR_CHEST = registerItem(new ItemLivingArmour(EntityEquipmentSlot.CHEST), "ItemLivingArmourChest");
        LIVING_ARMOUR_LEGS = registerItem(new ItemLivingArmour(EntityEquipmentSlot.LEGS), "ItemLivingArmourLegs");
        LIVING_ARMOUR_BOOTS = registerItem(new ItemLivingArmour(EntityEquipmentSlot.FEET), "ItemLivingArmourBoots");

        SENTIENT_ARMOUR_HELMET = registerItem(new ItemSentientArmour(EntityEquipmentSlot.HEAD), "ItemSentientArmourHelmet");
        SENTIENT_ARMOUR_CHEST = registerItem(new ItemSentientArmour(EntityEquipmentSlot.CHEST), "ItemSentientArmourChest");
        SENTIENT_ARMOUR_LEGS = registerItem(new ItemSentientArmour(EntityEquipmentSlot.LEGS), "ItemSentientArmourLegs");
        SENTIENT_ARMOUR_BOOTS = registerItem(new ItemSentientArmour(EntityEquipmentSlot.FEET), "ItemSentientArmourBoots");

        ALTAR_MAKER = registerItem(new ItemAltarMaker(), Constants.BloodMagicItem.ALTAR_MAKER.getRegName());
        UPGRADE_TOME = registerItem(new ItemUpgradeTome(), Constants.BloodMagicItem.UPGRADE_TOME.getRegName());
        UPGRADE_TRAINER = registerItem(new ItemUpgradeTrainer(), Constants.BloodMagicItem.UPGRADE_TRAINER.getRegName());

        ARCANE_ASHES = registerItem(new ItemArcaneAshes(), Constants.BloodMagicItem.ARCANE_ASHES.getRegName());
        MONSTER_SOUL = registerItem(new ItemMonsterSoul(), Constants.BloodMagicItem.MONSTER_SOUL.getRegName());
        SOUL_GEM = registerItem(new ItemSoulGem(), Constants.BloodMagicItem.SOUL_GEM.getRegName());
        SOUL_SNARE = registerItem(new ItemSoulSnare(), Constants.BloodMagicItem.SOUL_SNARE.getRegName());

        SENTIENT_SWORD = registerItem(new ItemSentientSword(), Constants.BloodMagicItem.SENTIENT_SWORD.getRegName());
        SENTIENT_BOW = registerItem(new ItemSentientBow(), Constants.BloodMagicItem.SENTIENT_BOW.getRegName());
        SENTIENT_ARMOUR_GEM = registerItem(new ItemSentientArmourGem(), Constants.BloodMagicItem.SENTIENT_ARMOR_GEM.getRegName());
        SENTIENT_AXE = registerItem(new ItemSentientAxe(), Constants.BloodMagicItem.SENTIENT_AXE.getRegName());
        SENTIENT_PICKAXE = registerItem(new ItemSentientPickaxe(), Constants.BloodMagicItem.SENTIENT_PICKAXE.getRegName());
        SENTIENT_SHOVEL = registerItem(new ItemSentientShovel(), Constants.BloodMagicItem.SENTIENT_SHOVEL.getRegName());

        NODE_ROUTER = registerItem(new ItemNodeRouter(), Constants.BloodMagicItem.NODE_ROUTER.getRegName());
        BASE_ITEM_FILTER = registerItem(new ItemRouterFilter(), Constants.BloodMagicItem.ROUTER_FILTER.getRegName());

        CUTTING_FLUID = registerItem(new ItemCuttingFluid(), Constants.BloodMagicItem.CUTTING_FLUID.getRegName());

        SANGUINE_BOOK = registerItem(new ItemSanguineBook(), Constants.BloodMagicItem.SANGUINE_BOOK.getRegName());

        ITEM_POINTS_UPGRADE = registerItem(new ItemLivingArmourPointsUpgrade(), Constants.BloodMagicItem.ARMOUR_POINTS_UPGRADE.getRegName());

        DEMON_WILL_GAUGE = registerItem(new ItemDemonWillGauge(), Constants.BloodMagicItem.DEMON_WILL_GAUGE.getRegName());

        POTION_FLASK = registerItem(new ItemPotionFlask(), Constants.BloodMagicItem.POTION_FLASK.getRegName());
    }

    public static void init()
    {
        OrbRegistry.registerOrb(ORB_WEAK);
        OrbRegistry.registerOrb(ORB_APPRENTICE);
        OrbRegistry.registerOrb(ORB_MAGICIAN);
        OrbRegistry.registerOrb(ORB_MASTER);
        OrbRegistry.registerOrb(ORB_ARCHMAGE);
        OrbRegistry.registerOrb(ORB_TRANSCENDENT);

        GameRegistry.registerFuelHandler((IFuelHandler) LAVA_CRYSTAL);
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();

        final ResourceLocation holdingLoc = new ResourceLocation("bloodmagic", "item/ItemSigilHolding");
        ModelLoader.setCustomMeshDefinition(SIGIL_HOLDING, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.COLOR))
                    return new ModelResourceLocation(holdingLoc, "type=color");
                return new ModelResourceLocation(holdingLoc, "type=normal");
            }
        });
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=normal"));
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=color"));

        renderHelper.itemRenderAll(BLOOD_ORB);
        OrbRegistry.registerOrbTexture(ORB_WEAK, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbWeak"));
        OrbRegistry.registerOrbTexture(ORB_APPRENTICE, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbApprentice"));
        OrbRegistry.registerOrbTexture(ORB_MAGICIAN, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMagician"));
        OrbRegistry.registerOrbTexture(ORB_MASTER, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMaster"));
        OrbRegistry.registerOrbTexture(ORB_ARCHMAGE, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbArchmage"));
        OrbRegistry.registerOrbTexture(ORB_TRANSCENDENT, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbTranscendent"));

        renderHelper.itemRender(SENTIENT_BOW, 0, "ItemSentientBow");
//        renderHelper.itemRender(sentientBow, 1, "ItemSentientBow_pulling_0");
//        renderHelper.itemRender(sentientBow, 2, "ItemSentientBow_pulling_1");
//        renderHelper.itemRender(sentientBow, 3, "ItemSentientBow_pulling_2");

        renderHelper.itemRender(SENTIENT_ARMOUR_GEM, 0, "ItemSentientArmourGem0");
        renderHelper.itemRender(SENTIENT_ARMOUR_GEM, 1, "ItemSentientArmourGem1");
    }

    private static Item registerItem(Item item, String name)
    {
        if (!ConfigHandler.itemBlacklist.contains(name))
        {
            if (item.getRegistryName() == null)
                item.setRegistryName(name);
            GameRegistry.register(item);
            BloodMagic.proxy.tryHandleItemModel(item, name);
        }

        return item;
    }
}
