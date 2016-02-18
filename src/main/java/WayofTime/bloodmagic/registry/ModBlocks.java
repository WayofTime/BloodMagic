package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.*;
import WayofTime.bloodmagic.item.block.*;
import WayofTime.bloodmagic.tile.*;
import WayofTime.bloodmagic.tile.routing.*;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        masterRoutingNode = registerBlock(new BlockMasterRoutingNode());
        inputRoutingNode = registerBlock(new BlockInputRoutingNode());
        outputRoutingNode = registerBlock(new BlockOutputRoutingNode());
        itemRoutingNode = registerBlock(new BlockItemRoutingNode());
        incenseAltar = registerBlock(new BlockIncenseAltar());
        pathBlock = registerBlock(new BlockPath(), ItemBlockPath.class);
        demonCrucible = registerBlock(new BlockDemonCrucible());

        dimensionalPortal = registerBlock(new BlockDimensionalPortal());
        bloodTank = registerBlock(new BlockBloodTank(), ItemBlockBloodTank.class);

//        testSpellBlock = registerBlock(new BlockTestSpellBlock());

        BloodMagicAPI.addToTeleposerBlacklist(inputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(outputRoutingNode);
        BloodMagicAPI.addToTeleposerBlacklist(itemRoutingNode);

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

        GameRegistry.registerTileEntity(TileDimensionalPortal.class, Constants.Mod.MODID + ":" + TileDimensionalPortal.class.getSimpleName());
        GameRegistry.registerTileEntity(TileBloodTank.class, Constants.Mod.MODID + ":" + TileBloodTank.class.getSimpleName());
    }

    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();

        renderHelper.fluidRender(lifeEssence);
        for (int i = 0; i < BlockBloodRune.names.length; i++)
        {
            renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), i);
        }

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(altar));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualController), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualController), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 2);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 3);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 4);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 5);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 6);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodStoneBrick), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodStoneBrick), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(crystal), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(crystal), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodLight));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pedestal), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pedestal), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(teleposer));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(alchemyArray));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(spectralBlock));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(phantomBlock));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(soulForge));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(outputRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(inputRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(masterRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(itemRoutingNode));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(incenseAltar));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(demonCrucible));
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 2);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 3);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 4);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 5);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 6);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(pathBlock), 7);

        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(dimensionalPortal));
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, itemBlock, name);

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock)
    {
        if (block.getRegistryName() == null)
        {
            BloodMagic.instance.getLogger().error("Attempted to register Block {} without setting a registry name. Block will not be registered. Please report this.", block.getClass().getCanonicalName());
            return block;
        }

        if (!ConfigHandler.blockBlacklist.contains(block.getRegistryName().split(":")[1]))
            GameRegistry.registerBlock(block, itemBlock);

        return block;
    }

    private static Block registerBlock(Block block, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, name);

        return block;
    }

    private static Block registerBlock(Block block)
    {
        if (block.getRegistryName() == null)
        {
            BloodMagic.instance.getLogger().error("Attempted to register Block {} without setting a registry name. Block will not be registered. Please report this.", block.getClass().getCanonicalName());
            return null;
        }

        if (!ConfigHandler.blockBlacklist.contains(block.getRegistryName().split(":")[1]))
            GameRegistry.registerBlock(block);

        return block;
    }
}
