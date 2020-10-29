package wayoftime.bloodmagic.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.block.RenderAlchemyArray;
import wayoftime.bloodmagic.client.render.block.RenderAltar;
import wayoftime.bloodmagic.client.screens.ScreenAlchemicalReactionChamber;
import wayoftime.bloodmagic.client.screens.ScreenSoulForge;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilToggleable;
import wayoftime.bloodmagic.common.item.soul.ItemSentientSword;
import wayoftime.bloodmagic.iface.IMultiWillTool;
import wayoftime.bloodmagic.tile.TileAlchemyArray;
import wayoftime.bloodmagic.tile.TileAltar;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, RenderAltar::new);
		ClientRegistry.bindTileEntityRenderer(TileAlchemyArray.TYPE, RenderAlchemyArray::new);
//		ClientRegistry.bindTileEntityRenderer(TileSoulForge.TYPE, RenderAlchemyArray::new);
	}

	public static void registerContainerScreens()
	{
		ScreenManager.registerFactory(BloodMagicBlocks.SOUL_FORGE_CONTAINER.get(), ScreenSoulForge::new);
		ScreenManager.registerFactory(BloodMagicBlocks.ARC_CONTAINER.get(), ScreenAlchemicalReactionChamber::new);
	}

	public static void registerItemModelProperties(FMLClientSetupEvent event)
	{
		registerToggleableProperties(BloodMagicItems.GREEN_GROVE_SIGIL.get());
		registerToggleableProperties(BloodMagicItems.FAST_MINER_SIGIL.get());
		registerToggleableProperties(BloodMagicItems.MAGNETISM_SIGIL.get());
		registerToggleableProperties(BloodMagicItems.ICE_SIGIL.get());
		registerMultiWillTool(BloodMagicItems.SENTIENT_SWORD.get());
		registerMultiWillTool(BloodMagicItems.PETTY_GEM.get());
		registerMultiWillTool(BloodMagicItems.LESSER_GEM.get());
		registerMultiWillTool(BloodMagicItems.COMMON_GEM.get());

		ItemModelsProperties.registerProperty(BloodMagicItems.SENTIENT_SWORD.get(), BloodMagic.rl("active"), new IItemPropertyGetter()
		{
			@Override
			public float call(ItemStack stack, ClientWorld world, LivingEntity entity)
			{
				return ((ItemSentientSword) stack.getItem()).getActivated(stack) ? 1 : 0;
			}
		});
	}

	public static void registerToggleableProperties(Item item)
	{
		ItemModelsProperties.registerProperty(item, BloodMagic.rl("active"), new IItemPropertyGetter()
		{
			@Override
			public float call(ItemStack stack, ClientWorld world, LivingEntity entity)
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
		ItemModelsProperties.registerProperty(item, BloodMagic.rl("type"), new IItemPropertyGetter()
		{
			@Override
			public float call(ItemStack stack, ClientWorld world, LivingEntity entity)
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
}
