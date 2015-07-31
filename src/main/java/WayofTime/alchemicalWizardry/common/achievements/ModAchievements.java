package WayofTime.alchemicalWizardry.common.achievements;

import WayofTime.alchemicalWizardry.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import WayofTime.alchemicalWizardry.ModItems;
import cpw.mods.fml.common.FMLCommonHandler;

public class ModAchievements
{
    public static AchievementPage alchemicalWizardryPage;

    public static Achievement firstPrick;
    public static Achievement weakOrb;
    public static Achievement bloodLettersPack;
    public static Achievement waterSigil;
    public static Achievement blankRunes;
    public static Achievement apprenticeOrb;
    public static Achievement airSigil;
    public static Achievement daggerOfSacrifice;
    public static Achievement brewingPotions;
    public static Achievement magicianOrb;
    public static Achievement sigilHolding;
    public static Achievement boundBlade;
    public static Achievement boundArmor;
    public static Achievement complexSpells;
    public static Achievement ritualDiviner;
    public static Achievement masterOrb;
    public static Achievement demonSpawn;
    public static Achievement phantomBridgeSigil;
    public static Achievement teleposer;
    public static Achievement suppressionSigil;
    public static Achievement archmageOrb;
    public static Achievement energyBazooka;
    public static Achievement demons;
    public static Achievement transcendentOrb;

    public static void init()
    {
        firstPrick = new AchievementsMod("firstPrick", 0, 0, ModItems.sacrificialDagger, null).setSpecial();
        weakOrb = new AchievementsMod("weakOrb", 3, 0, ModItems.weakBloodOrb, firstPrick);
        bloodLettersPack = new AchievementsMod("bloodLettersPack", 3, 2, ModItems.itemBloodPack, weakOrb);
        waterSigil = new AchievementsMod("waterSigil", 6, 2, ModItems.waterSigil, weakOrb);
        blankRunes = new AchievementsMod("blankRunes", 4, -2, ModBlocks.bloodRune, weakOrb);
        apprenticeOrb = new AchievementsMod("apprenticeOrb", 4, -4, ModItems.apprenticeBloodOrb, blankRunes);
        airSigil = new AchievementsMod("airSigil", 6, 1, ModItems.airSigil, apprenticeOrb);
        daggerOfSacrifice = new AchievementsMod("daggerSacrifice", 4, -5, ModItems.daggerOfSacrifice, apprenticeOrb);
        brewingPotions = new AchievementsMod("brewingPotions", 6, -3, ModBlocks.blockWritingTable, apprenticeOrb);
        magicianOrb = new AchievementsMod("magicianOrb", 2, -2, ModItems.magicianBloodOrb, apprenticeOrb);
        sigilHolding = new AchievementsMod("sigilHolding", 6, 0, ModItems.sigilOfHolding, magicianOrb);
        boundBlade = new AchievementsMod("boundBlade", 0, -2, ModItems.energySword, magicianOrb);
        boundArmor = new AchievementsMod("boundArmor", 1, -1, ModItems.boundPlate, magicianOrb);
        complexSpells = new AchievementsMod("complexSpells", 1, -4, ModItems.itemComplexSpellCrystal, magicianOrb);
        ritualDiviner = new AchievementsMod("ritualDiviner", 1, -3, ModItems.itemRitualDiviner, magicianOrb);
        masterOrb = new AchievementsMod("masterOrb", -2, -1, ModItems.masterBloodOrb, boundBlade);
        demonSpawn = new AchievementsMod("demonSpawn", -3, -2, ModItems.demonPlacer, masterOrb);
        phantomBridgeSigil = new AchievementsMod("phantomBridgeSigil", 6, -1, ModItems.sigilOfTheBridge, masterOrb);
        teleposer = new AchievementsMod("teleposer", -4, -1, ModBlocks.blockTeleposer, masterOrb);
        suppressionSigil = new AchievementsMod("suppressionSigil", 6, -2, ModItems.itemSigilOfSupression, masterOrb);
        archmageOrb = new AchievementsMod("archmageOrb", -1, 2, ModItems.archmageBloodOrb, masterOrb);
        energyBazooka = new AchievementsMod("energyBazooka", -3, 2, ModItems.energyBazooka, archmageOrb);
        demons = new AchievementsMod("demons", 0, 3, new ItemStack(ModItems.baseItems, 1, 29), archmageOrb).setSpecial();
        transcendentOrb = new AchievementsMod("trancsendentOrb", 0, 5, ModItems.transcendentBloodOrb, demons);

        alchemicalWizardryPage = new AchievementPage("Blood Magic", AchievementsMod.achievements.toArray(new Achievement[AchievementsMod.achievements.size()]));
        AchievementPage.registerAchievementPage(alchemicalWizardryPage);
        AchievementsRegistry.init();
        FMLCommonHandler.instance().bus().register(new AchievementTrigger());
    }
}
