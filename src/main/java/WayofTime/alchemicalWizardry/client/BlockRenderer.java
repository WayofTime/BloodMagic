package WayofTime.alchemicalWizardry.client;

import WayofTime.alchemicalWizardry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRenderer
{
    public static void registerBlockRenders()
    {
        registerBlock(ModBlocks.armourForge);
        registerBlock(ModBlocks.speedRune);
        registerBlock(ModBlocks.efficiencyRune);
        registerBlock(ModBlocks.bloodSocket);
        registerBlock(ModBlocks.emptySocket);
        registerBlock(ModBlocks.blockEnchantmentGlyph);
        registerBlock(ModBlocks.blockStabilityGlyph);
        registerBlock(ModBlocks.runeOfSelfSacrifice);
        registerBlock(ModBlocks.runeOfSacrifice);
        registerBlock(ModBlocks.bloodStoneBrick);
        registerBlock(ModBlocks.largeBloodStoneBrick);
        registerBlock(ModBlocks.blockMasterStone);
        registerBlock(ModBlocks.ritualStone);
        registerBlock(ModBlocks.blockTeleposer);
        registerBlock(ModBlocks.blockSpellTable);
        registerBlock(ModBlocks.imperfectRitualStone);
        registerBlock(ModBlocks.spectralBlock);
    }

    public static void registerBlock(Block block)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("alchemicalwizardry:" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
