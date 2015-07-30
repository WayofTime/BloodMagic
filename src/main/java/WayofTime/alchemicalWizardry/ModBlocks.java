package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.client.BlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.alchemicalWizardry.common.block.BlockArmourForge;
import WayofTime.alchemicalWizardry.common.block.BlockAlchemicalCalcinator;
import WayofTime.alchemicalWizardry.common.block.BlockAltar;
import WayofTime.alchemicalWizardry.common.block.BlockBelljar;
import WayofTime.alchemicalWizardry.common.block.BlockBloodLightSource;
import WayofTime.alchemicalWizardry.common.block.BlockConduit;
import WayofTime.alchemicalWizardry.common.block.BlockIncenseCrucible;
import WayofTime.alchemicalWizardry.common.block.BlockCrystal;
import WayofTime.alchemicalWizardry.common.block.BlockDemonPortal;
import WayofTime.alchemicalWizardry.common.block.BlockEnchantmentGlyph;
import WayofTime.alchemicalWizardry.common.block.BlockSpellTable;
import WayofTime.alchemicalWizardry.common.block.BlockMasterStone;
import WayofTime.alchemicalWizardry.common.block.BlockPedestal;
import WayofTime.alchemicalWizardry.common.block.BlockPlinth;
import WayofTime.alchemicalWizardry.common.block.BlockReagentConduit;
import WayofTime.alchemicalWizardry.common.block.BlockSchematicSaver;
import WayofTime.alchemicalWizardry.common.block.BlockFilledSocket;
import WayofTime.alchemicalWizardry.common.block.BlockSpectralContainer;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEffect;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEnhancement;
import WayofTime.alchemicalWizardry.common.block.BlockSpellModifier;
import WayofTime.alchemicalWizardry.common.block.BlockSpellParadigm;
import WayofTime.alchemicalWizardry.common.block.BlockStabilityGlyph;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.block.BlockChemistrySet;
import WayofTime.alchemicalWizardry.common.block.BlockBloodRune;
import WayofTime.alchemicalWizardry.common.block.BlockBloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.BlockEfficiencyRune;
import WayofTime.alchemicalWizardry.common.block.BlockEmptySocket;
import WayofTime.alchemicalWizardry.common.block.BlockImperfectRitualStone;
import WayofTime.alchemicalWizardry.common.block.BlockLargeBloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.BlockLifeEssence;
import WayofTime.alchemicalWizardry.common.block.BlockMimic;
import WayofTime.alchemicalWizardry.common.block.BlockRitualStone;
import WayofTime.alchemicalWizardry.common.block.BlockRuneOfSacrifice;
import WayofTime.alchemicalWizardry.common.block.BlockRuneOfSelfSacrifice;
import WayofTime.alchemicalWizardry.common.block.BlockSpectral;
import WayofTime.alchemicalWizardry.common.block.BlockSpeedRune;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.BlockDemonChest;
import WayofTime.alchemicalWizardry.common.items.ItemBlockCrystalBelljar;
import WayofTime.alchemicalWizardry.common.items.ItemBloodRuneBlock;
import WayofTime.alchemicalWizardry.common.items.ItemCrystalBlock;
import WayofTime.alchemicalWizardry.common.items.ItemEnchantmentGlyphBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEffectBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.items.ItemStabilityGlyphBlock;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:48
 */
public class ModBlocks
{
    public static Block bloodStoneBrick;
    public static Block largeBloodStoneBrick;
    public static BlockAltar blockAltar;
    public static BlockBloodRune bloodRune;
    public static BlockSpeedRune speedRune;
    public static BlockEfficiencyRune efficiencyRune;
    public static BlockRuneOfSacrifice runeOfSacrifice;
    public static BlockRuneOfSelfSacrifice runeOfSelfSacrifice;
    public static Block blockMasterStone;
    public static Block ritualStone;
    public static Block imperfectRitualStone;
    public static Block bloodSocket;
    public static Block emptySocket;
    public static Block armourForge;
    public static Block blockWritingTable;
    public static Block blockSpellTable;
    public static Block blockPedestal;
    public static Block blockPlinth;
    public static Block blockLifeEssence;
    public static Block blockTeleposer;
    public static Block spectralBlock;
    public static Block blockConduit;
    public static Block blockBloodLight;
    public static Block blockSpellEffect;
    public static Block blockSpellParadigm;
    public static Block blockSpellModifier;
    public static Block blockSpellEnhancement;
    public static Block blockSpectralContainer;
    public static Block blockBuildingSchematicSaver;
    public static Block blockDemonPortal;
    public static Block blockReagentConduit;
    public static Block blockAlchemicalCalcinator;
    public static Block blockCrystalBelljar;
    public static Block blockDemonChest;
    public static Block blockCrystal;
    public static Block blockMimic;
    public static Block blockEnchantmentGlyph;
    public static Block blockStabilityGlyph;
    public static Block blockCrucible;

    public static ArrayList<String> blocksNotToBeRegistered = new ArrayList<String>();

    public static void init()
    {
        blockAltar = (BlockAltar) registerBlock(new BlockAltar(), "altar");
        bloodRune = (BlockBloodRune) registerBlock(new BlockBloodRune(), ItemBloodRuneBlock.class, "base_rune");
        speedRune = (BlockSpeedRune) registerBlock(new BlockSpeedRune(), "speed_rune");
        efficiencyRune = (BlockEfficiencyRune) registerBlock(new BlockEfficiencyRune(), "efficiency_rune");
        runeOfSacrifice = (BlockRuneOfSacrifice) registerBlock(new BlockRuneOfSacrifice(), "sacrifice_rune");
        runeOfSelfSacrifice = (BlockRuneOfSelfSacrifice) registerBlock(new BlockRuneOfSelfSacrifice(), "self_sacrifice_rune");
        blockTeleposer = registerBlock(new BlockTeleposer(), "teleposer");
        spectralBlock = registerBlock(new BlockSpectral(), "spectral_block");
        ritualStone = registerBlock(new BlockRitualStone(), "ritual_stone");
        blockMasterStone = registerBlock(new BlockMasterStone(), "master_ritual_stone");
        imperfectRitualStone = registerBlock(new BlockImperfectRitualStone(), "imperfect_ritual_stone");
        emptySocket = registerBlock(new BlockEmptySocket(), "empty_socket");
        bloodSocket = registerBlock(new BlockFilledSocket(), "filled_socket");
        armourForge = registerBlock(new BlockArmourForge(), "soul_armour_forge");
        largeBloodStoneBrick = registerBlock(new BlockLargeBloodStoneBrick(), "large_bloodstone_brick");
        bloodStoneBrick = registerBlock(new BlockBloodStoneBrick(), "bloodstone_brick");
        blockWritingTable = registerBlock(new BlockChemistrySet(), "chemistry_set");
        blockSpellTable = registerBlock(new BlockSpellTable(), "spell_table");
        blockPedestal = registerBlock(new BlockPedestal(), "pedestal");
        blockPlinth = registerBlock(new BlockPlinth(), "plinth");
        blockConduit = registerBlock(new BlockConduit(), "spell_conduit");
        blockBloodLight = registerBlock(new BlockBloodLightSource(), "blood_light");
        blockSpellEffect = registerBlock(new BlockSpellEffect(), "spell_effect");
        blockSpellParadigm = registerBlock(new BlockSpellParadigm(), ItemSpellParadigmBlock.class, "spell_paradigm");
        blockSpellModifier = registerBlock(new BlockSpellModifier(), ItemSpellEnhancementBlock.class, "spell_modifier");
        blockSpellEnhancement = registerBlock(new BlockSpellEnhancement(), ItemSpellModifierBlock.class, "spell_enhancement");
        blockSpectralContainer = registerBlock(new BlockSpectralContainer(), ItemSpellEffectBlock.class, "spectral_container");
        blockDemonPortal = registerBlock(new BlockDemonPortal(), "demon_portal");
        blockBuildingSchematicSaver = registerBlock(new BlockSchematicSaver(), "schematic_saver");
        blockReagentConduit = registerBlock(new BlockReagentConduit(), "reagent_conduit");
        blockAlchemicalCalcinator = registerBlock(new BlockAlchemicalCalcinator(), "alchemical_calcinator");
        blockCrystalBelljar = registerBlock(new BlockBelljar(), ItemBlockCrystalBelljar.class, "belljar");
        blockDemonChest = registerBlock(new BlockDemonChest(), "demon_chest");
        blockCrystal = registerBlock(new BlockCrystal(), ItemCrystalBlock.class, "crystal_block");
        blockMimic = registerBlock(new BlockMimic(), "mimic_block");
        
        blockLifeEssence = registerBlock(new BlockLifeEssence(), "life_essence");
        blockEnchantmentGlyph = registerBlock(new BlockEnchantmentGlyph(), ItemEnchantmentGlyphBlock.class, "enchantment_glyph");
        blockStabilityGlyph = registerBlock(new BlockStabilityGlyph(), ItemStabilityGlyphBlock.class, "stability_glyph");
        blockCrucible = registerBlock(new BlockIncenseCrucible(), "incense_crucible");
    }

    public static Block registerBlock(Block block, String unlocalizedName)
    {
        //TODO Insert Model Code here
        block.setUnlocalizedName(unlocalizedName);

//        if (!) //creative tab blacklist
        {
            block.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        }

        blocksNotToBeRegistered.clear();
        for (String unlocName : BloodMagicConfiguration.blocksToBeDisabled)
        {
            if (unlocName.equals(unlocalizedName))
            {
                blocksNotToBeRegistered.add(unlocName);
            }
        }
        if (!blocksNotToBeRegistered.contains(unlocalizedName))
        {
            GameRegistry.registerBlock(block, unlocalizedName);
        }

        BlockRenderer.registerBlock(block);

        return block;
    }

    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String unlocalizedName)
    {
        block.setCreativeTab(AlchemicalWizardry.tabBloodMagic);

        for (String unlocName : BloodMagicConfiguration.blocksToBeDisabled)
        {
            System.out.println(unlocName);
            if (unlocName.equals(unlocalizedName))
            {
                blocksNotToBeRegistered.add(unlocName);
            }
        }
        if (!blocksNotToBeRegistered.contains(unlocalizedName))
        {
            GameRegistry.registerBlock(block, itemBlockClass, unlocalizedName);
        }

        BlockRenderer.registerBlock(block);

        return block;
    }
}
