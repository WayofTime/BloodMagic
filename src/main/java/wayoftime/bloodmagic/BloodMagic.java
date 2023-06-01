package wayoftime.bloodmagic;

import com.google.gson.Gson;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.client.ClientEvents;
import wayoftime.bloodmagic.client.hud.ElementRegistry;
import wayoftime.bloodmagic.client.hud.Elements;
import wayoftime.bloodmagic.client.key.BloodMagicKeyHandler;
import wayoftime.bloodmagic.client.key.KeyBindingBloodMagic;
import wayoftime.bloodmagic.client.key.KeyBindings;
import wayoftime.bloodmagic.client.model.MimicModelLoader;
import wayoftime.bloodmagic.client.model.SigilHoldingModelLoader;
import wayoftime.bloodmagic.client.sounds.SoundRegisterListener;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.*;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.loot.BloodMagicLootFunctionManager;
import wayoftime.bloodmagic.common.loot.BloodMagicLootTypeManager;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.common.tile.BloodMagicTileEntities;
import wayoftime.bloodmagic.compat.CuriosCompat;
import wayoftime.bloodmagic.compat.patchouli.RegisterPatchouliMultiblocks;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.data.DungeonRoomProvider;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.recipe.IngredientBloodOrb;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.impl.BloodMagicCorePlugin;
import wayoftime.bloodmagic.loot.GlobalLootModifier;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.CapabilityRuneType;
import wayoftime.bloodmagic.ritual.ModRituals;
import wayoftime.bloodmagic.ritual.RitualManager;
import wayoftime.bloodmagic.structures.ModDungeons;
import wayoftime.bloodmagic.structures.ModRoomPools;
import wayoftime.bloodmagic.util.handler.event.GenericHandler;
import wayoftime.bloodmagic.util.handler.event.WillHandler;

@Mod("bloodmagic")
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BloodMagic {
    public static final String MODID = "bloodmagic";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final BloodMagicPacketHandler packetHandler = new BloodMagicPacketHandler();
    public static final RitualManager RITUAL_MANAGER = new RitualManager();
    public static final CuriosCompat curiosCompat = new CuriosCompat();
    // Custom ItemGroup TAB
    public static final CreativeModeTab TAB = new CreativeModeTab("bloodmagictab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get());
        }
    };
    public static final String NAME = "Blood Magic: Alchemical Wizardry";
    public static Boolean curiosLoaded;
    private static final Gson GSON = null;

    public BloodMagic() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::onLoadComplete);

//		BloodMagicItems.BLOOD_ORBS.register(modBus);
//		LivingArmorRegistrar.UPGRADES.register(modBus);
//		AnointmentRegistrar.ANOINTMENTS.register(modBus);
        BloodMagicItems.BLOOD_ORBS.createAndRegister(modBus, BloodOrb.class);
        LivingArmorRegistrar.UPGRADES.createAndRegister(modBus, LivingUpgrade.class);
        AnointmentRegistrar.ANOINTMENTS.createAndRegister(modBus, Anointment.class);

        BloodMagicBlocks.BLOCKS.register(modBus);
        BloodMagicItems.ITEMS.register(modBus);

//		RegistrarBloodMagic.BLOOD_ORBS.createAndRegister(modBus, "bloodorbs");
//		BloodMagicItems.BLOOD_ORBS.createAndRegister(modBus, "bloodorbs");
//		LivingArmorRegistrar.UPGRADES.createAndRegister(modBus, "upgrades");
//		AnointmentRegistrar.ANOINTMENTS.createAndRegister(modBus, "anointments");

        BloodMagicItems.BASICITEMS.register(modBus);
        BloodMagicBlocks.BASICBLOCKS.register(modBus);
        BloodMagicBlocks.DUNGEONBLOCKS.register(modBus);
        BloodMagicBlocks.FLUID_TYPES.register(modBus);
        BloodMagicBlocks.FLUIDS.register(modBus);
        BloodMagicBlocks.CONTAINERS.register(modBus);
        BloodMagicEntityTypes.ENTITY_TYPES.register(modBus);
        BloodMagicTileEntities.TILE_ENTITIES.register(modBus);

        GlobalLootModifier.GLM.register(modBus);
        BloodMagicLootTypeManager.ENTRY_TYPES.register(modBus);
        BloodMagicLootFunctionManager.LOOT_FUNCTIONS.register(modBus);

        BloodMagicRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        BloodMagicRecipeType.RECIPE_TYPES.register(modBus);
        BloodMagicPotions.MOB_EFFECTS.register(modBus);

        // Register the setup method for modloading
        modBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modBus.addListener(this::doClientStuff);
        modBus.addListener(this::initRenderLayer);
        modBus.addListener(this::loadModels);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::onRegisterCapabilities);

        // FIXME
//        modBus.addListener(this::registerRecipes);
        modBus.addListener(ConfigManager::onCommonReload);
        modBus.addListener(ConfigManager::onClientReload);

        MinecraftForge.EVENT_BUS.register(new GenericHandler());
        MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
//		MinecraftForge.EVENT_BUS.register(new ClientHandler());
        modBus.addListener(this::registerColors);

        MinecraftForge.EVENT_BUS.register(new WillHandler());
//		MinecraftForge.EVENT_BUS.register(new BloodMagicBlocks());
//		MinecraftForge.EVENT_BUS.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_SPEC);
        context.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_SPEC);

//		BloodMagicLootFunctionManager.register();
//		BloodMagicLootTypeManager.register();

        ModDungeons.init();
        ModRoomPools.init();
    }

    public static ResourceLocation rl(String name) {
        return new ResourceLocation(BloodMagic.MODID, name);
    }

    public static void handleConfigValues(BloodMagicAPI api) {
        for (String value : ConfigManager.COMMON.sacrificialValues.get()) {
            String[] split = value.split(";");
            if (split.length != 2) // Not valid format
                continue;

            api.getValueManager().setSacrificialValue(new ResourceLocation(split[0]), Integer.parseInt(split[1]));
        }

        for (String value : ConfigManager.COMMON.wellOfSuffering.get()) {
            api.getBlacklist().addWellOfSuffering(new ResourceLocation(value));
        }
    }

    private void registerRecipes(RegisterEvent event) {
//		System.out.println("Registering IngredientBloodOrb Serializer.");
        if (event.getRegistryKey() == ForgeRegistries.Keys.RECIPE_SERIALIZERS) {
            CraftingHelper.register(IngredientBloodOrb.NAME, IngredientBloodOrb.Serializer.INSTANCE);
        }

//		System.out.println("Testing after IngredientBloodOrb");

//		event.getRegistry().registerAll(new SpecialRecipeSerializer<>(TestSpecialRecipe::new).setRegistryName("test"));

//        event.getRegistry().registerAll(
//                new SewingRecipe.Serializer().setRegistryName("sewing")
//        );
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CapabilityRuneType.class);
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        OrbRegistry.tierMap.put(BloodMagicItems.ORB_WEAK.get().getTier(), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()));
        OrbRegistry.tierMap.put(BloodMagicItems.ORB_APPRENTICE.get().getTier(), new ItemStack(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()));
        OrbRegistry.tierMap.put(BloodMagicItems.ORB_MAGICIAN.get().getTier(), new ItemStack(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()));
        OrbRegistry.tierMap.put(BloodMagicItems.ORB_MASTER.get().getTier(), new ItemStack(BloodMagicItems.MASTER_BLOOD_ORB.get()));
        OrbRegistry.tierMap.put(BloodMagicItems.ORB_ARCHMAGE.get().getTier(), new ItemStack(BloodMagicItems.ARCHMAGE_BLOOD_ORB.get()));
        BloodMagicCorePlugin.INSTANCE.register(BloodMagicAPI.INSTANCE);
        RITUAL_MANAGER.discover();
        ModRituals.initHarvestHandlers();
        LivingArmorRegistrar.register();
        AnointmentRegistrar.register();
        AlchemyArrayRegistry.registerBaseArrays();
        handleConfigValues(BloodMagicAPI.INSTANCE);

        ModRoomPools.registerSpecialRooms();

        if (curiosLoaded) {
            curiosCompat.registerInventory();
        }
        if (ModList.get().isLoaded("patchouli")) {
            new RegisterPatchouliMultiblocks();
        }
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
//		GSON = new GsonBuilder().registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).create();

        DataGenerator gen = event.getGenerator();

//        if(event.includeClient())
        {
            ItemModelProvider itemModels = new GeneratorItemModels(gen, event.getExistingFileHelper());
            gen.addProvider(event.includeServer(), itemModels);
            gen.addProvider(event.includeServer(), new GeneratorBlockStates(gen, itemModels.existingFileHelper));
            gen.addProvider(event.includeServer(), new GeneratorLanguage(gen));
            gen.addProvider(event.includeServer(), new BloodMagicRecipeProvider(gen));
            gen.addProvider(event.includeServer(), new GeneratorBaseRecipes(gen));
            gen.addProvider(event.includeServer(), new GeneratorLootTable(gen));
            gen.addProvider(event.includeServer(), new DungeonRoomProvider(gen));

            GeneratorBlockTags bmBlockTags = new GeneratorBlockTags(gen, event.getExistingFileHelper());
            gen.addProvider(event.includeServer(), bmBlockTags);
            gen.addProvider(event.includeServer(), new GeneratorItemTags(gen, bmBlockTags, event.getExistingFileHelper()));
            gen.addProvider(event.includeServer(), new GeneratorFluidTags(gen, event.getExistingFileHelper()));

        }
    }

    private void loadModels(final RegisterGeometryLoaders event) {
        event.register("mimicloader", new MimicModelLoader(BloodMagic.rl("block/solidopaquemimic")));
        event.register("mimicloader_ethereal", new MimicModelLoader(BloodMagic.rl("block/etherealopaquemimic")));

        event.register("loader_holding", new SigilHoldingModelLoader(BloodMagic.rl("item/sigilofholding_base")));
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
//		LOGGER.info("HELLO FROM PREINIT");
//		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        packetHandler.initialize();

        curiosLoaded = ModList.get().isLoaded("curios");
    }

    //	@OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
//		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

        ClientEvents.initClientEvents(event);
        Elements.registerElements();
        ElementRegistry.readConfig();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new KeyBindingBloodMagic());
        new BloodMagicKeyHandler();
//		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
//
    }

    private void initRenderLayer(EntityRenderersEvent.AddLayers event) {
        ClientEvents.initRenderLayer(event);
    }

    private void registerColors(final RegisterColorHandlersEvent event) {
        if (event instanceof RegisterColorHandlersEvent.Item)
            ClientEvents.colorHandlerEvent((RegisterColorHandlersEvent.Item) event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
//		InterModComms.sendTo("examplemod", "helloworld", () -> {
//			LOGGER.info("Hello world from the MDK");
//			return "Hello world";
//		});

        if (curiosLoaded) {
            curiosCompat.setupSlots(event);
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
//		LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
//		@SubscribeEvent
//		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
//		{
        // register a new block here
//			LOGGER.info("HELLO from Register Block");
//		}

        public static void onRecipeTypeRegister() {

        }
    }
}
