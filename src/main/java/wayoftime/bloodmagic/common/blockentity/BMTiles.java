package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.blockentity.BloodAltarRenderer;
import wayoftime.bloodmagic.client.render.blockentity.BloodTankRenderer;
import wayoftime.bloodmagic.client.render.blockentity.HellfireForgeRenderer;
import wayoftime.bloodmagic.common.block.BMBlocks;

import java.util.Set;

public class BMTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BloodMagic.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HellfireForgeTile>> HELLFIRE_FORGE_TYPE = TILES.register("hellfire_forge",
            () -> new BlockEntityType<>(HellfireForgeTile::new, Set.of(BMBlocks.HELLFIRE_FORGE.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BloodAltarTile>> BLOOD_ALTAR_TYPE = TILES.register("blood_altar",
            () -> new BlockEntityType<>(BloodAltarTile::new, Set.of(BMBlocks.BLOOD_ALTAR.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ARCTile>> ARC_TYPE = TILES.register("arc",
            () -> new BlockEntityType<>(ARCTile::new, Set.of(BMBlocks.ARC_BLOCK.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BloodTankTile>> BLOOD_TANK_TYPE = TILES.register("blood_tank",
            () -> new BlockEntityType<>(BloodTankTile::new, Set.of(BMBlocks.BLOOD_TANK.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LivingStationTile>> LIVING_STATION_TYPE = TILES.register("living_station",
            () -> new BlockEntityType<>(LivingStationTile::new, Set.of(BMBlocks.LIVING_STATION.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneAshesTile>> ARCANE_ASHES = TILES.register("arcane_ashes",
            () -> new BlockEntityType<>(ArcaneAshesTile::new, Set.of(BMBlocks.ARCANE_ASHES.block().get()), null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemyTableTile>> ALCHEMY_TABLE_TYPE = TILES.register("alchemy_table",
            () -> new BlockEntityType<>(AlchemyTableTile::new, Set.of(BMBlocks.ALCHEMY_TABLE.block().get()), null));

    private static void registerTileCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                HELLFIRE_FORGE_TYPE.get(),
                HellfireForgeTile::getInventory
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BLOOD_ALTAR_TYPE.get(),
                (tile, side) -> tile.getInventory()
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BLOOD_ALTAR_TYPE.get(),
                (tile, side) -> side == null ? null : tile
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ARC_TYPE.get(),
                ARCTile::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ARC_TYPE.get(),
                ARCTile::getFluidHandler
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BLOOD_TANK_TYPE.get(),
                BloodTankTile::getFluidHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                LIVING_STATION_TYPE.get(),
                (tile, side) -> {
                    int start = 0;
                    int end = 1;
                    if (side == Direction.UP) { // top = scrap
                        start++;
                        end++;
                    }
                    return new RangedWrapper(tile.itemCap, start, end);
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ALCHEMY_TABLE_TYPE.get(),
                AlchemyTableTile::getItemHandler
        );
    }

    private static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HELLFIRE_FORGE_TYPE.get(), HellfireForgeRenderer::new);
        event.registerBlockEntityRenderer(BLOOD_ALTAR_TYPE.get(), BloodAltarRenderer::new);
        event.registerBlockEntityRenderer(BLOOD_TANK_TYPE.get(), BloodTankRenderer::new);
    }

    public static void register(IEventBus modBus) {
        TILES.register(modBus);
        modBus.addListener(BMTiles::registerTileCapabilities);
        modBus.addListener(BMTiles::registerBlockEntityRenderer);
    }
}