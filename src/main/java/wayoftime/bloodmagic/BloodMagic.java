package wayoftime.bloodmagic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import wayoftime.bloodmagic.client.ClientEvents;
import wayoftime.bloodmagic.client.hud.Elements;
import wayoftime.bloodmagic.client.key.BloodMagicKeyHandler;
import wayoftime.bloodmagic.client.key.KeyBindings;
import wayoftime.bloodmagic.client.model.MimicModelLoader;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.GeneratorBaseRecipes;
import wayoftime.bloodmagic.common.data.GeneratorBlockStates;
import wayoftime.bloodmagic.common.data.GeneratorBlockTags;
import wayoftime.bloodmagic.common.data.GeneratorFluidTags;
import wayoftime.bloodmagic.common.data.GeneratorItemModels;
import wayoftime.bloodmagic.common.data.GeneratorItemTags;
import wayoftime.bloodmagic.common.data.GeneratorLanguage;
import wayoftime.bloodmagic.common.data.GeneratorLootTable;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.compat.CuriosCompat;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.recipe.IngredientBloodOrb;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.impl.BloodMagicCorePlugin;
import wayoftime.bloodmagic.loot.GlobalLootModifier;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.ModRituals;
import wayoftime.bloodmagic.ritual.RitualManager;
import wayoftime.bloodmagic.structures.ModDungeons;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.TileAlchemyArray;
import wayoftime.bloodmagic.tile.TileAlchemyTable;
import wayoftime.bloodmagic.tile.TileAltar;
import wayoftime.bloodmagic.tile.TileDeforesterCharge;
import wayoftime.bloodmagic.tile.TileDemonCrucible;
import wayoftime.bloodmagic.tile.TileDemonCrystal;
import wayoftime.bloodmagic.tile.TileDemonCrystallizer;
import wayoftime.bloodmagic.tile.TileFungalCharge;
import wayoftime.bloodmagic.tile.TileIncenseAltar;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.tile.TileMimic;
import wayoftime.bloodmagic.tile.TileShapedExplosive;
import wayoftime.bloodmagic.tile.TileSoulForge;
import wayoftime.bloodmagic.tile.TileVeinMineCharge;
import wayoftime.bloodmagic.util.handler.event.GenericHandler;
import wayoftime.bloodmagic.util.handler.event.WillHandler;

@Mod("bloodmagic")
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BloodMagic
{
	public static final String MODID = "bloodmagic";
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	private static Gson GSON = null;

	public static final BloodMagicPacketHandler packetHandler = new BloodMagicPacketHandler();
	public static final RitualManager RITUAL_MANAGER = new RitualManager();

	public static Boolean curiosLoaded;
	public static final CuriosCompat curiosCompat = new CuriosCompat();

	public BloodMagic()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setup);
		modBus.addListener(this::onLoadComplete);

		BloodMagicBlocks.BLOCKS.register(modBus);
		BloodMagicItems.ITEMS.register(modBus);
//		RegistrarBloodMagic.BLOOD_ORBS.createAndRegister(modBus, "bloodorbs");
		BloodMagicItems.BLOOD_ORBS.createAndRegister(modBus, "bloodorbs");
		LivingArmorRegistrar.UPGRADES.createAndRegister(modBus, "upgrades");
		AnointmentRegistrar.ANOINTMENTS.createAndRegister(modBus, "anointments");
		BloodMagicItems.BASICITEMS.register(modBus);
		BloodMagicBlocks.BASICBLOCKS.register(modBus);
		BloodMagicBlocks.DUNGEONBLOCKS.register(modBus);
		BloodMagicBlocks.FLUIDS.register(modBus);
		BloodMagicBlocks.CONTAINERS.register(modBus);
		BloodMagicEntityTypes.ENTITY_TYPES.register(modBus);

		GlobalLootModifier.GLM.register(modBus);

		BloodMagicRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);

		// Register the setup method for modloading
		modBus.addListener(this::setup);
		// Register the enqueueIMC method for modloading
		modBus.addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		modBus.addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		modBus.addListener(this::doClientStuff);
		modBus.addListener(this::loadModels);
		modBus.addListener(this::gatherData);

		modBus.addGenericListener(Fluid.class, this::registerFluids);
		modBus.addGenericListener(TileEntityType.class, this::registerTileEntityTypes);
		modBus.addGenericListener(IRecipeSerializer.class, this::registerRecipes);
		modBus.addGenericListener(Effect.class, BloodMagicPotions::registerPotions);

		MinecraftForge.EVENT_BUS.register(new GenericHandler());
//		MinecraftForge.EVENT_BUS.register(new ClientHandler());
		modBus.addListener(this::registerColors);

		MinecraftForge.EVENT_BUS.register(new WillHandler());
//		MinecraftForge.EVENT_BUS.register(new BloodMagicBlocks());
//		MinecraftForge.EVENT_BUS.addListener(this::commonSetup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_SPEC);

		ModDungeons.init();
	}

	private void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
//		System.out.println("Registering IngredientBloodOrb Serializer.");
		CraftingHelper.register(IngredientBloodOrb.NAME, IngredientBloodOrb.Serializer.INSTANCE);

//        event.getRegistry().registerAll(
//                new SewingRecipe.Serializer().setRegistryName("sewing")
//        );
	}

	public static ResourceLocation rl(String name)
	{
		return new ResourceLocation(BloodMagic.MODID, name);
	}

	public void registerFluids(RegistryEvent.Register<Fluid> event)
	{

	}

	public void onLoadComplete(FMLLoadCompleteEvent event)
	{
		OrbRegistry.tierMap.put(BloodMagicItems.ORB_WEAK.get().getTier(), new ItemStack(BloodMagicItems.WEAK_BLOOD_ORB.get()));
		OrbRegistry.tierMap.put(BloodMagicItems.ORB_APPRENTICE.get().getTier(), new ItemStack(BloodMagicItems.APPRENTICE_BLOOD_ORB.get()));
		OrbRegistry.tierMap.put(BloodMagicItems.ORB_MAGICIAN.get().getTier(), new ItemStack(BloodMagicItems.MAGICIAN_BLOOD_ORB.get()));
		OrbRegistry.tierMap.put(BloodMagicItems.ORB_MASTER.get().getTier(), new ItemStack(BloodMagicItems.MASTER_BLOOD_ORB.get()));
		BloodMagicCorePlugin.INSTANCE.register(BloodMagicAPI.INSTANCE);
		RITUAL_MANAGER.discover();
		ModRituals.initHarvestHandlers();
		LivingArmorRegistrar.register();
		AnointmentRegistrar.register();
		AlchemyArrayRegistry.registerBaseArrays();

		if (curiosLoaded)
		{
			curiosCompat.registerInventory();
		}
	}

	public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event)
	{
		LOGGER.info("Attempting to register Tile Entities");
		event.getRegistry().register(TileEntityType.Builder.create(TileAltar::new, BloodMagicBlocks.BLOOD_ALTAR.get()).build(null).setRegistryName("altar"));
		event.getRegistry().register(TileEntityType.Builder.create(TileAlchemyArray::new, BloodMagicBlocks.ALCHEMY_ARRAY.get()).build(null).setRegistryName("alchemyarray"));
		event.getRegistry().register(TileEntityType.Builder.create(TileSoulForge::new, BloodMagicBlocks.SOUL_FORGE.get()).build(null).setRegistryName("soulforge"));
		event.getRegistry().register(TileEntityType.Builder.create(TileMasterRitualStone::new, BloodMagicBlocks.MASTER_RITUAL_STONE.get()).build(null).setRegistryName("masterritualstone"));
		event.getRegistry().register(TileEntityType.Builder.create(TileAlchemicalReactionChamber::new, BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()).build(null).setRegistryName("alchemicalreactionchamber"));
		event.getRegistry().register(TileEntityType.Builder.create(TileAlchemyTable::new, BloodMagicBlocks.ALCHEMY_TABLE.get()).build(null).setRegistryName("alchemytable"));
		event.getRegistry().register(TileEntityType.Builder.create(TileDemonCrystal::new, BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get()).build(null).setRegistryName("demoncrystal"));
		event.getRegistry().register(TileEntityType.Builder.create(TileDemonCrucible::new, BloodMagicBlocks.DEMON_CRUCIBLE.get()).build(null).setRegistryName("demoncrucible"));
		event.getRegistry().register(TileEntityType.Builder.create(TileDemonCrystallizer::new, BloodMagicBlocks.DEMON_CRYSTALLIZER.get()).build(null).setRegistryName("demoncrystallizer"));
		event.getRegistry().register(TileEntityType.Builder.create(TileIncenseAltar::new, BloodMagicBlocks.INCENSE_ALTAR.get()).build(null).setRegistryName("incensealtar"));
		event.getRegistry().register(TileEntityType.Builder.create(TileMimic::new, BloodMagicBlocks.MIMIC.get(), BloodMagicBlocks.ETHEREAL_MIMIC.get()).build(null).setRegistryName("mimic"));
		event.getRegistry().register(TileEntityType.Builder.create(TileShapedExplosive::new, BloodMagicBlocks.SHAPED_CHARGE.get()).build(null).setRegistryName("shaped_explosive"));
		event.getRegistry().register(TileEntityType.Builder.create(TileDeforesterCharge::new, BloodMagicBlocks.DEFORESTER_CHARGE.get()).build(null).setRegistryName("deforester_charge"));
		event.getRegistry().register(TileEntityType.Builder.create(TileVeinMineCharge::new, BloodMagicBlocks.VEINMINE_CHARGE.get()).build(null).setRegistryName("veinmine_charge"));
		event.getRegistry().register(TileEntityType.Builder.create(TileFungalCharge::new, BloodMagicBlocks.FUNGAL_CHARGE.get()).build(null).setRegistryName("fungal_charge"));

	}

	@SubscribeEvent
	public void gatherData(GatherDataEvent event)
	{
//		GSON = new GsonBuilder().registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).create();

		DataGenerator gen = event.getGenerator();

//        if(event.includeClient())
		{
			ItemModelProvider itemModels = new GeneratorItemModels(gen, event.getExistingFileHelper());
			gen.addProvider(itemModels);
			gen.addProvider(new GeneratorBlockStates(gen, itemModels.existingFileHelper));
			gen.addProvider(new GeneratorLanguage(gen));
			gen.addProvider(new BloodMagicRecipeProvider(gen));
			gen.addProvider(new GeneratorBaseRecipes(gen));
			gen.addProvider(new GeneratorLootTable(gen));

			GeneratorBlockTags bmBlockTags = new GeneratorBlockTags(gen, event.getExistingFileHelper());
			gen.addProvider(bmBlockTags);
			gen.addProvider(new GeneratorItemTags(gen, bmBlockTags, event.getExistingFileHelper()));
			gen.addProvider(new GeneratorFluidTags(gen, event.getExistingFileHelper()));

		}
	}

	private void loadModels(final ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(BloodMagic.rl("mimicloader"), new MimicModelLoader(BloodMagic.rl("block/solidopaquemimic")));
		ModelLoaderRegistry.registerLoader(BloodMagic.rl("mimicloader_ethereal"), new MimicModelLoader(BloodMagic.rl("block/etherealopaquemimic")));
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		// some preinit code
//		LOGGER.info("HELLO FROM PREINIT");
//		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		packetHandler.initialize();

		curiosLoaded = ModList.get().isLoaded("curios");
	}

//	@OnlyIn(Dist.CLIENT)
	private void doClientStuff(final FMLClientSetupEvent event)
	{
		// do something that can only be done on the client
//		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

		ClientEvents.initClientEvents(event);
		Elements.registerElements();
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		KeyBindings.initializeKeys();
		new BloodMagicKeyHandler();
//		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
//		

	}

	private void registerColors(final ColorHandlerEvent event)
	{
		if (event instanceof ColorHandlerEvent.Item)
			ClientEvents.colorHandlerEvent((ColorHandlerEvent.Item) event);
	}

	private void enqueueIMC(final InterModEnqueueEvent event)
	{
		// some example code to dispatch IMC to another mod
//		InterModComms.sendTo("examplemod", "helloworld", () -> {
//			LOGGER.info("Hello world from the MDK");
//			return "Hello world";
//		});

		if (curiosLoaded)
		{
			curiosCompat.setupSlots(event);
		}
	}

	private void processIMC(final InterModProcessEvent event)
	{
		// some example code to receive and process InterModComms from other mods
//		LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event)
	{
		// do something when the server starts
//		LOGGER.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
		{
			// register a new block here
//			LOGGER.info("HELLO from Register Block");
		}
	}

	// Custom ItemGroup TAB
	public static final ItemGroup TAB = new ItemGroup("bloodmagictab")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get());
		}
	};
	public static final String NAME = "Blood Magic: Alchemical Wizardry";
}
