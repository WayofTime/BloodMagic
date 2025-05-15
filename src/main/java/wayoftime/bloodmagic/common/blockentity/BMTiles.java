package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.blockentity.HellfireForgeRenderer;
import wayoftime.bloodmagic.common.block.BMBlocks;

import java.util.Set;

public class BMTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BloodMagic.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HellfireForgeTile>> HELLFIRE_FORGE_TYPE = TILES.register("hellfire_forge",
            () -> new BlockEntityType<>(HellfireForgeTile::new, Set.of(BMBlocks.HELLFIRE_FORGE.block().get()), null));

    private static void registerTileCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                HELLFIRE_FORGE_TYPE.get(),
                HellfireForgeTile::getInventory
        );
    }

    private static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HELLFIRE_FORGE_TYPE.get(), HellfireForgeRenderer::new);
    }

    public static void register(IEventBus modBus) {
        TILES.register(modBus);
        modBus.addListener(BMTiles::registerTileCapabilities);
        modBus.addListener(BMTiles::registerBlockEntityRenderer);
    }
}