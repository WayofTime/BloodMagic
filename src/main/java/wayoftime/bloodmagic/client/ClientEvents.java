package wayoftime.bloodmagic.client;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.anointment.AnointmentColor;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.client.model.MimicColor;
import wayoftime.bloodmagic.client.render.RenderItemRoutingNode;
import wayoftime.bloodmagic.client.render.alchemyarray.BeaconAlchemyCircleRenderer;
import wayoftime.bloodmagic.client.render.alchemyarray.DayAlchemyCircleRenderer;
import wayoftime.bloodmagic.client.render.alchemyarray.LowStaticAlchemyCircleRenderer;
import wayoftime.bloodmagic.client.render.alchemyarray.NightAlchemyCircleRenderer;
import wayoftime.bloodmagic.client.render.alchemyarray.StaticAlchemyCircleRenderer;
import wayoftime.bloodmagic.client.render.block.RenderAlchemyArray;
import wayoftime.bloodmagic.client.render.block.RenderAltar;
import wayoftime.bloodmagic.client.render.block.RenderDemonCrucible;
import wayoftime.bloodmagic.client.render.entity.BloodLightRenderer;
import wayoftime.bloodmagic.client.render.entity.EntityMeteorRenderer;
import wayoftime.bloodmagic.client.render.entity.EntityShapedChargeRenderer;
import wayoftime.bloodmagic.client.render.entity.EntityThrowingDaggerRenderer;
import wayoftime.bloodmagic.client.render.entity.SoulSnareRenderer;
import wayoftime.bloodmagic.client.render.entity.layers.BloodElytraLayer;
import wayoftime.bloodmagic.client.screens.ScreenAlchemicalReactionChamber;
import wayoftime.bloodmagic.client.screens.ScreenAlchemyTable;
import wayoftime.bloodmagic.client.screens.ScreenFilter;
import wayoftime.bloodmagic.client.screens.ScreenHolding;
import wayoftime.bloodmagic.client.screens.ScreenItemRoutingNode;
import wayoftime.bloodmagic.client.screens.ScreenMasterRoutingNode;
import wayoftime.bloodmagic.client.screens.ScreenSoulForge;
import wayoftime.bloodmagic.client.screens.ScreenTeleposer;
import wayoftime.bloodmagic.client.screens.ScreenTrainingBracelet;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemSacrificialDagger;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilToggleable;
import wayoftime.bloodmagic.common.item.soul.ItemSentientSword;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRendererRegistry;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.SigilHoldingPacket;
import wayoftime.bloodmagic.potion.FlaskColor;
import wayoftime.bloodmagic.tile.TileAlchemyArray;
import wayoftime.bloodmagic.tile.TileAltar;
import wayoftime.bloodmagic.tile.TileDemonCrucible;
import wayoftime.bloodmagic.tile.routing.TileInputRoutingNode;
import wayoftime.bloodmagic.tile.routing.TileOutputRoutingNode;
import wayoftime.bloodmagic.tile.routing.TileRoutingNode;
import wayoftime.bloodmagic.util.GhostItemHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, RenderAltar::new);
		ClientRegistry.bindTileEntityRenderer(TileAlchemyArray.TYPE, RenderAlchemyArray::new);
		ClientRegistry.bindTileEntityRenderer(TileDemonCrucible.TYPE, RenderDemonCrucible::new);

		ClientRegistry.bindTileEntityRenderer(TileRoutingNode.TYPE, RenderItemRoutingNode::new);
		ClientRegistry.bindTileEntityRenderer(TileInputRoutingNode.TYPE, RenderItemRoutingNode::new);
		ClientRegistry.bindTileEntityRenderer(TileOutputRoutingNode.TYPE, RenderItemRoutingNode::new);

//		ClientRegistry.bindTileEntityRenderer(TileSoulForge.TYPE, RenderAlchemyArray::new);
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

	public static void colorHandlerEvent(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.SILK_TOUCH_ANOINTMENT.get(), BloodMagicItems.FORTUNE_ANOINTMENT.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(), BloodMagicItems.BOW_POWER_ANOINTMENT.get(), BloodMagicItems.WILL_POWER_ANOINTMENT.get(), BloodMagicItems.SMELTING_ANOINTMENT.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.BOW_POWER_ANOINTMENT_STRONG.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_L.get(), BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get(), BloodMagicItems.FORTUNE_ANOINTMENT_L.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_L.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_L.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_L.get(), BloodMagicItems.LOOTING_ANOINTMENT_L.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_L.get(), BloodMagicItems.SMELTING_ANOINTMENT_L.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_L.get());
		event.getItemColors().register(new AnointmentColor(), BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_2.get(), BloodMagicItems.FORTUNE_ANOINTMENT_2.get(), BloodMagicItems.HOLY_WATER_ANOINTMENT_2.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_2.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_2.get(), BloodMagicItems.LOOTING_ANOINTMENT_2.get(), BloodMagicItems.BOW_POWER_ANOINTMENT_2.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_2.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get());
		event.getItemColors().register(new FlaskColor(), BloodMagicItems.ALCHEMY_FLASK_LINGERING.get());
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
		BloodMagicPacketHandler.INSTANCE.sendToServer(new SigilHoldingPacket(player.inventory.selected, mode));
		ItemStack newStack = ItemSigilHolding.getItemStackInSlot(stack, ItemSigilHolding.getCurrentItemOrdinal(stack));
		player.displayClientMessage(newStack.isEmpty() ? new TextComponent("") : newStack.getDisplayName(), true);
	}

	@SubscribeEvent
	public void onMouseEvent(InputEvent.MouseScrollEvent event)
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
				event.getToolTip().add(new TranslatableComponent("tooltip.bloodmagic.ghost.everything"));
			} else
			{
				event.getToolTip().add(new TranslatableComponent("tooltip.bloodmagic.ghost.amount", amount));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void initClientEvents(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.SNARE.getEntityType(), SoulSnareRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.THROWING_DAGGER.getEntityType(), EntityThrowingDaggerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.getEntityType(), EntityThrowingDaggerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), BloodLightRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), EntityShapedChargeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.METEOR.getEntityType(), EntityMeteorRenderer::new);

		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.FLASK.getEntityType(), SoulSnareRenderer::new);

		DeferredWorkQueue.runLater(() -> {
			RenderType rendertype = RenderType.cutoutMipped();
			ItemBlockRenderTypes.setRenderLayer(BloodMagicBlocks.ALCHEMY_TABLE.get(), rendertype);
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
				public float call(ItemStack stack, ClientLevel world, LivingEntity entity)
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

		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
		PlayerRenderer render;
		render = skinMap.get("default");
		render.addLayer(new BloodElytraLayer(render));
		render = skinMap.get("slim");
		render.addLayer(new BloodElytraLayer(render));
	}

	public static void registerItemModelProperties(FMLClientSetupEvent event)
	{

	}

	public static void registerToggleableProperties(Item item)
	{
		ItemProperties.register(item, BloodMagic.rl("active"), new ItemPropertyFunction()
		{
			@Override
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity)
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
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity)
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
			public float call(ItemStack stack, ClientLevel world, LivingEntity entity)
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

	@SubscribeEvent
	public static void onTextureStitchEvent(TextureStitchEvent.Pre event)
	{
		event.addSprite(BloodMagic.rl("item/curios_empty_living_armour_socket"));
	}
}
