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

    public static Item activationCrystal;
    public static Item slate;
    public static Item inscriptionTool;

    public static Item sacrificialDagger;
    public static Item packSelfSacrifice;
    public static Item packSacrifice;
    public static Item daggerOfSacrifice;
    public static Item ritualDiviner;
    public static Item ritualReader;

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
    public static Item sigilHolding;

    public static Item sigilTeleposition;
    public static Item sigilTransposition;

    public static Item itemComponent;
    public static Item itemDemonCrystal;
    public static Item telepositionFocus;
    public static Item experienceTome;

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
    public static Item sentientAxe;
    public static Item sentientPickaxe;
    public static Item sentientShovel;

    public static Item nodeRouter;
    public static Item baseItemFilter;

    public static Item cuttingFluid;

    public static Item sanguineBook;

    public static Item.ToolMaterial boundToolMaterial = EnumHelper.addToolMaterial("BoundToolMaterial", 4, 1, 10, 8, 50);
    public static Item.ToolMaterial soulToolMaterial = EnumHelper.addToolMaterial("SoulToolMaterial", 4, 520, 7, 8, 50);

    public static void init()
    {
        bloodOrb = registerItem(new ItemBloodOrb(), Constants.BloodMagicItem.BLOOD_ORB.getRegName());
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

        activationCrystal = registerItem(new ItemActivationCrystal(), Constants.BloodMagicItem.ACTIVATION_CRYSTAL.getRegName());
        slate = registerItem(new ItemSlate(), Constants.BloodMagicItem.SLATE.getRegName());
        inscriptionTool = registerItem(new ItemInscriptionTool(), Constants.BloodMagicItem.INSCRIPTION_TOOL.getRegName());

        sacrificialDagger = registerItem(new ItemSacrificialDagger(), Constants.BloodMagicItem.SACRIFICIAL_DAGGER.getRegName());
        packSacrifice = registerItem(new ItemPackSacrifice(), Constants.BloodMagicItem.SACRIFICE_PACK.getRegName());
        packSelfSacrifice = registerItem(new ItemPackSelfSacrifice(), Constants.BloodMagicItem.SELF_SACRIFICE_PACK.getRegName());
        daggerOfSacrifice = registerItem(new ItemDaggerOfSacrifice(), Constants.BloodMagicItem.DAGGER_OF_SACRIFICE.getRegName());

        ritualDiviner = registerItem(new ItemRitualDiviner(), Constants.BloodMagicItem.RITUAL_DIVINER.getRegName());
        ritualReader = registerItem(new ItemRitualReader(), Constants.BloodMagicItem.RITUAL_READER.getRegName());

        lavaCrystal = registerItem(new ItemLavaCrystal(), Constants.BloodMagicItem.LAVA_CRYSTAL.getRegName());
        GameRegistry.registerFuelHandler((IFuelHandler) lavaCrystal);

        boundSword = registerItem(new ItemBoundSword(), Constants.BloodMagicItem.BOUND_SWORD.getRegName());
        boundPickaxe = registerItem(new ItemBoundPickaxe(), Constants.BloodMagicItem.BOUND_PICKAXE.getRegName());
        boundAxe = registerItem(new ItemBoundAxe(), Constants.BloodMagicItem.BOUND_AXE.getRegName());
        boundShovel = registerItem(new ItemBoundShovel(), Constants.BloodMagicItem.BOUND_SHOVEL.getRegName());

        sigilDivination = registerItem(new ItemSigilDivination(), Constants.BloodMagicItem.SIGIL_DIVINATION.getRegName());
        sigilAir = registerItem(new ItemSigilAir(), Constants.BloodMagicItem.SIGIL_AIR.getRegName());
        sigilWater = registerItem(new ItemSigilWater(), Constants.BloodMagicItem.SIGIL_WATER.getRegName());
        sigilLava = registerItem(new ItemSigilLava(), Constants.BloodMagicItem.SIGIL_LAVA.getRegName());
        sigilVoid = registerItem(new ItemSigilVoid(), Constants.BloodMagicItem.SIGIL_VOID.getRegName());
        sigilGreenGrove = registerItem(new ItemSigilGreenGrove(), Constants.BloodMagicItem.SIGIL_GREEN_GROVE.getRegName());
        sigilBloodLight = registerItem(new ItemSigilBloodLight(), Constants.BloodMagicItem.SIGIL_BLOOD_LIGHT.getRegName());
        sigilElementalAffinity = registerItem(new ItemSigilElementalAffinity(), Constants.BloodMagicItem.SIGIL_ELEMENTAL_AFFINITY.getRegName());
        sigilMagnetism = registerItem(new ItemSigilMagnetism(), Constants.BloodMagicItem.SIGIL_MAGNETISM.getRegName());
        sigilSuppression = registerItem(new ItemSigilSuppression(), Constants.BloodMagicItem.SIGIL_SUPPRESION.getRegName());
        sigilHaste = registerItem(new ItemSigilHaste(), Constants.BloodMagicItem.SIGIL_HASTE.getRegName());
        sigilFastMiner = registerItem(new ItemSigilFastMiner(), Constants.BloodMagicItem.SIGIL_FAST_MINER.getRegName());
        sigilSeer = registerItem(new ItemSigilSeer(), Constants.BloodMagicItem.SIGIL_SEER.getRegName());
        sigilPhantomBridge = registerItem(new ItemSigilPhantomBridge(), Constants.BloodMagicItem.SIGIL_PHANTOM_BRIDGE.getRegName());
        sigilWhirlwind = registerItem(new ItemSigilWhirlwind(), Constants.BloodMagicItem.SIGIL_WHIRLWIND.getRegName());
        sigilCompression = registerItem(new ItemSigilCompression(), Constants.BloodMagicItem.SIGIL_COMPRESSION.getRegName());
        sigilEnderSeverance = registerItem(new ItemSigilEnderSeverance(), Constants.BloodMagicItem.SIGIL_ENDER_SEVERANCE.getRegName());
        sigilHolding = registerItem(new ItemSigilHolding(), Constants.BloodMagicItem.SIGIL_HOLDING.getRegName());

        sigilTeleposition = registerItem(new ItemSigilTeleposition(), Constants.BloodMagicItem.SIGIL_TELEPOSITION.getRegName());
        sigilTransposition = registerItem(new ItemSigilTransposition(), Constants.BloodMagicItem.SIGIL_TRANSPOSITION.getRegName());

        itemComponent = registerItem(new ItemComponent(), Constants.BloodMagicItem.COMPONENT.getRegName());
        itemDemonCrystal = registerItem(new ItemDemonCrystal(), Constants.BloodMagicItem.DEMON_CRYSTAL.getRegName());
        telepositionFocus = registerItem(new ItemTelepositionFocus(), Constants.BloodMagicItem.TELEPOSITION_FOCUS.getRegName());
        experienceTome = registerItem(new ItemExperienceBook(), Constants.BloodMagicItem.EXPERIENCE_TOME.getRegName());

        bloodShard = registerItem(new ItemBloodShard(), Constants.BloodMagicItem.BLOOD_SHARD.getRegName());

        livingArmourHelmet = registerItem(new ItemLivingArmour(EntityEquipmentSlot.HEAD), "ItemLivingArmourHelmet");
        livingArmourChest = registerItem(new ItemLivingArmour(EntityEquipmentSlot.CHEST), "ItemLivingArmourChest");
        livingArmourLegs = registerItem(new ItemLivingArmour(EntityEquipmentSlot.LEGS), "ItemLivingArmourLegs");
        livingArmourBoots = registerItem(new ItemLivingArmour(EntityEquipmentSlot.FEET), "ItemLivingArmourBoots");

        sentientArmourHelmet = registerItem(new ItemSentientArmour(EntityEquipmentSlot.HEAD), "ItemSentientArmourHelmet");
        sentientArmourChest = registerItem(new ItemSentientArmour(EntityEquipmentSlot.CHEST), "ItemSentientArmourChest");
        sentientArmourLegs = registerItem(new ItemSentientArmour(EntityEquipmentSlot.LEGS), "ItemSentientArmourLegs");
        sentientArmourBoots = registerItem(new ItemSentientArmour(EntityEquipmentSlot.FEET), "ItemSentientArmourBoots");

        altarMaker = registerItem(new ItemAltarMaker(), Constants.BloodMagicItem.ALTAR_MAKER.getRegName());
        upgradeTome = registerItem(new ItemUpgradeTome(), Constants.BloodMagicItem.UPGRADE_TOME.getRegName());
        upgradeTrainer = registerItem(new ItemUpgradeTrainer(), Constants.BloodMagicItem.UPGRADE_TRAINER.getRegName());

        arcaneAshes = registerItem(new ItemArcaneAshes(), Constants.BloodMagicItem.ARCANE_ASHES.getRegName());
        monsterSoul = registerItem(new ItemMonsterSoul(), Constants.BloodMagicItem.MONSTER_SOUL.getRegName());
        soulGem = registerItem(new ItemSoulGem(), Constants.BloodMagicItem.SOUL_GEM.getRegName());
        soulSnare = registerItem(new ItemSoulSnare(), Constants.BloodMagicItem.SOUL_SNARE.getRegName());

        sentientSword = registerItem(new ItemSentientSword(), Constants.BloodMagicItem.SENTIENT_SWORD.getRegName());
        sentientBow = registerItem(new ItemSentientBow(), Constants.BloodMagicItem.SENTIENT_BOW.getRegName());
        sentientArmourGem = registerItem(new ItemSentientArmourGem(), Constants.BloodMagicItem.SENTIENT_ARMOR_GEM.getRegName());
        sentientAxe = registerItem(new ItemSentientAxe(), Constants.BloodMagicItem.SENTIENT_AXE.getRegName());
        sentientPickaxe = registerItem(new ItemSentientPickaxe(), Constants.BloodMagicItem.SENTIENT_PICKAXE.getRegName());
        sentientShovel = registerItem(new ItemSentientShovel(), Constants.BloodMagicItem.SENTIENT_SHOVEL.getRegName());

        nodeRouter = registerItem(new ItemNodeRouter(), Constants.BloodMagicItem.NODE_ROUTER.getRegName());
        baseItemFilter = registerItem(new ItemRouterFilter(), Constants.BloodMagicItem.ROUTER_FILTER.getRegName());

        cuttingFluid = registerItem(new ItemCuttingFluid(), Constants.BloodMagicItem.CUTTING_FLUID.getRegName());

        sanguineBook = registerItem(new ItemSanguineBook(), Constants.BloodMagicItem.SANGUINE_BOOK.getRegName());
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();

        final ResourceLocation holdingLoc = new ResourceLocation("bloodmagic", "item/ItemSigilHolding");
        ModelLoader.setCustomMeshDefinition(sigilHolding, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.COLOR))
                    return new ModelResourceLocation(holdingLoc, "type=color");
                return new ModelResourceLocation(holdingLoc, "type=normal");
            }
        });
        ModelLoader.registerItemVariants(sigilHolding, new ModelResourceLocation(holdingLoc, "type=normal"));
        ModelLoader.registerItemVariants(sigilHolding, new ModelResourceLocation(holdingLoc, "type=color"));

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbWeak"));
        OrbRegistry.registerOrbTexture(orbApprentice, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbApprentice"));
        OrbRegistry.registerOrbTexture(orbMagician, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMagician"));
        OrbRegistry.registerOrbTexture(orbMaster, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbMaster"));
        OrbRegistry.registerOrbTexture(orbArchmage, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbArchmage"));
        OrbRegistry.registerOrbTexture(orbTranscendent, new ResourceLocation(Constants.Mod.DOMAIN + "ItemBloodOrbTranscendent"));

        renderHelper.itemRender(sentientBow, 0, "ItemSentientBow");
//        renderHelper.itemRender(sentientBow, 1, "ItemSentientBow_pulling_0");
//        renderHelper.itemRender(sentientBow, 2, "ItemSentientBow_pulling_1");
//        renderHelper.itemRender(sentientBow, 3, "ItemSentientBow_pulling_2");

        renderHelper.itemRender(sentientArmourGem, 0, "ItemSentientArmourGem0");
        renderHelper.itemRender(sentientArmourGem, 1, "ItemSentientArmourGem1");
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
