package WayofTime.bloodmagic.compat.guideapi;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import WayofTime.bloodmagic.compat.ICompatibility;
import amerifrance.guideapi.api.GuideAPI;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CompatibilityGuideAPI implements ICompatibility
{
    private static IRecipe guideRecipe = null;
    private static boolean worldFlag;

    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        switch (phase)
        {
        case PRE_INIT:
        {
            GuideBloodMagic.initBook();
            GameRegistry.register(GuideBloodMagic.guideBook);
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
                GuideAPI.setModel(GuideBloodMagic.guideBook);

            break;
        }
        case INIT:
        {
            guideRecipe = new ShapelessOreRecipe(GuideAPI.getStackFromBook(GuideBloodMagic.guideBook), new ItemStack(Items.BOOK), Blocks.GLASS, Items.FEATHER);
            break;
        }
        case POST_INIT:
        {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
                GuideBloodMagic.initCategories();
            break;
        }
        case MAPPING:
        {
            if (!worldFlag) {
                GameRegistry.addRecipe(guideRecipe);
                worldFlag = true;
            } else {
                CraftingManager.getInstance().getRecipeList().remove(guideRecipe);
                worldFlag = false;
            }
            break;
        }
        }
    }

    @Override
    public String getModId()
    {
        return "guideapi";
    }

    @Override
    public boolean enableCompat()
    {
        return true;
    }
}
