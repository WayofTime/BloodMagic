package joshie.alchemicalWizardy.nei;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;

import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIAltarRecipeHandler extends TemplateRecipeHandler {
	public class CachedAltarRecipe extends CachedRecipe {
		PositionedStack input;
		PositionedStack output;
		int tier, lp_amount, consumption, drain;
		
		public CachedAltarRecipe(AltarRecipe recipe) {
			input = new PositionedStack(recipe.requiredItem, 38, 2, false);
			output = new PositionedStack(recipe.result, 132, 32, false);
			tier = recipe.minTier;
			lp_amount = recipe.liquidRequired;
			consumption = recipe.consumptionRate;
			drain = recipe.drainRate;
		}
		
		@Override
		public PositionedStack getIngredient() {
            return input;
        }
		
		@Override
		public PositionedStack getResult() {
			return output;
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("altarrecipes") && getClass() == NEIAltarRecipeHandler.class) {
			for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
				if(recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
			if(NEIServerUtils.areStacksSameTypeCrafting(recipe.result, result)) {
				if(recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		}
	}
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient)  {
		for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
			if(NEIServerUtils.areStacksSameTypeCrafting(recipe.requiredItem, ingredient)) {
				if(recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		}
    }
	
	//Mouse Position helper
	public Point getMouse(int width, int height) {
		Point mousepos = this.getMousePosition();
		int guiLeft = (width - 176) / 2;
        int guiTop = (height - 166) / 2;
        Point relMouse = new Point(mousepos.x - guiLeft, mousepos.y - guiTop);
        return relMouse;
	}
	
	//width helper, getting width normal way hates me on compile
	public int getGuiWidth(GuiRecipe gui) {
		try {
			Field f = gui.getClass().getField("width");
			return (Integer) f.get(gui);
		} catch (NoSuchFieldException e) {
			try {
				Field f = gui.getClass().getField("field_146294_l");
				return (Integer) f.get(gui);
			} catch (Exception e2) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0; 
		}
	}
	
	//height helper, getting height normal way hates me on compile
	public int getGuiHeight(GuiRecipe gui) {
		try {
			Field f = gui.getClass().getField("height");
			return (Integer) f.get(gui);
		} catch (NoSuchFieldException e) {
			try {
				Field f = gui.getClass().getField("field_146295_m");
				return (Integer) f.get(gui);
			} catch (Exception e2) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0; 
		}
	}
	
	@Override
	public void drawExtras(int id) {
		CachedAltarRecipe recipe = (CachedAltarRecipe) arecipes.get(id);
		Minecraft.getMinecraft().fontRenderer.drawString("\u00a77" + StatCollector.translateToLocal("bm.string.tier") + ": " + recipe.tier, 78, 5, 0);
		Minecraft.getMinecraft().fontRenderer.drawString("\u00a77" + "LP: " + recipe.lp_amount, 78, 15, 0);
	}
	
	@Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int id) {
		currenttip = super.handleTooltip(gui, currenttip, id);
		Point mouse = getMouse(getGuiWidth(gui), getGuiHeight(gui));
		CachedAltarRecipe recipe = (CachedAltarRecipe) arecipes.get(id);
		int yLow = id % 2 == 0 ? 38 : 102;
		int yHigh = id % 2 == 0 ? 72 : 136;
		if(mouse.x >= 19 && mouse.x <= 80 && mouse.y >= yLow && mouse.y <= yHigh) {
			currenttip.add(StatCollector.translateToLocal("bm.string.consume") + ": " + recipe.consumption + "LP/t");
			currenttip.add(StatCollector.translateToLocal("bm.string.drain") + ": " + recipe.drain + "LP/t");
        }
        
        return currenttip;
    }
	
	@Override
	public String getOverlayIdentifier() {
		return "altarrecipes";
	}
	
	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(90, 32, 22, 16), "altarrecipes"));
	}
	
	@Override
	public String getRecipeName() {
		return "          " + StatCollector.translateToLocal("tile.bloodAltar.name");
	}

	@Override
	public String getGuiTexture() {
		return new ResourceLocation("alchemicalwizardry", "gui/nei/altar.png").toString();
	}
	
	public static Point getMousePosition() {
        Dimension size = displaySize();
        Dimension res = displayRes();
        return new Point(Mouse.getX() * size.width / res.width, size.height - Mouse.getY() * size.height / res.height - 1);
    }

	public static Dimension displaySize() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    public static Dimension displayRes() {
        Minecraft mc = Minecraft.getMinecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }
}
