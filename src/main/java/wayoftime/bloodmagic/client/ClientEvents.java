package wayoftime.bloodmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.anointment.AnointmentColor;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.client.model.MimicColor;
import wayoftime.bloodmagic.client.model.MimicModelLoader;
import wayoftime.bloodmagic.client.model.SigilHoldingModelLoader;
import wayoftime.bloodmagic.client.render.BloodMagicModelLayerLocations;
import wayoftime.bloodmagic.client.render.RenderItemRoutingNode;
import wayoftime.bloodmagic.client.render.alchemyarray.*;
import wayoftime.bloodmagic.client.render.block.RenderAlchemyArray;
import wayoftime.bloodmagic.client.render.block.RenderAltar;
import wayoftime.bloodmagic.client.render.block.RenderDemonCrucible;
import wayoftime.bloodmagic.client.render.entity.*;
import wayoftime.bloodmagic.client.render.entity.layers.BloodElytraLayer;
import wayoftime.bloodmagic.client.render.model.ModelMeteor;
import wayoftime.bloodmagic.client.screens.*;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemSacrificialDagger;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilToggleable;
import wayoftime.bloodmagic.common.item.soul.ItemSentientSword;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.common.tile.BloodMagicTileEntities;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRendererRegistry;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.SigilHoldingPacket;
import wayoftime.bloodmagic.potion.FlaskColor;
import wayoftime.bloodmagic.potion.TippedDaggerColor;
import wayoftime.bloodmagic.util.GhostItemHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
//	@SubscribeEvent
//	public static void registerModels(ModelRegistryEvent event)
//	{
//
//
////		ClientRegistry.bindTileEntityRenderer(TileSoulForge.TYPE, RenderAlchemyArray::new);
//	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(BloodMagicModelLayerLocations.METEOR_LOC, ModelMeteor::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerModels(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(BloodMagicTileEntities.ALTAR_TYPE.get(), RenderAltar::new);
		event.registerBlockEntityRenderer(BloodMagicTileEntities.ALCHEMY_ARRAY_TYPE.get(), RenderAlchemyArray::new);
		event.registerBlockEntityRenderer(BloodMagicTileEntities.DEMON_CRUCIBLE_TYPE.get(), RenderDemonCrucible::new);

		event.registerBlockEntityRenderer(BloodMagicTileEntities.ROUTING_NODE_TYPE.get(), RenderItemRoutingNode::new);
		event.registerBlockEntityRenderer(BloodMagicTileEntities.INPUT_ROUTING_NODE_TYPE.get(), RenderItemRoutingNode::new);
		event.registerBlockEntityRenderer(BloodMagicTileEntities.OUTPUT_ROUTING_NODE_TYPE.get(), RenderItemRoutingNode::new);

//		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, RenderAltar::new);
//		ClientRegistry.bindTileEntityRenderer(TileAlchemyArray.TYPE, RenderAlchemyArray::new);
//		ClientRegistry.bindTileEntityRenderer(TileDemonCrucible.TYPE, RenderDemonCrucible::new);
//
//		ClientRegistry.bindTileEntityRenderer(TileRoutingNode.TYPE, RenderItemRoutingNode::new);
//		ClientRegistry.bindTileEntityRenderer(TileInputRoutingNode.TYPE, RenderItemRoutingNode::new);
//		ClientRegistry.bindTileEntityRenderer(TileOutputRoutingNode.TYPE, RenderItemRoutingNode::new);

		event.registerEntityRenderer(BloodMagicEntityTypes.SNARE.getEntityType(), SoulSnareRenderer::new);
		event.registerEntityRenderer(BloodMagicEntityTypes.THROWING_DAGGER.getEntityType(), EntityThrowingDaggerRenderer::new);
		event.registerEntityRenderer(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.getEntityType(), EntityThrowingDaggerRenderer::new);
		event.registerEntityRenderer(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), BloodLightRenderer::new);
		event.registerEntityRenderer(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), EntityShapedChargeRenderer::new);
		event.registerEntityRenderer(BloodMagicEntityTypes.METEOR.getEntityType(), EntityMeteorRenderer::new);

		event.registerEntityRenderer(BloodMagicEntityTypes.FLASK.getEntityType(), SoulSnareRenderer::new);
//		event.registerBlockEntityRenderer(BloodMagicTileEntities.ALTAR_TYPE, RenderAltar::new);
	}

	public static void registerContainerScreens()
	{
		MenuScreens.register(BloodMagicBlocks.SOUL_FORGE_CONTAINER.get(), ScreenSoulForge::new);
		MenuScreens.register(BloodMagicBlocks.ARC_CONTAINER.get(), ScreenAlchemicalReactionChamber::new);
		MenuScreens.register(BloodMagicBlocks.ALCHEMY_TABLE_CONTAINER.get(), ScreenAlchemyTable::new);
		MenuScreens.register(BloodMagicBlocks.HOLDING_CONTAINER.get(), ScreenHolding::new);
		MenuScreens.register(BloodMagicBlocks.FILTER_CONTAINER.get(), ScreenFilter::new);
		MenuScreens.register(BloodMagicBlocks.ROUTING_NODE_CONTAINER.get(), ScreenItemRoutingNode::new);
		MenuScreens.register(BloodMagicBlocks.TRAINING_BRACELET_CONTAINER.get(), ScreenTrainingBracelet::new);
		MenuScreens.register(BloodMagicBlocks.TELEPOSER_CONTAINER.get(), ScreenTeleposer::new);
		MenuScreens.register(BloodMagicBlocks.MASTER_ROUTING_NODE_CONTAINER.get(), ScreenMasterRoutingNode::new);

	}

	public static void colorHandlerEvent(RegisterColorHandlersEvent.Item event)
	{
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.SILK_TOUCH_ANOINTMENT.get(), BloodMagicItems.FORTUNE_ANOINTMENT.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(), BloodMagicItems.BOW_POWER_ANOINTMENT.get(), BloodMagicItems.WILL_POWER_ANOINTMENT.get(), BloodMagicItems.SMELTING_ANOINTMENT.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT.get(), BloodMagicItems.VOIDING_ANOINTMENT.get(), BloodMagicItems.WEAPON_REPAIR_ANOINTMENT.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.BOW_POWER_ANOINTMENT_STRONG.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_L.get(), BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get(), BloodMagicItems.FORTUNE_ANOINTMENT_L.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_L.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_L.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_L.get(), BloodMagicItems.LOOTING_ANOINTMENT_L.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_L.get(), BloodMagicItems.SMELTING_ANOINTMENT_L.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_L.get(), BloodMagicItems.VOIDING_ANOINTMENT_L.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_2.get(), BloodMagicItems.FORTUNE_ANOINTMENT_2.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_2.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_2.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_2.get(), BloodMagicItems.LOOTING_ANOINTMENT_2.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_2.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_2.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_XL.get(), BloodMagicItems.SILK_TOUCH_ANOINTMENT_XL.get(), BloodMagicItems.FORTUNE_ANOINTMENT_XL.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_XL.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_XL.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_XL.get(), BloodMagicItems.LOOTING_ANOINTMENT_XL.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_XL.get(), BloodMagicItems.SMELTING_ANOINTMENT_XL.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_XL.get(), BloodMagicItems.VOIDING_ANOINTMENT_XL.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_3.get(), BloodMagicItems.FORTUNE_ANOINTMENT_3.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_3.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_3.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_3.get(), BloodMagicItems.LOOTING_ANOINTMENT_3.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_3.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_3.get(), BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_2.get(), BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_3.get(), BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_L.get(), BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_XL.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK_LINGERING.get());
		event.getItemColors().register(new TippedDaggerColor(), BloodMagicItems.THROWING_DAGGER_COPPER_POTION.get());
	}

	public static void cycleSigil(ItemStack stack, Player player, int dWheel)
	{
		int mode = dWheel;
		if (!ConfigManager.CLIENT.sigilHoldingSkipsEmptySlots.get())
		{
			mode = ItemSigilHolding.getCurrentItemOrdinal(stack);
			mode = dWheel < 0 ? ItemSigilHolding.next(mode) : ItemSigilHolding.prev(mode);
		}

		ItemSigilHolding.cycleToNextSigil(stack, mode);
		BloodMagicPacketHandler.INSTANCE.sendToServer(new SigilHoldingPacket(player.getInventory().selected, mode));
		ItemStack newStack = ItemSigilHolding.getItemStackInSlot(stack, ItemSigilHolding.getCurrentItemOrdinal(stack));
		player.displayClientMessage(newStack.isEmpty() ? Component.literal("") : newStack.getDisplayName(), true);
	}

	@SubscribeEvent
	public void onMouseEvent(InputEvent.MouseScrollingEvent event)
	{
		LocalPlayer player = Minecraft.getInstance().player;

		if (event.getScrollDelta() != 0 && player != null && player.isShiftKeyDown())
		{
			ItemStack stack = player.getMainHandItem();

			if (!stack.isEmpty())
			{
				Item item = stack.getItem();

				if (item instanceof ItemSigilHolding)
				{
					cycleSigil(stack, player, event.getScrollDelta() > 0 ? 1 : -1);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void appendTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		AnointmentHolder holder = AnointmentHolder.fromItemStack(stack);
		AnointmentHolder.appendAnointmentTooltip(holder, event.getToolTip());
		if (GhostItemHelper.hasGhostAmount(stack))
		{
			int amount = GhostItemHelper.getItemGhostAmount(stack);
			if (amount == 0)
			{
				event.getToolTip().add(Component.translatable("tooltip.bloodmagic.ghost.everything"));
			} else
			{
				event.getToolTip().add(Component.translatable("tooltip.bloodmagic.ghost.amount", amount));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void initClientEvents(FMLClientSetupEvent event)
	{
//		DeferredWorkQueue.runLater(() -> {
		event.enqueueWork(() -> {
			RenderType rendertype = RenderType.cutoutMipped();
//			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.ALCHEMY_TABLE.get(), RenderType.entityCutoutNoCull(BloodMagic.rl("models/alchemytable")));
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.ALCHEMY_TABLE.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.GROWING_DOUBT.get(), rendertype);
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.WEAK_TAU.get(), rendertype);
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.STRONG_TAU.get(), rendertype);
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.SPIKES.get(), rendertype);
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.SPECTRAL.get(), RenderType.translucent());

			ClientEvents.registerContainerScreens();

			registerToggleableProperties(BloodMagicItems.GREEN_GROVE_SIGIL.get());
			registerToggleableProperties(BloodMagicItems.FAST_MINER_SIGIL.get());
			registerToggleableProperties(BloodMagicItems.MAGNETISM_SIGIL.get());
			registerToggleableProperties(BloodMagicItems.SUPPRESSION_SIGIL.get());
			registerToggleableProperties(BloodMagicItems.ICE_SIGIL.get());
			registerMultiWillTool(BloodMagicItems.SENTIENT_SWORD.get());
			registerMultiWillTool(BloodMagicItems.SENTIENT_AXE.get());
			registerMultiWillTool(BloodMagicItems.SENTIENT_PICKAXE.get());
			registerMultiWillTool(BloodMagicItems.SENTIENT_SHOVEL.get());
			registerMultiWillTool(BloodMagicItems.SENTIENT_SCYTHE.get());
			registerMultiWillTool(BloodMagicItems.PETTY_GEM.get());
			registerMultiWillTool(BloodMagicItems.LESSER_GEM.get());
			registerMultiWillTool(BloodMagicItems.COMMON_GEM.get());
			registerMultiWillTool(BloodMagicItems.GREATER_GEM.get());
			registerSacrificialKnife(BloodMagicItems.SACRIFICIAL_DAGGER.get());

			ItemProperties.register(BloodMagicItems.SENTIENT_SWORD.get(), BloodMagic.rl("active"), new ItemPropertyFunction()
			{
				@Override
				public float call(ItemStack stack, ClientLevel world, LivingEntity entity, int value)
				{
					return ((ItemSentientSword) stack.getItem()).getActivated(stack) ? 1 : 0;
				}
			});

			Minecraft.getInstance().getBlockColors().register(new MimicColor(), BloodMagicBlocks.MIMIC.get());
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.MIMIC.get(), (RenderType) -> true);
		});

		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/movement"), new StaticAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/movementarray.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/updraft"), new BeaconAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/updraftarray.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/spike"), new LowStaticAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/spikearray.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/day"), new DayAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/sunarray.png"), BloodMagic.rl("textures/models/alchemyarrays/sunarrayspikes.png"), BloodMagic.rl("textures/models/alchemyarrays/sunarraycircle.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/night"), new NightAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/moonarrayoutside.png"), BloodMagic.rl("textures/models/alchemyarrays/moonarraysymbols.png"), BloodMagic.rl("textures/models/alchemyarrays/moonarrayinside.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/grove"), new BeaconAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/growthsigil.png")));
		AlchemyArrayRendererRegistry.registerRenderer(BloodMagic.rl("array/bounce"), new LowStaticAlchemyCircleRenderer(BloodMagic.rl("textures/models/alchemyarrays/bouncearray.png")));

	}

	@SubscribeEvent
	public static void initRenderLayer(EntityRenderersEvent.AddLayers event)
	{
		PlayerRenderer render = event.getSkin("default");
		render.addLayer(new BloodElytraLayer(render, event.getEntityModels()));
		render = event.getSkin("slim");
		render.addLayer(new BloodElytraLayer(render, event.getEntityModels()));
	}

	@SubscribeEvent
	public static void loadModels(final ModelEvent.RegisterGeometryLoaders event) {
		event.register("mimicloader", new MimicModelLoader(BloodMagic.rl("block/solidopaquemimic")));
		event.register("mimicloader_ethereal", new MimicModelLoader(BloodMagic.rl("block/etherealopaquemimic")));

		event.register("loader_holding", new SigilHoldingModelLoader(BloodMagic.rl("item/sigilofholding_base")));
	}

	public static void registerItemModelProperties(FMLClientSetupEvent event)
	{

	}

	public static void registerToggleableProperties(Item item)
	{
		ItemProperties.register(item, BloodMagic.rl("active"), new ItemPropertyFunction()
		{
			@Override
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity, int val)
			{
				Item item = stack.getItem();
				if (item instanceof ItemSigilToggleable)
				{
					return ((ItemSigilToggleable) item).getActivated(stack) ? 1 : 0;
				}
				return 0;
			}
		});
	}

	public static void registerMultiWillTool(Item item)
	{
		ItemProperties.register(item, BloodMagic.rl("type"), new ItemPropertyFunction()
		{
			@Override
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity, int val)
			{
				Item item = stack.getItem();
				if (item instanceof IMultiWillTool)
				{
					return ((IMultiWillTool) item).getCurrentType(stack).ordinal();
				}
				return 0;
			}
		});
	}

	public static void registerSacrificialKnife(Item item)
	{
		ItemProperties.register(item, BloodMagic.rl("incense"), new ItemPropertyFunction()
		{
			@Override
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity, int val)
			{
				Item item = stack.getItem();
				if (item instanceof ItemSacrificialDagger)
				{
					return ((ItemSacrificialDagger) item).canUseForSacrifice(stack) ? 1 : 0;
				}
				return 0;
			}
		});
	}
}
