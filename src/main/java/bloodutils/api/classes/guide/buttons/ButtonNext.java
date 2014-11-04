package bloodutils.api.classes.guide.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class ButtonNext extends GuiButton {
	 private final boolean field_146151_o;

    public ButtonNext(int id, int x, int y, boolean p_i1079_4_){
        super(id, x, y, 23, 13, "");
        this.field_146151_o = p_i1079_4_;
    }
    
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_){
        if (this.visible){
            boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation("bloodutils:textures/gui/guide.png"));
            int k = 0;
            int l = 192;

            if (flag){
                k += 23;
            }

            if (!this.field_146151_o){
                l += 13;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}