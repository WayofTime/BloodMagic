package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.*;
import WayofTime.bloodmagic.item.block.*;
import WayofTime.bloodmagic.tile.*;
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
    public static Block testSpellBlock;
    public static Block pedestal;
    public static Block teleposer;
    public static Block alchemyArray;
    public static Block spectralBlock;
    public static Block phantomBlock;
    public static Block soulForge;

    public static Block lifeEssence;

    public static Block crystal;
    public static Block bloodStoneBrick;

    public static void init()
    {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        lifeEssence = registerBlock(new BlockLifeEssence());

        altar = registerBlock(new BlockAltar());
        bloodRune = registerBlock(new BlockBloodRune(), ItemBlockBloodRune.class);
        ritualController = registerBlock(new BlockRitualController(), ItemBlockRitualController.class);
        ritualStone = registerBlock(new BlockRitualStone(), ItemBlockRitualStone.class);
        bloodLight = registerBlock(new BlockBloodLight());
        testSpellBlock = registerBlock(new BlockTestSpellBlock());
        pedestal = registerBlock(new BlockPedestal(), ItemBlockPedestal.class);
        teleposer = registerBlock(new BlockTeleposer());
        alchemyArray = registerBlock(new BlockAlchemyArray());
        spectralBlock = registerBlock(new BlockSpectral());
        phantomBlock = registerBlock(new BlockPhantom());
        soulForge = registerBlock(new BlockSoulForge());
        crystal = registerBlock(new BlockCrystal(), ItemBlockCrystal.class);
        bloodStoneBrick = registerBlock(new BlockBloodStoneBrick(), ItemBlockBloodStoneBrick.class);

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
    }

    public static void initRenders()
    {
        InventoryRenderHelper renderHelper = BloodMagic.proxy.getRenderHelper();

        renderHelper.fluidRender(lifeEssence);
        for (int i = 0; i < BlockBloodRune.names.length; i++)
        {
            renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), i);
        }

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
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, itemBlock, name);

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock)
    {
        return registerBlock(block, itemBlock, block.getClass().getSimpleName());
    }

    private static Block registerBlock(Block block, String name)
    {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, name);

        return block;
    }

    private static Block registerBlock(Block block)
    {
        return registerBlock(block, block.getClass().getSimpleName());
    }
}
