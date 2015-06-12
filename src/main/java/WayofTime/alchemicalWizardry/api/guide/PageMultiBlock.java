package WayofTime.alchemicalWizardry.api.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.PageBase;
import amerifrance.guideapi.gui.GuiBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageMultiBlock extends PageBase 
{
	ItemStack[][][] structure;
	boolean canTick = false;
	int tick = 0;
	int showLayer = -1;
	float scaleFactor = 1;
	
	boolean renderMouseOver = true;
	
    public PageMultiBlock(ItemStack[][][] structure) 
    {
    	this.structure = structure;
    	initPage(structure);
    }
    
    int blockCount=0;
	int[] countPerLevel;
	int structureHeight = 0;
	int structureLength = 0;
	int structureWidth = 0;

	public void initPage(ItemStack[][][] structure)
	{
		structureHeight = structure.length;
		structureWidth=0;
		structureLength=0;
		countPerLevel = new int[structureHeight];
		blockCount=0;
		for(int h=0; h<structure.length; h++)
		{
			if(structure[h].length-1>structureLength)
				structureLength = structure[h].length-1;
			int perLvl=0;
			for(int l=0; l<structure[h].length; l++)
			{
				if(structure[h][l].length-1>structureWidth)
					structureWidth = structure[h][l].length-1;
				for(ItemStack ss : structure[h][l])
					if(ss!=null)
						perLvl++;
			}
			countPerLevel[h] = perLvl;
			blockCount += perLvl; 
		}
		tick= (showLayer==-1?blockCount:countPerLevel[showLayer])*40;
		int yOff = (structureHeight-1)*12+structureWidth*5+structureLength*5+16;
//		pageButtons.add(new GuiButtonManualNavigation(gui, 100, x+4,y+yOff/2-5, 10,10, 4));
//		pageButtons.add(new GuiButtonManualNavigation(gui, 101, x+4,y+yOff/2-8-16, 10,16, 3));
//		pageButtons.add(new GuiButtonManualNavigation(gui, 102, x+4,y+yOff/2+8, 10,16, 2));
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) 
    {
//    	if(multiblock.getStructureManual()!=null)
    	Minecraft mc = Minecraft.getMinecraft();
    	
    	int x = guiLeft + 32;
    	int y = guiTop + 30;
		{
			if(canTick)
				tick++;

			int prevLayers = 0;
			if(showLayer!=-1)
				for(int ll=0; ll<showLayer; ll++)
					prevLayers+=countPerLevel[ll];
			int limiter = prevLayers+ (tick/40)% ((showLayer==-1?blockCount:countPerLevel[showLayer])+4);			

			int xHalf = (structureWidth*5 - structureLength*5);
			int yOffPartial = (structureHeight-1)*12+structureWidth*5+structureLength*5;
			int yOffTotal = yOffPartial+16;

			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			GL11.glPushMatrix();
			GL11.glTranslatef((1-scaleFactor)*(guiLeft + 64), (1-scaleFactor)*(guiTop+60), 0);
			GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.enableGUIStandardItemLighting();
			RenderItem.getInstance().renderWithColor=true;
			int i=0;
			ItemStack highlighted = null;
			for(int h=0; h<structure.length; h++)
				if(showLayer==-1 || h<=showLayer)
				{
					ItemStack[][] level = structure[h];
					for(int l=level.length-1; l>=0; l--)
					{
						ItemStack[] row = level[l];
						for(int w=row.length-1; w>=0; w--)
						{
							int xx = 60 +xHalf -10*w +10*l -7;
							int yy = yOffPartial - 5*w - 5*l -12*h;
							GL11.glTranslated(0, 0, 1);
							if(row[w]!=null && i<=limiter)
							{
								i++;
								RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, row[w], x+xx, y+yy);
								if(mouseX>=x+xx&&mouseX<x+xx+16 && mouseY>=y+yy&&mouseY<y+yy+16)
									highlighted = row[w];
							}
						}
					}
				}
			GL11.glTranslated(0, 0, -i);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			mc.fontRenderer.setUnicodeFlag(false);
			if(highlighted!=null && renderMouseOver)
				guiBase.renderToolTip(highlighted, mouseX, mouseY);
			RenderHelper.disableStandardItemLighting();
            
//			mc.fontRenderer.setUnicodeFlag(true);
//			if(localizedText!=null&&!localizedText.isEmpty())
//				manual.fontRenderer.drawSplitString(localizedText, x,y+yOffTotal, 120, manual.getTextColour());
		}
    }
}