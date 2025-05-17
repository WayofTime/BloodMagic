package wayoftime.bloodmagic;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import wayoftime.bloodmagic.client.menu.BMMenus;
import wayoftime.bloodmagic.common.attribute.BMAttributes;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.blockentity.BMTiles;
import wayoftime.bloodmagic.common.command.BMCommands;
import wayoftime.bloodmagic.common.creativetab.BMTabs;
import wayoftime.bloodmagic.common.dataattachment.BMDataAttachments;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.structure.BMMultiblock;

@Mod(BloodMagic.MODID)
public class BloodMagic {
    public static final String MODID = "bloodmagic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation TYPE_PROPERTY = rl("will_type");
    public static final ResourceLocation INCENSE_PROPERTY = rl("incense_type");

    public static final ServerConfig SERVER_CONFIG;
    private static final ModConfigSpec SERVER_CONFIG_SPEC;

    static {
        Pair<ServerConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = pair.getLeft();
        SERVER_CONFIG_SPEC = pair.getRight();
    }

    public BloodMagic(IEventBus modBus, ModContainer container) {
        BMDataComponents.register(modBus);
        BMFluids.register(modBus);
        BMBlocks.register(modBus);
        BMTiles.register(modBus);
        BMItems.register(modBus);
        modBus.addListener(BMDataMaps::register);
        BMRegistries.register(modBus);
        BMDataAttachments.register(modBus);
        BMAttributes.register(modBus);
        BMRecipes.register(modBus);
        BMMultiblock.register(NeoForge.EVENT_BUS);
        BMMenus.register(modBus);
        BMTabs.register(modBus);

        container.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG_SPEC);

        NeoForge.EVENT_BUS.addListener(BMCommands::register);
        NeoForge.EVENT_BUS.addListener(BMCommands::register);

    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
