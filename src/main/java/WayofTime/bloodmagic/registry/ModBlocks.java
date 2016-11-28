package WayofTime.bloodmagic.registry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.block.BlockAlchemyArray;
import WayofTime.bloodmagic.block.BlockAlchemyTable;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockBloodLight;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockBloodStoneBrick;
import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.block.BlockCrystal;
import WayofTime.bloodmagic.block.BlockDemonBase;
import WayofTime.bloodmagic.block.BlockDemonCrucible;
import WayofTime.bloodmagic.block.BlockDemonCrystal;
import WayofTime.bloodmagic.block.BlockDemonCrystallizer;
import WayofTime.bloodmagic.block.BlockDemonLight;
import WayofTime.bloodmagic.block.BlockDemonPillarBase;
import WayofTime.bloodmagic.block.BlockDemonPillarCapBase;
import WayofTime.bloodmagic.block.BlockDemonPylon;
import WayofTime.bloodmagic.block.BlockDemonStairsBase;
import WayofTime.bloodmagic.block.BlockDemonWallBase;
import WayofTime.bloodmagic.block.BlockDimensionalPortal;
import WayofTime.bloodmagic.block.BlockIncenseAltar;
import WayofTime.bloodmagic.block.BlockInputRoutingNode;
import WayofTime.bloodmagic.block.BlockInversionPillar;
import WayofTime.bloodmagic.block.BlockInversionPillarEnd;
import WayofTime.bloodmagic.block.BlockItemRoutingNode;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.block.BlockMasterRoutingNode;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.block.BlockOutputRoutingNode;
import WayofTime.bloodmagic.block.BlockPath;
import WayofTime.bloodmagic.block.BlockPhantom;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.block.BlockSoulForge;
import WayofTime.bloodmagic.block.BlockSpectral;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.block.enums.EnumBloodRune;
import WayofTime.bloodmagic.block.enums.EnumBrickSize;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock1;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock2;
import WayofTime.bloodmagic.block.enums.EnumDemonBlock3;
import WayofTime.bloodmagic.block.enums.EnumInversionCap;
import WayofTime.bloodmagic.block.enums.EnumMimic;
import WayofTime.bloodmagic.block.enums.EnumPath;
import WayofTime.bloodmagic.block.enums.EnumRitualController;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.block.enums.EnumSubWillType1;
import WayofTime.bloodmagic.block.enums.EnumSubWillType2;
import WayofTime.bloodmagic.block.enums.EnumSubWillType3;
import WayofTime.bloodmagic.block.enums.EnumWillWall;
import WayofTime.bloodmagic.item.block.ItemBlockAlchemyTable;
import WayofTime.bloodmagic.item.block.ItemBlockBloodTank;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.item.block.base.ItemBlockEnum;
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
import WayofTime.bloodmagic.tile.TilePlinth;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelperV2;

public class ModBlocks
{
    public static final Block ALTAR;
    public static final Block BLOOD_RUNE;
    public static final Block RITUAL_CONTROLLER;
    public static final Block RITUAL_STONE;
    public static final Block BLOOD_LIGHT;
    public static final Block TELEPOSER;
    public static final Block ALCHEMY_ARRAY;
    public static final Block SPECTRAL_BLOCK;
    public static final Block PHANTOM_BLOCK;
    public static final Block SOUL_FORGE;
    public static final Block INCENSE_ALTAR;
    public static final Block DEMON_CRUCIBLE;
    public static final Block DEMON_PYLON;
    public static final Block DEMON_CRYSTALLIZER;
    public static final Block DEMON_CRYSTAL;
    public static final Block ALCHEMY_TABLE;
    public static final Block LIFE_ESSENCE;
    public static final Block CRYSTAL;
    public static final Block BLOOD_STONE;
    public static final Block PATH_BLOCK;
    public static final Block MASTER_ROUTING_NODE;
    public static final Block INPUT_ROUTING_NODE;
    public static final Block OUTPUT_ROUTING_NODE;
    public static final Block ITEM_ROUTING_NODE;
    public static final Block DIMENSIONAL_PORTAL;
    public static final Block BLOOD_TANK;
    public static final Block MIMIC;
    public static final Block DEMON_BRICK_1;
    public static final Block DEMON_BRICK_2;
    public static final Block DEMON_EXTRAS;
    public static final Block DEMON_PILLAR_1;
    public static final Block DEMON_PILLAR_2;
    public static final Block DEMON_PILLAR_CAP_1;
    public static final Block DEMON_PILLAR_CAP_2;
    public static final Block DEMON_PILLAR_CAP_3;
    public static final Block DEMON_LIGHT;
    public static final Block DEMON_WALL_1;
    public static final Block DEMON_STAIRS_1;
    public static final Block DEMON_STAIRS_2;
    public static final Block DEMON_STAIRS_3;
    public static final Block INVERSION_PILLAR;
    public static final Block INVERSION_PILLAR_END;

    static
    {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        FluidRegistry.addBucketForFluid(BlockLifeEssence.getLifeEssence());
        LIFE_ESSENCE = registerBlock(new BlockLifeEssence(), Constants.BloodMagicBlock.LIFE_ESSENCE.getRegName());

        ALTAR = registerBlock(new BlockAltar(), Constants.BloodMagicBlock.ALTAR.getRegName());
        BLOOD_RUNE = registerBlock(new ItemBlockEnum<EnumBloodRune>(new BlockBloodRune()), Constants.BloodMagicBlock.BLOOD_RUNE.getRegName());
        RITUAL_CONTROLLER = registerBlock(new ItemBlockEnum<EnumRitualController>(new BlockRitualController()), Constants.BloodMagicBlock.RITUAL_CONTROLLER.getRegName());
        RITUAL_STONE = registerBlock(new ItemBlockEnum<EnumRuneType>(new BlockRitualStone()), Constants.BloodMagicBlock.RITUAL_STONE.getRegName());
        BLOOD_LIGHT = registerBlock(new BlockBloodLight(), Constants.BloodMagicBlock.BLOOD_LIGHT.getRegName());
        TELEPOSER = registerBlock(new BlockTeleposer(), Constants.BloodMagicBlock.TELEPOSER.getRegName());
        ALCHEMY_ARRAY = registerBlock(new BlockAlchemyArray(), Constants.BloodMagicBlock.ALCHEMY_ARRAY.getRegName());
        SPECTRAL_BLOCK = registerBlock(new BlockSpectral(), Constants.BloodMagicBlock.SPECTRAL.getRegName());
        PHANTOM_BLOCK = registerBlock(new BlockPhantom(), Constants.BloodMagicBlock.PHANTOM.getRegName());
        SOUL_FORGE = registerBlock(new BlockSoulForge(), Constants.BloodMagicBlock.SOUL_FORGE.getRegName());
        CRYSTAL = registerBlock(new ItemBlockEnum<EnumBrickSize>(new BlockCrystal()), Constants.BloodMagicBlock.CRYSTAL.getRegName());
        BLOOD_STONE = registerBlock(new ItemBlockEnum<EnumBrickSize>(new BlockBloodStoneBrick()), Constants.BloodMagicBlock.BLOOD_STONE.getRegName());
        MASTER_ROUTING_NODE = registerBlock(new ItemBlock(new BlockMasterRoutingNode()), Constants.BloodMagicBlock.MASTER_ROUTING_NODE.getRegName());
        INPUT_ROUTING_NODE = registerBlock(new ItemBlock(new BlockInputRoutingNode()), Constants.BloodMagicBlock.INPUT_ROUTING_NODE.getRegName());
        OUTPUT_ROUTING_NODE = registerBlock(new ItemBlock(new BlockOutputRoutingNode()), Constants.BloodMagicBlock.OUTPUT_ROUTING_NODE.getRegName());
        ITEM_ROUTING_NODE = registerBlock(new ItemBlock(new BlockItemRoutingNode()), Constants.BloodMagicBlock.ITEM_ROUTING_NODE.getRegName());
        INCENSE_ALTAR = registerBlock(new BlockIncenseAltar(), Constants.BloodMagicBlock.INCENSE_ALTAR.getRegName());
        PATH_BLOCK = registerBlock(new ItemBlockEnum<EnumPath>(new BlockPath()), Constants.BloodMagicBlock.PATH.getRegName());
        DEMON_CRUCIBLE = registerBlock(new BlockDemonCrucible(), Constants.BloodMagicBlock.DEMON_CRUCIBLE.getRegName());
        DEMON_PYLON = registerBlock(new BlockDemonPylon(), Constants.BloodMagicBlock.DEMON_PYLON.getRegName());
        DEMON_CRYSTALLIZER = registerBlock(new BlockDemonCrystallizer(), Constants.BloodMagicBlock.DEMON_CRYSTALLIZER.getRegName());
        DEMON_CRYSTAL = registerBlock(new ItemBlockDemonCrystal(new BlockDemonCrystal()), Constants.BloodMagicBlock.DEMON_CRYSTAL.getRegName());

        ALCHEMY_TABLE = registerBlock(new ItemBlockAlchemyTable(new BlockAlchemyTable()), Constants.BloodMagicBlock.ALCHEMY_TABLE.getRegName());

        DIMENSIONAL_PORTAL = registerBlock(new BlockDimensionalPortal(), Constants.BloodMagicBlock.DIMENSIONAL_PORTAL.getRegName());
        BLOOD_TANK = registerBlock(new ItemBlockBloodTank(new BlockBloodTank()), Constants.BloodMagicBlock.BLOOD_TANK.getRegName());

        MIMIC = registerBlock(new ItemBlockEnum<EnumMimic>(new BlockMimic()), Constants.BloodMagicBlock.MIMIC.getRegName());

        DEMON_BRICK_1 = registerBlock(new ItemBlockEnum<EnumDemonBlock1>(new BlockDemonBase<EnumDemonBlock1>("bricks1", EnumDemonBlock1.class)), Constants.BloodMagicBlock.DEMON_BRICK_1.getRegName());
        DEMON_BRICK_2 = registerBlock(new ItemBlockEnum<EnumDemonBlock2>(new BlockDemonBase<EnumDemonBlock2>("bricks2", EnumDemonBlock2.class)), Constants.BloodMagicBlock.DEMON_BRICK_2.getRegName());
        DEMON_EXTRAS = registerBlock(new ItemBlockEnum<EnumDemonBlock3>(new BlockDemonBase<EnumDemonBlock3>("extras", EnumDemonBlock3.class)), Constants.BloodMagicBlock.DEMON_BLOCK_EXTRA.getRegName());

        DEMON_PILLAR_1 = registerBlock(new ItemBlockEnum<EnumSubWillType>(new BlockDemonPillarBase<EnumSubWillType>("pillar1", Material.ROCK, EnumSubWillType.class)), Constants.BloodMagicBlock.DEMON_PILLAR_1.getRegName());
        DEMON_PILLAR_2 = registerBlock(new ItemBlockEnum<EnumSubWillType>(new BlockDemonPillarBase<EnumSubWillType>("pillar2", Material.ROCK, EnumSubWillType.class)), Constants.BloodMagicBlock.DEMON_PILLAR_2.getRegName());
        DEMON_PILLAR_CAP_1 = registerBlock(new ItemBlockEnum<EnumSubWillType1>(new BlockDemonPillarCapBase<EnumSubWillType1>("pillarCap1", Material.ROCK, EnumSubWillType1.class)), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_1.getRegName());
        DEMON_PILLAR_CAP_2 = registerBlock(new ItemBlockEnum<EnumSubWillType2>(new BlockDemonPillarCapBase<EnumSubWillType2>("pillarCap2", Material.ROCK, EnumSubWillType2.class)), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_2.getRegName());
        DEMON_PILLAR_CAP_3 = registerBlock(new ItemBlockEnum<EnumSubWillType3>(new BlockDemonPillarCapBase<EnumSubWillType3>("pillarCap3", Material.ROCK, EnumSubWillType3.class)), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_3.getRegName());

        DEMON_LIGHT = registerBlock(new ItemBlockEnum<EnumSubWillType>(new BlockDemonLight()), Constants.BloodMagicBlock.DEMON_LIGHT.getRegName());

        DEMON_WALL_1 = registerBlock(new ItemBlockEnum<EnumWillWall>(new BlockDemonWallBase<EnumWillWall>("wall1", Material.ROCK, EnumWillWall.class)), Constants.BloodMagicBlock.DEMON_WALL_1.getRegName());

        DEMON_STAIRS_1 = registerBlock(new ItemBlockEnum<EnumSubWillType1>(new BlockDemonStairsBase<EnumSubWillType1>("stairs1", Material.ROCK, EnumSubWillType1.class)), Constants.BloodMagicBlock.DEMON_STAIRS_1.getRegName());
        DEMON_STAIRS_2 = registerBlock(new ItemBlockEnum<EnumSubWillType2>(new BlockDemonStairsBase<EnumSubWillType2>("stairs2", Material.ROCK, EnumSubWillType2.class)), Constants.BloodMagicBlock.DEMON_STAIRS_2.getRegName());
        DEMON_STAIRS_3 = registerBlock(new ItemBlockEnum<EnumSubWillType3>(new BlockDemonStairsBase<EnumSubWillType3>("stairs3", Material.ROCK, EnumSubWillType3.class)), Constants.BloodMagicBlock.DEMON_STAIRS_3.getRegName());

        INVERSION_PILLAR = registerBlock(new ItemBlockEnum<EnumSubWillType>(new BlockInversionPillar()), Constants.BloodMagicBlock.INVERSION_PILLAR.getRegName());
        INVERSION_PILLAR_END = registerBlock(new ItemBlockEnum<EnumInversionCap>(new BlockInversionPillarEnd()), Constants.BloodMagicBlock.INVERSION_PILLAR_END.getRegName());
    }

    public static void init()
    {
        BloodMagicAPI.addToTeleposerBlacklist(INPUT_ROUTING_NODE);
        BloodMagicAPI.addToTranspositionBlacklist(INPUT_ROUTING_NODE);
        BloodMagicAPI.addToTeleposerBlacklist(OUTPUT_ROUTING_NODE);
        BloodMagicAPI.addToTranspositionBlacklist(OUTPUT_ROUTING_NODE);
        BloodMagicAPI.addToTeleposerBlacklist(ITEM_ROUTING_NODE);
        BloodMagicAPI.addToTranspositionBlacklist(ITEM_ROUTING_NODE);
        BloodMagicAPI.addToTeleposerBlacklist(DEMON_CRYSTAL);
        BloodMagicAPI.addToTranspositionBlacklist(DEMON_CRYSTAL);

        initTiles();
    }

    public static void initTiles()
    {
        GameRegistry.registerTileEntity(TileAltar.class, Constants.Mod.MODID + ":" + TileAltar.class.getSimpleName());
        GameRegistry.registerTileEntity(TileImperfectRitualStone.class, Constants.Mod.MODID + ":" + TileImperfectRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMasterRitualStone.class, Constants.Mod.MODID + ":" + TileMasterRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TilePlinth.class, Constants.Mod.MODID + ":" + TilePlinth.class.getSimpleName());
        GameRegistry.registerTileEntity(TileAlchemyArray.class, Constants.Mod.MODID + ":" + TileAlchemyArray.class.getSimpleName());
        GameRegistry.registerTileEntity(TileSpectralBlock.class, Constants.Mod.MODID + ":" + TileSpectralBlock.class.getSimpleName());
        GameRegistry.registerTileEntity(TilePhantomBlock.class, Constants.Mod.MODID + ":" + TilePhantomBlock.class.getSimpleName());
        GameRegistry.registerTileEntity(TileTeleposer.class, Constants.Mod.MODID + ":" + TileTeleposer.class.getSimpleName());
        GameRegistry.registerTileEntity(TileSoulForge.class, Constants.Mod.MODID + ":" + TileSoulForge.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMasterRoutingNode.class, Constants.Mod.MODID + ":" + TileMasterRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileInputRoutingNode.class, Constants.Mod.MODID + ":" + TileInputRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileOutputRoutingNode.class, Constants.Mod.MODID + ":" + TileOutputRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileItemRoutingNode.class, Constants.Mod.MODID + ":" + TileItemRoutingNode.class.getSimpleName());
        GameRegistry.registerTileEntity(TileIncenseAltar.class, Constants.Mod.MODID + ":" + TileIncenseAltar.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrucible.class, Constants.Mod.MODID + ":" + TileDemonCrucible.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonPylon.class, Constants.Mod.MODID + ":" + TileDemonPylon.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrystallizer.class, Constants.Mod.MODID + ":" + TileDemonCrystallizer.class.getSimpleName());
        GameRegistry.registerTileEntity(TileDemonCrystal.class, Constants.Mod.MODID + ":" + TileDemonCrystal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileAlchemyTable.class, Constants.Mod.MODID + ":" + TileAlchemyTable.class.getSimpleName());

        GameRegistry.registerTileEntity(TileDimensionalPortal.class, Constants.Mod.MODID + ":" + TileDimensionalPortal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileBloodTank.class, Constants.Mod.MODID + ":" + TileBloodTank.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMimic.class, Constants.Mod.MODID + ":" + TileMimic.class.getSimpleName());
        GameRegistry.registerTileEntity(TileInversionPillar.class, Constants.Mod.MODID + ":" + TileInversionPillar.class.getSimpleName());
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();
        InventoryRenderHelperV2 renderHelperV2 = BloodMagic.proxy.getRenderHelperV2();

        renderHelper.fluidRender(LIFE_ESSENCE);

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(BLOOD_LIGHT));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ALCHEMY_ARRAY));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(OUTPUT_ROUTING_NODE));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(INPUT_ROUTING_NODE));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(MASTER_ROUTING_NODE));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ITEM_ROUTING_NODE));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ALCHEMY_TABLE));
        for (int i = 0; i < 16; i++)
            renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(BLOOD_TANK), i, InventoryRenderHelper.getClassName(InventoryRenderHelper.getItemFromBlock(BLOOD_TANK)));
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(DEMON_CRYSTAL), 0, "ItemBlockDemonCrystal", "default");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(DEMON_CRYSTAL), 1, "ItemBlockDemonCrystal", "corrosive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(DEMON_CRYSTAL), 2, "ItemBlockDemonCrystal", "destructive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(DEMON_CRYSTAL), 3, "ItemBlockDemonCrystal", "vengeful");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(DEMON_CRYSTAL), 4, "ItemBlockDemonCrystal", "steadfast");

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(DIMENSIONAL_PORTAL));
    }

    private static Block registerBlock(Block block, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
        {
            if (block.getRegistryName() == null)
                block.setRegistryName(name);
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(name));
            BloodMagic.proxy.tryHandleBlockModel(block, name);
        }

        return block;
    }

    private static Block registerBlock(ItemBlock itemBlock, String name)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
            block.setRegistryName(name);

        if (!ConfigHandler.blockBlacklist.contains(name))
        {
            GameRegistry.register(block);
            GameRegistry.register(itemBlock.setRegistryName(name));
            BloodMagic.proxy.tryHandleBlockModel(block, name);
        }

        return block;
    }
}
