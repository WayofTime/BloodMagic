package WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.page.reciperenderer.BasicRecipeRenderer;

// TODO: Fix rendering of recipe
public class ShapedBloodOrbRecipeRenderer extends BasicRecipeRenderer<ShapedBloodOrbRecipe>
{
    public ShapedBloodOrbRecipeRenderer(ShapedBloodOrbRecipe recipe)
    {
        super(recipe);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj)
    {
        super.draw(book, category, entry, guiLeft, guiTop, mouseX, mouseY, guiBase, fontRendererObj);
        for (int y = 0; y < recipe.height; y++)
        {
            for (int x = 0; x < recipe.width; x++)
            {
                int stackX = (x + 1) * 17 + (guiLeft + 29);
                int stackY = (y + 1) * 17 + (guiTop + 40);
                Object component = recipe.getInput()[y * recipe.width + x];
                if (component != null)
                {
                    if (component instanceof ItemStack)
                    {
                        ItemStack input = (ItemStack) component;
                        if (input.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                            input.setItemDamage(0);

                        GuiHelper.drawItemStack((ItemStack) component, stackX, stackY);
                        if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15))
                        {
                            tooltips = GuiHelper.getTooltip((ItemStack) component);
                        }
                    } else if (component instanceof Integer)
                    {
                        List<ItemStack> list = OrbRegistry.getOrbsDownToTier((Integer) component);
                        if (!list.isEmpty())
                        {
                            ItemStack stack = list.get(getRandomizedCycle(x + (y * 3), list.size()));
                            GuiHelper.drawItemStack(stack, stackX, stackY);
                            if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15))
                            {
                                tooltips = GuiHelper.getTooltip(stack);
                            }
                        }
                    } else
                    {
                        List<ItemStack> list = (List<ItemStack>) component;
                        if (!list.isEmpty())
                        {
                            ItemStack stack = list.get(getRandomizedCycle(x + (y * 3), list.size()));
                            GuiHelper.drawItemStack(stack, stackX, stackY);
                            if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15))
                            {
                                tooltips = GuiHelper.getTooltip(stack);
                            }
                        }
                    }
                }
            }
        }
    }
}