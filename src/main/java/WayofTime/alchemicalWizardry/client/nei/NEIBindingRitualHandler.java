package WayofTime.alchemicalWizardry.client.nei;

import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * Binding Ritual Handler by Arcaratus
 */
public class NEIBindingRitualHandler extends TemplateRecipeHandler
{
    public class CachedBindingRecipe extends CachedRecipe
    {
        PositionedStack input;
        PositionedStack output;

        public CachedBindingRecipe(BindingRecipe recipe)
        {
            input = new PositionedStack(recipe.requiredItem, 37, 21, false);
            output = new PositionedStack(recipe.outputItem, 110, 21, false);
        }

        @Override
        public PositionedStack getIngredient()
        {
            return input;
        }

        @Override
        public PositionedStack getResult()
        {
            return output;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("alchemicalwizardry.binding") && getClass() == NEIBindingRitualHandler.class)
        {
            for (BindingRecipe recipe : BindingRegistry.bindingRecipes)
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (BindingRecipe recipe: BindingRegistry.bindingRecipes)
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.outputItem, result))
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        for (BindingRecipe recipe: BindingRegistry.bindingRecipes)
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.requiredItem, ingredient))
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public String getOverlayIdentifier()
    {
        return "bindingritual";
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(90, 32, 22, 16), "alchemicalwizardry.bindingritual"));
    }

    @Override
    public String getRecipeName()
    {
        return "Binding Ritual";
    }

    @Override
    public String getGuiTexture()
    {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/bindingRitual.png").toString();
    }

    public static Point getMousePosition()
    {
        Dimension size = displaySize();
        Dimension res = displayRes();
        return new Point(Mouse.getX() * size.width / res.width, size.height - Mouse.getY() * size.height / res.height - 1);
    }

    public static Dimension displaySize()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    public static Dimension displayRes()
    {
        Minecraft mc = Minecraft.getMinecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }
}