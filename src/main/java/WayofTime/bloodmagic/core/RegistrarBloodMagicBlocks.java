package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.*;
import WayofTime.bloodmagic.block.enums.*;
import WayofTime.bloodmagic.tile.*;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
@GameRegistry.ObjectHolder(BloodMagic.MODID)
@SuppressWarnings("unused")
public class RegistrarBloodMagicBlocks {
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

    static List<Block> blocks;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        FluidRegistry.addBucketForFluid(BlockLifeEssence.getLifeEssence());

        blocks = Lists.newArrayList(
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

        registerTileEntities();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(LIFE_ESSENCE, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(state.getBlock().getRegistryName(), "fluid");
            }
        });
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileAltar.class, BloodMagic.MODID + ":altar");
        GameRegistry.registerTileEntity(TileImperfectRitualStone.class, BloodMagic.MODID + ":imperfect_ritual_stone");
        GameRegistry.registerTileEntity(TileMasterRitualStone.class, BloodMagic.MODID + ":master_ritual_stone");
        GameRegistry.registerTileEntity(TileAlchemyArray.class, BloodMagic.MODID + ":alchemy_array");
        GameRegistry.registerTileEntity(TileSpectralBlock.class, BloodMagic.MODID + ":spectral_block");
        GameRegistry.registerTileEntity(TilePhantomBlock.class, BloodMagic.MODID + ":phantom_block");
        GameRegistry.registerTileEntity(TileTeleposer.class, BloodMagic.MODID + ":teleposer");
        GameRegistry.registerTileEntity(TileSoulForge.class, BloodMagic.MODID + ":soul_forge");
        GameRegistry.registerTileEntity(TileMasterRoutingNode.class, BloodMagic.MODID + ":master_routing_node");
        GameRegistry.registerTileEntity(TileInputRoutingNode.class, BloodMagic.MODID + ":input_routing_node");
        GameRegistry.registerTileEntity(TileOutputRoutingNode.class, BloodMagic.MODID + ":output_routing_node");
        GameRegistry.registerTileEntity(TileItemRoutingNode.class, BloodMagic.MODID + ":item_routing_node");
        GameRegistry.registerTileEntity(TileIncenseAltar.class, BloodMagic.MODID + ":incense_altar");
        GameRegistry.registerTileEntity(TileDemonCrucible.class, BloodMagic.MODID + ":demon_crucible");
        GameRegistry.registerTileEntity(TileDemonPylon.class, BloodMagic.MODID + ":demon_pylon");
        GameRegistry.registerTileEntity(TileDemonCrystallizer.class, BloodMagic.MODID + ":demon_crystallizer");
        GameRegistry.registerTileEntity(TileDemonCrystal.class, BloodMagic.MODID + ":demon_crystal");
        GameRegistry.registerTileEntity(TileAlchemyTable.class, BloodMagic.MODID + ":alchemy_table");
        GameRegistry.registerTileEntity(TileDimensionalPortal.class, BloodMagic.MODID + ":dimensional_portal");
        GameRegistry.registerTileEntity(TileBloodTank.class, BloodMagic.MODID + ":blood_tank");
        GameRegistry.registerTileEntity(TileMimic.class, BloodMagic.MODID + ":mimic");
        GameRegistry.registerTileEntity(TileInversionPillar.class, BloodMagic.MODID + ":inversion_pillar");
    }
}
