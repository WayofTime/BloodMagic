package WayofTime.bloodmagic.registry;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.item.*;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.item.gear.ItemPackSelfSacrifice;
import WayofTime.bloodmagic.item.sigil.*;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class ModItems {

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

    public static Item itemComponent;

    public static Item bloodShard;
    
    public static Item livingArmourHelmet;
    public static Item livingArmourChest;
    public static Item livingArmourLegs;
    public static Item livingArmourBoots;

    public static Item altarMaker;

    public static Item.ToolMaterial boundToolMaterial = EnumHelper.addToolMaterial("BoundToolMaterial", 4, 0, 12, 8, 50);

    public static void init() {
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

        itemComponent = registerItem(new ItemComponent());

        bloodShard = registerItem(new ItemBloodShard());
        
        livingArmourHelmet = registerItem(new ItemLivingArmour(0), "ItemLivingArmourHelmet");
        livingArmourChest = registerItem(new ItemLivingArmour(1), "ItemLivingArmourChest");
        livingArmourLegs = registerItem(new ItemLivingArmour(2), "ItemLivingArmourLegs");
        livingArmourBoots = registerItem(new ItemLivingArmour(3), "ItemLivingArmourBoots");

        altarMaker = registerItem(new ItemAltarMaker());
    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, Constants.Mod.DOMAIN + "ItemBloodOrbWeak");
        OrbRegistry.registerOrbTexture(orbApprentice, Constants.Mod.DOMAIN + "ItemBloodOrbApprentice");
        OrbRegistry.registerOrbTexture(orbMagician, Constants.Mod.DOMAIN + "ItemBloodOrbMagician");
        OrbRegistry.registerOrbTexture(orbMaster, Constants.Mod.DOMAIN + "ItemBloodOrbMaster");
        OrbRegistry.registerOrbTexture(orbArchmage, Constants.Mod.DOMAIN + "ItemBloodOrbArchmage");
        OrbRegistry.registerOrbTexture(orbTranscendent, Constants.Mod.DOMAIN + "ItemBloodOrbTranscendent");

        renderHelper.itemRender(bucketEssence);

        renderHelper.itemRender(activationCrystal, 0);
        renderHelper.itemRender(activationCrystal, 1);
        renderHelper.itemRender(activationCrystal, 2, "ItemActivationCrystal0");

        renderHelper.itemRender(slate, 0);
        renderHelper.itemRender(slate, 1);
        renderHelper.itemRender(slate, 2);
        renderHelper.itemRender(slate, 3);
        renderHelper.itemRender(slate, 4);

        renderHelper.itemRender(inscriptionTool, 1);
        renderHelper.itemRender(inscriptionTool, 2);
        renderHelper.itemRender(inscriptionTool, 3);
        renderHelper.itemRender(inscriptionTool, 4);
        renderHelper.itemRender(inscriptionTool, 5);
        renderHelper.itemRender(inscriptionTool, 6);

        renderHelper.itemRender(sacrificialDagger, 0);
        renderHelper.itemRender(sacrificialDagger, 1);
        renderHelper.itemRender(packSacrifice);
        renderHelper.itemRender(packSelfSacrifice);
        renderHelper.itemRender(daggerOfSacrifice);
        
        renderHelper.itemRender(ritualDiviner, 0);

        renderHelper.itemRender(boundSword, 0);
        renderHelper.itemRender(boundSword, 1);
        renderHelper.itemRender(boundPickaxe, 0);
        renderHelper.itemRender(boundPickaxe, 1);
        renderHelper.itemRender(boundAxe, 0);
        renderHelper.itemRender(boundAxe, 1);
        renderHelper.itemRender(boundShovel, 0);
        renderHelper.itemRender(boundShovel, 1);

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

        for(int i = 0 ; i < ItemComponent.getNames().size() ; i++)
        	renderHelper.itemRender(itemComponent, i);

        renderHelper.itemRender(bloodShard, 0);
        renderHelper.itemRender(bloodShard, 1);

        renderHelper.itemRender(livingArmourHelmet);
        renderHelper.itemRender(livingArmourChest);
        renderHelper.itemRender(livingArmourLegs);
        renderHelper.itemRender(livingArmourBoots);

        renderHelper.itemRender(altarMaker);
    }

    private static Item registerItem(Item item, String name) {
        if (!ConfigHandler.itemBlacklist.contains(name))
            GameRegistry.registerItem(item, name);

        return item;
    }

    private static Item registerItem(Item item) {
        return registerItem(item, item.getClass().getSimpleName());
    }
}
