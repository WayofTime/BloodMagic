package WayofTime.bloodmagic;

import java.io.File;

import WayofTime.bloodmagic.client.gui.GuiHandler;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.proxy.CommonProxy;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModCompatibility;
import WayofTime.bloodmagic.registry.ModEntities;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.registry.ModRecipes;
import WayofTime.bloodmagic.registry.ModRituals;
import WayofTime.bloodmagic.util.handler.EventHandler;

@Mod(modid = Constants.Mod.MODID, name = Constants.Mod.NAME, version = Constants.Mod.VERSION, dependencies = Constants.Mod.DEPEND, acceptedMinecraftVersions = "[1.8.8,1.8.9]", guiFactory = "WayofTime.bloodmagic.client.gui.config.ConfigGuiFactory")
@Getter
public class BloodMagic
{

    @SidedProxy(serverSide = "WayofTime.bloodmagic.proxy.CommonProxy", clientSide = "WayofTime.bloodmagic.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance(Constants.Mod.MODID)
    public static BloodMagic instance;

    public static CreativeTabs tabBloodMagic = new CreativeTabs(Constants.Mod.MODID + ".creativeTab")
    {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.bloodOrb;
        }
    };

    private LogHelper logger = new LogHelper(Constants.Mod.MODID);
    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configDir = new File(event.getModConfigurationDirectory(), "BloodMagic");
        ConfigHandler.init(new File(getConfigDir(), "BloodMagic.cfg"));

        MinecraftForge.EVENT_BUS.register(new EventHandler());

        ModBlocks.init();
        ModItems.init();
        ModPotions.init();
        ModEntities.init();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        BloodMagicPacketHandler.init();

        ModRecipes.init();
        ModRituals.initRituals();
        ModRituals.initImperfectRituals();
        ModCompatibility.registerModCompat();
        ConfigHandler.checkRituals();
        NetworkRegistry.INSTANCE.registerGuiHandler(BloodMagic.instance, new GuiHandler());

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModRecipes.addCompressionHandlers();

        proxy.postInit();
    }
}
