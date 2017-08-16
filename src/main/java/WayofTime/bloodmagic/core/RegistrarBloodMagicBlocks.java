package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.block.*;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock1;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock2;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock3;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.block.enums.EnumSubWillType1;
import WayofTime.bloodmagic.block.enums.EnumSubWillType2;
import WayofTime.bloodmagic.block.enums.EnumSubWillType3;
import WayofTime.bloodmagic.block.enums.EnumWillWall;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.tile.TileDemonCrucible;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import WayofTime.bloodmagic.tile.TileDemonCrystallizer;
import WayofTime.bloodmagic.tile.TileDemonPylon;
import WayofTime.bloodmagic.tile.TileDimensionalPortal;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.tile.TileInversionPillar;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.tile.TileMimic;
import WayofTime.bloodmagic.tile.TilePhantomBlock;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;

import java.util.Set;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
@GameRegistry.ObjectHolder(BloodMagic.MODID)
@SuppressWarnings("unused")
public class RegistrarBloodMagicBlocks
{
    public static final Block ALTAR = Blocks.AIR;
    public static final Block BLOOD_RUNE = Blocks.AIR;
    public static final Block RITUAL_CONTROLLER = Blocks.AIR;
    public static final Block RITUAL_STONE = Blocks.AIR;
    public static final Block BLOOD_LIGHT = Blocks.AIR;
    public static final Block TELEPOSER = Blocks.AIR;
    public static final Block ALCHEMY_ARRAY = Blocks.AIR;
    public static final Block SPECTRAL = Blocks.AIR;
    public static final Block PHANTOM = Blocks.AIR;
    public static final Block SOUL_FORGE = Blocks.AIR;
    public static final Block INCENSE_ALTAR = Blocks.AIR;
    public static final Block DEMON_CRUCIBLE = Blocks.AIR;
    public static final Block DEMON_PYLON = Blocks.AIR;
    public static final Block DEMON_CRYSTALLIZER = Blocks.AIR;
    public static final Block DEMON_CRYSTAL = Blocks.AIR;
    public static final Block ALCHEMY_TABLE = Blocks.AIR;
    public static final Block LIFE_ESSENCE = Blocks.AIR;
    public static final Block DECORATIVE_BRICK = Blocks.AIR;
    public static final Block PATH = Blocks.AIR;
    public static final Block MASTER_ROUTING_NODE = Blocks.AIR;
    public static final Block INPUT_ROUTING_NODE = Blocks.AIR;
    public static final Block OUTPUT_ROUTING_NODE = Blocks.AIR;
    public static final Block ITEM_ROUTING_NODE = Blocks.AIR;
    public static final Block DIMENSIONAL_PORTAL = Blocks.AIR;
    public static final Block BLOOD_TANK = Blocks.AIR;
    public static final Block MIMIC = Blocks.AIR;
    public static final Block DEMON_BRICK_1 = Blocks.AIR;
    public static final Block DEMON_BRICK_2 = Blocks.AIR;
    public static final Block DEMON_EXTRAS = Blocks.AIR;
    public static final Block DEMON_PILLAR_1 = Blocks.AIR;
    public static final Block DEMON_PILLAR_2 = Blocks.AIR;
    public static final Block DEMON_PILLAR_CAP_1 = Blocks.AIR;
    public static final Block DEMON_PILLAR_CAP_2 = Blocks.AIR;
    public static final Block DEMON_PILLAR_CAP_3 = Blocks.AIR;
    public static final Block DEMON_LIGHT = Blocks.AIR;
    public static final Block DEMON_WALL_1 = Blocks.AIR;
    public static final Block DEMON_STAIRS_1 = Blocks.AIR;
    public static final Block DEMON_STAIRS_2 = Blocks.AIR;
    public static final Block DEMON_STAIRS_3 = Blocks.AIR;
    public static final Block INVERSION_PILLAR = Blocks.AIR;
    public static final Block INVERSION_PILLAR_END = Blocks.AIR;

    static Set<Block> blocks;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        FluidRegistry.addBucketForFluid(BlockLifeEssence.getLifeEssence());
        
        blocks = Sets.newHashSet(
                new BlockAltar().setRegistryName("altar"),
                new BlockBloodRune().setRegistryName("blood_rune"),
                new BlockRitualController().setRegistryName("ritual_controller"),
                new BlockRitualStone().setRegistryName("ritual_stone"),
                new BlockBloodLight().setRegistryName("blood_light"),
                new BlockTeleposer().setRegistryName("teleposer"),
                new BlockAlchemyArray().setRegistryName("alchemy_array"),
                new BlockSpectral().setRegistryName("spectral"),
                new BlockPhantom().setRegistryName("phantom"),
                new BlockSoulForge().setRegistryName("soul_forge"),
                new BlockIncenseAltar().setRegistryName("incense_altar"),
                new BlockDemonCrucible().setRegistryName("demon_crucible"),
                new BlockDemonPylon().setRegistryName("demon_pylon"),
                new BlockDemonCrystallizer().setRegistryName("demon_crystallizer"),
                new BlockDemonCrystal().setRegistryName("demon_crystal"),
                new BlockAlchemyTable().setRegistryName("alchemy_table"),
                new BlockLifeEssence().setRegistryName("life_essence"),
                new BlockDecorative().setRegistryName("decorative_brick"),
                new BlockPath().setRegistryName("path"),
                new BlockMasterRoutingNode().setRegistryName("master_routing_node"),
                new BlockInputRoutingNode().setRegistryName("input_routing_node"),
                new BlockOutputRoutingNode().setRegistryName("output_routing_node"),
                new BlockItemRoutingNode().setRegistryName("item_routing_node"),
                new BlockDimensionalPortal().setRegistryName("dimensional_portal"),
                new BlockBloodTank().setRegistryName("blood_tank"),
                new BlockMimic().setRegistryName("mimic"),
                new BlockDemonBase<>("bricks1", EnumDemonBlock1.class).setRegistryName("demon_brick_1"),
                new BlockDemonBase<>("bricks2", EnumDemonBlock2.class).setRegistryName("demon_brick_2"),
                new BlockDemonBase<>("extras", EnumDemonBlock3.class).setRegistryName("demon_extras"),
                new BlockDemonPillarBase<>("pillar1", Material.ROCK, EnumSubWillType.class).setRegistryName("demon_pillar_1"),
                new BlockDemonPillarBase<>("pillar2", Material.ROCK, EnumSubWillType.class).setRegistryName("demon_pillar_2"),
                new BlockDemonPillarCapBase<>("pillarCap1", Material.ROCK, EnumSubWillType1.class).setRegistryName("demon_pillar_cap_1"),
                new BlockDemonPillarCapBase<>("pillarCap2", Material.ROCK, EnumSubWillType2.class).setRegistryName("demon_pillar_cap_2"),
                new BlockDemonPillarCapBase<>("pillarCap3", Material.ROCK, EnumSubWillType3.class).setRegistryName("demon_pillar_cap_3"),
                new BlockDemonLight().setRegistryName("demon_light"),
                new BlockDemonWallBase<>("wall1", Material.ROCK, EnumWillWall.class).setRegistryName("demon_wall_1"),
                new BlockDemonStairsBase<>("stairs1", Material.ROCK, EnumSubWillType1.class).setRegistryName("demon_stairs_1"),
                new BlockDemonStairsBase<>("stairs2", Material.ROCK, EnumSubWillType2.class).setRegistryName("demon_stairs_2"),
                new BlockDemonStairsBase<>("stairs3", Material.ROCK, EnumSubWillType3.class).setRegistryName("demon_stairs_3"),
                new BlockInversionPillar().setRegistryName("inversion_pillar"),
                new BlockInversionPillarEnd().setRegistryName("inversion_pillar_end")
        );

        event.getRegistry().registerAll(blocks.toArray(new Block[0]));

        registerTiles();
    }

    private static void registerTiles() {
        GameRegistry.registerTileEntity(TileAltar.class, BloodMagic.MODID + ":" + TileAltar.class.getSimpleName());
        GameRegistry.registerTileEntity(TileImperfectRitualStone.class, BloodMagic.MODID + ":" + TileImperfectRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMasterRitualStone.class, BloodMagic.MODID + ":" + TileMasterRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TileAlchemyArray.class, BloodMagic.MODID + ":" + TileAlchemyArray.class.getSimpleName());
        GameRegistry.registerTileEntity(TileSpectralBlock.class, BloodMagic.MODID + ":" + TileSpectralBlock.class.getSimpleName());
        GameRegistry.registerTileEntity(TilePhantomBlock.class, BloodMagic.MODID + ":" + TilePhantomBlock.class.getSimpleName());
        GameRegistry.registerTileEntity(TileTeleposer.class, BloodMagic.MODID + ":" + TileTeleposer.class.getSimpleName());
        GameRegistry.registerTileEntity(TileSoulForge.class, BloodMagic.MODID + ":" + TileSoulForge.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMasterRoutingNode.class, BloodMagic.MODID + ":" + TileMasterRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileInputRoutingNode.class, BloodMagic.MODID + ":" + TileInputRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileOutputRoutingNode.class, BloodMagic.MODID + ":" + TileOutputRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileItemRoutingNode.class, BloodMagic.MODID + ":" + TileItemRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileIncenseAltar.class, BloodMagic.MODID + ":" + TileIncenseAltar.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrucible.class, BloodMagic.MODID + ":" + TileDemonCrucible.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonPylon.class, BloodMagic.MODID + ":" + TileDemonPylon.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrystallizer.class, BloodMagic.MODID + ":" + TileDemonCrystallizer.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrystal.class, BloodMagic.MODID + ":" + TileDemonCrystal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileAlchemyTable.class, BloodMagic.MODID + ":" + TileAlchemyTable.class.getSimpleName());

        GameRegistry.registerTileEntity(TileDimensionalPortal.class, BloodMagic.MODID + ":" + TileDimensionalPortal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileBloodTank.class, BloodMagic.MODID + ":" + TileBloodTank.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMimic.class, BloodMagic.MODID + ":" + TileMimic.class.getSimpleName());
        GameRegistry.registerTileEntity(TileInversionPillar.class, BloodMagic.MODID + ":" + TileInversionPillar.class.getSimpleName());
    }
}
