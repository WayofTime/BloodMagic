package wayoftime.bloodmagic.common.tile;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.routing.TileInputRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileMasterRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileOutputRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileRoutingNode;

public class BloodMagicTileEntities
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BloodMagic.MODID);

	public static final RegistryObject<BlockEntityType<TileAltar>> ALTAR_TYPE = TILE_ENTITIES.register("altar", () -> BlockEntityType.Builder.of(TileAltar::new, BloodMagicBlocks.BLOOD_ALTAR.get()).build(null));

	public static final RegistryObject<BlockEntityType<TileAlchemyArray>> ALCHEMY_ARRAY_TYPE = TILE_ENTITIES.register("alchemyarray", () -> BlockEntityType.Builder.of(TileAlchemyArray::new, BloodMagicBlocks.ALCHEMY_ARRAY.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileSoulForge>> SOUL_FORGE_TYPE = TILE_ENTITIES.register("soulforge", () -> BlockEntityType.Builder.of(TileSoulForge::new, BloodMagicBlocks.SOUL_FORGE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileSpikeTrap>> SPIKE_TRAP_TYPE = TILE_ENTITIES.register("spiketrap", () -> BlockEntityType.Builder.of(TileSpikeTrap::new, BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDungeonAlternator>> DUNGEON_ALTERNATOR_TYPE = TILE_ENTITIES.register("dungeonalternator", () -> BlockEntityType.Builder.of(TileDungeonAlternator::new, BloodMagicBlocks.DUNGEON_ALTERNATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileMasterRitualStone>> MASTER_RITUAL_STONE_TYPE = TILE_ENTITIES.register("masterritualstone", () -> BlockEntityType.Builder.of(TileMasterRitualStone::new, BloodMagicBlocks.MASTER_RITUAL_STONE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileAlchemicalReactionChamber>> ARC_TYPE = TILE_ENTITIES.register("alchemicalreactionchamber", () -> BlockEntityType.Builder.of(TileAlchemicalReactionChamber::new, BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileAlchemyTable>> ALCHEMY_TABLE_TYPE = TILE_ENTITIES.register("alchemytable", () -> BlockEntityType.Builder.of(TileAlchemyTable::new, BloodMagicBlocks.ALCHEMY_TABLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDemonCrystal>> DEMON_CRYSTAL_TYPE = TILE_ENTITIES.register("demoncrystal", () -> BlockEntityType.Builder.of(TileDemonCrystal::new, BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDemonCrucible>> DEMON_CRUCIBLE_TYPE = TILE_ENTITIES.register("demoncrucible", () -> BlockEntityType.Builder.of(TileDemonCrucible::new, BloodMagicBlocks.DEMON_CRUCIBLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDemonCrystallizer>> DEMON_CRYSTALLIZER_TYPE = TILE_ENTITIES.register("demoncrystallizer", () -> BlockEntityType.Builder.of(TileDemonCrystallizer::new, BloodMagicBlocks.DEMON_CRYSTALLIZER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDemonPylon>> DEMON_PYLON_TYPE = TILE_ENTITIES.register("demonpylon", () -> BlockEntityType.Builder.of(TileDemonPylon::new, BloodMagicBlocks.DEMON_PYLON.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileIncenseAltar>> INCENSE_ALTAR_TYPE = TILE_ENTITIES.register("incensealtar", () -> BlockEntityType.Builder.of(TileIncenseAltar::new, BloodMagicBlocks.INCENSE_ALTAR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileMimic>> MIMIC_TYPE = TILE_ENTITIES.register("mimic", () -> BlockEntityType.Builder.of(TileMimic::new, BloodMagicBlocks.ETHEREAL_MIMIC.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileShapedExplosive>> SHAPED_EXPLOSIVE_TYPE = TILE_ENTITIES.register("shaped_explosive", () -> BlockEntityType.Builder.of(TileShapedExplosive::new, BloodMagicBlocks.SHAPED_CHARGE.get(), BloodMagicBlocks.AUG_SHAPED_CHARGE.get(), BloodMagicBlocks.SHAPED_CHARGE_DEEP.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDeforesterCharge>> DEFORESTER_CHARGE_TYPE = TILE_ENTITIES.register("deforester_charge", () -> BlockEntityType.Builder.of(TileDeforesterCharge::new, BloodMagicBlocks.DEFORESTER_CHARGE.get(), BloodMagicBlocks.DEFORESTER_CHARGE_2.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileVeinMineCharge>> VEINMINE_CHARGE_TYPE = TILE_ENTITIES.register("veinmine_charge", () -> BlockEntityType.Builder.of(TileVeinMineCharge::new, BloodMagicBlocks.VEINMINE_CHARGE.get(), BloodMagicBlocks.VEINMINE_CHARGE_2.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileFungalCharge>> FUNGAL_CHARGE_TYPE = TILE_ENTITIES.register("fungal_charge", () -> BlockEntityType.Builder.of(TileFungalCharge::new, BloodMagicBlocks.FUNGAL_CHARGE.get(), BloodMagicBlocks.FUNGAL_CHARGE_2.get()).build(null));

	public static final RegistryObject<BlockEntityType<TileRoutingNode>> ROUTING_NODE_TYPE = TILE_ENTITIES.register("itemroutingnode", () -> BlockEntityType.Builder.of(TileRoutingNode::new, BloodMagicBlocks.ROUTING_NODE_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileInputRoutingNode>> INPUT_ROUTING_NODE_TYPE = TILE_ENTITIES.register("inputroutingnode", () -> BlockEntityType.Builder.of(TileInputRoutingNode::new, BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileOutputRoutingNode>> OUTPUT_ROUTING_NODE_TYPE = TILE_ENTITIES.register("outputroutingnode", () -> BlockEntityType.Builder.of(TileOutputRoutingNode::new, BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileMasterRoutingNode>> MASTER_ROUTING_NODE_TYPE = TILE_ENTITIES.register("masterroutingnode", () -> BlockEntityType.Builder.of(TileMasterRoutingNode::new, BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get()).build(null));

	public static final RegistryObject<BlockEntityType<TileTeleposer>> TELEPOSER_TYPE = TILE_ENTITIES.register("teleposer", () -> BlockEntityType.Builder.of(TileTeleposer::new, BloodMagicBlocks.TELEPOSER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileSpectral>> SPECTRAL_TYPE = TILE_ENTITIES.register("spectral", () -> BlockEntityType.Builder.of(TileSpectral::new, BloodMagicBlocks.SPECTRAL.get()).build(null));

	public static final RegistryObject<BlockEntityType<TileDungeonController>> DUNGEON_CONTROLLER_TYPE = TILE_ENTITIES.register("dungeon_controller", () -> BlockEntityType.Builder.of(TileDungeonController::new, BloodMagicBlocks.DUNGEON_CONTROLLER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileDungeonSeal>> DUNGEON_SEAL_TYPE = TILE_ENTITIES.register("dungeon_seal", () -> BlockEntityType.Builder.of(TileDungeonSeal::new, BloodMagicBlocks.DUNGEON_SEAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileSpecialRoomDungeonSeal>> SPECIAL_DUNGEON_SEAL_TYPE = TILE_ENTITIES.register("special_dungeon_seal", () -> BlockEntityType.Builder.of(TileSpecialRoomDungeonSeal::new, BloodMagicBlocks.DUNGEON_SEAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileInversionPillar>> INVERSION_PILLAR_TYPE = TILE_ENTITIES.register("inversion_pillar", () -> BlockEntityType.Builder.of(TileInversionPillar::new, BloodMagicBlocks.INVERSION_PILLAR.get()).build(null));
}
