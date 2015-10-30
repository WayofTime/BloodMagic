package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.api.util.helper.LogHelper;
import WayofTime.alchemicalWizardry.registry.ModBlocks;
import WayofTime.alchemicalWizardry.registry.ModEntities;
import WayofTime.alchemicalWizardry.registry.ModItems;
import WayofTime.alchemicalWizardry.registry.ModPotions;
import WayofTime.alchemicalWizardry.proxy.CommonProxy;
import WayofTime.alchemicalWizardry.util.helper.InventoryRenderHelper;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.Locale;

@Mod(modid = AlchemicalWizardry.MODID, name = AlchemicalWizardry.NAME, version = AlchemicalWizardry.VERSION, dependencies = AlchemicalWizardry.DEPEND, guiFactory = "WayofTime.alchemicalWizardry.client.gui.ConfigGuiFactory")
@Getter
public class AlchemicalWizardry {

    public static final String MODID = "AlchemicalWizardry";
    public static final String NAME = "Blood Magic: Alchemical Wizardry";
    public static final String VERSION = "@VERSION@";
    public static final String DEPEND = "";
    public static final String DOMAIN = MODID.toLowerCase(Locale.ENGLISH) + ":";

    @SidedProxy(serverSide = "WayofTime.alchemicalWizardry.proxy.CommonProxy", clientSide = "WayofTime.alchemicalWizardry.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static AlchemicalWizardry instance;

    public static CreativeTabs tabBloodMagic = new CreativeTabs(MODID + ".creativeTab") {
        @Override
        public Item getTabIconItem() {
            return ModItems.bloodOrb;
        }
    };

    private InventoryRenderHelper renderHelper = new InventoryRenderHelper(DOMAIN);
    private LogHelper logger = new LogHelper(MODID);
    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "BloodMagic");
        ConfigHandler.init(new File(getConfigDir(), "BloodMagic.cfg"));

        proxy.preInit();

        ModItems.init();
        ModBlocks.init();
        ModPotions.init();
        ModEntities.init();
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
