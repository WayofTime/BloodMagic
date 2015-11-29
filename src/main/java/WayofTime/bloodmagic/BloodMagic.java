package WayofTime.bloodmagic;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.proxy.CommonProxy;
import WayofTime.bloodmagic.registry.*;
import WayofTime.bloodmagic.util.handler.EventHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = Constants.Mod.MODID, name = Constants.Mod.NAME, version = Constants.Mod.VERSION, dependencies = Constants.Mod.DEPEND, guiFactory = "WayofTime.bloodmagic.client.gui.ConfigGuiFactory")
@Getter
public class BloodMagic {

    @SidedProxy(serverSide = "WayofTime.bloodmagic.proxy.CommonProxy", clientSide = "WayofTime.bloodmagic.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance(Constants.Mod.MODID)
    public static BloodMagic instance;

    public static CreativeTabs tabBloodMagic = new CreativeTabs(Constants.Mod.MODID + ".creativeTab") {
        @Override
        public Item getTabIconItem() {
            return ModItems.bloodOrb;
        }
    };

    private InventoryRenderHelper renderHelper = new InventoryRenderHelper(Constants.Mod.DOMAIN);
    private LogHelper logger = new LogHelper(Constants.Mod.MODID);
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
        BloodMagicPacketHandler.init();

        ModRecipes.init();
        ModRituals.initRituals();
        ModRituals.initImperfectRituals();
        ModCompatibility.registerModCompat();
        ConfigHandler.checkRituals();

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
