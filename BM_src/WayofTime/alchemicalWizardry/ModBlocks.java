package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.common.LifeEssence;
import WayofTime.alchemicalWizardry.common.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

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
    //    public static Block lifeEssenceStill;
//    public static Block lifeEssenceFlowing;
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

    public static void init()
    {

        //testingBlock = new TestingBlock(AlchemicalWizardry.testingBlockBlockID, Material.ground).setHardness(2.0F).setStepSound(Block.soundStoneFootstep).setCreativeTab(CreativeTabs.tabBlock).setLightValue(1.0F);
//lifeEssenceStill = new LifeEssenceStill(lifeEssenceStillBlockID);
//lifeEssenceFlowing = new LifeEssenceFlowing(lifeEssenceFlowingBlockID);
        blockAltar = new BlockAltar(AlchemicalWizardry.blockAltarBlockID);
        bloodRune = new BloodRune(AlchemicalWizardry.bloodRuneBlockID);
        speedRune = new SpeedRune(AlchemicalWizardry.speedRuneBlockID);
        efficiencyRune = new EfficiencyRune(AlchemicalWizardry.efficiencyRuneBlockID);
        runeOfSacrifice = new RuneOfSacrifice(AlchemicalWizardry.runeOfSacrificeBlockID);
        runeOfSelfSacrifice = new RuneOfSelfSacrifice(AlchemicalWizardry.runeOfSelfSacrificeBlockID);
        AlchemicalWizardry.lifeEssenceFluid = new LifeEssence("Life Essence");
        blockTeleposer = new BlockTeleposer(AlchemicalWizardry.blockTeleposerBlockID);
        spectralBlock = new SpectralBlock(AlchemicalWizardry.spectralBlockBlockID);
        ritualStone = new RitualStone(AlchemicalWizardry.ritualStoneBlockID);
        blockMasterStone = new BlockMasterStone(AlchemicalWizardry.blockMasterStoneBlockID);
        imperfectRitualStone = new ImperfectRitualStone(AlchemicalWizardry.imperfectRitualStoneBlockID);
        bloodSocket = new BlockSocket(AlchemicalWizardry.bloodSocketBlockID);
        armourForge = new ArmourForge(AlchemicalWizardry.armourForgeBlockID);
        emptySocket = new EmptySocket(AlchemicalWizardry.emptySocketBlockID);
        largeBloodStoneBrick = new LargeBloodStoneBrick(AlchemicalWizardry.largeBloodStoneBrickBlockID);
        bloodStoneBrick = new BloodStoneBrick(AlchemicalWizardry.bloodStoneBrickBlockID);
        blockWritingTable = new BlockWritingTable(AlchemicalWizardry.blockWritingTableBlockID);
        blockHomHeart = new BlockHomHeart(AlchemicalWizardry.blockHomHeartBlockID);
        blockPedestal = new BlockPedestal(AlchemicalWizardry.blockPedestalBlockID);
        blockPlinth = new BlockPlinth(AlchemicalWizardry.blockPlinthBlockID);
        blockBloodLight = new BlockBloodLightSource(AlchemicalWizardry.blockBloodLightBlockID);

    }
}
