package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.item.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockAlchemyArray;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockBloodLight;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockBloodStoneBrick;
import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.block.BlockCrystal;
import WayofTime.bloodmagic.block.BlockDemonCrucible;
import WayofTime.bloodmagic.block.BlockDemonCrystal;
import WayofTime.bloodmagic.block.BlockDemonCrystallizer;
import WayofTime.bloodmagic.block.BlockDemonPylon;
import WayofTime.bloodmagic.block.BlockDimensionalPortal;
import WayofTime.bloodmagic.block.BlockIncenseAltar;
import WayofTime.bloodmagic.block.BlockInputRoutingNode;
import WayofTime.bloodmagic.block.BlockItemRoutingNode;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.block.BlockMasterRoutingNode;
import WayofTime.bloodmagic.block.BlockOutputRoutingNode;
import WayofTime.bloodmagic.block.BlockPath;
import WayofTime.bloodmagic.block.BlockPedestal;
import WayofTime.bloodmagic.block.BlockPhantom;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.block.BlockSoulForge;
import WayofTime.bloodmagic.block.BlockSpectral;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
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

    public static void init()
    {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        lifeEssence = registerBlock(new BlockLifeEssence());

        altar = registerBlock(new BlockAltar());
        bloodRune = registerBlock(new BlockBloodRune(), ItemBlockBloodRune.class);
        ritualController = registerBlock(new BlockRitualController(), ItemBlockRitualController.class);
        ritualStone = registerBlock(new BlockRitualStone(), ItemBlockRitualStone.class);
        bloodLight = registerBlock(new BlockBloodLight());
        pedestal = registerBlock(new BlockPedestal(), ItemBlockPedestal.class);
        teleposer = registerBlock(new BlockTeleposer());
        alchemyArray = registerBlock(new BlockAlchemyArray());
        spectralBlock = registerBlock(new BlockSpectral());
        phantomBlock = registerBlock(new BlockPhantom());
        soulForge = registerBlock(new BlockSoulForge());
        crystal = registerBlock(new BlockCrystal(), ItemBlockCrystal.class);
        bloodStoneBrick = registerBlock(new BlockBloodStoneBrick(), ItemBlockBloodStoneBrick.class);
        masterRoutingNode = registerBlock(new BlockMasterRoutingNode(), ItemBlockRoutingNode.class);
        inputRoutingNode = registerBlock(new BlockInputRoutingNode(), ItemBlockRoutingNode.class);
        outputRoutingNode = registerBlock(new BlockOutputRoutingNode(), ItemBlockRoutingNode.class);
        itemRoutingNode = registerBlock(new BlockItemRoutingNode(), ItemBlockRoutingNode.class);
        incenseAltar = registerBlock(new BlockIncenseAltar());
        pathBlock = registerBlock(new BlockPath(), ItemBlockPath.class);
        demonCrucible = registerBlock(new BlockDemonCrucible());
        demonPylon = registerBlock(new BlockDemonPylon());
        demonCrystallizer = registerBlock(new BlockDemonCrystallizer());
        demonCrystal = registerBlock(new BlockDemonCrystal(), ItemBlockDemonCrystal.class);

        dimensionalPortal = registerBlock(new BlockDimensionalPortal());
        bloodTank = registerBlock(new BlockBloodTank(), ItemBlockBloodTank.class);

//        testSpellBlock = registerBlock(new BlockTestSpellBlock());

        BloodMagicAPI.addToTeleposerBlacklist(inputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(outputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(itemRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(demonCrystal);

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

        GameRegistry.registerTileEntity(TileDimensionalPortal.class, Constants.Mod.MODID + ":" + TileDimensionalPortal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileBloodTank.class, Constants.Mod.MODID + ":" + TileBloodTank.class.getSimpleName());
    }

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
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 0, "ItemBlockDemonCrystal", "default");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 1, "ItemBlockDemonCrystal", "corrosive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 2, "ItemBlockDemonCrystal", "destructive");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 3, "ItemBlockDemonCrystal", "vengeful");
        renderHelperV2.registerRender(InventoryRenderHelper.getItemFromBlock(demonCrystal), 4, "ItemBlockDemonCrystal", "steadfast");

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(dimensionalPortal));
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
        {
            GameRegistry.registerBlock(block, itemBlock);
            BloodMagic.proxy.tryHandleBlockModel(block, name);
        }

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock)
    {
        if (block.getRegistryName() == null)
        {
            BloodMagic.instance.getLogger().error("Attempted to register Block {} without setting a registry name. Block will not be registered. Please report this.", block.getClass().getCanonicalName());
            return block;
        }

        String blockName = block.getRegistryName().split(":")[1];
        if (!ConfigHandler.blockBlacklist.contains(blockName))
        {
            GameRegistry.registerBlock(block, itemBlock);
            BloodMagic.proxy.tryHandleBlockModel(block, blockName);
        }

        return block;
    }

    private static Block registerBlock(Block block)
    {
        if (block.getRegistryName() == null)
        {
            BloodMagic.instance.getLogger().error("Attempted to register Block {} without setting a registry name. Block will not be registered. Please report this.", block.getClass().getCanonicalName());
            return null;
        }

        String blockName = block.getRegistryName().split(":")[1];
        if (!ConfigHandler.blockBlacklist.contains(blockName))
        {
            GameRegistry.registerBlock(block);
            BloodMagic.proxy.tryHandleBlockModel(block, blockName);
        }

        return block;
    }
}
