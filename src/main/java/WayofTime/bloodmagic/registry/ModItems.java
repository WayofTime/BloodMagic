package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.item.*;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.item.gear.ItemPackSelfSacrifice;
import WayofTime.bloodmagic.item.sigil.*;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

    public static Item sacrificialDagger;
    public static Item packSelfSacrifice;
    public static Item packSacrifice;

    public static Item sigilDivination;
    public static Item sigilAir;
    public static Item sigilWater;
    public static Item sigilLava;
    public static Item sigilVoid;

    public static Item livingArmourHelmet;
    public static Item livingArmourChest;
    public static Item livingArmourLegs;
    public static Item livingArmourBoots;

    public static Item altarMaker;

    public static void init() {
        bloodOrb = registerItem(new ItemBloodOrb());
        BloodMagicAPI.setOrbItem(bloodOrb);
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

        sacrificialDagger = registerItem(new ItemSacrificialDagger());
        packSacrifice = registerItem(new ItemPackSacrifice());
        packSelfSacrifice = registerItem(new ItemPackSelfSacrifice());

        sigilDivination = registerItem(new ItemSigilDivination());
        sigilAir = registerItem(new ItemSigilAir());
        sigilWater = registerItem(new ItemSigilWater());
        sigilLava = registerItem(new ItemSigilLava());
        sigilVoid = registerItem(new ItemSigilVoid());

        livingArmourHelmet = registerItem(new ItemLivingArmour(0), "ItemLivingArmourHelmet");
        livingArmourChest = registerItem(new ItemLivingArmour(1), "ItemLivingArmourChest");
        livingArmourLegs = registerItem(new ItemLivingArmour(2), "ItemLivingArmourLegs");
        livingArmourBoots = registerItem(new ItemLivingArmour(3), "ItemLivingArmourBoots");

        altarMaker = registerItem(new ItemAltarMaker());
    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = BloodMagic.instance.getRenderHelper();

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

        renderHelper.itemRender(sacrificialDagger, 0);
        renderHelper.itemRender(sacrificialDagger, 1);
        renderHelper.itemRender(packSacrifice);
        renderHelper.itemRender(packSelfSacrifice);

        renderHelper.itemRender(sigilDivination);
        renderHelper.itemRender(sigilAir);
        renderHelper.itemRender(sigilWater);
        renderHelper.itemRender(sigilLava);
        renderHelper.itemRender(sigilVoid);

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
