package WayofTime.alchemicalWizardry.client.nei;

import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.bloodOrbs;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipe;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.common.tileEntity.gui.GuiWritingTable;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

/**
 * NEI Alchemy Recipe Handler by joshie *
 */
public class NEIAlchemyRecipeHandler extends TemplateRecipeHandler {
    public class CachedAlchemyRecipe extends CachedRecipe {
        public class BloodOrbs {
            public PositionedStack stack;

            public BloodOrbs(ItemStack orb) {
                this.stack = new PositionedStack(orb, 136, 47, false);
            }
        }

        ArrayList<BloodOrbs> orbs;
        PositionedStack output;
        List<PositionedStack> inputs;
        int lp;

        public CachedAlchemyRecipe(AlchemyRecipe recipe, ItemStack orb) {
            this(recipe);
            this.orbs = new ArrayList<BloodOrbs>();
            orbs.add(new BloodOrbs(orb));
        }

        public CachedAlchemyRecipe(AlchemyRecipe recipe) {
            List<PositionedStack> inputs = new ArrayList<PositionedStack>();
            ItemStack[] stacks = recipe.getRecipe();
            if (stacks.length > 0) inputs.add(new PositionedStack(stacks[0], 76, 3));
            if (stacks.length > 1) inputs.add(new PositionedStack(stacks[1], 51, 19));
            if (stacks.length > 2) inputs.add(new PositionedStack(stacks[2], 101, 19));
            if (stacks.length > 3) inputs.add(new PositionedStack(stacks[3], 64, 47));
            if (stacks.length > 4) inputs.add(new PositionedStack(stacks[4], 88, 47));
            this.inputs = inputs;
            this.output = new PositionedStack(recipe.getResult(), 76, 25);
            this.lp = recipe.getAmountNeeded() * 100;
            this.orbs = new ArrayList<BloodOrbs>();
            for (Item orb : bloodOrbs) {
                if (((IBloodOrb) orb).getOrbLevel() >= recipe.getOrbLevel()) {
                    orbs.add(new BloodOrbs(new ItemStack(orb)));
                }
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return inputs;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

        @Override
        public PositionedStack getOtherStack() {
            if (orbs == null || orbs.size() <= 0) return null;
            return orbs.get((cycleticks / 48) % orbs.size()).stack;
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        for (ItemStack item : ItemList.items) {
            if (item != null && item.getItem() instanceof IBloodOrb) {
                bloodOrbs.add(item.getItem());
            }
        }

        return super.newInstance();
    }

    @Override
    public String getOverlayIdentifier() {
        return "alchemicalwizardry.alchemy";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(134, 22, 16, 24), "alchemicalwizardry.alchemy"));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiWritingTable.class;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("alchemicalwizardry.alchemy") && getClass() == NEIAlchemyRecipeHandler.class) {
            for (AlchemyRecipe recipe : AlchemyRecipeRegistry.recipes) {
                if (recipe.getResult() != null) arecipes.add(new CachedAlchemyRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (AlchemyRecipe recipe : AlchemyRecipeRegistry.recipes) {
            if (recipe == null) continue;
            if (NEIServerUtils.areStacksSameTypeCrafting(result, recipe.getResult())) {
                arecipes.add(new CachedAlchemyRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() instanceof IBloodOrb) {
            for (AlchemyRecipe recipe : AlchemyRecipeRegistry.recipes) {
                if (recipe == null) continue;
                if (((IBloodOrb) ingredient.getItem()).getOrbLevel() >= recipe.getOrbLevel()) {
                    arecipes.add(new CachedAlchemyRecipe(recipe, ingredient));
                }
            }
        } else {
            for (AlchemyRecipe recipe : AlchemyRecipeRegistry.recipes) {
                if (recipe == null) continue;
                ItemStack[] stacks = recipe.getRecipe();
                for (ItemStack stack : stacks) {
                    if (NEIServerUtils.areStacksSameTypeCrafting(stack, ingredient)) {
                        arecipes.add(new CachedAlchemyRecipe(recipe));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void drawExtras(int id) {
        CachedAlchemyRecipe cache = (CachedAlchemyRecipe) arecipes.get(id);
        Minecraft.getMinecraft().fontRenderer.drawString("\u00a77" + cache.lp + "LP", getLPX(cache.lp), 34, 0);
    }

    public int getLPX(int lp) {
        if (lp < 10) return 122;
        else if (lp < 100) return 122;
        else if (lp < 1000) return 130;
        else if (lp < 10000) return 127;
        else if (lp < 100000) return 124;
        return 122;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("tile.blockWritingTable.name");
    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/alchemy.png").toString();
    }
}
