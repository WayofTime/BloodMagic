package WayofTime.bloodmagic.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockAlchemyArray;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockBloodLight;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockBloodStoneBrick;
import WayofTime.bloodmagic.block.BlockCrystal;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.block.BlockPedestal;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.block.BlockTestSpellBlock;
import WayofTime.bloodmagic.item.block.ItemBlockBloodRune;
import WayofTime.bloodmagic.item.block.ItemBlockBloodStoneBrick;
import WayofTime.bloodmagic.item.block.ItemBlockCrystal;
import WayofTime.bloodmagic.item.block.ItemBlockPedestal;
import WayofTime.bloodmagic.item.block.ItemBlockRitualController;
import WayofTime.bloodmagic.item.block.ItemBlockRitualStone;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.tile.TilePlinth;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class ModBlocks {
    public static Block altar;
    public static Block bloodRune;
    public static Block ritualController;
    public static Block ritualStone;
    public static Block bloodLight;
    public static Block testSpellBlock;
    public static Block pedestal;
    public static Block teleposer;
    public static Block alchemyArray;

    public static Block lifeEssence;

    public static Block crystal;
    public static Block bloodStoneBrick;

    public static void init() {
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
        crystal = registerBlock(new BlockCrystal(), ItemBlockCrystal.class);
        bloodStoneBrick = registerBlock(new BlockBloodStoneBrick(), ItemBlockBloodStoneBrick.class);

        initTiles();
    }

    public static void initTiles() {
        GameRegistry.registerTileEntity(TileAltar.class, Constants.Mod.MODID + ":" + TileAltar.class.getSimpleName());
        GameRegistry.registerTileEntity(TileImperfectRitualStone.class, Constants.Mod.MODID + ":" + TileImperfectRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TileMasterRitualStone.class, Constants.Mod.MODID + ":" + TileMasterRitualStone.class.getSimpleName());
        GameRegistry.registerTileEntity(TilePlinth.class, Constants.Mod.MODID + ":" + TilePlinth.class.getSimpleName());
        GameRegistry.registerTileEntity(TileAlchemyArray.class, Constants.Mod.MODID + ":" + TileAlchemyArray.class.getSimpleName());
    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = BloodMagic.instance.getRenderHelper();

        renderHelper.fluidRender(lifeEssence);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 1);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 2);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 3);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 4);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 5);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 6);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 7);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 8);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(bloodRune), 9);
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
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name) {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, itemBlock, name);

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
        return registerBlock(block, itemBlock, block.getClass().getSimpleName());
    }

    private static Block registerBlock(Block block, String name) {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, name);

        return block;
    }

    private static Block registerBlock(Block block) {
        return registerBlock(block, block.getClass().getSimpleName());
    }
}
