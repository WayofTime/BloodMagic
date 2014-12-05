package WayofTime.alchemicalWizardry;

import net.minecraft.block.Block;
import WayofTime.alchemicalWizardry.common.block.ArmourForge;
import WayofTime.alchemicalWizardry.common.block.BlockAlchemicCalcinator;
import WayofTime.alchemicalWizardry.common.block.BlockAltar;
import WayofTime.alchemicalWizardry.common.block.BlockBelljar;
import WayofTime.alchemicalWizardry.common.block.BlockBloodLightSource;
import WayofTime.alchemicalWizardry.common.block.BlockConduit;
import WayofTime.alchemicalWizardry.common.block.BlockCrystal;
import WayofTime.alchemicalWizardry.common.block.BlockDemonPortal;
import WayofTime.alchemicalWizardry.common.block.BlockHomHeart;
import WayofTime.alchemicalWizardry.common.block.BlockMasterStone;
import WayofTime.alchemicalWizardry.common.block.BlockPedestal;
import WayofTime.alchemicalWizardry.common.block.BlockPlinth;
import WayofTime.alchemicalWizardry.common.block.BlockReagentConduit;
import WayofTime.alchemicalWizardry.common.block.BlockSchematicSaver;
import WayofTime.alchemicalWizardry.common.block.BlockSocket;
import WayofTime.alchemicalWizardry.common.block.BlockSpectralContainer;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEffect;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEnhancement;
import WayofTime.alchemicalWizardry.common.block.BlockSpellModifier;
import WayofTime.alchemicalWizardry.common.block.BlockSpellParadigm;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.block.BlockWritingTable;
import WayofTime.alchemicalWizardry.common.block.BloodRune;
import WayofTime.alchemicalWizardry.common.block.BloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.EfficiencyRune;
import WayofTime.alchemicalWizardry.common.block.EmptySocket;
import WayofTime.alchemicalWizardry.common.block.ImperfectRitualStone;
import WayofTime.alchemicalWizardry.common.block.LargeBloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.LifeEssenceBlock;
import WayofTime.alchemicalWizardry.common.block.RitualStone;
import WayofTime.alchemicalWizardry.common.block.RuneOfSacrifice;
import WayofTime.alchemicalWizardry.common.block.RuneOfSelfSacrifice;
import WayofTime.alchemicalWizardry.common.block.SpectralBlock;
import WayofTime.alchemicalWizardry.common.block.SpeedRune;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.BlockDemonChest;
import WayofTime.alchemicalWizardry.common.items.ItemBlockCrystalBelljar;
import WayofTime.alchemicalWizardry.common.items.ItemBloodRuneBlock;
import WayofTime.alchemicalWizardry.common.items.ItemCrystalBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEffectBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellParadigmBlock;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:48
 */
public class ModBlocks
{

    public static Block testingBlock;
    public static Block bloodStoneBrick;
    public static Block largeBloodStoneBrick;
    public static BlockAltar blockAltar;
    public static BloodRune bloodRune;
    public static SpeedRune speedRune;
    public static EfficiencyRune efficiencyRune;
    public static RuneOfSacrifice runeOfSacrifice;
    public static RuneOfSelfSacrifice runeOfSelfSacrifice;
    public static Block blockMasterStone;
    public static Block ritualStone;
    public static Block imperfectRitualStone;
    public static Block bloodSocket;
    public static Block emptySocket;
    public static Block armourForge;
    public static Block blockWritingTable;
    public static Block blockHomHeart;
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
    public static Block blockAlchemicCalcinator;
    public static Block blockCrystalBelljar;
    public static Block blockDemonChest;
    public static Block blockCrystal;

    public static void init()
    {
        blockAltar = new BlockAltar();
        bloodRune = new BloodRune();
        speedRune = new SpeedRune();
        efficiencyRune = new EfficiencyRune();
        runeOfSacrifice = new RuneOfSacrifice();
        runeOfSelfSacrifice = new RuneOfSelfSacrifice();
        blockTeleposer = new BlockTeleposer();
        spectralBlock = new SpectralBlock();
        ritualStone = new RitualStone();
        blockMasterStone = new BlockMasterStone();
        imperfectRitualStone = new ImperfectRitualStone();
        bloodSocket = new BlockSocket();
        armourForge = new ArmourForge();
        emptySocket = new EmptySocket();
        largeBloodStoneBrick = new LargeBloodStoneBrick();
        bloodStoneBrick = new BloodStoneBrick();
        blockWritingTable = new BlockWritingTable();
        blockHomHeart = new BlockHomHeart();
        blockPedestal = new BlockPedestal();
        blockPlinth = new BlockPlinth();
        blockConduit = new BlockConduit();
        blockBloodLight = new BlockBloodLightSource();
        blockSpellEffect = new BlockSpellEffect();
        blockSpellParadigm = new BlockSpellParadigm();
        blockSpellModifier = new BlockSpellModifier();
        blockSpellEnhancement = new BlockSpellEnhancement();
        blockSpectralContainer = new BlockSpectralContainer();
        blockDemonPortal = new BlockDemonPortal();
        blockBuildingSchematicSaver = new BlockSchematicSaver();
        blockReagentConduit = new BlockReagentConduit();
        blockAlchemicCalcinator = new BlockAlchemicCalcinator();
        blockCrystalBelljar = new BlockBelljar();
        blockDemonChest = new BlockDemonChest();
        blockCrystal = new BlockCrystal();
        
        blockLifeEssence = new LifeEssenceBlock();
    }

    public static void registerBlocksInPre()
    {
        GameRegistry.registerBlock(ModBlocks.blockAltar, "Altar");
        GameRegistry.registerBlock(ModBlocks.bloodRune, ItemBloodRuneBlock.class, "AlchemicalWizardry" + (ModBlocks.bloodRune.getUnlocalizedName().substring(5)));
        GameRegistry.registerBlock(ModBlocks.blockLifeEssence, "lifeEssence");
        GameRegistry.registerBlock(ModBlocks.speedRune, "speedRune");
        GameRegistry.registerBlock(ModBlocks.efficiencyRune, "efficiencyRune");
        GameRegistry.registerBlock(ModBlocks.runeOfSacrifice, "runeOfSacrifice");
        GameRegistry.registerBlock(ModBlocks.runeOfSelfSacrifice, "runeOfSelfSacrifice");
        GameRegistry.registerBlock(ModBlocks.ritualStone, "ritualStone");
        GameRegistry.registerBlock(ModBlocks.blockMasterStone, "masterStone");
        GameRegistry.registerBlock(ModBlocks.bloodSocket, "bloodSocket");
        GameRegistry.registerBlock(ModBlocks.imperfectRitualStone, "imperfectRitualStone");

        GameRegistry.registerBlock(ModBlocks.armourForge, "armourForge");
        GameRegistry.registerBlock(ModBlocks.emptySocket, "emptySocket");
        GameRegistry.registerBlock(ModBlocks.bloodStoneBrick, "bloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.largeBloodStoneBrick, "largeBloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.blockWritingTable, "blockWritingTable");
        GameRegistry.registerBlock(ModBlocks.blockHomHeart, "blockHomHeart");
        GameRegistry.registerBlock(ModBlocks.blockPedestal, "blockPedestal");
        GameRegistry.registerBlock(ModBlocks.blockPlinth, "blockPlinth");
        GameRegistry.registerBlock(ModBlocks.blockTeleposer, "blockTeleposer");
        GameRegistry.registerBlock(ModBlocks.spectralBlock, "spectralBlock");
        GameRegistry.registerBlock(ModBlocks.blockBloodLight, "bloodLight");

        GameRegistry.registerBlock(ModBlocks.blockConduit, "blockConduit");
        GameRegistry.registerBlock(ModBlocks.blockSpellParadigm, ItemSpellParadigmBlock.class, "AlchemicalWizardry" + (ModBlocks.blockSpellParadigm.getUnlocalizedName()));
        GameRegistry.registerBlock(ModBlocks.blockSpellEnhancement, ItemSpellEnhancementBlock.class, "AlchemicalWizardry" + (ModBlocks.blockSpellEnhancement.getUnlocalizedName()));
        GameRegistry.registerBlock(ModBlocks.blockSpellModifier, ItemSpellModifierBlock.class, "AlchemicalWizardry" + (ModBlocks.blockSpellModifier.getUnlocalizedName()));
        GameRegistry.registerBlock(ModBlocks.blockSpellEffect, ItemSpellEffectBlock.class, "AlchemicalWizardry" + (ModBlocks.blockSpellEffect.getUnlocalizedName()));

        GameRegistry.registerBlock(ModBlocks.blockSpectralContainer, "spectralContainer");
        GameRegistry.registerBlock(ModBlocks.blockDemonPortal, "demonPortalMain");
        GameRegistry.registerBlock(ModBlocks.blockBuildingSchematicSaver, "blockSchemSaver");
        GameRegistry.registerBlock(ModBlocks.blockReagentConduit, "blockReagentConduit");
        GameRegistry.registerBlock(ModBlocks.blockAlchemicCalcinator, "blockAlchemicCalcinator");
        GameRegistry.registerBlock(ModBlocks.blockCrystalBelljar, ItemBlockCrystalBelljar.class, "blockCrystalBelljar");
        GameRegistry.registerBlock(ModBlocks.blockDemonChest, "blockDemonChest");
        GameRegistry.registerBlock(ModBlocks.blockCrystal, ItemCrystalBlock.class, "blockCrystal");
    }

    public static void registerBlocksInInit()
    {
    }
}
