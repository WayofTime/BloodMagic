package WayofTime.bloodmagic.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
import WayofTime.bloodmagic.item.sigil.ItemSigilLava;
import WayofTime.bloodmagic.item.sigil.ItemSigilMagnetism;
import WayofTime.bloodmagic.item.sigil.ItemSigilPhantomBridge;
import WayofTime.bloodmagic.item.sigil.ItemSigilSeer;
import WayofTime.bloodmagic.item.sigil.ItemSigilSuppression;
import WayofTime.bloodmagic.item.sigil.ItemSigilVoid;
import WayofTime.bloodmagic.item.sigil.ItemSigilWater;
import WayofTime.bloodmagic.item.sigil.ItemSigilWhirlwind;
import WayofTime.bloodmagic.item.soul.ItemMonsterSoul;
import WayofTime.bloodmagic.item.soul.ItemSentientArmourGem;
import WayofTime.bloodmagic.item.soul.ItemSentientBow;
import WayofTime.bloodmagic.item.soul.ItemSentientSword;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.soul.ItemSoulSnare;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

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

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbWeak"));
        OrbRegistry.registerOrbTexture(orbApprentice, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbApprentice"));
        OrbRegistry.registerOrbTexture(orbMagician, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMagician"));
        OrbRegistry.registerOrbTexture(orbMaster, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMaster"));
        OrbRegistry.registerOrbTexture(orbArchmage, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbArchmage"));
        OrbRegistry.registerOrbTexture(orbTranscendent, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbTranscendent"));

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
        renderHelper.itemRender(ritualDiviner, 1);
        renderHelper.itemRender(ritualDiviner, 2);

        renderHelper.itemRender(lavaCrystal);

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

        for (int i = 0; i < ItemComponent.getNames().size(); i++)
            renderHelper.itemRender(itemComponent, i);
        for (int i = 0; i < ItemTelepositionFocus.names.length; i++)
            renderHelper.itemRender(telepositionFocus, i);
        for (int i = 0; i < ItemRouterFilter.names.length; i++)
            renderHelper.itemRender(baseItemFilter, i);

        renderHelper.itemRender(bloodShard, 0);
        renderHelper.itemRender(bloodShard, 1);

        renderHelper.itemRender(livingArmourHelmet, "ItemLivingArmour0");
        renderHelper.itemRender(livingArmourChest, "ItemLivingArmour1");
        renderHelper.itemRender(livingArmourLegs, "ItemLivingArmour2");
        renderHelper.itemRender(livingArmourBoots, "ItemLivingArmour3");

        renderHelper.itemRender(sentientArmourHelmet, "ItemSentientArmour0");
        renderHelper.itemRender(sentientArmourChest, "ItemSentientArmour1");
        renderHelper.itemRender(sentientArmourLegs, "ItemSentientArmour2");
        renderHelper.itemRender(sentientArmourBoots, "ItemSentientArmour3");

        renderHelper.itemRender(altarMaker);
        renderHelper.itemRender(upgradeTome);

        renderHelper.itemRender(arcaneAshes);
        renderHelper.itemRender(monsterSoul, 0);
        renderHelper.itemRender(soulGem, 0);
        renderHelper.itemRender(soulGem, 1);
        renderHelper.itemRender(soulGem, 2);
        renderHelper.itemRender(soulGem, 3);
        renderHelper.itemRender(soulGem, 4);
        renderHelper.itemRender(soulSnare);

        renderHelper.customItemRender(sentientSword, 0);
        renderHelper.customItemRender(sentientSword, 1);
        renderHelper.itemRender(sentientBow, 0, "ItemSentientBow");
        renderHelper.itemRender(sentientBow, 1, "ItemSentientBow_pulling_0");
        renderHelper.itemRender(sentientBow, 2, "ItemSentientBow_pulling_1");
        renderHelper.itemRender(sentientBow, 3, "ItemSentientBow_pulling_2");

        renderHelper.itemRender(sentientArmourGem, 0, "ItemSentientArmourGem0");
        renderHelper.itemRender(sentientArmourGem, 1, "ItemSentientArmourGem1");

        renderHelper.itemRender(nodeRouter);
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
