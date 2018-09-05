package com.wayoftime.bloodmagic;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.api.BloodMagicPlugin;
import com.wayoftime.bloodmagic.api.IBloodMagicPlugin;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicBlocks;
import com.wayoftime.bloodmagic.core.util.PluginUtil;
import com.wayoftime.bloodmagic.proxy.IProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

@Mod(modid = BloodMagic.MODID, name = BloodMagic.NAME, version = BloodMagic.VERSION)
public class BloodMagic {

    public static final String MODID = "bloodmagic";
    public static final String NAME = "Blood Magic: Alchemical Wizardry";
    public static final String VERSION = "${VERSION}";
    public static final List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> PLUGINS = Lists.newArrayList();
    public static final CreativeTabs TAB_BM = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistrarBloodMagicBlocks.BLOOD_ALTAR);
        }
    };

    @SidedProxy(clientSide = "com.wayoftime.bloodmagic.proxy.ClientProxy", serverSide = "com.wayoftime.bloodmagic.proxy.ServerProxy")
    public static IProxy PROXY;
    @Mod.Instance(value = MODID)
    public static BloodMagic INSTANCE;
    public static File configDir;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), BloodMagic.MODID);
        if (!configDir.exists())
            configDir.mkdirs();

        PLUGINS.addAll(PluginUtil.gatherPlugins(event.getAsmData()));
        PluginUtil.injectAPIInstances(PluginUtil.gatherInjections(event.getAsmData()));

        PROXY.preInit(); // TODO - Remove proxy. Switch altar model to json
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PluginUtil.handlePluginStep(PluginUtil.RegistrationStep.PLUGIN_REGISTER);
    }
}
