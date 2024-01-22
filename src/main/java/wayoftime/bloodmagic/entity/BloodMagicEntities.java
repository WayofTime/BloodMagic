package wayoftime.bloodmagic.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BloodMagicEntities
{
	private BloodMagicEntities()
	{
	}

//	@SubscribeEvent
//	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
//	{
//		System.out.println("Ow O");
//		event.getRegistry().register(EntityType.Builder.<EntitySoulSnare>create(EntitySoulSnare::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).setCustomClientFactory(((spawnEntity, world) -> new EntitySoulSnare(EntitySoulSnare.TYPE, world))).build("").setRegistryName(BloodMagic.rl("entitysoulsnare")));
//	}

//	@SubscribeEvent
//	public static void registerModels(RegisterGeometryLoaders evt)
//	{
//		System.out.println("O wO");
//		RenderingRegistry.registerEntityRenderingHandler(BloodMagicEntityTypes.SNARE.getEntityType(), SoulSnareRenderer::new);
//	}
}
