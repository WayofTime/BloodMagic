package WayofTime.alchemicalWizardry.common.achievements;

import WayofTime.alchemicalWizardry.ModItems;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

public class ModAchievements
{
    public static AchievementPage alchemicalWizardryPage;

    public static Achievement firstPrick;

    public static void init()
    {
        firstPrick = new AchievementsMod(StatCollector.translateToLocal("achievements.firstPrick"), 0, 0, ModItems.sacrificialDagger, firstPrick);

        alchemicalWizardryPage = new AchievementPage("AlchemicalWizardry", AchievementsMod.achievements.toArray(new Achievement[AchievementsMod.achievements.size()]));
        AchievementPage.registerAchievementPage(alchemicalWizardryPage);
        FMLCommonHandler.instance().bus().register(new AchievementTrigger());
    }
}
