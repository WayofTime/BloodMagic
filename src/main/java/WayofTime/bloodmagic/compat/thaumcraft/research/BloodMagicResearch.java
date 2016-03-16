package WayofTime.bloodmagic.compat.thaumcraft.research;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchPage;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;

public class BloodMagicResearch
{
    public static void addResearch()
    {
        final String BLOOD_MAGIC = "BLOODMAGIC";

        ResearchCategories.registerCategory(BLOOD_MAGIC, null, new ResourceLocation("bloodmagic", "textures/items/WeakBloodOrb.png"), new ResourceLocation("bloodmagic", "textures/gui/thaumcraft/gui_research_back.jpg"), new ResourceLocation("bloodmagic", "textures/gui/thaumcraft/gui_research_back_over.png"));

        (new SanguineResearchItem("BLOODMAGIC", BLOOD_MAGIC, new AspectList(), 0, 0, 0, new ItemStack(BloodMagicAPI.getItem(Constants.BloodMagicItem.BLOOD_ORB), 1, 0))).setPages(new ResearchPage[] { new ResearchPage(researchPage("BLOODMAGIC")) }).setAutoUnlock().setStub().setRound().registerResearchItem();
    }

    private static String researchPage(String researchName)
    {
        return researchPage(researchName, 1);
    }

    private static String researchPage(String researchName, int pageNumber)
    {
        return "bloodmagic.research_page." + researchName + "." + pageNumber;
    }
}
