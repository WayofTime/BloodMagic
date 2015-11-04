package WayofTime.bloodmagic;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import WayofTime.bloodmagic.network.AlchemicalWizardryPacketHandler;
import WayofTime.bloodmagic.proxy.CommonProxy;
import WayofTime.bloodmagic.registry.*;
import WayofTime.bloodmagic.util.handler.EventHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.Locale;

@Mod(modid = BloodMagic.MODID, name = BloodMagic.NAME, version = BloodMagic.VERSION, dependencies = BloodMagic.DEPEND, guiFactory = "WayofTime.bloodmagic.client.gui.ConfigGuiFactory")
@Getter
public class BloodMagic {

    public static final String MODID = "BloodMagic";
    public static final String NAME = "Blood Magic: Alchemical Wizardry";
    public static final String VERSION = "@VERSION@";
    public static final String DEPEND = "";
    public static final String DOMAIN = MODID.toLowerCase(Locale.ENGLISH) + ":";

    @SidedProxy(serverSide = "WayofTime.bloodmagic.proxy.CommonProxy", clientSide = "WayofTime.bloodmagic.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static BloodMagic instance;

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

        EventHandler eventHandler = new EventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);

        ModBlocks.init();
        ModItems.init();
        ModPotions.init();
        ModEntities.init();


        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        AlchemicalWizardryPacketHandler.init();

        ModRecipes.init();
        logger.info(AltarRecipeRegistry.getRecipeForInput(new ItemStackWrapper(Items.diamond)));
        ModRituals.initRituals();
        ModRituals.initImperfectRituals();
        ConfigHandler.checkRituals();

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
