package WayofTime.bloodmagic.render;

import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntityBloodLight extends Render<EntityBloodLight>
{

    public RenderEntityBloodLight(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.0F;
    }

    public void renderEntityAt(EntityBloodLight entity, double x, double y, double z, float fq, float pticks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.1F, 0.1F, 0.1F);
        this.bindTexture(this.getEntityTexture(entity));
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        tessellator.getWorldRenderer().pos(-0.5D, -0.25D, 0.0D).tex(0D, 1D).normal(0F, 1F, 0F).endVertex();
        tessellator.getWorldRenderer().pos(0.5D, -0.25D, 0.0D).tex(1D, 1D).normal(0F, 1F, 0F).endVertex();
        tessellator.getWorldRenderer().pos(0.5D, 0.75D, 0.0D).tex(1D, 0D).normal(0F, 1F, 0F).endVertex();
        tessellator.getWorldRenderer().pos(-0.5D, 0.75D, 0.0D).tex(0D, 1D).normal(0F, 1F, 0F).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    @Override
    public void doRender(EntityBloodLight entityBloodLight, double d, double d1, double d2, float f, float f1)
    {
        renderEntityAt(entityBloodLight, d, d1, d2, f, f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBloodLight entityBloodLight)
    {
        return TextureMap.locationBlocksTexture;
    }
}
