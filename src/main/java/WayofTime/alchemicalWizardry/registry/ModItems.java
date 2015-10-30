package WayofTime.alchemicalWizardry.registry;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ConfigHandler;
import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import WayofTime.alchemicalWizardry.api.orb.BloodOrb;
import WayofTime.alchemicalWizardry.api.registry.OrbRegistry;
import WayofTime.alchemicalWizardry.item.ItemBloodOrb;
import WayofTime.alchemicalWizardry.util.helper.InventoryRenderHelper;
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


    public static void init() {
        bloodOrb = registerItem(new ItemBloodOrb());
        AlchemicalWizardryAPI.setOrbItem(bloodOrb);
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
    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = AlchemicalWizardry.instance.getRenderHelper();

        renderHelper.itemRenderAll(bloodOrb);
        OrbRegistry.registerOrbTexture(orbWeak, AlchemicalWizardry.DOMAIN + "ItemBloodOrbWeak");
        OrbRegistry.registerOrbTexture(orbApprentice, AlchemicalWizardry.DOMAIN + "ItemBloodOrbApprentice");
        OrbRegistry.registerOrbTexture(orbMagician, AlchemicalWizardry.DOMAIN + "ItemBloodOrbMagician");
        OrbRegistry.registerOrbTexture(orbMaster, AlchemicalWizardry.DOMAIN + "ItemBloodOrbMaster");
        OrbRegistry.registerOrbTexture(orbArchmage, AlchemicalWizardry.DOMAIN + "ItemBloodOrbArchmage");
        OrbRegistry.registerOrbTexture(orbTranscendent, AlchemicalWizardry.DOMAIN + "ItemBloodOrbTranscendent");
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
