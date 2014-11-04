package bloodutils.api.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import bloodutils.api.enums.EnumType;

public class GuiHelper {
	public static boolean isMouseBetween(int mouseX, int mouseY, int x, int y, int width, int height) {
		int xSize = x + width;
		int ySize = y + height;
		
		return (mouseX > x && mouseX < xSize && mouseY > y && mouseY < ySize);
	}
	
    public static void renderIcon(int x, int y, int width, int height, IIcon icon, EnumType type){
    	if(type == EnumType.BLOCK)
    		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    	else if(type == EnumType.ITEM)
    		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

    	int zLevel = 0;
    	
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV((double)(x + 0), (double)(y + height), (double)zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        t.addVertexWithUV((double)(x + width), (double)(y + height), (double)zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        t.addVertexWithUV((double)(x + width), (double)(y + 0), (double)zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        t.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        t.draw();
        
        RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_LIGHTING);
 
		GL11.glPopMatrix();
    }
    
    public static void drawScaledIconWithoutColor(int x, int y, int width, int height, float zLevel){
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glScaled(0.13D, 0.13D, 0.13D);
		GL11.glTranslated(x + 900, y + 250, 0);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
    	Tessellator t = Tessellator.instance;
	    t.startDrawingQuads();
	    t.addVertexWithUV(x + 0, y + height, zLevel, 0D, 1D);
	    t.addVertexWithUV(x + width, y + height, zLevel, 1D, 1D);
	    t.addVertexWithUV(x + width, y + 0, zLevel, 1D, 0D);
	    t.addVertexWithUV(x + 0, y + 0, zLevel, 0D, 0D);
	    t.draw();
	    
        RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_LIGHTING);
 
		GL11.glPopMatrix();
	}
	
    public static void drawIconWithoutColor(int x, int y, int width, int height, float zLevel){
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
    	Tessellator t = Tessellator.instance;
	    t.startDrawingQuads();
	    t.addVertexWithUV(x + 0, y + height, zLevel, 0D, 1D);
	    t.addVertexWithUV(x + width, y + height, zLevel, 1D, 1D);
	    t.addVertexWithUV(x + width, y + 0, zLevel, 1D, 0D);
	    t.addVertexWithUV(x + 0, y + 0, zLevel, 0D, 0D);
	    t.draw();
	    
        RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_LIGHTING);
 
		GL11.glPopMatrix();
	}
}