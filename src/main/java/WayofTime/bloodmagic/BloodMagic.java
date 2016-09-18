package WayofTime.bloodmagic;

import java.io.File;
import java.util.Map;

import WayofTime.bloodmagic.meteor.MeteorConfigHandler;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import WayofTime.bloodmagic.client.gui.GuiHandler;
import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.minecraft.ICrossVersionProxy;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.proxy.CommonProxy;
import WayofTime.bloodmagic.registry.ModArmourTrackers;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModCompatibility;
import WayofTime.bloodmagic.registry.ModCorruptionBlocks;
import WayofTime.bloodmagic.registry.ModEntities;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.registry.ModRecipes;
import WayofTime.bloodmagic.registry.ModRituals;
import WayofTime.bloodmagic.registry.ModTranquilityHandlers;
import WayofTime.bloodmagic.structures.ModDungeons;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.handler.IMCHandler;

import com.google.common.collect.ImmutableMap;

@Mod(modid = Constants.Mod.MODID, name = Constants.Mod.NAME, version = Constants.Mod.VERSION, dependencies = Constants.Mod.DEPEND, guiFactory = "WayofTime.bloodmagic.client.gui.config.ConfigGuiFactory")
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
            return ModItems.BLOOD_ORB;
        }
    };

    public static CreativeTabs tabUpgradeTome = new CreativeTabs(Constants.Mod.MODID + ".creativeTabTome")
    {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.UPGRADE_TOME;
        }
    };

    @Getter
    private static boolean isDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @Getter
    private static ICrossVersionProxy crossVersionProxy;
    private static final Map<String, String> PROXY_MAP = ImmutableMap.of("1.9.4", "WayofTime.bloodmagic.compat.minecraft.CrossVersionProxy19", "1.10", "WayofTime.bloodmagic.compat.minecraft.CrossVersionProxy110", "1.10.2", "WayofTime.bloodmagic.compat.minecraft.CrossVersionProxy110");

    static
    {
        try
        {
            String mcVersion = (String) Loader.class.getDeclaredField("MC_VERSION").get(null);

            if (!PROXY_MAP.containsKey(mcVersion))
                throw new IllegalStateException("Blood Magic couldn't find a cross version proxy!");

            Class proxyClass = Class.forName(PROXY_MAP.get(mcVersion));
            crossVersionProxy = (ICrossVersionProxy) proxyClass.newInstance();
        } catch (Exception e)
        {
            throw new IllegalArgumentException("Blood Magic could not find a cross version proxy!", e);
        }

        FluidRegistry.enableUniversalBucket();
    }

    private LogHelper logger = new LogHelper(Constants.Mod.MODID);
    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configDir = new File(event.getModConfigurationDirectory(), "BloodMagic");
        ConfigHandler.init(new File(getConfigDir(), "BloodMagic.cfg"));

        ModBlocks.init();
        ModItems.init();
        ModPotions.init();
        ModEntities.init();
        ModCompatibility.registerModCompat();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.PRE_INIT);
        ModTranquilityHandlers.init();
        ModDungeons.init();

        Utils.registerHandlers(event.getAsmData().getAll(Handler.class.getCanonicalName()));
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        BloodMagicPacketHandler.init();

        ModRecipes.init();
        ModRituals.initRituals();
        ModRituals.initImperfectRituals();
        MeteorConfigHandler.init(new File(configDir, "meteors"));
        ModArmourTrackers.init();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.INIT);
        NetworkRegistry.INSTANCE.registerGuiHandler(BloodMagic.instance, new GuiHandler());
        ModCorruptionBlocks.init();

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModRecipes.addCompressionHandlers();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.POST_INIT);

        proxy.postInit();
    }

    @Mod.EventHandler
    public void modMapping(FMLModIdMappingEvent event)
    {
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.MAPPING);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandBloodMagic());
    }

    @Mod.EventHandler
    public void onIMCRecieved(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.handleIMC(event);
    }
}
