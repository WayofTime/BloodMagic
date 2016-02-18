package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.item.sigil.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemArcaneAshes;
import WayofTime.bloodmagic.item.ItemBloodOrb;
import WayofTime.bloodmagic.item.ItemBloodShard;
import WayofTime.bloodmagic.item.ItemBoundAxe;
import WayofTime.bloodmagic.item.ItemBoundPickaxe;
import WayofTime.bloodmagic.item.ItemBoundShovel;
import WayofTime.bloodmagic.item.ItemBoundSword;
import WayofTime.bloodmagic.item.ItemBucketEssence;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDaggerOfSacrifice;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import WayofTime.bloodmagic.item.ItemLavaCrystal;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import WayofTime.bloodmagic.item.ItemSacrificialDagger;
import WayofTime.bloodmagic.item.ItemSlate;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.item.ItemUpgradeTrainer;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.item.gear.ItemPackSelfSacrifice;
import WayofTime.bloodmagic.item.routing.ItemNodeRouter;
import WayofTime.bloodmagic.item.routing.ItemRouterFilter;
import WayofTime.bloodmagic.item.soul.ItemMonsterSoul;
import WayofTime.bloodmagic.item.soul.ItemSentientArmourGem;
import WayofTime.bloodmagic.item.soul.ItemSentientBow;
import WayofTime.bloodmagic.item.soul.ItemSentientSword;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.soul.ItemSoulSnare;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelperV2;

public class ModItems
{
    public static Item bloodOrb;
    public static BloodOrb orbWeak;
    public static BloodOrb orbApprentice;
    public static BloodOrb orbMagician;
    public static BloodOrb orbMaster;
    public static BloodOrb orbArchmage;
    public static BloodOrb orbTranscendent;

    public static Item bucketEssence;

    public static Item activationCrystal;
    public static Item slate;
    public static Item inscriptionTool;

    public static Item sacrificialDagger;
    public static Item packSelfSacrifice;
    public static Item packSacrifice;
    public static Item daggerOfSacrifice;
    public static Item ritualDiviner;

    public static Item lavaCrystal;

    public static Item boundSword;
    public static Item boundPickaxe;
    public static Item boundAxe;
    public static Item boundShovel;

    public static Item sigilDivination;
    public static Item sigilAir;
    public static Item sigilWater;
    public static Item sigilLava;
    public static Item sigilVoid;
    public static Item sigilGreenGrove;
    public static Item sigilBloodLight;
    public static Item sigilElementalAffinity;
    public static Item sigilHaste;
    public static Item sigilMagnetism;
    public static Item sigilSuppression;
    public static Item sigilFastMiner;
    public static Item sigilSeer;
    public static Item sigilEnderSeverance;
    public static Item sigilWhirlwind;
    public static Item sigilPhantomBridge;
    public static Item sigilCompression;

    public static Item sigilTeleposition;
    public static Item sigilTransposition;

    public static Item itemComponent;
    public static Item telepositionFocus;

    public static Item bloodShard;

    public static Item livingArmourHelmet;
    public static Item livingArmourChest;
    public static Item livingArmourLegs;
    public static Item livingArmourBoots;

    public static Item sentientArmourHelmet;
    public static Item sentientArmourChest;
    public static Item sentientArmourLegs;
    public static Item sentientArmourBoots;

    public static Item altarMaker;
    public static Item upgradeTome;
    public static Item upgradeTrainer;

    public static Item arcaneAshes;
    public static Item monsterSoul;
    public static Item soulGem;
    public static Item soulSnare;

    public static Item sentientSword;
    public static Item sentientBow;
    public static Item sentientArmourGem;

    public static Item nodeRouter;
    public static Item baseItemFilter;

    public static Item.ToolMaterial boundToolMaterial = EnumHelper.addToolMaterial("BoundToolMaterial", 4, 1, 10, 8, 50);
    public static Item.ToolMaterial soulToolMaterial = EnumHelper.addToolMaterial("SoulToolMaterial", 4, 520, 7, 8, 50);

    public static void init()
    {
        bloodOrb = registerItem(new ItemBloodOrb());
        orbWeak = new BloodOrb("weak", 1, 5000);
        OrbRegistry.registerOrb(orbWeak);
        orbApprentice = new BloodOrb("apprentice", 2, 25000);
        OrbRegistry.registerOrb(orbApprentice);
        orbMagician = new BloodOrb("magician", 3, 150000);
        OrbRegistry.registerOrb(orbMagician);
        orbMaster = new BloodOrb("master", 4, 1000000);
        OrbRegistry.registerOrb(orbMaster);
        orbArchmage = new BloodOrb("archmage", 5, 10000000);
        OrbRegistry.registerOrb(orbArchmage);
        orbTranscendent = new BloodOrb("transcendent", 6, 30000000);
        OrbRegistry.registerOrb(orbTranscendent);

        bucketEssence = registerItem(new ItemBucketEssence());

        activationCrystal = registerItem(new ItemActivationCrystal());
        slate = registerItem(new ItemSlate());
        inscriptionTool = registerItem(new ItemInscriptionTool());

        sacrificialDagger = registerItem(new ItemSacrificialDagger());
        packSacrifice = registerItem(new ItemPackSacrifice());
        packSelfSacrifice = registerItem(new ItemPackSelfSacrifice());
        daggerOfSacrifice = registerItem(new ItemDaggerOfSacrifice());

        ritualDiviner = registerItem(new ItemRitualDiviner());

        lavaCrystal = registerItem(new ItemLavaCrystal());
        GameRegistry.registerFuelHandler(new ItemLavaCrystal());

        boundSword = registerItem(new ItemBoundSword());
        boundPickaxe = registerItem(new ItemBoundPickaxe());
        boundAxe = registerItem(new ItemBoundAxe());
        boundShovel = registerItem(new ItemBoundShovel());

        sigilDivination = registerItem(new ItemSigilDivination());
        sigilAir = registerItem(new ItemSigilAir());
        sigilWater = registerItem(new ItemSigilWater());
        sigilLava = registerItem(new ItemSigilLava());
        sigilVoid = registerItem(new ItemSigilVoid());
        sigilGreenGrove = registerItem(new ItemSigilGreenGrove());
        sigilBloodLight = registerItem(new ItemSigilBloodLight());
        sigilElementalAffinity = registerItem(new ItemSigilElementalAffinity());
        sigilMagnetism = registerItem(new ItemSigilMagnetism());
        sigilSuppression = registerItem(new ItemSigilSuppression());
        sigilHaste = registerItem(new ItemSigilHaste());
        sigilFastMiner = registerItem(new ItemSigilFastMiner());
        sigilSeer = registerItem(new ItemSigilSeer());
        sigilPhantomBridge = registerItem(new ItemSigilPhantomBridge());
        sigilWhirlwind = registerItem(new ItemSigilWhirlwind());
        sigilCompression = registerItem(new ItemSigilCompression());
        sigilEnderSeverance = registerItem(new ItemSigilEnderSeverance());

        sigilTeleposition = registerItem(new ItemSigilTeleposition());
        sigilTransposition = registerItem(new ItemSigilTransposition());

        itemComponent = registerItem(new ItemComponent());
        telepositionFocus = registerItem(new ItemTelepositionFocus());

        bloodShard = registerItem(new ItemBloodShard());

        livingArmourHelmet = registerItem(new ItemLivingArmour(0), "ItemLivingArmourHelmet");
        livingArmourChest = registerItem(new ItemLivingArmour(1), "ItemLivingArmourChest");
        livingArmourLegs = registerItem(new ItemLivingArmour(2), "ItemLivingArmourLegs");
        livingArmourBoots = registerItem(new ItemLivingArmour(3), "ItemLivingArmourBoots");

        sentientArmourHelmet = registerItem(new ItemSentientArmour(0), "ItemSentientArmourHelmet");
        sentientArmourChest = registerItem(new ItemSentientArmour(1), "ItemSentientArmourChest");
        sentientArmourLegs = registerItem(new ItemSentientArmour(2), "ItemSentientArmourLegs");
        sentientArmourBoots = registerItem(new ItemSentientArmour(3), "ItemSentientArmourBoots");

        altarMaker = registerItem(new ItemAltarMaker());
        upgradeTome = registerItem(new ItemUpgradeTome());
        upgradeTrainer = registerItem(new ItemUpgradeTrainer());

        arcaneAshes = registerItem(new ItemArcaneAshes());
        monsterSoul = registerItem(new ItemMonsterSoul());
        soulGem = registerItem(new ItemSoulGem());
        soulSnare = registerItem(new ItemSoulSnare());

        sentientSword = registerItem(new ItemSentientSword());
        sentientBow = registerItem(new ItemSentientBow());
        sentientArmourGem = registerItem(new ItemSentientArmourGem());

        nodeRouter = registerItem(new ItemNodeRouter());
        baseItemFilter = registerItem(new ItemRouterFilter());
    }

    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();
        InventoryRenderHelperV2 renderHelperV2 = BloodMagic.proxy.getRenderHelperV2();

        renderHelperV2.registerRender(altarMaker, "altarMaker");
        renderHelperV2.registerRender(upgradeTome, "upgradeTome");
        renderHelperV2.registerRender(upgradeTrainer, "upgradeTrainer");

        renderHelperV2.registerRender(arcaneAshes, "arcaneAshes");
        renderHelperV2.registerRender(monsterSoul, "monsterSoul");
        renderHelperV2.registerRender(soulGem, 0, "petty");
        renderHelperV2.registerRender(soulGem, 1, "lesser");
        renderHelperV2.registerRender(soulGem, 2, "common");
        renderHelperV2.registerRender(soulGem, 3, "greater");
        renderHelperV2.registerRender(soulGem, 4, "grand");
        renderHelperV2.registerRender(soulSnare, "soulSnare");

        renderHelperV2.registerRender(slate, 0, "blank");
        renderHelperV2.registerRender(slate, 1, "reinforced");
        renderHelperV2.registerRender(slate, 2, "imbued");
        renderHelperV2.registerRender(slate, 3, "demonic");
        renderHelperV2.registerRender(slate, 4, "ethereal");

        renderHelperV2.registerRender(inscriptionTool, 1, EnumRuneType.WATER.name());
        renderHelperV2.registerRender(inscriptionTool, 2, EnumRuneType.FIRE.name());
        renderHelperV2.registerRender(inscriptionTool, 3, EnumRuneType.EARTH.name());
        renderHelperV2.registerRender(inscriptionTool, 4, EnumRuneType.AIR.name());
        renderHelperV2.registerRender(inscriptionTool, 5, EnumRuneType.DUSK.name());
        renderHelperV2.registerRender(inscriptionTool, 6, EnumRuneType.DAWN.name());

        renderHelperV2.registerRender(activationCrystal, 0, "weak");
        renderHelperV2.registerRender(activationCrystal, 1, "demonic");
        renderHelperV2.registerRender(activationCrystal, 2, "creative");

        renderHelperV2.registerRender(sacrificialDagger, 0, "normal");
        renderHelperV2.registerRender(sacrificialDagger, 1, "creative");
        renderHelperV2.registerRender(packSacrifice, "normal");
        renderHelperV2.registerRender(packSelfSacrifice, "normal");
        renderHelperV2.registerRender(daggerOfSacrifice, "normal");

        renderHelperV2.registerRender(ritualDiviner, 0, "basic");
        renderHelperV2.registerRender(ritualDiviner, 1, "dusk");
        renderHelperV2.registerRender(ritualDiviner, 2, "dawn");

        renderHelperV2.registerRender(lavaCrystal, "normal");

        renderHelperV2.registerRender(bloodShard, 0, "weak");
        renderHelperV2.registerRender(bloodShard, 1, "demonic");

        renderHelperV2.registerRender(livingArmourHelmet, "ItemLivingArmour", "helm");
        renderHelperV2.registerRender(livingArmourChest, "ItemLivingArmour", "chest");
        renderHelperV2.registerRender(livingArmourLegs, "ItemLivingArmour", "legs");
        renderHelperV2.registerRender(livingArmourBoots, "ItemLivingArmour", "boots");

        renderHelperV2.registerRender(sentientArmourHelmet, "ItemSentientArmour", "helm");
        renderHelperV2.registerRender(sentientArmourChest, "ItemSentientArmour", "chest");
        renderHelperV2.registerRender(sentientArmourLegs, "ItemSentientArmour", "legs");
        renderHelperV2.registerRender(sentientArmourBoots, "ItemSentientArmour", "boots");

//        renderHelperV2.registerRender(sentientBow, 0, "still");
//        renderHelperV2.registerRender(sentientBow, 1, "pull0");
//        renderHelperV2.registerRender(sentientBow, 2, "pull1");
//        renderHelperV2.registerRender(sentientBow, 3, "pull2");

//        renderHelperV2.registerRender(sentientArmourGem, 0, "deactivated");
//        renderHelperV2.registerRender(sentientArmourGem, 1, "activated");

        for (int i = 0; i < ItemComponent.getNames().size(); i++)
            renderHelperV2.registerRender(itemComponent, i, ItemComponent.getNames().get(i));

        renderHelperV2.registerRender(telepositionFocus, 0, "weak");
        renderHelperV2.registerRender(telepositionFocus, 1, "enhanced");
        renderHelperV2.registerRender(telepositionFocus, 2, "reinforced");
        renderHelperV2.registerRender(telepositionFocus, 3, "demonic");

        renderHelperV2.registerRender(baseItemFilter, 0, "exact");
        renderHelperV2.registerRender(baseItemFilter, 1, "ignorenbt");
        renderHelperV2.registerRender(baseItemFilter, 2, "moditems");
        renderHelperV2.registerRender(baseItemFilter, 3, "oredict");

        renderHelperV2.registerRender(nodeRouter, "normal");

        // TODO - Move all below to InventoryRenderHelperV2

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbWeak"));
        OrbRegistry.registerOrbTexture(orbApprentice, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbApprentice"));
        OrbRegistry.registerOrbTexture(orbMagician, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMagician"));
        OrbRegistry.registerOrbTexture(orbMaster, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMaster"));
        OrbRegistry.registerOrbTexture(orbArchmage, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbArchmage"));
        OrbRegistry.registerOrbTexture(orbTranscendent, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbTranscendent"));

        renderHelper.itemRender(bucketEssence);

        renderHelper.customItemRender(boundSword, 0);
        renderHelper.customItemRender(boundSword, 1);
        renderHelper.customItemRender(boundPickaxe, 0);
        renderHelper.customItemRender(boundPickaxe, 1);
        renderHelper.customItemRender(boundAxe, 0);
        renderHelper.customItemRender(boundAxe, 1);
        renderHelper.customItemRender(boundShovel, 0);
        renderHelper.customItemRender(boundShovel, 1);

        renderHelper.itemRender(sigilDivination);
        renderHelper.itemRender(sigilAir);
        renderHelper.itemRender(sigilWater);
        renderHelper.itemRender(sigilLava);
        renderHelper.itemRender(sigilVoid);
        renderHelper.itemRender(sigilGreenGrove, 0);
        renderHelper.itemRender(sigilGreenGrove, 1);
        renderHelper.itemRender(sigilBloodLight);
        renderHelper.itemRender(sigilElementalAffinity, 0);
        renderHelper.itemRender(sigilElementalAffinity, 1);
        renderHelper.itemRender(sigilMagnetism, 0);
        renderHelper.itemRender(sigilMagnetism, 1);
        renderHelper.itemRender(sigilSuppression, 0);
        renderHelper.itemRender(sigilSuppression, 1);
        renderHelper.itemRender(sigilHaste, 0);
        renderHelper.itemRender(sigilHaste, 1);
        renderHelper.itemRender(sigilFastMiner, 0);
        renderHelper.itemRender(sigilFastMiner, 1);
        renderHelper.itemRender(sigilSeer);
        renderHelper.itemRender(sigilPhantomBridge, 0);
        renderHelper.itemRender(sigilPhantomBridge, 1);
        renderHelper.itemRender(sigilWhirlwind, 0);
        renderHelper.itemRender(sigilWhirlwind, 1);
        renderHelper.itemRender(sigilCompression, 0);
        renderHelper.itemRender(sigilCompression, 1);
        renderHelper.itemRender(sigilEnderSeverance, 0);
        renderHelper.itemRender(sigilEnderSeverance, 1);

        renderHelper.itemRender(sigilTeleposition);
        renderHelper.itemRender(sigilTransposition);

        renderHelper.customItemRender(sentientSword, 0);
        renderHelper.customItemRender(sentientSword, 1);
        renderHelper.itemRender(sentientBow, 0, "ItemSentientBow");
        renderHelper.itemRender(sentientBow, 1, "ItemSentientBow_pulling_0");
        renderHelper.itemRender(sentientBow, 2, "ItemSentientBow_pulling_1");
        renderHelper.itemRender(sentientBow, 3, "ItemSentientBow_pulling_2");

        renderHelper.itemRender(sentientArmourGem, 0, "ItemSentientArmourGem0");
        renderHelper.itemRender(sentientArmourGem, 1, "ItemSentientArmourGem1");
    }

    private static Item registerItem(Item item, String name)
    {
        if (!ConfigHandler.itemBlacklist.contains(name))
            GameRegistry.registerItem(item, name);

        return item;
    }

    private static Item registerItem(Item item)
    {
        if (item.getRegistryName() == null)
        {
            BloodMagic.instance.getLogger().error("Attempted to register Item {} without setting a registry name. Item will not be registered. Please report this.", item.getClass().getCanonicalName());
            return item;
        }

        if (!ConfigHandler.itemBlacklist.contains(item.getRegistryName().split(":")[1]))
            GameRegistry.registerItem(item);

        return item;
    }
}
