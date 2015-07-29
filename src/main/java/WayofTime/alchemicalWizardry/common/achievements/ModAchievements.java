package WayofTime.alchemicalWizardry.common.achievements;

import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import WayofTime.alchemicalWizardry.ModItems;

public class ModAchievements
{
    public static AchievementPage alchemicalWizardryPage;

    public static Achievement firstPrick;
    public static Achievement weakOrb;

    public static void init()
    {
        firstPrick = new AchievementsMod(StatCollector.translateToLocal("firstPrick"), 0, 0, ModItems.sacrificialDagger, null);
        weakOrb = new AchievementsMod(StatCollector.translateToLocal("weakOrb"), 3, 0, ModItems.weakBloodOrb, firstPrick);

        alchemicalWizardryPage = new AchievementPage("AlchemicalWizardry", AchievementsMod.achievements.toArray(new Achievement[AchievementsMod.achievements.size()]));
        AchievementPage.registerAchievementPage(alchemicalWizardryPage);
        AchievementsRegistry.init();
        FMLCommonHandler.instance().bus().register(new AchievementTrigger());
    }
}
