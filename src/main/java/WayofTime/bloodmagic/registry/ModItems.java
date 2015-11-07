package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.item.ItemBloodOrb;
import WayofTime.bloodmagic.item.ItemBucketEssence;
import WayofTime.bloodmagic.item.ItemSacrificialDagger;
import WayofTime.bloodmagic.item.sigil.ItemSigilAir;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
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

    public static Item sigilDivination;
    public static Item sigilAir;

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

        sigilDivination = registerItem(new ItemSigilDivination());
        sigilAir = registerItem(new ItemSigilAir());
    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = BloodMagic.instance.getRenderHelper();

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, BloodMagic.DOMAIN + "ItemBloodOrbWeak");
        OrbRegistry.registerOrbTexture(orbApprentice, BloodMagic.DOMAIN + "ItemBloodOrbApprentice");
        OrbRegistry.registerOrbTexture(orbMagician, BloodMagic.DOMAIN + "ItemBloodOrbMagician");
        OrbRegistry.registerOrbTexture(orbMaster, BloodMagic.DOMAIN + "ItemBloodOrbMaster");
        OrbRegistry.registerOrbTexture(orbArchmage, BloodMagic.DOMAIN + "ItemBloodOrbArchmage");
        OrbRegistry.registerOrbTexture(orbTranscendent, BloodMagic.DOMAIN + "ItemBloodOrbTranscendent");

        renderHelper.itemRender(bucketEssence);

        renderHelper.itemRender(activationCrystal, 0);
        renderHelper.itemRender(activationCrystal, 1);
        renderHelper.itemRender(activationCrystal, 2, "ItemActivationCrystal0");

        renderHelper.itemRender(sacrificialDagger, 0);
        renderHelper.itemRender(sacrificialDagger, 1);

        renderHelper.itemRender(sigilDivination);
        renderHelper.itemRender(sigilAir);
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
