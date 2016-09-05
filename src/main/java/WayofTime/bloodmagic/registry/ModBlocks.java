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
import WayofTime.bloodmagic.block.BlockDemonWallBase;
import WayofTime.bloodmagic.block.BlockDimensionalPortal;
import WayofTime.bloodmagic.block.BlockIncenseAltar;
import WayofTime.bloodmagic.block.BlockInputRoutingNode;
import WayofTime.bloodmagic.block.BlockItemRoutingNode;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.block.BlockMasterRoutingNode;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.block.BlockOutputRoutingNode;
import WayofTime.bloodmagic.block.BlockPath;
import WayofTime.bloodmagic.block.BlockPedestal;
import WayofTime.bloodmagic.block.BlockPhantom;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.block.BlockSoulForge;
import WayofTime.bloodmagic.block.BlockSpectral;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.block.ItemBlockAlchemyTable;
import WayofTime.bloodmagic.item.block.ItemBlockBloodRune;
import WayofTime.bloodmagic.item.block.ItemBlockBloodStoneBrick;
import WayofTime.bloodmagic.item.block.ItemBlockBloodTank;
import WayofTime.bloodmagic.item.block.ItemBlockCrystal;
import WayofTime.bloodmagic.item.block.ItemBlockDemonBase;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.item.block.ItemBlockDemonLight;
import WayofTime.bloodmagic.item.block.ItemBlockDemonPillarBase;
import WayofTime.bloodmagic.item.block.ItemBlockDemonPillarCapBase;
import WayofTime.bloodmagic.item.block.ItemBlockDemonWallBase;
import WayofTime.bloodmagic.item.block.ItemBlockMimic;
import WayofTime.bloodmagic.item.block.ItemBlockPath;
import WayofTime.bloodmagic.item.block.ItemBlockPedestal;
import WayofTime.bloodmagic.item.block.ItemBlockRitualController;
import WayofTime.bloodmagic.item.block.ItemBlockRitualStone;
import WayofTime.bloodmagic.item.block.ItemBlockRoutingNode;
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
    public static Block altar;
    public static Block bloodRune;
    public static Block ritualController;
    public static Block ritualStone;
    public static Block bloodLight;
    //    public static Block testSpellBlock;
    public static Block pedestal;
    public static Block teleposer;
    public static Block alchemyArray;
    public static Block spectralBlock;
    public static Block phantomBlock;
    public static Block soulForge;
    public static Block incenseAltar;
    public static Block demonCrucible;
    public static Block demonPylon;
    public static Block demonCrystallizer;
    public static Block demonCrystal;

    public static Block alchemyTable;

    public static Block lifeEssence;

    public static Block crystal;
    public static Block bloodStoneBrick;
    public static Block pathBlock;

    public static Block masterRoutingNode;
    public static Block inputRoutingNode;
    public static Block outputRoutingNode;
    public static Block itemRoutingNode;

    public static Block dimensionalPortal;
    public static Block bloodTank;

    public static Block mimic;

    public static Block demonBrick1;
    public static Block demonBrick2;
    public static Block demonExtras;
    public static Block demonPillar1;
    public static Block demonPillar2;

    public static Block demonPillarCap1;
    public static Block demonPillarCap2;
    public static Block demonPillarCap3;

    public static Block demonLight;

    public static Block demonWall1;

    public static void init()
    {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        FluidRegistry.addBucketForFluid(BlockLifeEssence.getLifeEssence());
        lifeEssence = registerBlock(new BlockLifeEssence(), Constants.BloodMagicBlock.LIFE_ESSENCE.getRegName());

        altar = registerBlock(new BlockAltar(), Constants.BloodMagicBlock.ALTAR.getRegName());
        bloodRune = registerBlock(new ItemBlockBloodRune(new BlockBloodRune()), Constants.BloodMagicBlock.BLOOD_RUNE.getRegName());
        ritualController = registerBlock(new ItemBlockRitualController(new BlockRitualController()), Constants.BloodMagicBlock.RITUAL_CONTROLLER.getRegName());
        ritualStone = registerBlock(new ItemBlockRitualStone(new BlockRitualStone()), Constants.BloodMagicBlock.RITUAL_STONE.getRegName());
        bloodLight = registerBlock(new BlockBloodLight(), Constants.BloodMagicBlock.BLOOD_LIGHT.getRegName());
        pedestal = registerBlock(new ItemBlockPedestal(new BlockPedestal()), Constants.BloodMagicBlock.PEDESTAL.getRegName());
        teleposer = registerBlock(new BlockTeleposer(), Constants.BloodMagicBlock.TELEPOSER.getRegName());
        alchemyArray = registerBlock(new BlockAlchemyArray(), Constants.BloodMagicBlock.ALCHEMY_ARRAY.getRegName());
        spectralBlock = registerBlock(new BlockSpectral(), Constants.BloodMagicBlock.SPECTRAL.getRegName());
        phantomBlock = registerBlock(new BlockPhantom(), Constants.BloodMagicBlock.PHANTOM.getRegName());
        soulForge = registerBlock(new BlockSoulForge(), Constants.BloodMagicBlock.SOUL_FORGE.getRegName());
        crystal = registerBlock(new ItemBlockCrystal(new BlockCrystal()), Constants.BloodMagicBlock.CRYSTAL.getRegName());
        bloodStoneBrick = registerBlock(new ItemBlockBloodStoneBrick(new BlockBloodStoneBrick()), Constants.BloodMagicBlock.BLOOD_STONE.getRegName());
        masterRoutingNode = registerBlock(new ItemBlockRoutingNode(new BlockMasterRoutingNode()), Constants.BloodMagicBlock.MASTER_ROUTING_NODE.getRegName());
        inputRoutingNode = registerBlock(new ItemBlockRoutingNode(new BlockInputRoutingNode()), Constants.BloodMagicBlock.INPUT_ROUTING_NODE.getRegName());
        outputRoutingNode = registerBlock(new ItemBlockRoutingNode(new BlockOutputRoutingNode()), Constants.BloodMagicBlock.OUTPUT_ROUTING_NODE.getRegName());
        itemRoutingNode = registerBlock(new ItemBlockRoutingNode(new BlockItemRoutingNode()), Constants.BloodMagicBlock.ITEM_ROUTING_NODE.getRegName());
        incenseAltar = registerBlock(new BlockIncenseAltar(), Constants.BloodMagicBlock.INCENSE_ALTAR.getRegName());
        pathBlock = registerBlock(new ItemBlockPath(new BlockPath()), Constants.BloodMagicBlock.PATH.getRegName());
        demonCrucible = registerBlock(new BlockDemonCrucible(), Constants.BloodMagicBlock.DEMON_CRUCIBLE.getRegName());
        demonPylon = registerBlock(new BlockDemonPylon(), Constants.BloodMagicBlock.DEMON_PYLON.getRegName());
        demonCrystallizer = registerBlock(new BlockDemonCrystallizer(), Constants.BloodMagicBlock.DEMON_CRYSTALLIZER.getRegName());
        demonCrystal = registerBlock(new ItemBlockDemonCrystal(new BlockDemonCrystal()), Constants.BloodMagicBlock.DEMON_CRYSTAL.getRegName());

        alchemyTable = registerBlock(new ItemBlockAlchemyTable(new BlockAlchemyTable()), Constants.BloodMagicBlock.ALCHEMY_TABLE.getRegName());

        dimensionalPortal = registerBlock(new BlockDimensionalPortal(), Constants.BloodMagicBlock.DIMENSIONAL_PORTAL.getRegName());
        bloodTank = registerBlock(new ItemBlockBloodTank(new BlockBloodTank()), Constants.BloodMagicBlock.BLOOD_TANK.getRegName());

        mimic = registerBlock(new ItemBlockMimic(new BlockMimic()), Constants.BloodMagicBlock.MIMIC.getRegName());

        demonBrick1 = registerBlock(new ItemBlockDemonBase(new BlockDemonBase("bricks1", new String[] { "brick1_raw", "brick1_corrosive", "brick1_destructive", "brick1_vengeful", "brick1_steadfast", "brick2_raw", "brick2_corrosive", "brick2_destructive", "brick2_vengeful", "brick2_steadfast", "brick3_raw", "brick3_corrosive", "brick3_destructive", "brick3_vengeful", "brick3_steadfast" })), Constants.BloodMagicBlock.DEMON_BRICK_1.getRegName());
        demonBrick2 = registerBlock(new ItemBlockDemonBase(new BlockDemonBase("bricks2", new String[] { "smallbrick_raw", "smallbrick_corrosive", "smallbrick_destructive", "smallbrick_vengeful", "smallbrick_steadfast", "tile_raw", "tile_corrosive", "tile_destructive", "tile_vengeful", "tile_steadfast", "tilespecial_raw", "tilespecial_corrosive", "tilespecial_destructive", "tilespecial_vengeful", "tilespecial_steadfast" })), Constants.BloodMagicBlock.DEMON_BRICK_2.getRegName());
        demonExtras = registerBlock(new ItemBlockDemonBase(new BlockDemonBase("extras", new String[] { "stone_raw", "stone_corrosive", "stone_destructive", "stone_vengeful", "stone_steadfast", "polished_raw", "polished_corrosive", "polished_destructive", "polished_vengeful", "polished_steadfast", "metal_raw", "metal_corrosive", "metal_destructive", "metal_vengeful", "metal_steadfast" })), Constants.BloodMagicBlock.DEMON_BLOCK_EXTRA.getRegName());

        demonPillar1 = registerBlock(new ItemBlockDemonPillarBase(new BlockDemonPillarBase("pillar1", Material.ROCK, new String[] { "raw", "corrosive", "destructive", "vengeful", "steadfast" })), Constants.BloodMagicBlock.DEMON_PILLAR_1.getRegName());
        demonPillar2 = registerBlock(new ItemBlockDemonPillarBase(new BlockDemonPillarBase("pillar2", Material.ROCK, new String[] { "raw", "corrosive", "destructive", "vengeful", "steadfast" })), Constants.BloodMagicBlock.DEMON_PILLAR_2.getRegName());
        demonPillarCap1 = registerBlock(new ItemBlockDemonPillarCapBase(new BlockDemonPillarCapBase("pillarCap1", Material.ROCK, new String[] { "raw", "corrosive" })), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_1.getRegName());
        demonPillarCap2 = registerBlock(new ItemBlockDemonPillarCapBase(new BlockDemonPillarCapBase("pillarCap2", Material.ROCK, new String[] { "destructive", "vengeful" })), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_2.getRegName());
        demonPillarCap3 = registerBlock(new ItemBlockDemonPillarCapBase(new BlockDemonPillarCapBase("pillarCap3", Material.ROCK, new String[] { "steadfast" })), Constants.BloodMagicBlock.DEMON_PILLAR_CAP_3.getRegName());
//        testSpellBlock = registerBlock(new BlockTestSpellBlock());

        demonLight = registerBlock(new ItemBlockDemonLight(new BlockDemonLight()), Constants.BloodMagicBlock.DEMON_LIGHT.getRegName());

        demonWall1 = registerBlock(new ItemBlockDemonWallBase(new BlockDemonWallBase("wall1", Material.ROCK, new String[] { "raw", "corrosive", "destructive", "vengeful", "steadfast" })), "BlockWall1");

        BloodMagicAPI.addToTeleposerBlacklist(inputRoutingNode);
        BloodMagicAPI.addToTranspositionBlacklist(inputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(outputRoutingNode);
        BloodMagicAPI.addToTranspositionBlacklist(outputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(itemRoutingNode);
        BloodMagicAPI.addToTranspositionBlacklist(itemRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(demonCrystal);
        BloodMagicAPI.addToTranspositionBlacklist(demonCrystal);

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
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();
        InventoryRenderHelperV2 renderHelperV2 = BloodMagic.proxy.getRenderHelperV2();

        renderHelper.fluidRender(lifeEssence);

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodLight));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pedestal), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pedestal), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(alchemyArray));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(outputRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(inputRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(masterRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(itemRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(alchemyTable));
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 0, "ItemBlockDemonCrystal", "default");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 1, "ItemBlockDemonCrystal", "corrosive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 2, "ItemBlockDemonCrystal", "destructive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 3, "ItemBlockDemonCrystal", "vengeful");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 4, "ItemBlockDemonCrystal", "steadfast");

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(dimensionalPortal));
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
